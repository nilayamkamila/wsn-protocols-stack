package com.ssu.wsn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ssu.wsn.WSNNIRMMain.GraphMode;
import com.ssu.wsn.commons.Constants;
import com.ssu.wsn.commons.Utils;
import com.ssu.wsn.deploy.BaseStation;
import com.ssu.wsn.deploy.WSNDACHNode;
import com.ssu.wsn.deploy.WSNNode;
import com.ssu.wsn.graphics.WSNXYLineGenericChart2;
import com.ssu.wsn.process.ClusterHeadElectionProcess;

public class WSNCAIRMain {
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
	private static WSNNode[] wsnNodesR1 = new WSNNode[TOTAL_NUMBER_NODES];
	private static WSNNode[] wsnNodesR2 = new WSNNode[TOTAL_NUMBER_NODES];
	private static List<Double> genericREListR1 = new ArrayList<Double>();
	private static List<Double> genericREListR2 = new ArrayList<Double>();
	private static List<Double> cairREListR1 = new ArrayList<Double>();
	private static List<Double> cairREListR2 = new ArrayList<Double>();
	enum GraphMode {ITERATION, RESIDUAL_ENERGY};
	private static GraphMode graphMode =  GraphMode.RESIDUAL_ENERGY;
	private static final int MESSAGE_RANDOMNESS = 50;
	private static int CLUSTER_HEAD_INDEX_R1 = 0;
	private static int CLUSTER_HEAD_INDEX_R2 = 0;
	private static BaseStation baseStation = null;
	public static Map<String, double[]> seriesMap = null;
	public static double interationImprovement;
	public static double energyLebelEfficiency;
	public static void main(String[] args) throws Exception{
		invokeProtocol();
		WSNXYLineGenericChart2.plotGenericChart(seriesMap);
	}
	public static void invokeProtocol() {
		genericREListR1 = new ArrayList<Double>();
		genericREListR2 = new ArrayList<Double>();
		cairREListR1 = new ArrayList<Double>();
		cairREListR2 = new ArrayList<Double>();
		Random random = new Random();

		long range = (long)10 - (long)0 + 1;
		/*for(int i=0;i<100;i++){
			System.out.println("Double Random Number is :" + Utils.roundTwoDecimals(range * random.nextDouble()));
		}*/

		baseStation = new BaseStation(Utils.roundTwoDecimals(
				range * random.nextDouble())
				, Utils.roundTwoDecimals(
						range * random.nextDouble()));
		for(int i=0; i<TOTAL_NUMBER_NODES; i++){
			wsnNodesR1[i] = new WSNNode(1
					, Utils.roundTwoDecimals(
							range * random.nextDouble())
					, Utils.roundTwoDecimals(
							range * random.nextDouble())
					, EPS_TE
					, EPS_TA
					, EPS_R
					, WIRELESS_RANGE
					, INITIAL_RESIDUAL_ENERGY);
			wsnNodesR2[i] = new WSNNode(1
					, Utils.roundTwoDecimals(
							range * random.nextDouble())
					, Utils.roundTwoDecimals(
							range * random.nextDouble())
					, EPS_TE
					, EPS_TA
					, EPS_R
					, WIRELESS_RANGE
					, INITIAL_RESIDUAL_ENERGY);
		}
		ClusterHeadElectionProcess.election(wsnNodesR1);
		ClusterHeadElectionProcess.election(wsnNodesR2);
		for(int i=0; i<TOTAL_NUMBER_NODES; i++){
			if(wsnNodesR1[i].isClusterHeadStatus()){
				CLUSTER_HEAD_INDEX_R1 = i;
			}
			if(wsnNodesR2[i].isClusterHeadStatus()){
				CLUSTER_HEAD_INDEX_R2 = i;
			}
		}
		processGeneric();
		processCAIR();
		System.out.println("genericREListR1 : " + genericREListR1);
		System.out.println("cairREListR1    : " + cairREListR1);
		
		System.out.println("genericREListR2 : " + genericREListR2);
		System.out.println("cairREListR2    : " + cairREListR2);
		
		if(genericREListR1.size() < genericREListR2.size()){
			System.out.println("Drawing GenericList1 " + genericREListR1.size());
		seriesMap = new HashMap<String, double[]>();
		seriesMap.put("Generic Routing", Utils.convertObject2Double(genericREListR1.toArray()));
		seriesMap.put("CAIR Routing", Utils.convertObject2Double(cairREListR1.toArray()));
		interationImprovement = ((double)(cairREListR1.size() - genericREListR1.size())/cairREListR1.size())*100;
		System.out.println("interationImprovement :" + interationImprovement);
		energyLebelEfficiency = ((cairREListR1.get(genericREListR1.size()-1) - genericREListR1.get(genericREListR1.size()-1))/genericREListR1.get(genericREListR1.size()-1))*100;
		System.out.println("energyLebelEfficiency :" + energyLebelEfficiency);
		//WSNXYLineGenericChart2.plotGenericChart(seriesMap);
		}else{
			System.out.println("Drawing GenericList2 " + genericREListR2.size());
			seriesMap = new HashMap<String, double[]>();
			seriesMap.put("Generic Routing", Utils.convertObject2Double(genericREListR2.toArray()));
			seriesMap.put("CAIR Routing", Utils.convertObject2Double(cairREListR2.toArray()));
			interationImprovement = ((double)(cairREListR2.size() - genericREListR2.size())/cairREListR2.size())*100;
			System.out.println("interationImprovement :" + interationImprovement);
			energyLebelEfficiency = ((cairREListR2.get(genericREListR2.size()-1) - genericREListR2.get(genericREListR2.size()-1))/genericREListR2.get(genericREListR2.size()-1))*100;
			System.out.println("energyLebelEfficiency :" + energyLebelEfficiency);
		//WSNXYLineGenericChart2.plotGenericChart(seriesMap);
		}
	}
	private static void resetResidualEnergy(){
		for(int i=0; i<TOTAL_NUMBER_NODES; i++){
			wsnNodesR1[i].setResidualEnergy(INITIAL_RESIDUAL_ENERGY);
			wsnNodesR2[i].setResidualEnergy(INITIAL_RESIDUAL_ENERGY);
		}
	}
	public static void processGeneric(){
		resetResidualEnergy();
		boolean wsnAlive = true;
		int previousCount = -1;
		int currentCount = 0;
		double clusterHeadResidualEnergy = 0;

		while(wsnAlive){
			System.out.println("Heloo " + currentCount + ":::" + previousCount);
			if(previousCount == currentCount){
				wsnAlive = false;
				break;
			}else{
				previousCount = currentCount;
			}
			for(int i=0; i<TOTAL_NUMBER_NODES; i++){
				if(i != CLUSTER_HEAD_INDEX_R1){
					if(wsnNodesR1[i].getResidualEnergy() > 0 && wsnNodesR1[CLUSTER_HEAD_INDEX_R1].getResidualEnergy() > 0)
						wsnNodesR1[i].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNodesR1[CLUSTER_HEAD_INDEX_R1]);
				}
				if(i != CLUSTER_HEAD_INDEX_R2){
					if(wsnNodesR2[i].getResidualEnergy() > 0 && wsnNodesR2[CLUSTER_HEAD_INDEX_R2].getResidualEnergy() > 0)
						wsnNodesR2[i].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNodesR2[CLUSTER_HEAD_INDEX_R2]);
				}

			}
			if(wsnNodesR1[CLUSTER_HEAD_INDEX_R1].getResidualEnergy() > 0){
				wsnNodesR1[CLUSTER_HEAD_INDEX_R1].transmit(Constants.MESSAGE_PACKAET_SIZE*TOTAL_NUMBER_NODES, baseStation);
				currentCount ++;
			}
			if(wsnNodesR2[CLUSTER_HEAD_INDEX_R2].getResidualEnergy() > 0){
				wsnNodesR2[CLUSTER_HEAD_INDEX_R2].transmit(Constants.MESSAGE_PACKAET_SIZE*TOTAL_NUMBER_NODES, baseStation);
				currentCount ++;
			}

