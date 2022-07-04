package com.ssu.wsn.deploy;


import java.util.ArrayList;
import java.util.List;

import com.ssu.wsn.commons.Constants;
import com.ssu.wsn.commons.Utils;
import com.ssu.wsn.config.EnvironmentConfiguration;

public class WSNNode {
	int node_id;
	double location_x, location_y;
	double eps_ta, eps_r, eps_te;
	boolean clusterHeadStatus;
	double radioRange;
	double residualEnergy;
	int chEligibilityIndex;
	List<WSNNode> neighbours;
	WSNNode nearestClusterHead;
	private int clusterId;
	

	public WSNNode(int node_id
			, double location_x
			, double location_y
			, double eps_te
			, double eps_ta
			, double eps_r
			, double radioRange
			, double residualEnergy){
		this.node_id = node_id;
		this.location_x = location_x;
		this.location_y = location_y;
		this.eps_te = eps_te;
		this.eps_ta = eps_ta;
		this.eps_r = eps_r;
		this.clusterHeadStatus = false;
		this.radioRange = radioRange;
		this.residualEnergy = residualEnergy;
		this.neighbours = new ArrayList<WSNNode>();
		this.nearestClusterHead = this;
	}
	public WSNNode(int node_id
			, double location_x
			, double location_y
			, double eps_te
			, double eps_ta
			, double eps_r
			, double radioRange
			, double residualEnergy, int clusterId){
		this(node_id,location_x,location_y,eps_te,eps_ta,eps_r,radioRange,residualEnergy);
		this.clusterId = clusterId;
	}

