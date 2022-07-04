package com.ssu.wsn.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ssu.wsn.deploy.WSNNode;

public class ReadConfigurations {
	private static List<WSNNode> deployConfigDataList;
	public ReadConfigurations(){
		deployConfigDataList = new ArrayList<WSNNode>();
	}
	public static void main(String[] args) {
		ReadConfigurations obj = new ReadConfigurations();
		obj.deployAndLoad();
	}

	public void deployAndLoad() {
		String wsnConfigDataFile = "WSNDeployConfigData.dat";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int node_id;
		double location_x, location_y;
		double eps_ta, eps_r, eps_te;
		double radioRange;
		double residualEnergy;
		try {
			br = new BufferedReader(new FileReader(wsnConfigDataFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] wsnNodeData = line.split(cvsSplitBy);
				
					node_id = Integer.parseInt(wsnNodeData[0]);
					location_x = Double.parseDouble(wsnNodeData[1]);
					location_y = Double.parseDouble(wsnNodeData[2]);
					eps_ta = Double.parseDouble(wsnNodeData[3]);
					eps_r = Double.parseDouble(wsnNodeData[4]);
					eps_te = Double.parseDouble(wsnNodeData[5]);
					radioRange = Double.parseDouble(wsnNodeData[6]); 
					residualEnergy = Double.parseDouble(wsnNodeData[7]); 
					deployConfigDataList.add(new WSNNode(node_id
							, location_x
							, location_y
							, eps_te
							, eps_ta
							, eps_r
							, radioRange
							, residualEnergy));
				System.out.println("WSN Node [Node Id= " + node_id
						+ " , Loaction Y =" + location_y + "]");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");
	}
}
