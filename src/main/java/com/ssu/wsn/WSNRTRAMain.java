package com.ssu.wsn;

import com.ssu.wsn.commons.Constants;
import com.ssu.wsn.commons.Utils;
import com.ssu.wsn.config.EnvironmentConfiguration;
import com.ssu.wsn.deploy.WSNDACHNode;
import com.ssu.wsn.deploy.WSNNode;
import com.ssu.wsn.process.ClusterHeadElectionProcess;

public class WSNRTRAMain {
	static WSNNode[] wsnNodes = EnvironmentConfiguration.getWsnNodes();
	public static void main(String[] args) throws Exception{
		ClusterHeadElectionProcess.election(wsnNodes);
		//ClusterHeadElectionProcess.election(wsnDACHNodes);
		/*for(int i=0; i<wsnNodes.length;i++){
			System.out.println(wsnNodes[i]);
		}*/
		
		


		processFlooding();
		//processRTRA();
		

	}
public static void processFlooding(){
		
		boolean wsnAlive = true;
		int count = 0;
		double clusterHeadResidualEnergy = 0;
		String energyTotal ="";
		while(wsnAlive){
			System.out.println("*******************"+(count ++)+"*************************");
			energyTotal = energyTotal + "," + Utils.getTotalResidualEnergy(wsnNodes);
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
	
	boolean wsnAlive = true;
	int count = 0;
	double clusterHeadResidualEnergy = 0;
	String energyTotal ="";
	while(wsnAlive){
		System.out.println("*******************"+(count ++)+"*************************");
		energyTotal = energyTotal + "," + Utils.getTotalResidualEnergy(wsnNodes);
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