	public int getNode_id() {
		return node_id;
	}

	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}

	public double getLocation_x() {
		return location_x;
	}

	public void setLocation_x(double location_x) {
		this.location_x = location_x;
	}

	public double getLocation_y() {
		return location_y;
	}

	public void setLocation_y(double location_y) {
		this.location_y = location_y;
	}

	public double getEps_ta() {
		return eps_ta;
	}

	public void setEps_ta(double eps_ta) {
		this.eps_ta = eps_ta;
	}

	public double getEps_r() {
		return eps_r;
	}

	public void setEps_r(double eps_r) {
		this.eps_r = eps_r;
	}

	public double getEps_te() {
		return eps_te;
	}

	public void setEps_te(double eps_te) {
		this.eps_te = eps_te;
	}

	public boolean isClusterHeadStatus() {
		return clusterHeadStatus;
	}

	public void setClusterHeadStatus(boolean clusterHeadStatus) {
		this.clusterHeadStatus = clusterHeadStatus;
	}

	public double getRadioRange() {
		return radioRange;
	}

	public void setRadioRange(double radioRange) {
		this.radioRange = radioRange;
	}

	public double getResidualEnergy() {
		return residualEnergy;
	}

	public void setResidualEnergy(double residualEnergy) {
		this.residualEnergy = residualEnergy;
	}

	public int getChEligibilityIndex() {
		return chEligibilityIndex;
	}

	public void setChEligibilityIndex(int chEligibilityIndex) {
		this.chEligibilityIndex = chEligibilityIndex;
	}

	public List<WSNNode> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(List<WSNNode> neighbours) {
		this.neighbours = neighbours;
	}

	public WSNNode getNearestClusterHead() {
		return nearestClusterHead;
	}

	public void setNearestClusterHead(WSNNode nearestClusterHead) {
		this.nearestClusterHead = nearestClusterHead;
	}
	public int getClusterId() {
		return clusterId;
	}

	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}
	public double findTransmissionEnergy(int p_d, WSNNode wsnNode){
		double energy = 0;
		if(this != wsnNode){
			double d_2 = Utils.square(this.location_y - wsnNode.location_y) + Utils.square(this.location_x - wsnNode.location_x);
			energy = this.eps_te * p_d + (this.eps_ta * p_d * d_2);
		}
		return energy;
	}
	
	public double findTransmissionEnergy(int p_d, WSNNode wsnNode1, WSNNode wsnNode2){
		double energy = 0;
		if(wsnNode1 != wsnNode2){
			double d_2 = Utils.square(wsnNode1.location_y - wsnNode2.location_y) + Utils.square(wsnNode1.location_x - wsnNode2.location_x);
			energy = wsnNode1.eps_te * p_d + (wsnNode1.eps_ta * p_d * d_2);
		}
		return energy;
	}
	
	public double findTransmissionEnergy(int p_d, BaseStation baseStation){
		double energy = 0;
		double d_2 = Utils.square(this.location_y - baseStation.location_y) + Utils.square(this.location_x - baseStation.location_x);
			energy = this.eps_te * p_d + (this.eps_ta * p_d * d_2);
		return energy;
	}
	
	public void transmit(int p_d, WSNNode wsnNode){
		this.residualEnergy -=findTransmissionEnergy(p_d, wsnNode);
	}
	public void transmit(int p_d, BaseStation baseStation){
		this.residualEnergy -=findTransmissionEnergy(p_d, baseStation);
	}
	public double findReceivingEnergy(int p_d){
		double energy =  0;
		energy = this.eps_r * p_d;
		return energy;
	}
	public void receive(int p_d){
		this.residualEnergy -=findReceivingEnergy(p_d);
	}

	public void chEligibilityIndex(){
		int transmissionIteration = 0;
		double currentResidualEnergyCapacity = this.residualEnergy;
		while(currentResidualEnergyCapacity > 0){
			for(int i=0; i<this.neighbours.size();i++){
				currentResidualEnergyCapacity -= findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, this.neighbours.get(i));				
			}
			transmissionIteration++;
		}
		this.chEligibilityIndex = transmissionIteration;
	}
	public void sendPreElectionInfo(WSNNode[] wsnNodes){
		for(int i=0; i<wsnNodes.length;i++){
			if(this != wsnNodes[i]){
				this.neighbours.add(wsnNodes[i]);
				transmit(Constants.ELECTION_PACKAET_SIZE, wsnNodes[i]);
			}
		}
	}
	/*public void selfUpdatePostElectionInfo(){
		for(int i=0; i<this.neighbours.size();i++){
			if(this.chEligibilityIndex <= this.neighbours.get(i).chEligibilityIndex && this.nearestClusterHead.chEligibilityIndex <= this.neighbours.get(i).chEligibilityIndex){
				this.nearestClusterHead = this.neighbours.get(i);
			}
		}
		if(this == this.nearestClusterHead)
			this.clusterHeadStatus = true;
	}*/
	
	public void selfUpdatePostElectionInfo(){
		int chEligibilityIndexMax = this.chEligibilityIndex, chIndex = 0;
		double residualEnergyMax = this.residualEnergy; int residualEnergyMaxIndex = 0;
		for(int i=0; i<this.neighbours.size();i++){
			if(chEligibilityIndexMax < this.neighbours.get(i).chEligibilityIndex){
				chEligibilityIndexMax = this.neighbours.get(i).chEligibilityIndex;
				chIndex = i;
			}
			if(residualEnergyMax < this.neighbours.get(i).residualEnergy){
				residualEnergyMax = this.neighbours.get(i).residualEnergy;
				residualEnergyMaxIndex = i;
			}
		}
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
	}
	public void transmitMessage(){
		if(this.residualEnergy > 0 && findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, this.nearestClusterHead) > 0){
			transmit(Constants.MESSAGE_PACKAET_SIZE, this.nearestClusterHead);
			this.nearestClusterHead.receive(Constants.MESSAGE_PACKAET_SIZE);
			this.nearestClusterHead.transmit(Constants.MESSAGE_PACKAET_SIZE, EnvironmentConfiguration.baseStation);
			/*for(int i=0; i<this.neighbours.size();i++){
				if(this.neighbours.get(i).clusterHeadStatus){
					transmit(Constants.MESSAGE_PACKAET_SIZE, this.neighbours.get(i));
					this.neighbours.get(i).receive(Constants.MESSAGE_PACKAET_SIZE);
				}
			}*/
		}
	}
	public void transmitRTRAMessage(){
		if(this.residualEnergy > 0 && findTransmissionEnergy(Constants.MESSAGE_PACKAET_SIZE, this.nearestClusterHead) > 0){
			transmit(Constants.MESSAGE_PACKAET_SIZE, this.nearestClusterHead);
			this.nearestClusterHead.receive(Constants.MESSAGE_PACKAET_SIZE);
			//this.nearestClusterHead.transmit(Constants.MESSAGE_PACKAET_SIZE, EnvironmentConfiguration.baseStation);
			/*for(int i=0; i<this.neighbours.size();i++){
				if(this.neighbours.get(i).clusterHeadStatus){
					transmit(Constants.MESSAGE_PACKAET_SIZE, this.neighbours.get(i));
					this.neighbours.get(i).receive(Constants.MESSAGE_PACKAET_SIZE);
				}
			}*/
		}
	}
	public String toString(){
		return "Node Id : " + this.node_id 
				+ ",  Location : " + this.location_x +"," +  this.location_y
				+ ",  Self chEligibilityIndex " + this.chEligibilityIndex 
				+ ", cluster Head :" +  this.nearestClusterHead.node_id 
				+", Cluster Head chEligibilityIndex :" + this.nearestClusterHead.chEligibilityIndex
				+", Cluster Head Status :" + this.clusterHeadStatus;
	}

}
