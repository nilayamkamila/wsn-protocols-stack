package com.ssu.wsn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ssu.wsn.commons.Constants;
import com.ssu.wsn.commons.Utils;
import com.ssu.wsn.config.EnvironmentConfiguration;
import com.ssu.wsn.deploy.BaseStation;
import com.ssu.wsn.deploy.WSNNode;
import com.ssu.wsn.graphics.WSNXYLineGenericChart2;
import com.ssu.wsn.process.ClusterHeadElectionProcess;

public class WSNNNEWMMain {
	public static double INITIAL_RESIDUAL_ENERGY =  1000000;
	public static final double EPS_TE = 1.0;
	public static final double EPS_TA = 1.0;
	public static final double EPS_R = 0.5;
	private static final double WIRELESS_RANGE = 7.25;
	public static final int TOTAL_NUMBER_NODES = 20;
	private static WSNNode[] wsnNodes = new WSNNode[TOTAL_NUMBER_NODES];
	private static List<Double> genericREList = new ArrayList<Double>();
	private static List<Double> nnewmREList = new ArrayList<Double>();
	private static int ELECTION_PACKET_SIZE = 512;
	enum GraphMode {ITERATION, RESIDUAL_ENERGY};
	private static GraphMode graphMode =  GraphMode.RESIDUAL_ENERGY;
	public static Map<String, double[]> seriesMap;
	public static double interationImprovement;
	public static double energyLebelEfficiency;
	public static void main(String[] args) throws Exception{
		invokeProtocol();
		WSNXYLineGenericChart2.plotGenericChart(seriesMap);
	}
	static BaseStation baseStation = null;
	public static void populateSeriesMap() {
		seriesMap = new HashMap<String, double[]>();
		seriesMap.put("Generic WSN", Utils.convertObject2Double(genericREList.toArray()));
		seriesMap.put("NNEWM", Utils.convertObject2Double(nnewmREList.toArray()));
	}
	public static void invokeProtocol() {
		genericREList = null;
		nnewmREList = null;
		genericREList = new ArrayList<Double>();
		nnewmREList = new ArrayList<Double>();
		Random random = new Random();
		long range = (long)10 - (long)0 + 1;
		baseStation = new BaseStation(Utils.roundTwoDecimals(
					range * random.nextDouble())
					, Utils.roundTwoDecimals(range * random.nextDouble()));
		for(int i=0; i<TOTAL_NUMBER_NODES; i++){
			System.out.println(Math.random());
			wsnNodes[i] = new WSNNode(1, Utils.roundTwoDecimals(
					range * random.nextDouble())
					, Utils.roundTwoDecimals(range * random.nextDouble())
					, EPS_TE
					, EPS_TA
					, EPS_R
					, WIRELESS_RANGE
					, INITIAL_RESIDUAL_ENERGY * Math.random());
		}
		//ClusterHeadElectionProcess.election(wsnNodes);
		processFlooding();
		processNNEWM();
		/*System.out.println(genericREList);
		System.out.println(nnewmREList);*/
		/*interationImprovement = ((double)(cidafREList.size() - genericREList.size())/cidafREList.size())*100;
		System.out.println("interationImprovement :" + interationImprovement);*/
		Double genericRETotalResidualEnergy = 0.0;
		Double nnewmRETotalResidualEnergy = 0.0;
		for(int i=1; i< genericREList.size(); i++){
			genericRETotalResidualEnergy+= genericREList.get(i);
		}
		for(int i=1; i< nnewmREList.size(); i++){
			nnewmRETotalResidualEnergy+= nnewmREList.get(i);
		}
		energyLebelEfficiency = ((nnewmRETotalResidualEnergy - genericRETotalResidualEnergy)/nnewmRETotalResidualEnergy)*100;
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

		for(int i=0; i<TOTAL_NUMBER_NODES; i++){
			for(int j=0; j<TOTAL_NUMBER_NODES; j++){
				if(i!=j){
					wsnNodes[i].transmit(ELECTION_PACKET_SIZE, wsnNodes[j]);
					wsnNodes[j].receive(ELECTION_PACKET_SIZE);
				}
			}
		}
		System.out.println("**********************Flooding**************************");
		for(int i=0; i<TOTAL_NUMBER_NODES; i++){
			genericREList.add(wsnNodes[i].getResidualEnergy());
			System.out.println("Residual Energy of Each Node :" + wsnNodes[i].getResidualEnergy());
		}

	}
	public static void processNNEWM(){
		resetResidualEnergy();
		boolean wsnAlive = true;
		int count = 0;
		double clusterHeadResidualEnergy = 0;
		String energyTotal ="";

		for(int i=0; i<TOTAL_NUMBER_NODES; i++){
			wsnNodes[i].transmit(ELECTION_PACKET_SIZE, baseStation);
			wsnNodes[i].receive(ELECTION_PACKET_SIZE);
		}
		System.out.println("**********************NNEWM**************************");
		for(int i=0; i<TOTAL_NUMBER_NODES; i++){
			nnewmREList.add(wsnNodes[i].getResidualEnergy());
			System.out.println("Residual Energy of Each Node :" + wsnNodes[i].getResidualEnergy());
		}

		/*

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
	System.out.println(energyTotal);*/
	}

}
