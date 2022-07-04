package com.ssu.wsn.deploy;


import java.util.ArrayList;
import java.util.List;

import com.ssu.wsn.commons.Constants;
import com.ssu.wsn.commons.Utils;
import com.ssu.wsn.config.EnvironmentConfiguration;

public class WSNDACHNode extends WSNNode{
	protected boolean alternateClusterHeadStatus;
	public boolean isAlternateClusterHeadStatus() {
		return alternateClusterHeadStatus;
	}


	public void setAlternateClusterHeadStatus(boolean alternateClusterHeadStatus) {
		this.alternateClusterHeadStatus = alternateClusterHeadStatus;
	}


	public WSNNode getNearestAlternateClusterHead() {
		return nearestAlternateClusterHead;
	}


	public void setNearestAlternateClusterHead(WSNNode nearestAlternateClusterHead) {
		this.nearestAlternateClusterHead = nearestAlternateClusterHead;
	}
	protected WSNNode nearestAlternateClusterHead;
	public WSNDACHNode(int node_id
			, double location_x
			, double location_y
			, double eps_te
			, double eps_ta
			, double eps_r
			, double radioRange
			, double residualEnergy){
		super(node_id
				, location_x
				, location_y
				, eps_te
				, eps_ta
				, eps_r
				, radioRange
				, residualEnergy);

		this.alternateClusterHeadStatus = false;
		this.nearestAlternateClusterHead = this;
	}


	/*public void selfUpdatePostElectionInfo(){
		int chEligibilityIndexMax = 0, chEligibilityIndexAlternateMax, chIndex = 0, alternateCHIndex = 0;
		for(int i=0; i<this.neighbours.size();i++){
			if(chEligibilityIndexMax < this.neighbours.get(i).chEligibilityIndex){
				chEligibilityIndexAlternateMax = chEligibilityIndexMax;
				alternateCHIndex = chIndex;
				chEligibilityIndexMax = this.neighbours.get(i).chEligibilityIndex;
				chIndex = i;
			}
		}


		for(int i=0; i<this.neighbours.size();i++){
			if(this.chEligibilityIndex <= this.neighbours.get(i).chEligibilityIndex 
					&& this.nearestClusterHead.chEligibilityIndex <= this.neighbours.get(i).chEligibilityIndex){
				this.nearestClusterHead = this.neighbours.get(i);
			}
		}
		if(this == this.nearestClusterHead)
			this.clusterHeadStatus = true;
	}*/

	public void selfUpdatePostElectionInfo(){
		int chEligibilityIndexMax = this.chEligibilityIndex, chIndex = 0;
		int chEligibilityAlternateIndexMax = this.chEligibilityIndex, chAlternateIndex = 0;
		double residualEnergyMax = this.residualEnergy; int residualEnergyMaxIndex = 0;

		for(int i=0; i<this.neighbours.size();i++){
			if(chEligibilityIndexMax <= this.neighbours.get(i).chEligibilityIndex){
				chEligibilityAlternateIndexMax = chEligibilityIndexMax;
				//chAlternateIndex = chIndex;
				chEligibilityIndexMax = this.neighbours.get(i).chEligibilityIndex;
				chIndex = i;
			}
			if(residualEnergyMax < this.neighbours.get(i).residualEnergy){
				residualEnergyMax = this.neighbours.get(i).residualEnergy;
				residualEnergyMaxIndex = i;
			}
		}
		/*for(int i=0; i<this.neighbours.size();i++){
			if(this.neighbours.get(i).chEligibilityIndex == chEligibilityAlternateIndexMax){
				chAlternateIndex = i;
				break;
			}
		}*/
		/*for(int i=0; i<this.neighbours.size();i++){
			if ((this.neighbours.get(i).chEligibilityIndex > chEligibilityAlternateIndexMax 
					&& this.neighbours.get(i).chEligibilityIndex < chEligibilityIndexMax)  || chEligibilityAlternateIndexMax == chEligibilityIndexMax)   
				chEligibilityAlternateIndexMax = this.neighbours.get(i).chEligibilityIndex;
				chAlternateIndex = i;
		}*/
		if(chEligibilityIndexMax != this.chEligibilityIndex){
			this.nearestClusterHead = this.neighbours.get(chIndex);
			this.clusterHeadStatus = false;
		}else if(residualEnergyMax != this.residualEnergy){
			this.nearestClusterHead = this.neighbours.get(residualEnergyMaxIndex);
			this.clusterHeadStatus = false;
		}else{
			this.nearestClusterHead = this;
			this.clusterHeadStatus = true;
		}
		if(this.clusterHeadStatus || this.chEligibilityIndex != chEligibilityAlternateIndexMax){
			this.nearestAlternateClusterHead = this.neighbours.get(chAlternateIndex);
			this.alternateClusterHeadStatus  = false;
		}else{
			this.nearestAlternateClusterHead = this;
			this.alternateClusterHeadStatus = true;
		}

	}
	public void transmitMessage(){
		if(this.residualEnergy > 0 
				&& findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, this.nearestClusterHead) > 0
				&& findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, this.nearestAlternateClusterHead) > 0){
			if(findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, this.nearestClusterHead) 
					+ this.nearestClusterHead.findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, EnvironmentConfiguration.baseStation) > 
			(findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, this.nearestAlternateClusterHead) 
					+ this.nearestAlternateClusterHead.findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, EnvironmentConfiguration.baseStation)
					)){
				System.out.println("Routing Via Alternate Cluster Head");
				transmit(Constants.MESSAGE_PACKAET_SIZE, this.nearestAlternateClusterHead);
				this.nearestAlternateClusterHead.receive(Constants.MESSAGE_PACKAET_SIZE);
				this.nearestAlternateClusterHead.transmit(Constants.MESSAGE_PACKAET_SIZE, EnvironmentConfiguration.baseStation);
			}else{
				transmit(Constants.MESSAGE_PACKAET_SIZE, this.nearestClusterHead);
				this.nearestClusterHead.receive(Constants.MESSAGE_PACKAET_SIZE);
				this.nearestClusterHead.transmit(Constants.MESSAGE_PACKAET_SIZE, EnvironmentConfiguration.baseStation);
			}

		}
	}
	public String toString(){
		return "Node Id : " + this.node_id 
				+ ",  Residual Energy " + this.residualEnergy
				+ ",  Self chEligibilityIndex " + this.chEligibilityIndex 
				+ ", cluster Head :" +  this.nearestClusterHead.node_id 
				+", Cluster Head chEligibilityIndex :" + this.nearestClusterHead.chEligibilityIndex
				+", Cluster Head Status :" + this.clusterHeadStatus
				+", Alternate Cluster Head  :" + this.nearestAlternateClusterHead.node_id
				//+", Alternate Cluster Head chEligibilityIndex :" + this.nearestAlternateClusterHead.chEligibilityIndex
				+", Alternate Cluster Head chEligibilityIndex :" + this.nearestAlternateClusterHead.chEligibilityIndex;
	}

}
