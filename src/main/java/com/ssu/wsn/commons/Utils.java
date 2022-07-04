package com.ssu.wsn.commons;

import java.text.DecimalFormat;
import java.util.Random;

import com.ssu.wsn.config.EnvironmentConfiguration;
import com.ssu.wsn.deploy.BaseStation;
import com.ssu.wsn.deploy.WSNNode;

public class Utils {
	public static double square(double i){
		return i*i;
	}
	public static double findDistance(WSNNode node1, WSNNode node2){
		double distance = square(node2.getLocation_y() - node1.getLocation_y()) + square(node2.getLocation_x() - node1.getLocation_x());
		return Math.sqrt(distance);
	}
	public static double findBaseStationDistance(WSNNode node1){
		WSNNode baseStation = EnvironmentConfiguration.baseStation;
		double distance = square(baseStation.getLocation_y() - node1.getLocation_y()) + square(baseStation.getLocation_x() - node1.getLocation_x());
		return Math.sqrt(distance);
	}
	public static WSNNode[] path2BaseStation(WSNNode[] wsnNodes, WSNNode source){
		WSNNode[] wsnViaNodes = new WSNNode[1];
		double minimum = 999999999.0;
		for (int i=0; i<wsnNodes.length; i++){
			if(minimum >Utils.findDistance(source, wsnNodes[i]) + Utils.findBaseStationDistance(wsnNodes[i])){
				minimum = Utils.findDistance(source, wsnNodes[i]) + Utils.findBaseStationDistance(wsnNodes[i]);
				wsnViaNodes[0] = wsnNodes[i];
			}
		}
		return wsnViaNodes;
	}
	public static double getTotalResidualEnergy(WSNNode[] wsnNodes){
		double totalResidualEnergy = 0.0;
		for (int i=0; i<wsnNodes.length; i++){
			if(wsnNodes[i].getResidualEnergy() > 0)
				totalResidualEnergy += wsnNodes[i].getResidualEnergy();
		}
		return totalResidualEnergy;
	}
	public static double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("###.##");
		return Double.valueOf(twoDForm.format(d));
	}
	private static final Random random = new Random();
	public static int randomInt(int endIndex) {
		return random.nextInt(endIndex);
	}
	public static double[] convertObject2Double(Object[] objects){
		double[] doublePrim = new double[objects.length];
		for(int i=0; i< objects.length; i++){
			doublePrim[i] = (Double)objects[i];
		}
		return doublePrim;
	}
	public static void main(String[] args){
		System.out.println(randomInt(5));
	}
}
