package com.ssu.wsn;

import com.ssu.wsn.commons.Constants;
import com.ssu.wsn.config.EnvironmentConfiguration;
import com.ssu.wsn.deploy.BaseStation;
import com.ssu.wsn.deploy.WSNDACHNode;
import com.ssu.wsn.deploy.WSNNode;
import com.ssu.wsn.process.ClusterHeadElectionProcess;

public class WSNFloodingMain {
	static WSNNode[] wsnNodes = EnvironmentConfiguration.getWsnNodes();
	static WSNDACHNode[] wsnDACHNodes = EnvironmentConfiguration.getWsnDACHNodes();
	public static void main(String[] args) throws Exception{
		//ClusterHeadElectionProcess.election(wsnNodes);
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



		//processFlooding();

		processDACH();

	}

	public static void processFlooding(){
		
		boolean wsnAlive = true;
		int count = 0;
		double clusterHeadResidualEnergy = 0;
		while(wsnAlive){
			System.out.println("*******************"+(count ++)+"*************************");
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
		
		boolean wsnAlive = true;
		int count = 0;
		double clusterHeadResidualEnergy = 0;
		selectAlternateClusterHead();
		while(wsnAlive){
			System.out.println("*******************"+(count ++)+"*************************");
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
