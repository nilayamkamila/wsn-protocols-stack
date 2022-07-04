package com.ssu.wsn.process;

import com.ssu.wsn.deploy.WSNNode;

public class ClusterHeadElectionProcess {
	public static void election(WSNNode[] wsnNodes){
		
		for(int i=0; i< wsnNodes.length; i++){
			wsnNodes[i].sendPreElectionInfo(wsnNodes);
			/*for(int j=0; j< wsnNodes.length; j++){
				if(i != j)
					wsnNodes[i].sendPreElectionInfo(wsnNodes[j]);
			}*/
		}
		for(int i=0; i< wsnNodes.length; i++){
			wsnNodes[i].chEligibilityIndex();
		}
		
		for(int i=0; i< wsnNodes.length; i++){
			wsnNodes[i].selfUpdatePostElectionInfo();
		}
		/*for(int i=0; i< wsnNodes.length; i++){
			for(int j=0; j< wsnNodes.length; j++){
				if(i != j )
					wsnNodes[i].selfUpdatePostElectionInfo();
			}
			System.out.println("wsnNodes["+i+"] : " + wsnNodes[i]);
		}*/
		/*for(int i=0; i< wsnNodes.length; i++){
			System.out.println("wsnNodes["+i+"] : " + wsnNodes[i]);
		}*/
		for(int i=0; i< wsnNodes.length; i++){
			System.out.println("wsnNodes["+i+"] Residual Energy : " + wsnNodes[i].getResidualEnergy());
		}
	}



}
