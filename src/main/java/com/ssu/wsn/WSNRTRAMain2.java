package com.ssu.wsn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ssu.wsn.commons.Constants;
import com.ssu.wsn.commons.Utils;
import com.ssu.wsn.config.EnvironmentConfiguration;
import com.ssu.wsn.deploy.WSNNode;
import com.ssu.wsn.graphics.WSNXYLineGenericChart2;
import com.ssu.wsn.process.ClusterHeadElectionProcess;

public class WSNRTRAMain2 {
	public static double INITIAL_RESIDUAL_ENERGY = 1000000;
	public static final double EPS_TE = 1.0;
	public static final double EPS_TA = 1.0;
	public static final double EPS_R = 0.5;
	private static final double WIRELESS_RANGE = 7.25;
	private static final int REFERENCE_INTERMEDIATE_NODE_ID_1 = 1;
	private static final int REFERENCE_INTERMEDIATE_NODE_ID_2 = 2;
	private static final int REFERENCE_CLISTER_HEAD_NODE_ID = 3;
	public static final int TOTAL_NUMBER_NODES = 10;
	private static WSNNode[] wsnNodes = new WSNNode[TOTAL_NUMBER_NODES];
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
	public static void populateSeriesMap() {
		seriesMap = new HashMap<String, double[]>();
	    seriesMap.put("Generic Flooding", Utils.convertObject2Double(genericREList.toArray()));
	    seriesMap.put("RTRA", Utils.convertObject2Double(cidafREList.toArray()));
	}
	public static void invokeProtocol() {
		genericREList = null;
		spinREList = null;
		cidafREList = null;
		genericREList = new ArrayList<Double>();
		spinREList = new ArrayList<Double>();
		cidafREList = new ArrayList<Double>();
		Random random = new Random();
		long range = (long)10 - (long)0 + 1;
		for(int i=0; i<TOTAL_NUMBER_NODES; i++){
			wsnNodes[i] = new WSNNode(1, Utils.roundTwoDecimals(
					range * random.nextDouble())
					, Utils.roundTwoDecimals(range * random.nextDouble())
					, EPS_TE
					, EPS_TA
					, EPS_R
					, WIRELESS_RANGE
					, INITIAL_RESIDUAL_ENERGY);
		}
		ClusterHeadElectionProcess.election(wsnNodes);
		processFlooding();
		processRTRA();
		System.out.println(genericREList);
		System.out.println(cidafREList);
		interationImprovement = ((double)(cidafREList.size() - genericREList.size())/cidafREList.size())*100;
		System.out.println("interationImprovement :" + interationImprovement);
		energyLebelEfficiency = ((cidafREList.get(genericREList.size()-1) - genericREList.get(genericREList.size()-1))/genericREList.get(genericREList.size()-1))*100;
		System.out.println("energyLebelEfficiency :" + energyLebelEfficiency);
		populateSeriesMap();
	}
	private static void resetResidualEnergy(){
		for(int i=0; i<wsnNodes.length; i++){
			wsnNodes[i].setResidualEnergy(INITIAL_RESIDUAL_ENERGY);
	}
	}
public static void processFlooding(){
	resetResidualEnergy();
		boolean wsnAlive = true;
		int count = 0;
		double clusterHeadResidualEnergy = 0;
		String energyTotal ="";
		while(wsnAlive){
			System.out.println("*******************"+(count ++)+"*************************");
			energyTotal = energyTotal + "," + Utils.getTotalResidualEnergy(wsnNodes);
			genericREList.add((double)Utils.getTotalResidualEnergy(wsnNodes));
			System.out.println("Total residual Energy :" + Utils.getTotalResidualEnergy(wsnNodes));
			for(int i=0; i<wsnNodes.length;i++){
				if(wsnNodes[i].getResidualEnergy() > 0 
						&& wsnNodes[i].findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, wsnNodes[i].getNearestClusterHead()) > 0 && !wsnNodes[i].isClusterHeadStatus()){
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
		System.out.println(energyTotal);
	}
public static void processRTRA(){
	resetResidualEnergy();
	boolean wsnAlive = true;
	int count = 0;
	double clusterHeadResidualEnergy = 0;
	String energyTotal ="";
	while(wsnAlive){
		System.out.println("*******************"+(count ++)+"*************************");
		energyTotal = energyTotal + "," + Utils.getTotalResidualEnergy(wsnNodes);
		cidafREList.add((double)Utils.getTotalResidualEnergy(wsnNodes));
		System.out.println("Total residual Energy :" + Utils.getTotalResidualEnergy(wsnNodes));
		WSNNode[] wsnNode = null;
		for(int i=0; i<wsnNodes.length;i++){
			wsnNode = Utils.path2BaseStation(wsnNodes,wsnNodes[i]);
		}
		for(int i=0; i<wsnNodes.length;i++){
			if(wsnNodes[i].getResidualEnergy() > 0 
					&& wsnNodes[i].findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, wsnNodes[i].getNearestClusterHead()) > 0 && !wsnNodes[i].isClusterHeadStatus() && !wsnNode.equals(wsnNodes[i])){
				wsnNodes[i].transmitRTRAMessage();
			}

			if(wsnNodes[i].isClusterHeadStatus()){
				System.out.println("Node["+i+"]:" + wsnNodes[i].isClusterHeadStatus() + ":" + System.currentTimeMillis() + ":" + wsnNodes[i].getResidualEnergy());
				if(wsnNodes[i].getResidualEnergy() <= 0 || clusterHeadResidualEnergy == wsnNodes[i].getResidualEnergy()){
					wsnAlive = false;
					break;
				}else{
					wsnNodes[i].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNode[0]);
					wsnNode[0].transmit(Constants.MESSAGE_PACKAET_SIZE, EnvironmentConfiguration.baseStation);
					clusterHeadResidualEnergy = wsnNodes[i].getResidualEnergy();
				}

			}else{
				System.out.println("Node["+i+"]:" + wsnNodes[i].isClusterHeadStatus() + ":" + System.currentTimeMillis() + ":" + wsnNodes[i].getResidualEnergy());
			}
		}
		//wsnNodes[0].getNearestClusterHead().transmit(Constants.MESSAGE_PACKAET_SIZE * (wsnNodes.length - 1), EnvironmentConfiguration.baseStation);

	}
	System.out.println(energyTotal);
}

}
