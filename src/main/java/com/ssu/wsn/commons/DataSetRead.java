package com.ssu.wsn.commons;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import com.ssu.wsn.deploy.WSNNode;

public class DataSetRead {

	public static void main(String[] args) {
		BufferedReader br = null;
		Set set = new HashSet();
		try {
			br = new BufferedReader(new FileReader("DataSets/out.txt"));
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    //System.out.println("First Line : " + line);
		    int count =0;
		    StringTokenizer st = null;
		    StringTokenizer st2 = null;
		    WSNNode node = null;
		    String[] tokenizedString = new String[9];
		    String[] location = new String[2]; 
		    List nodeList = new ArrayList();
		    while (line != null) {
		        //sb.append(line);
		        //sb.append(System.lineSeparator());
		    	System.out.println(line);
		    	st = new StringTokenizer(line," ");  
		    	int i = 0;
		    	while (st.hasMoreTokens()) {  
		    		tokenizedString[i] = st.nextToken();
		    		if(i==3){
		    			st2=new StringTokenizer(tokenizedString[i],",");
		    			int j=0;
		    			while(st2.hasMoreTokens()){
		    				location[j++]=st2.nextToken();
		    			}
		    		}
		    		i++;
		        } 
		    	node = new WSNNode(count,Double.parseDouble(location[0]), Double.parseDouble(location[1]), Double.parseDouble(tokenizedString[7]),Double.parseDouble(tokenizedString[8]),Double.parseDouble(tokenizedString[2]),Double.parseDouble(tokenizedString[7]), Double.parseDouble(tokenizedString[0]), Integer.parseInt(tokenizedString[1]));
		    	System.out.println(node);
		    	nodeList.add(node);
		    	
		    	set.add(node.getClusterId());
		    	//System.out.println(tokenizedString[8]);
		        line = br.readLine();
		        
		        count ++;
		    }
		    //String everything = sb.toString();
		    
		    System.out.println("Total Count :" + count);
		    System.out.println(set);
		    
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
		}
		    
		try{
			br.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		}
	}

