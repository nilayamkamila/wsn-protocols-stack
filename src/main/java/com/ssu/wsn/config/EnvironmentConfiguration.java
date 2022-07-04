package com.ssu.wsn.config;

import com.ssu.wsn.deploy.BaseStation;
import com.ssu.wsn.deploy.WSNDACHNode;
import com.ssu.wsn.deploy.WSNNode;

public class EnvironmentConfiguration {
	public static WSNNode baseStation = new WSNNode(0, 11.1, 11.2, 0, 0, 0, 0, 100000000);
	public static WSNNode[] wsnNodes = new WSNNode[]{
			  new WSNNode(1, 10.3, 14.1, 10, 10, 5, 7.25, 10000000)	
			,  new WSNNode(2, 7.3, 17.1, 10, 10, 5, 7.25, 10000000)
			,  new WSNNode(3, 14.3, 15.1, 10, 10, 5, 7.25, 10000000)
			,  new WSNNode(4, 10.3, 12.1, 10, 10, 5, 7.25, 10000000)
			,  new WSNNode(5, 10.5, 14.2, 10, 10, 5, 7.25, 10000000)
			, new WSNNode(6,12.3, 13.1, 1, 1, .5, 7.25, 10000000)
			,  new WSNNode(7, 9.6, 11.2, 10, 10, 5, 7.25, 10000000)
			,  new WSNNode(8, 8.7, 10.1, 10, 10, 5, 7.25, 10000000)
			,  new WSNNode(9, 12.7, 13.4, 10, 10, 5, 7.25, 10000000)
			,  new WSNNode(10, 12.3, 13.1, 10, 10, 5, 7.25, 10000000)
			,  new WSNNode(11, 11.6, 12.2, 10, 10, 5, 7.25, 10000000)
			,  new WSNNode(12, 10.2, 11.8, 10, 10, 5, 7.25, 10000000)
			,  new WSNNode(13, 9.7, 10.1, 10, 10, 5, 7.25, 10000000)
	};

	public static WSNDACHNode[] wsnDACHNodes = new WSNDACHNode[]{
			  new WSNDACHNode(1, 10.3, 14.1, 10, 10, 5, 7.25, 10000000)	
			,  new WSNDACHNode(2, 7.3, 17.1, 10, 10, 5, 7.25, 10000000)
			,  new WSNDACHNode(3, 14.3, 15.1, 10, 10, 5, 7.25, 10000000)
			,  new WSNDACHNode(4, 10.3, 12.1, 10, 10, 5, 7.25, 10000000)
			,  new WSNDACHNode(5, 10.5, 14.2, 10, 10, 5, 7.25, 10000000)
			, new WSNDACHNode(6,12.3, 13.1, 1, 1, .5, 7.25, 10000000)
			,  new WSNDACHNode(7, 9.6, 11.2, 10, 10, 5, 7.25, 10000000)
			,  new WSNDACHNode(8, 8.7, 10.1, 10, 10, 5, 7.25, 10000000)
			,  new WSNDACHNode(9, 12.7, 13.4, 10, 10, 5, 7.25, 10000000)
			,  new WSNDACHNode(10, 12.3, 13.1, 10, 10, 5, 7.25, 10000000)
			,  new WSNDACHNode(11, 11.6, 12.2, 10, 10, 5, 7.25, 10000000)
			,  new WSNDACHNode(12, 10.2, 11.8, 10, 10, 5, 7.25, 10000000)
			,  new WSNDACHNode(13, 9.7, 10.1, 10, 10, 5, 7.25, 10000000)
	};
	public static WSNNode[] getWsnNodes() {
		return wsnNodes;
	}
	
	public static WSNDACHNode[] getWsnDACHNodes() {
		return wsnDACHNodes;
	}

}
