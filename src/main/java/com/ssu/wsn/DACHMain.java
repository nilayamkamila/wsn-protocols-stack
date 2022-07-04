package com.ssu.wsn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ssu.wsn.commons.Constants;
import com.ssu.wsn.commons.Utils;
import com.ssu.wsn.deploy.WSNDACHNode;
import com.ssu.wsn.deploy.WSNNode;
import com.ssu.wsn.graphics.WSNXYLineGenericChart2;
import com.ssu.wsn.process.ClusterHeadElectionProcess;

public class DACHMain {
	//static WSNNode[] wsnNodes = EnvironmentConfiguration.getWsnNodes();
	//static WSNDACHNode[] wsnDACHNodes = EnvironmentConfiguration.getWsnDACHNodes();
	public static final double INITIAL_RESIDUAL_ENERGY = 10000000;
	public static final double EPS_TE = 1.0;
	public static final double EPS_TA = 1.0;
	public static final double EPS_R = 0.5;
	public static final double WIRELESS_RANGE = 7.25;
	private static final int REFERENCE_INTERMEDIATE_NODE_ID_1 = 1;
	private static final int REFERENCE_INTERMEDIATE_NODE_ID_2 = 2;
	private static final int REFERENCE_CLISTER_HEAD_NODE_ID = 3;
	public static final int TOTAL_NUMBER_NODES = 10;
	private static WSNNode[] wsnNodes = new WSNNode[TOTAL_NUMBER_NODES];
	private static WSNDACHNode[] wsnDACHNodes = new WSNDACHNode[TOTAL_NUMBER_NODES];
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
			double x_axis = Utils.roundTwoDecimals(
					range * random.nextDouble());
			double y_axis = Utils.roundTwoDecimals(
					range * random.nextDouble());
			wsnNodes[i] = new WSNNode(1
					, x_axis
					, y_axis
					, EPS_TE
					, EPS_TA
					, EPS_R
					, WIRELESS_RANGE
					, INITIAL_RESIDUAL_ENERGY);
			wsnDACHNodes[i] = new WSNDACHNode(1
					, x_axis
					, y_axis
					, EPS_TE
					, EPS_TA
					, EPS_R
					, WIRELESS_RANGE
					, INITIAL_RESIDUAL_ENERGY);
		}
		ClusterHeadElectionProcess.election(wsnNodes);
		ClusterHeadElectionProcess.election(wsnDACHNodes);
		/*for(int i=0; i<wsnNodes.length;i++){
			System.out.println(wsnNodes[i]);
		}*/

		for(int i=0; i<wsnDACHNodes.length;i++){
			System.out.println(wsnDACHNodes[i]);
		}


		for(int i=0; i<wsnDACHNodes.length;i++){
			System.out.println(wsnDACHNodes[i]);
		}

		processFlooding();
		processDACH();
		System.out.println(genericREList);
		System.out.println(cidafREList);
		
		seriesMap = new HashMap<String, double[]>();
	    seriesMap.put("Single CH", Utils.convertObject2Double(genericREList.toArray()));
	    seriesMap.put("DACH", Utils.convertObject2Double(cidafREList.toArray()));
	    interationImprovement = ((double)(cidafREList.size() - genericREList.size())/cidafREList.size())*100;
		System.out.println("interationImprovement :" + interationImprovement);
		if(cidafREList.size() > genericREList.size())
		energyLebelEfficiency = ((cidafREList.get(genericREList.size()-1) - genericREList.get(genericREList.size()-1))/genericREList.get(genericREList.size()-1))*100;
		System.out.println("energyLebelEfficiency :" + energyLebelEfficiency);
		return seriesMap;
	}
	private static void resetResidualEnergy(){
		for(int i=0; i<wsnNodes.length; i++){
			wsnNodes[i].setResidualEnergy(INITIAL_RESIDUAL_ENERGY);
			wsnDACHNodes[i].setResidualEnergy(INITIAL_RESIDUAL_ENERGY);
	}
	}
	public static void processFlooding(){
		resetResidualEnergy();
		boolean wsnAlive = true;
		int count = 0;
		double clusterHeadResidualEnergy = 0;
		while(wsnAlive){
			System.out.println("*******************"+(count ++)+"*************************");
			genericREList.add((double)Utils.getTotalResidualEnergy(wsnNodes));
			for(int i=0; i<wsnNodes.length;i++){
				if(wsnNodes[i].getResidualEnergy() > 0 
						&& wsnNodes[i].findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, wsnNodes[i].getNearestClusterHead()) > 0){
					wsnNodes[i].transmitMessage();
				}

				if(wsnNodes[i].isClusterHeadStatus()){
					System.out.println("Node["+i+"]:" + wsnNodes[i].isClusterHeadStatus() + ":" + System.currentTimeMillis() + ":" + wsnNodes[i].getResidualEnergy());
					if(wsnNodes[i].getResidualEnergy() <= 0 || clusterHeadResidualEnergy == wsnNodes[i].getResidualEnergy()){
						wsnAlive = false;
						break;
					}else{
						clusterHeadResidualEnergy = wsnNodes[i].getResidualEnergy();
					}

				}else{
					System.out.println("Node["+i+"]:" + wsnNodes[i].isClusterHeadStatus() + ":" + System.currentTimeMillis() + ":" + wsnNodes[i].getResidualEnergy());
				}
			}
			//wsnNodes[0].getNearestClusterHead().transmit(Constants.MESSAGE_PACKAET_SIZE * (wsnNodes.length - 1), EnvironmentConfiguration.baseStation);

		}
	}

	public static void processDACH(){
		resetResidualEnergy();
		boolean wsnAlive = true;
		int count = 0;
		double clusterHeadResidualEnergy = 0;
		selectAlternateClusterHead();
		while(wsnAlive){
			System.out.println("*******************"+(count ++)+"*************************");
			cidafREList.add((double)Utils.getTotalResidualEnergy(wsnDACHNodes));
			for(int i=0; i<wsnDACHNodes.length;i++){
				if(wsnDACHNodes[i].getResidualEnergy() > 0 
						&& wsnDACHNodes[i].findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, wsnDACHNodes[i].getNearestClusterHead()) > 0){
					wsnDACHNodes[i].transmitMessage();
				}

				if(wsnDACHNodes[i].isClusterHeadStatus()){
					System.out.println("Node["+i+"]:" + wsnDACHNodes[i].isClusterHeadStatus() + ":" + System.currentTimeMillis() + ":" + wsnDACHNodes[i].getResidualEnergy());
					if(wsnDACHNodes[i].getResidualEnergy() <= 0 || clusterHeadResidualEnergy == wsnDACHNodes[i].getResidualEnergy()){
						wsnAlive = false;
						break;
					}else{
						clusterHeadResidualEnergy = wsnDACHNodes[i].getResidualEnergy();
					}

				}else{
					System.out.println("Node["+i+"]:" + wsnDACHNodes[i].isClusterHeadStatus() + ":" + System.currentTimeMillis() + ":" + wsnDACHNodes[i].getResidualEnergy());
				}
			}
			//wsnNodes[0].getNearestClusterHead().transmit(Constants.MESSAGE_PACKAET_SIZE * (wsnNodes.length - 1), EnvironmentConfiguration.baseStation);

		}
	}

	public static void selectAlternateClusterHead(){
		int highest = 0;
		int secondHighest = 0;
		int highest_i = 0;
		int secondHighest_i = 0;
		for (int i=0; i<wsnDACHNodes.length;i++) {

			// If we've found a new highest number...
			if (wsnDACHNodes[i].getChEligibilityIndex() > highest) {

				// ...shift the current highest number to second highest
				secondHighest = highest;
				secondHighest_i = highest_i;
				// ...and set the new highest.
				highest = wsnDACHNodes[i].getChEligibilityIndex();
				highest_i= i;
			} else if (wsnDACHNodes[i].getChEligibilityIndex() > secondHighest){
				// Just replace the second highest
				secondHighest = wsnDACHNodes[i].getChEligibilityIndex();
				secondHighest_i = i;
			}
		}
		System.out.println("highest["+highest_i+"] :::::" + highest + ", Second highest["+secondHighest_i+"] :::::" + secondHighest);
		for(int i=0; i<wsnDACHNodes.length;i++){
			wsnDACHNodes[i].setNearestAlternateClusterHead(wsnDACHNodes[secondHighest_i]);
			if(i != secondHighest_i){
				wsnDACHNodes[i].setAlternateClusterHeadStatus(false);
			}else{
				wsnDACHNodes[i].setAlternateClusterHeadStatus(true);
			}
		}
	}
}
