package com.ssu.wsn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ssu.wsn.commons.Constants;
import com.ssu.wsn.commons.Utils;
import com.ssu.wsn.config.EnvironmentConfiguration;
import com.ssu.wsn.deploy.WSNDACHNode;
import com.ssu.wsn.deploy.WSNNode;
import com.ssu.wsn.graphics.WSNGenericGraphics;
import com.ssu.wsn.graphics.WSNXYLineGenericChart;
import com.ssu.wsn.graphics.WSNXYLineGenericChart2;
import com.ssu.wsn.process.ClusterHeadElectionProcess;


public class WSNCIDAFMain {
	public static final double INITIAL_RESIDUAL_ENERGY = 1000000;
	public static final double EPS_TE = 1.0;
	public static final double EPS_TA = 1.0;
	public static final double EPS_R = 0.5;
	public static final double WIRELESS_RANGE = 7.25;
	private static final int REFERENCE_INTERMEDIATE_NODE_ID = 2;
	private static final int REFERENCE_CLISTER_HEAD_NODE_ID = 3;
	public static final int TOTAL_NUMBER_NODES = 4;
	private static WSNNode[] wsnNode = new WSNNode[TOTAL_NUMBER_NODES];
	private static List<Double> genericREList = new ArrayList<Double>();
	private static List<Double> spinREList = new ArrayList<Double>();
	private static List<Double> cidafREList = new ArrayList<Double>();
	enum GraphMode {ITERATION, RESIDUAL_ENERGY};
	private static GraphMode graphMode =  GraphMode.RESIDUAL_ENERGY;
	private static final int MESSAGE_RANDOMNESS = 50;
	public static Map<String, double[]> seriesMap;
	public static double interationImprovement;
	public static double energyLebelEfficiency;
	public static void main(String[] args) throws Exception{


		invokeProtocol();
		//WSNXYLineGenericChart.plotGenericChart(Utils.convertObject2Double(genericREList.toArray()), Utils.convertObject2Double(genericREList.toArray()));
		//WSNXYLineGenericChart.plotGenericChart(Utils.convertObject2Double(genericREList.toArray()), Utils.convertObject2Double(cidafREList.toArray()));
		//WSNXYLineGenericChart.plotGenericChart(Utils.convertObject2Double(spinREList.toArray()), Utils.convertObject2Double(cidafREList.toArray()));
		 WSNXYLineGenericChart2.plotGenericChart(seriesMap);
	}
	public static Map<String, double[]> invokeProtocol() {
		genericREList = new ArrayList<Double>();
		spinREList = new ArrayList<Double>();
		cidafREList = new ArrayList<Double>();
		Random random = new Random();
		long range = (long)10 - (long)0 + 1;
		/*for(int i=0;i<100;i++){
			System.out.println("Double Random Number is :" + Utils.roundTwoDecimals(range * random.nextDouble()));
		}*/

		for(int i=0; i<TOTAL_NUMBER_NODES; i++){
			wsnNode[i] = new WSNNode(1, Utils.roundTwoDecimals(
					range * random.nextDouble())
					, Utils.roundTwoDecimals(range * random.nextDouble())
					, EPS_TE
					, EPS_TA
					, EPS_R
					, WIRELESS_RANGE
					, INITIAL_RESIDUAL_ENERGY);
		}
		processGeneric();
		processSPIN();
		processDelayForwardCIDAF();
		//WSNGenericGraphics.displayFrame(Utils.convertObject2Double(genericREList.toArray()), Utils.convertObject2Double(spinREList.toArray()));
		//WSNGenericGraphics.displayFrame(Utils.convertObject2Double(genericREList.toArray()), Utils.convertObject2Double(cidafREList.toArray()));
		//WSNGenericGraphics.displayFrame(Utils.convertObject2Double(spinREList.toArray()), Utils.convertObject2Double(cidafREList.toArray()));
		System.out.println(genericREList);
		System.out.println(spinREList);
		System.out.println(cidafREList);
		seriesMap = new HashMap<String, double[]>();
		seriesMap.put("Generic Protocol", Utils.convertObject2Double(genericREList.toArray()));
	    seriesMap.put("SPIN Protocol", Utils.convertObject2Double(spinREList.toArray()));
	    seriesMap.put("CIDAF Protocol", Utils.convertObject2Double(cidafREList.toArray()));
	    interationImprovement = ((float)(cidafREList.size() - genericREList.size())/cidafREList.size())*100;
		System.out.println("interationImprovement :" + interationImprovement);
		energyLebelEfficiency = ((cidafREList.get(genericREList.size()-1) - genericREList.get(genericREList.size()-1))/genericREList.get(genericREList.size()-1))*100;
		System.out.println("energyLebelEfficiency :" + energyLebelEfficiency);
		return seriesMap;
	}
	private static void resetResidualEnergy(){
		for(int i=0; i<wsnNode.length; i++){
			wsnNode[i].setResidualEnergy(INITIAL_RESIDUAL_ENERGY);
	}
	}
	private static void processGeneric() {
		resetResidualEnergy();
		int networkIteration = 0;
		double remainingResidualEnergy = 0f;
		while(networkIteration<20){
			for(int i=0; i<2; i++){
				wsnNode[i].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNode[REFERENCE_INTERMEDIATE_NODE_ID]);
				wsnNode[REFERENCE_INTERMEDIATE_NODE_ID].receive(Constants.MESSAGE_PACKAET_SIZE);
				wsnNode[REFERENCE_INTERMEDIATE_NODE_ID].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID]);
				wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID].receive(Constants.MESSAGE_PACKAET_SIZE);
			}
			networkIteration ++;
			remainingResidualEnergy = 0f;
			for(int i=0; i<4; i++){
				remainingResidualEnergy += wsnNode[i].getResidualEnergy();
			}
			//System.out.println("**************************PROCESS GENERICS**********************");
			//System.out.println(networkIteration + "::" + remainingResidualEnergy);
			if(graphMode.equals(GraphMode.ITERATION)){
				genericREList.add((double)networkIteration);
			}else{
				if(remainingResidualEnergy > 0)
				genericREList.add(remainingResidualEnergy);
			}
			if((wsnNode[0].getResidualEnergy() > 0 || wsnNode[1].getResidualEnergy() >0) && wsnNode[2].getResidualEnergy() > 0 && wsnNode[2].getResidualEnergy() > 0){
				continue;
			}else{
				break;
			}
		}
	}
	private static void processSPIN() {
		resetResidualEnergy();
		int networkIteration = 0;
		double remainingResidualEnergy = 0f;
		
		while(networkIteration<20){
			int[] message_id={-1,-1};
			for(int i=0; i<2; i++){
				message_id[i] = Utils.randomInt(MESSAGE_RANDOMNESS);
				wsnNode[i].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNode[REFERENCE_INTERMEDIATE_NODE_ID]);
				wsnNode[REFERENCE_INTERMEDIATE_NODE_ID].receive(Constants.MESSAGE_PACKAET_SIZE);

			}
			if(message_id[0] == message_id[1]){
				System.out.println("Message Id Same ::::::" + networkIteration);
				wsnNode[REFERENCE_INTERMEDIATE_NODE_ID].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID]);
				wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID].receive(Constants.MESSAGE_PACKAET_SIZE);
			}else{
				wsnNode[REFERENCE_INTERMEDIATE_NODE_ID].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID]);
				wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID].receive(Constants.MESSAGE_PACKAET_SIZE);
				wsnNode[REFERENCE_INTERMEDIATE_NODE_ID].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID]);
				wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID].receive(Constants.MESSAGE_PACKAET_SIZE);
			}
			networkIteration ++;
			remainingResidualEnergy = 0f;
			for(int i=0; i<TOTAL_NUMBER_NODES; i++){
				remainingResidualEnergy += wsnNode[i].getResidualEnergy();
			}
			//System.out.println("**************************PROCESS SPIN**********************");
			//System.out.println(networkIteration + "::" + remainingResidualEnergy);
			if(graphMode.equals(GraphMode.ITERATION)){
				spinREList.add((double)networkIteration);
			}else{
				if(remainingResidualEnergy > 0)
				spinREList.add(remainingResidualEnergy);
			}
			
			if((wsnNode[0].getResidualEnergy() > 0 || wsnNode[1].getResidualEnergy() >0) && wsnNode[2].getResidualEnergy() > 0 && wsnNode[2].getResidualEnergy() > 0){
				continue;
			}else{
				break;
			}
		}
	}
	private static void processDelayForwardCIDAF() {
		resetResidualEnergy();
		int networkIteration = 0;
		double remainingResidualEnergy = 0f;
		List<Integer> messageList = new ArrayList<Integer>();
		Map messageMap = new HashMap();
		
		while(true){
			int[] message_id={-1,-1};
			for(int i=0; i<2; i++){
				message_id[i] = Utils.randomInt(MESSAGE_RANDOMNESS);
				wsnNode[i].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNode[REFERENCE_INTERMEDIATE_NODE_ID]);
				wsnNode[REFERENCE_INTERMEDIATE_NODE_ID].receive(Constants.MESSAGE_PACKAET_SIZE);
				if(!messageMap.containsKey(message_id[i])){
					messageMap.put(message_id[i], false);
				}
			}
				
				if(message_id[0] == message_id[1]){
					if(!(Boolean)messageMap.get(message_id[0])){
						System.out.println("Message Id Same :" + message_id[0] + ", Sent first time");
						wsnNode[REFERENCE_INTERMEDIATE_NODE_ID].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID]);
						wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID].receive(Constants.MESSAGE_PACKAET_SIZE);
						messageMap.put(message_id[0], true);
					}else{
						System.out.println("Message Id Same :" + message_id[0] + ", Already Sent");
					}
				}else{
					if(!(Boolean)messageMap.get(message_id[0])){
							System.out.println("Message Id Different :" + message_id[0] + ", Sent first time");
							wsnNode[REFERENCE_INTERMEDIATE_NODE_ID].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID]);
							wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID].receive(Constants.MESSAGE_PACKAET_SIZE);
							messageMap.put(message_id[0], true);
						}else{
							System.out.println("Message Id Different :" + message_id[0] + ", Already Sent");
						}
						if(!(Boolean)messageMap.get(message_id[1])){
							System.out.println("Message Id Different :" + message_id[0] + ", Sent first time");
							wsnNode[REFERENCE_INTERMEDIATE_NODE_ID].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID]);
							wsnNode[REFERENCE_CLISTER_HEAD_NODE_ID].receive(Constants.MESSAGE_PACKAET_SIZE);
							messageMap.put(message_id[1], true);
						}else{
							System.out.println("Message Id Different :" + message_id[0] + ", Already Sent");
						}

					}
				
			networkIteration ++;
			remainingResidualEnergy = 0f;
			for(int i=0; i<4; i++){
				remainingResidualEnergy += wsnNode[i].getResidualEnergy();
			}
			//System.out.println("**************************PROCESS CIDAF**********************");
			//System.out.println(networkIteration + "::" + remainingResidualEnergy);
			if(graphMode.equals(GraphMode.ITERATION)){
				cidafREList.add((double)networkIteration);
			}else{
				if(remainingResidualEnergy > 0)
				cidafREList.add(remainingResidualEnergy);
			}
			if((wsnNode[0].getResidualEnergy() > 0 || wsnNode[1].getResidualEnergy() >0) && wsnNode[2].getResidualEnergy() > 0 && wsnNode[2].getResidualEnergy() > 0){
				continue;
			}else{
				break;
			}
		}
		}
	}



