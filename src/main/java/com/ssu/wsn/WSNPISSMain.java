package com.ssu.wsn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ssu.wsn.WSNMNFMain.GraphMode;
import com.ssu.wsn.commons.Constants;
import com.ssu.wsn.commons.Utils;
import com.ssu.wsn.config.EnvironmentConfiguration;
import com.ssu.wsn.deploy.BaseStation;
import com.ssu.wsn.deploy.WSNDACHNode;
import com.ssu.wsn.deploy.WSNNode;
import com.ssu.wsn.graphics.WSNXYLineGenericChart;
import com.ssu.wsn.graphics.WSNXYLineGenericChart2;
import com.ssu.wsn.process.ClusterHeadElectionProcess;

public class WSNPISSMain {
	public static final double INITIAL_RESIDUAL_ENERGY = 1000000;
	public static final int NUMBER_OF_NODES = 50;
	public static final double EPS_TE = 0.5;
	public static final double EPS_TA = 0.5;
	public static final double EPS_R = 0.1;
	public static final double WIRELESS_RANGE = 7.25;
	private static final int REFERENCE_INTERMEDIATE_NODE_ID_1 = 1;
	private static final int REFERENCE_INTERMEDIATE_NODE_ID_2 = 2;
	private static final int REFERENCE_CLISTER_HEAD_NODE_ID = 3;
	private static WSNNode[] wsnNode = new WSNNode[NUMBER_OF_NODES];
	private static List<Double> genericREList = new ArrayList<Double>();
	private static List<Double> pissREList = new ArrayList<Double>();
	enum GraphMode {ITERATION, RESIDUAL_ENERGY};
	private static GraphMode graphMode =  GraphMode.RESIDUAL_ENERGY;
	private static final int MESSAGE_RANDOMNESS = 50;
	public static Map<String, double[]> seriesMap;
	public static double interationImprovement;
	public static double energyLebelEfficiency;
	public static void main(String[] args) throws Exception{
		invokeProtocol();
		//WSNXYLineGenericChart.plotGenericChart(Utils.convertObject2Double(genericREList.toArray()), Utils.convertObject2Double(pissREList.toArray()));
		WSNXYLineGenericChart2.plotGenericChart(seriesMap);

	}

	public static Map<String, double[]> invokeProtocol() {
		genericREList = new ArrayList<Double>();
		pissREList = new ArrayList<Double>();
		Random random = new Random();
		long range = (long)10 - (long)0 + 1;
		for(int i=0; i<NUMBER_OF_NODES; i++){
			wsnNode[i] = new WSNNode(1, Utils.roundTwoDecimals(
					range * random.nextDouble())
					, Utils.roundTwoDecimals(range * random.nextDouble())
					, EPS_TE
					, EPS_TA
					, EPS_R
					, WIRELESS_RANGE
					, INITIAL_RESIDUAL_ENERGY);
		}
		ClusterHeadElectionProcess.election(wsnNode);
		//ClusterHeadElectionProcess.election(wsnDACHNodes);
		/*for(int i=0; i<wsnNodes.length;i++){
			System.out.println(wsnNodes[i]);
		}*/

		processFlooding();
		System.out.println("**************************************************************************************");
		processPISS();
		System.out.println(genericREList);
		System.out.println(pissREList);
		seriesMap = new HashMap<String, double[]>();
	    seriesMap.put("WSN without SS Protocol", Utils.convertObject2Double(genericREList.toArray()));
	    seriesMap.put("WSN with SS Protocol", Utils.convertObject2Double(pissREList.toArray()));
	    interationImprovement = ((double)(pissREList.size() - genericREList.size())/pissREList.size())*100;
		System.out.println("interationImprovement :" + interationImprovement);
		energyLebelEfficiency = ((pissREList.get(genericREList.size()-1) - genericREList.get(genericREList.size()-1))/genericREList.get(genericREList.size()-1))*100;
		System.out.println("energyLebelEfficiency :" + energyLebelEfficiency);
		return seriesMap;
	}

	public static void processFlooding(){
		resetResidualEnergy();
		boolean wsnAlive = true;
		int count = 0;
		double clusterHeadResidualEnergy = 0;
		double remainingResidualEnergy = 0f;
		int networkIteration = 0;
		while(wsnAlive){
			//System.out.println("*******************"+(count ++)+"*************************");
			for(int i=0; i<wsnNode.length;i++){
				/*if(wsnNode[i].getResidualEnergy() > 0 
						&& wsnNode[i].findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, wsnNode[i].getNearestClusterHead()) > 0){*/
				wsnNode[i].transmitMessage();
				//}

				if(wsnNode[i].isClusterHeadStatus()){
					//System.out.println("Node["+i+"]:" + wsnNode[i].isClusterHeadStatus() + ":" + System.currentTimeMillis() + ":" + wsnNode[i].getResidualEnergy());
					/*if(wsnNode[i].getResidualEnergy() <= 0 || clusterHeadResidualEnergy == wsnNode[i].getResidualEnergy()){
						wsnAlive = false;
						break;
					}else{
						clusterHeadResidualEnergy = wsnNode[i].getResidualEnergy();
					}*/

				}else{
					//System.out.println("Node["+i+"]:" + wsnNode[i].isClusterHeadStatus() + ":" + System.currentTimeMillis() + ":" + wsnNode[i].getResidualEnergy());
				}
			}
			networkIteration ++;
			remainingResidualEnergy = 0f;
			for(int i=0; i<wsnNode.length; i++){
				remainingResidualEnergy += wsnNode[i].getResidualEnergy();
			}
			//System.out.println("**************************PROCESS GENERICS**********************");
			//System.out.println(networkIteration + "::" + remainingResidualEnergy);
			if(remainingResidualEnergy > 0)
				if(graphMode.equals(GraphMode.ITERATION)){

					genericREList.add((double)networkIteration);
				}else{

					genericREList.add(remainingResidualEnergy);
				}else{
					wsnAlive = false;
				}
			//wsnNodes[0].getNearestClusterHead().transmit(Constants.MESSAGE_PACKAET_SIZE * (wsnNodes.length - 1), EnvironmentConfiguration.baseStation);

		}
	}

