package com.ssu.wsn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ssu.wsn.commons.Utils;
import com.ssu.wsn.deploy.WSNNode;
import com.ssu.wsn.graphics.WSNXYLineGenericChart2;
import com.ssu.wsn.process.ClusterHeadElectionProcess;

public class WSNNIRMMain {
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
	private static List<Double> nirmREList = new ArrayList<Double>();
	enum GraphMode {ITERATION, RESIDUAL_ENERGY};
	private static GraphMode graphMode =  GraphMode.RESIDUAL_ENERGY;
	private static final int MESSAGE_RANDOMNESS = 50;
	private static int numberOfInactives = (int)Utils.roundTwoDecimals(((long)NUMBER_OF_NODES - (long)0 + 1) * new Random().nextDouble());
	private static List inactiveList = new ArrayList();
	public static Map<String, double[]> seriesMap;
	public static double interationImprovement;
	public static double energyLebelEfficiency;
	public static void main(String[] args) throws Exception{
		invokeProtocol();
	    WSNXYLineGenericChart2.plotGenericChart(seriesMap);
	}

	public static Map<String, double[]> invokeProtocol() {
		genericREList = new ArrayList<Double>();
		pissREList = new ArrayList<Double>();
		nirmREList = new ArrayList<Double>();
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
		for(int i=0; i< numberOfInactives; i++){
			inactiveList.add((int)Utils.roundTwoDecimals(((long)numberOfInactives - (long)0 + 1) * new Random().nextDouble()));
		}
		processFlooding();
		System.out.println("**************************************************************************************");
		processPISS();
		processNIRM();
		System.out.println(genericREList);
		System.out.println(pissREList);
		System.out.println(nirmREList);
		//WSNXYLineGenericChart.plotGenericChart(Utils.convertObject2Double(genericREList.toArray()), Utils.convertObject2Double(nirmREList.toArray()));
		//WSNXYLineGenericChart.plotGenericChart(Utils.convertObject2Double(pissREList.toArray()), Utils.convertObject2Double(nirmREList.toArray()));
		
		seriesMap = new HashMap<String, double[]>();
	    seriesMap.put("Generic Protocol", Utils.convertObject2Double(genericREList.toArray()));
	    seriesMap.put("PISS Protocol", Utils.convertObject2Double(pissREList.toArray()));
	    seriesMap.put("NIRM Protocol", Utils.convertObject2Double(nirmREList.toArray()));
	    interationImprovement = ((double)(nirmREList.size() - genericREList.size())/nirmREList.size())*100;
		System.out.println("interationImprovement :" + interationImprovement);
		energyLebelEfficiency = ((nirmREList.get(genericREList.size()-1) - genericREList.get(genericREList.size()-1))/genericREList.get(genericREList.size()-1))*100;
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
	
	public static void processNIRM(){
		resetResidualEnergy();
		boolean wsnAlive = true;
		int count = 0;
		double clusterHeadResidualEnergy = 0;
		double remainingResidualEnergy = 0f;
		int networkIteration = 0;
		int chDecision = Utils.randomInt(inactiveList.size());
		
		System.out.println("numberOfInactives :" + numberOfInactives + ", Cluster Head Decided to Make Number of Inactive and Wake up in Next round :" + chDecision);
		while(wsnAlive){
			//System.out.println("*******************"+(count ++)+"*************************");
			for(int i=0; i<wsnNode.length;i++){
				/*if(wsnNode[i].getResidualEnergy() > 0.6* INITIAL_RESIDUAL_ENERGY
						&& wsnNode[i].findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, wsnNode[i].getNearestClusterHead()) > 0){*/
				if(!inactiveList.contains(i)){
					wsnNode[i].transmitMessage();
				}else if(wsnNode[i].getResidualEnergy() > 0.6* INITIAL_RESIDUAL_ENERGY && count < chDecision){
					wsnNode[i].transmitMessage();
					count++;
				}

				//}

				
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

					nirmREList.add((double)networkIteration);
				}else{

					nirmREList.add(remainingResidualEnergy);
				}
			}else{
				wsnAlive = false;
			}

		}

		/*wsnAlive = true;
		while(wsnAlive){
			//System.out.println("*******************"+(count ++)+"*************************");
			count = 0;
			for(int i=0; i<wsnNode.length;i++){
				if(wsnNode[i].getResidualEnergy() > 0
						&& wsnNode[i].findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, wsnNode[i].getNearestClusterHead()) > 0){
				if(!inactiveList.contains(i)){
				wsnNode[i].transmitMessage();
				
				}else if(count < 4){
					wsnNode[i].transmitMessage();
					count ++;
				}
				//}

				
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

					nirmREList.add((double)networkIteration);
				}else{

					nirmREList.add(remainingResidualEnergy);
				}
			}else{
				wsnAlive = false;
			}

		}*/
		wsnAlive = true;
		while(wsnAlive){
			//System.out.println("*******************"+(count ++)+"*************************");
			for(int i=0; i<wsnNode.length;i++){
				/*if(wsnNode[i].getResidualEnergy() > 0
						&& wsnNode[i].findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, wsnNode[i].getNearestClusterHead()) > 0){*/
				wsnNode[i].transmitMessage();
				//}

				
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

					nirmREList.add((double)networkIteration);
				}else{

					nirmREList.add(remainingResidualEnergy);
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