			/*if(wsnNodesR1[CLUSTER_HEAD_INDEX_R1].getResidualEnergy() > 0){
				count_R1 ++;
			}
			if(wsnNodesR2[CLUSTER_HEAD_INDEX_R2].getResidualEnergy() > 0){
				count_R2 ++;
			}*/
			/*if(wsnNodesR1[CLUSTER_HEAD_INDEX_R1].getResidualEnergy() < 0 && wsnNodesR2[CLUSTER_HEAD_INDEX_R2].getResidualEnergy() < 0){
				wsnAlive = false;
				break;
			}*/
			//count ++;
			double remainingResidualEnergy = 0f;
			for(int i=0; i<TOTAL_NUMBER_NODES; i++){
				if(wsnNodesR1[CLUSTER_HEAD_INDEX_R1].getResidualEnergy() > 0 && wsnNodesR1[i].getResidualEnergy() > 0)
				remainingResidualEnergy += wsnNodesR1[i].getResidualEnergy();
			}
			if(remainingResidualEnergy > 0)
				genericREListR1.add(remainingResidualEnergy);
			remainingResidualEnergy = 0f;
			for(int i=0; i<TOTAL_NUMBER_NODES; i++){
				if(wsnNodesR2[CLUSTER_HEAD_INDEX_R2].getResidualEnergy() > 0 && wsnNodesR2[i].getResidualEnergy() > 0)
				remainingResidualEnergy += wsnNodesR2[i].getResidualEnergy();
			}
			if(remainingResidualEnergy > 0)
				genericREListR2.add(remainingResidualEnergy);
		}
		/*System.out.println("Total Network Iteration :" + count);
		System.out.println("Total Network Iteration :" + count_R1);
		System.out.println("Total Network Iteration :" + count_R2);*/
	}
	public static void processCAIR(){
		resetResidualEnergy();
		boolean wsnAlive = true;
		int previousCount = -1;
		int currentCount = 0;
		double clusterHeadResidualEnergy = 0;
		int count_R1 = 0;
		int count_R2 = 0;
		while(wsnAlive){
			if(previousCount == currentCount){
				wsnAlive = false;
				break;
			}else{
				previousCount = currentCount;
			}
			for(int i=0; i<TOTAL_NUMBER_NODES; i++){
				if(i != CLUSTER_HEAD_INDEX_R1){
					if(wsnNodesR1[i].getResidualEnergy() > 0){
						wsnNodesR1[i].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNodesR1[i].getNearestClusterHead());
						}
					}
				if(i != CLUSTER_HEAD_INDEX_R2){
					if(wsnNodesR2[i].getResidualEnergy() > 0){
						wsnNodesR2[i].transmit(Constants.MESSAGE_PACKAET_SIZE, wsnNodesR2[i].getNearestClusterHead());
						}
				}

			}
			if(wsnNodesR1[CLUSTER_HEAD_INDEX_R1].getResidualEnergy() > 0){
				wsnNodesR1[CLUSTER_HEAD_INDEX_R1].transmit(Constants.MESSAGE_PACKAET_SIZE*TOTAL_NUMBER_NODES, baseStation);
				currentCount ++;
			}
			if(wsnNodesR2[CLUSTER_HEAD_INDEX_R2].getResidualEnergy() > 0){
				wsnNodesR2[CLUSTER_HEAD_INDEX_R2].transmit(Constants.MESSAGE_PACKAET_SIZE*TOTAL_NUMBER_NODES, baseStation);
				currentCount ++;
			}
			double remainingResidualEnergy = 0f;
			for(int i=0; i<TOTAL_NUMBER_NODES; i++){
				if((wsnNodesR1[CLUSTER_HEAD_INDEX_R1].getResidualEnergy() > 0 || wsnNodesR2[CLUSTER_HEAD_INDEX_R2].getResidualEnergy() > 0) && wsnNodesR1[i].getResidualEnergy() > 0)
				remainingResidualEnergy += wsnNodesR1[i].getResidualEnergy();
			}
			if(remainingResidualEnergy > 0)
				cairREListR1.add(remainingResidualEnergy);
			remainingResidualEnergy = 0f;
			for(int i=0; i<TOTAL_NUMBER_NODES; i++){
				if((wsnNodesR1[CLUSTER_HEAD_INDEX_R1].getResidualEnergy() > 0 || wsnNodesR2[CLUSTER_HEAD_INDEX_R2].getResidualEnergy() > 0) && wsnNodesR2[i].getResidualEnergy() > 0)
				remainingResidualEnergy += wsnNodesR2[i].getResidualEnergy();
			}
			if(remainingResidualEnergy > 0)
				cairREListR2.add(remainingResidualEnergy);
			if(wsnNodesR1[CLUSTER_HEAD_INDEX_R1].getResidualEnergy() < 0 || wsnNodesR2[CLUSTER_HEAD_INDEX_R2].getResidualEnergy() < 0){
				if(wsnNodesR1[CLUSTER_HEAD_INDEX_R1].getResidualEnergy() < 0){
					boolean status = false;
					remainingResidualEnergy = 0f;
					for(int i = 0; i< TOTAL_NUMBER_NODES; i++){
						if(wsnNodesR1[i].getResidualEnergy()>0){
							status = true;
							wsnNodesR1[i].setNearestClusterHead(wsnNodesR2[CLUSTER_HEAD_INDEX_R2]);
							remainingResidualEnergy += wsnNodesR1[i].getResidualEnergy();
						}
					}
					//cairREListR1.add(remainingResidualEnergy);
					if(status){
						count_R1 ++;
					}
				}else if(wsnNodesR2[CLUSTER_HEAD_INDEX_R2].getResidualEnergy() < 0){
					boolean status = false;
					remainingResidualEnergy = 0f;
					for(int i = 0; i< TOTAL_NUMBER_NODES; i++){
						if(wsnNodesR2[i].getResidualEnergy()>0){
							status = true;
							wsnNodesR2[i].setNearestClusterHead(wsnNodesR1[CLUSTER_HEAD_INDEX_R1]);
							remainingResidualEnergy += wsnNodesR2[i].getResidualEnergy();
						}
					}
					//cairREListR2.add(remainingResidualEnergy);
					if(status){
						count_R2 ++;
					}
				}
			}else{
				count_R1 ++;
				count_R2 ++;
			}
			/*if(wsnNodesR1[CLUSTER_HEAD_INDEX_R1].getResidualEnergy() < 0 && wsnNodesR2[CLUSTER_HEAD_INDEX_R2].getResidualEnergy() < 0){
				wsnAlive = false;
				break;
			}*/
			//count ++;
			/*double remainingResidualEnergy = 0f;
			for(int i=0; i<TOTAL_NUMBER_NODES; i++){
				remainingResidualEnergy += wsnNodesR1[i].getResidualEnergy();
			}
			if(remainingResidualEnergy > 0)
				cairREListR1.add(remainingResidualEnergy);
			remainingResidualEnergy = 0f;
			for(int i=0; i<TOTAL_NUMBER_NODES; i++){
				remainingResidualEnergy += wsnNodesR2[i].getResidualEnergy();
			}
			if(remainingResidualEnergy > 0)
				cairREListR2.add(remainingResidualEnergy);*/
		}
		/*System.out.println(" processCAIR : Total Network Iteration 2:" + count);
		System.out.println(" processCAIR : Total Network Iteration :" + count_R1);
		System.out.println(" processCAIR : Total Network Iteration :" + count_R2);*/
	}



}
