package com.ssu.wsn.process;

import com.ssu.wsn.commons.Utils;
import com.ssu.wsn.deploy.WSNNode;

public class NodeRoute2BS {
	public WSNNode[] path2BaseStation(WSNNode[] wsnNodes, WSNNode source){
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

}