	public static void processPISS(){
		resetResidualEnergy();
		boolean wsnAlive = true;
		int count = 0;
		double clusterHeadResidualEnergy = 0;
		double remainingResidualEnergy = 0f;
		int networkIteration = 0;
		int numberOfInactives = (int)Utils.roundTwoDecimals(((long)NUMBER_OF_NODES - (long)0 + 1) * new Random().nextDouble());
		List inactiveList = new ArrayList();
		for(int i=0; i< numberOfInactives; i++){
			inactiveList.add((int)Utils.roundTwoDecimals(((long)numberOfInactives - (long)0 + 1) * new Random().nextDouble()));
		}
		System.out.println("numberOfInactives :" + numberOfInactives);
		while(wsnAlive){
			//System.out.println("*******************"+(count ++)+"*************************");
			for(int i=0; i<wsnNode.length;i++){
				/*if(wsnNode[i].getResidualEnergy() > 0.6* INITIAL_RESIDUAL_ENERGY
						&& wsnNode[i].findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, wsnNode[i].getNearestClusterHead()) > 0){*/
				if(!inactiveList.contains(i)){
					wsnNode[i].transmitMessage();
				}else if(wsnNode[i].getResidualEnergy() > 0.6* INITIAL_RESIDUAL_ENERGY){
					wsnNode[i].transmitMessage();
				}

				//}

				if(wsnNode[i].isClusterHeadStatus()){
					//System.out.println("Node["+i+"]:" + wsnNode[i].isClusterHeadStatus() + ":" + System.currentTimeMillis() + ":" + wsnNode[i].getResidualEnergy());
					/*if(wsnNode[i].getResidualEnergy() <= 0 || clusterHeadResidualEnergy == wsnNode[i].getResidualEnergy()){
						wsnAlive = false;
						break;
					}else{
						clusterHeadResidualEnergy = wsnNode[i].getResidualEnergy();
					}*/

				}else{
					//System.out.println("Node["+i+"]:" + wsnNode[i].isClusterHeadStatus() + ":" + System.currentTimeMillis() + ":" + wsnNode[i].getResidualEnergy());
				}
			}
			//wsnNodes[0].getNearestClusterHead().transmit(Constants.MESSAGE_PACKAET_SIZE * (wsnNodes.length - 1), EnvironmentConfiguration.baseStation);
			networkIteration ++;
			remainingResidualEnergy = 0f;
			for(int i=0; i<wsnNode.length; i++){
				remainingResidualEnergy += wsnNode[i].getResidualEnergy();
			}
			//System.out.println("**************************PROCESS GENERICS**********************");
			//System.out.println(networkIteration + "::" + remainingResidualEnergy);
			if(remainingResidualEnergy > 0){
				if(graphMode.equals(GraphMode.ITERATION)){

					pissREList.add((double)networkIteration);
				}else{

					pissREList.add(remainingResidualEnergy);
				}
			}else{
				wsnAlive = false;
			}

		}

		wsnAlive = true;
		while(wsnAlive){
			//System.out.println("*******************"+(count ++)+"*************************");
			for(int i=0; i<wsnNode.length;i++){
				/*if(wsnNode[i].getResidualEnergy() > 0
						&& wsnNode[i].findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, wsnNode[i].getNearestClusterHead()) > 0){*/
				wsnNode[i].transmitMessage();
				//}

				if(wsnNode[i].isClusterHeadStatus()){
					//System.out.println("Node["+i+"]:" + wsnNode[i].isClusterHeadStatus() + ":" + System.currentTimeMillis() + ":" + wsnNode[i].getResidualEnergy());
					/*if(wsnNode[i].getResidualEnergy() <= 0 || clusterHeadResidualEnergy == wsnNode[i].getResidualEnergy()){
						wsnAlive = false;
						break;
					}else{
						clusterHeadResidualEnergy = wsnNode[i].getResidualEnergy();
					}*/

				}else{
					//System.out.println("Node["+i+"]:" + wsnNode[i].isClusterHeadStatus() + ":" + System.currentTimeMillis() + ":" + wsnNode[i].getResidualEnergy());
				}
			}
			//wsnNodes[0].getNearestClusterHead().transmit(Constants.MESSAGE_PACKAET_SIZE * (wsnNodes.length - 1), EnvironmentConfiguration.baseStation);
			networkIteration ++;
			remainingResidualEnergy = 0f;
			for(int i=0; i<wsnNode.length; i++){
				remainingResidualEnergy += wsnNode[i].getResidualEnergy();
			}
			//System.out.println("**************************PROCESS GENERICS**********************");
			//System.out.println(networkIteration + "::" + remainingResidualEnergy);
			if(remainingResidualEnergy > 0){
				if(graphMode.equals(GraphMode.ITERATION)){

					pissREList.add((double)networkIteration);
				}else{

					pissREList.add(remainingResidualEnergy);
				}
			}else{
				wsnAlive = false;
			}

		}

	}
	private static void resetResidualEnergy(){
		for(int i=0; i<wsnNode.length; i++){
			wsnNode[i].setResidualEnergy(INITIAL_RESIDUAL_ENERGY);
		}
	}


}
