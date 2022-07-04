package com.ssu.wsn.graphics.frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.ssu.wsn.WSNCAIRMain;
import com.ssu.wsn.WSNCIDAFMain;
import com.ssu.wsn.DACHMain;
import com.ssu.wsn.WSNMNFMain;
import com.ssu.wsn.WSNNIRMMain;
import com.ssu.wsn.WSNPISSMain;
import com.ssu.wsn.WSNRTRAMain2;
import com.ssu.wsn.WSNNNEWMMain;
import com.ssu.wsn.graphics.WSNXYLineGenericChart2;

public class WSNMainHomePane
extends 	JFrame
{
	private		JTabbedPane tabbedPane;
	private		JPanel		panel1;
	private		JPanel		panel2;
	private		JPanel		panel3;
	private		JPanel		panel4;
	private		JPanel		panel5;
	private		JPanel		panel6;
	private		JPanel		panel7;
	private		JPanel		panel8;



	public WSNMainHomePane()
	{
		// NOTE: to reduce the amount of code in this example, it uses
		// panels with a NULL layout.  This is NOT suitable for
		// production code since it may not display correctly for
		// a look-and-feel.

		setTitle( "WSN Protocols Study" );
		setSize( 800, 750 );
		setBackground( Color.gray );

		JPanel topPanel = new JPanel();
		JLabel titleLabel = new JLabel("Wireless Sensor Network Protocol Performances" );
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		topPanel.add(titleLabel);
		/*topPanel.add( new JButton( "North" ), BorderLayout.NORTH );
		topPanel.add( new JButton( "South" ), BorderLayout.SOUTH );*/
		//topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );

		// Create the tab pages
		pageRTRA();
		pageDACH();
		pageCIDAF();
		pageMNF();
		pagePISS();
		pageNIRM();
		pageCAIR();
		pageNNEWM();

		// Create a tabbed pane
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab( "RTRA", panel1 );
		tabbedPane.addTab( "DACH", panel2 );
		tabbedPane.addTab( "CIDAF", panel3 );
		tabbedPane.addTab( "MNF", panel4 );
		tabbedPane.addTab( "PISS", panel5 );
		tabbedPane.addTab( "NIRM", panel6 );
		tabbedPane.addTab( "CAIR", panel7 );
		tabbedPane.addTab( "NNEWM", panel8 );
		topPanel.add( tabbedPane, BorderLayout.CENTER );
	}

	public void pageRTRA()
	{
		panel1 = new JPanel();
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		//topPanel.setBackground(Color.black);
		JButton btnRTRAInvoke = new JButton ("Invoke RTRA");
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		//topPanel.setBorder(raisedetched);
		topPanel.add(btnRTRAInvoke, BorderLayout.EAST);
		JPanel topInnerWestPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		topInnerWestPanel.setSize(10,10);
		/*JPanel energyPanel = new JPanel();
		energyPanel.add(new JLabel("Residual Energy          :"));
		JTextField txtInitailEnergy=new JTextField(""+WSNRTRAMain2.INITIAL_RESIDUAL_ENERGY);
		energyPanel.add(txtInitailEnergy);
		topInnerWestPanel.add(energyPanel);*/
		topInnerWestPanel.add(new JLabel("Residual Energy          :" + WSNRTRAMain2.INITIAL_RESIDUAL_ENERGY));
		topInnerWestPanel.add(new JLabel("Transmission Power :" + WSNRTRAMain2.EPS_TE));
		topInnerWestPanel.add(new JLabel("Amplifier Power          :" + WSNRTRAMain2.EPS_TA));
		topInnerWestPanel.add(new JLabel("Receiver Power           :" + WSNRTRAMain2.EPS_R));
		topInnerWestPanel.add(new JLabel("Number of Nodes        :" + WSNRTRAMain2.TOTAL_NUMBER_NODES));
		//topInnerWestPanel.setBorder(raisedetched);
		topInnerWestPanel.setBackground(Color.LIGHT_GRAY);
		topPanel.add(topInnerWestPanel, BorderLayout.NORTH);
		//topPanel.setBorder(raisedetched);
		topPanel.setBackground(Color.LIGHT_GRAY);
		JPanel bottomPanel = new JPanel();
		JLabel label = new JLabel("Hello " + Math.random());
		WSNRTRAMain2.invokeProtocol();
		WSNXYLineGenericChart2 wSNXYLineGenericChart2 = new WSNXYLineGenericChart2(WSNRTRAMain2.seriesMap);
		panel1.setLayout(new BorderLayout());
		panel1.add(topPanel, BorderLayout.NORTH);
		panel1.add(bottomPanel, BorderLayout.CENTER);
		WSNRTRAMain2.seriesMap = null;
		WSNRTRAMain2.invokeProtocol();
		bottomPanel.setName("RTRA Panel");
		bottomPanel.add(new WSNXYLineGenericChart2(WSNRTRAMain2.seriesMap).getChartPanel());
		
		JPanel bottomLowerPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		bottomLowerPanel.setName("Efficiency Results");
		JLabel jlabelResults = new JLabel("Efficiency Results");
		jlabelResults.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		bottomLowerPanel.add(jlabelResults);
		bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNRTRAMain2.energyLebelEfficiency + "%"), BorderLayout.CENTER);
		bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNRTRAMain2.interationImprovement + "%"));
		bottomLowerPanel.setBorder(raisedetched);
		bottomLowerPanel.setBackground(Color.GRAY);
		panel1.add(bottomLowerPanel, BorderLayout.SOUTH);
		topPanel.add(label, BorderLayout.SOUTH);
		label.setText("Date & Time :"+ new Date());
		btnRTRAInvoke.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bottomPanel.repaint();
				bottomPanel.removeAll();
				topPanel.add(label, BorderLayout.SOUTH);
				label.setText("Date & Time :"+ new Date());
				WSNRTRAMain2.seriesMap = null;
				//WSNRTRAMain2.INITIAL_RESIDUAL_ENERGY = Double.parseDouble(txtInitailEnergy.getText());
				WSNRTRAMain2.invokeProtocol();
				bottomPanel.setName("RTRA Panel");
				bottomPanel.add(new WSNXYLineGenericChart2(WSNRTRAMain2.seriesMap).getChartPanel());
				
				bottomLowerPanel.removeAll();
				bottomLowerPanel.repaint();
				bottomLowerPanel.add(jlabelResults);
				bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNRTRAMain2.energyLebelEfficiency + "%"), BorderLayout.CENTER);
				bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNRTRAMain2.interationImprovement + "%"));
				bottomLowerPanel.setBorder(raisedetched);
				
			}
		});
	}

	public void pageDACH()
	{
		panel2 = new JPanel();
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		//topPanel.setBackground(Color.black);
		JButton btnRTRAInvoke = new JButton ("Invoke");
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		//topPanel.setBorder(raisedetched);
		topPanel.add(btnRTRAInvoke, BorderLayout.EAST);
		JPanel topInnerWestPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		topInnerWestPanel.setSize(10,10);
		topInnerWestPanel.add(new JLabel("Residual Energy          :" + DACHMain.INITIAL_RESIDUAL_ENERGY));
		topInnerWestPanel.add(new JLabel("Transmission Power :" + DACHMain.EPS_TE));
		topInnerWestPanel.add(new JLabel("Amplifier Power          :" + DACHMain.EPS_TA));
		topInnerWestPanel.add(new JLabel("Receiver Power           :" + DACHMain.EPS_R));
		topInnerWestPanel.add(new JLabel("Number of Nodes        :" + DACHMain.TOTAL_NUMBER_NODES));
		topPanel.add(topInnerWestPanel, BorderLayout.NORTH);
		JPanel bottomPanel = new JPanel();
		JLabel label = new JLabel("Hello " + Math.random());
		DACHMain.invokeProtocol();
		WSNXYLineGenericChart2 wSNXYLineGenericChart2 = new WSNXYLineGenericChart2(DACHMain.seriesMap);
		panel2.setLayout(new BorderLayout());
		panel2.add(topPanel, BorderLayout.NORTH);
		panel2.add(bottomPanel, BorderLayout.CENTER);
		bottomPanel.setName("DACH Panel");
		bottomPanel.add(new WSNXYLineGenericChart2(DACHMain.seriesMap).getChartPanel());
		
		JPanel bottomLowerPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		bottomLowerPanel.setName("Efficiency Results");
		JLabel jlabelResults = new JLabel("Efficiency Results");
		jlabelResults.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		bottomLowerPanel.add(jlabelResults);
		bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + DACHMain.energyLebelEfficiency + "%"), BorderLayout.CENTER);
		bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + DACHMain.interationImprovement + "%"));
		bottomLowerPanel.setBorder(raisedetched);
		bottomLowerPanel.setBackground(Color.GRAY);
		panel2.add(bottomLowerPanel, BorderLayout.SOUTH);
		
		topPanel.add(label, BorderLayout.SOUTH);
		label.setText("Date & Time :"+ new Date());
		btnRTRAInvoke.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bottomPanel.repaint();
				bottomPanel.removeAll();
				topPanel.add(label, BorderLayout.SOUTH);
				label.setText("Date & Time :"+ new Date());
				DACHMain.invokeProtocol();
				bottomPanel.setName("DACH Panel");
				bottomPanel.add(new WSNXYLineGenericChart2(DACHMain.seriesMap).getChartPanel());
				
				bottomLowerPanel.removeAll();
				bottomLowerPanel.repaint();
				bottomLowerPanel.add(jlabelResults);
				bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + DACHMain.energyLebelEfficiency + "%"), BorderLayout.CENTER);
				bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + DACHMain.interationImprovement + "%"));
				bottomLowerPanel.setBorder(raisedetched);
			}
		});
	}

	public void pageCIDAF()
	{
		panel3 = new JPanel();
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		//topPanel.setBackground(Color.black);
		JButton btnRTRAInvoke = new JButton ("Invoke CIDAF");
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		//topPanel.setBorder(raisedetched);
		topPanel.add(btnRTRAInvoke, BorderLayout.EAST);
		JPanel topInnerWestPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		topInnerWestPanel.setSize(10,10);
		topInnerWestPanel.add(new JLabel("Residual Energy          :" + WSNCIDAFMain.INITIAL_RESIDUAL_ENERGY));
		topInnerWestPanel.add(new JLabel("Transmission Power :" + WSNCIDAFMain.EPS_TE));
		topInnerWestPanel.add(new JLabel("Amplifier Power          :" + WSNCIDAFMain.EPS_TA));
		topInnerWestPanel.add(new JLabel("Receiver Power           :" + WSNCIDAFMain.EPS_R));
		topInnerWestPanel.add(new JLabel("Number of Nodes        :" + WSNCIDAFMain.TOTAL_NUMBER_NODES));
		topPanel.add(topInnerWestPanel, BorderLayout.NORTH);
		JPanel bottomPanel = new JPanel();
		JLabel label = new JLabel("Hello " + Math.random());
		WSNCIDAFMain.invokeProtocol();
		WSNXYLineGenericChart2 wSNXYLineGenericChart2 = new WSNXYLineGenericChart2(WSNCIDAFMain.seriesMap);
		panel3.setLayout(new BorderLayout());
		panel3.add(topPanel, BorderLayout.NORTH);
		panel3.add(bottomPanel, BorderLayout.CENTER);
		bottomPanel.setName("CIDAF Panel");
		bottomPanel.add(new WSNXYLineGenericChart2(WSNCIDAFMain.seriesMap).getChartPanel());
		
		JPanel bottomLowerPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		bottomLowerPanel.setName("Efficiency Results");
		JLabel jlabelResults = new JLabel("Efficiency Results");
		jlabelResults.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		bottomLowerPanel.add(jlabelResults);
		bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNCIDAFMain.energyLebelEfficiency + "%"), BorderLayout.CENTER);
		bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNCIDAFMain.interationImprovement + "%"));
		bottomLowerPanel.setBorder(raisedetched);
		bottomLowerPanel.setBackground(Color.GRAY);
		panel3.add(bottomLowerPanel, BorderLayout.SOUTH);
		
		topPanel.add(label, BorderLayout.SOUTH);
		label.setText("Date & Time :"+ new Date());
		btnRTRAInvoke.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bottomPanel.repaint();
				bottomPanel.removeAll();
				topPanel.add(label, BorderLayout.SOUTH);
				label.setText("Date & Time :"+ new Date());
				WSNCIDAFMain.seriesMap = null;
				WSNCIDAFMain.invokeProtocol();
				bottomPanel.setName("CIDAF Panel");
				bottomPanel.add(new WSNXYLineGenericChart2(WSNCIDAFMain.seriesMap).getChartPanel());
				
				bottomLowerPanel.removeAll();
				bottomLowerPanel.repaint();
				bottomLowerPanel.add(jlabelResults);
				bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNCIDAFMain.energyLebelEfficiency + "%"), BorderLayout.CENTER);
				bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNCIDAFMain.interationImprovement + "%"));
				bottomLowerPanel.setBorder(raisedetched);
			}
		});
	}
	public void pageMNF()
	{
		panel4 = new JPanel();
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		//topPanel.setBackground(Color.black);
		JButton btnRTRAInvoke = new JButton ("Invoke MNF");
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		//topPanel.setBorder(raisedetched);
		topPanel.add(btnRTRAInvoke, BorderLayout.EAST);
		JPanel topInnerWestPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		topInnerWestPanel.setSize(10,10);
		topInnerWestPanel.add(new JLabel("Residual Energy          :" + WSNMNFMain.INITIAL_RESIDUAL_ENERGY));
		topInnerWestPanel.add(new JLabel("Transmission Power :" + WSNMNFMain.EPS_TE));
		topInnerWestPanel.add(new JLabel("Amplifier Power          :" + WSNMNFMain.EPS_TA));
		topInnerWestPanel.add(new JLabel("Receiver Power           :" + WSNMNFMain.EPS_R));
		topInnerWestPanel.add(new JLabel("Number of Nodes        :" + WSNMNFMain.TOTAL_NUMBER_NODES));
		topPanel.add(topInnerWestPanel, BorderLayout.NORTH);
		JPanel bottomPanel = new JPanel();
		JLabel label = new JLabel("Hello " + Math.random());
		WSNMNFMain.invokeProtocol();
		WSNXYLineGenericChart2 wSNXYLineGenericChart2 = new WSNXYLineGenericChart2(WSNMNFMain.seriesMap);
		panel4.setLayout(new BorderLayout());
		panel4.add(topPanel, BorderLayout.NORTH);
		panel4.add(bottomPanel, BorderLayout.CENTER);
		bottomPanel.setName("MNF Panel");
		bottomPanel.add(new WSNXYLineGenericChart2(WSNMNFMain.seriesMap).getChartPanel());
		
		JPanel bottomLowerPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		bottomLowerPanel.setName("Efficiency Results");
		JLabel jlabelResults = new JLabel("Efficiency Results");
		jlabelResults.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		bottomLowerPanel.add(jlabelResults);
		bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNMNFMain.energyLebelEfficiency), BorderLayout.CENTER);
		bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNMNFMain.interationImprovement + "%"));
		bottomLowerPanel.setBorder(raisedetched);
		bottomLowerPanel.setBackground(Color.GRAY);
		panel4.add(bottomLowerPanel, BorderLayout.SOUTH);
		
		topPanel.add(label, BorderLayout.SOUTH);
		label.setText("Date & Time :"+ new Date());
		btnRTRAInvoke.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bottomPanel.repaint();
				bottomPanel.removeAll();
				topPanel.add(label, BorderLayout.SOUTH);
				label.setText("Date & Time :"+ new Date());
				WSNMNFMain.seriesMap = null;
				WSNMNFMain.invokeProtocol();
				bottomPanel.setName("MNF Panel");
				bottomPanel.add(new WSNXYLineGenericChart2(WSNMNFMain.seriesMap).getChartPanel());
				
				bottomLowerPanel.removeAll();
				bottomLowerPanel.repaint();
				bottomLowerPanel.add(jlabelResults);
				bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNMNFMain.energyLebelEfficiency), BorderLayout.CENTER);
				bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNMNFMain.interationImprovement + "%"));
				bottomLowerPanel.setBorder(raisedetched);
			}
		});
	}
	public void pagePISS()
	{
		panel5 = new JPanel();
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		//topPanel.setBackground(Color.black);
		JButton btnRTRAInvoke = new JButton ("Invoke PISS");
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		//topPanel.setBorder(raisedetched);
		topPanel.add(btnRTRAInvoke, BorderLayout.EAST);
		JPanel topInnerWestPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		topInnerWestPanel.setSize(10,10);
		topInnerWestPanel.add(new JLabel("Residual Energy          :" + WSNPISSMain.INITIAL_RESIDUAL_ENERGY));
		topInnerWestPanel.add(new JLabel("Transmission Power :" + WSNPISSMain.EPS_TE));
		topInnerWestPanel.add(new JLabel("Amplifier Power          :" + WSNPISSMain.EPS_TA));
		topInnerWestPanel.add(new JLabel("Receiver Power           :" + WSNPISSMain.EPS_R));
		topInnerWestPanel.add(new JLabel("Number of Nodes        :" + WSNPISSMain.NUMBER_OF_NODES));
		topPanel.add(topInnerWestPanel, BorderLayout.NORTH);
		JPanel bottomPanel = new JPanel();
		JLabel label = new JLabel("Hello " + Math.random());
		WSNPISSMain.invokeProtocol();
		WSNXYLineGenericChart2 wSNXYLineGenericChart2 = new WSNXYLineGenericChart2(WSNPISSMain.seriesMap);
		panel5.setLayout(new BorderLayout());
		panel5.add(topPanel, BorderLayout.NORTH);
		panel5.add(bottomPanel, BorderLayout.CENTER);
		bottomPanel.setName("PISS Panel");
		bottomPanel.add(new WSNXYLineGenericChart2(WSNPISSMain.seriesMap).getChartPanel());
		
		JPanel bottomLowerPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		bottomLowerPanel.setName("Efficiency Results");
		JLabel jlabelResults = new JLabel("Efficiency Results");
		jlabelResults.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		bottomLowerPanel.add(jlabelResults);
		bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNPISSMain.energyLebelEfficiency), BorderLayout.CENTER);
		bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNPISSMain.interationImprovement + "%"));
		bottomLowerPanel.setBorder(raisedetched);
		bottomLowerPanel.setBackground(Color.GRAY);
		panel5.add(bottomLowerPanel, BorderLayout.SOUTH);
		
		topPanel.add(label, BorderLayout.SOUTH);
		label.setText("Date & Time :"+ new Date());
		btnRTRAInvoke.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bottomPanel.repaint();
				bottomPanel.removeAll();
				topPanel.add(label, BorderLayout.SOUTH);
				label.setText("Date & Time :"+ new Date());
				WSNPISSMain.seriesMap = null;
				WSNPISSMain.invokeProtocol();
				bottomPanel.setName("RTRA Panel");
				bottomPanel.add(new WSNXYLineGenericChart2(WSNPISSMain.seriesMap).getChartPanel());
				
				bottomLowerPanel.removeAll();
				bottomLowerPanel.repaint();
				bottomLowerPanel.add(jlabelResults);
				bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNPISSMain.energyLebelEfficiency), BorderLayout.CENTER);
				bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNPISSMain.interationImprovement + "%"));
				bottomLowerPanel.setBorder(raisedetched);
			}
		});
	}
	public void pageNIRM()
	{
		panel6 = new JPanel();
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		//topPanel.setBackground(Color.black);
		JButton btnRTRAInvoke = new JButton ("Invoke NIRM");
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		//topPanel.setBorder(raisedetched);
		topPanel.add(btnRTRAInvoke, BorderLayout.EAST);
		JPanel topInnerWestPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		topInnerWestPanel.setSize(10,10);
		topInnerWestPanel.add(new JLabel("Residual Energy          :" + WSNNIRMMain.INITIAL_RESIDUAL_ENERGY));
		topInnerWestPanel.add(new JLabel("Transmission Power :" + WSNNIRMMain.EPS_TE));
		topInnerWestPanel.add(new JLabel("Amplifier Power          :" + WSNNIRMMain.EPS_TA));
		topInnerWestPanel.add(new JLabel("Receiver Power           :" + WSNNIRMMain.EPS_R));
		topInnerWestPanel.add(new JLabel("Number of Nodes        :" + WSNNIRMMain.NUMBER_OF_NODES));
		topPanel.add(topInnerWestPanel, BorderLayout.NORTH);
		JPanel bottomPanel = new JPanel();
		JLabel label = new JLabel("Hello " + Math.random());
		WSNNIRMMain.invokeProtocol();
		WSNXYLineGenericChart2 wSNXYLineGenericChart2 = new WSNXYLineGenericChart2(WSNNIRMMain.seriesMap);
		panel6.setLayout(new BorderLayout());
		panel6.add(topPanel, BorderLayout.NORTH);
		panel6.add(bottomPanel, BorderLayout.CENTER);
		bottomPanel.setName("NIRM Panel");
		bottomPanel.add(new WSNXYLineGenericChart2(WSNNIRMMain.seriesMap).getChartPanel());
		
		JPanel bottomLowerPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		bottomLowerPanel.setName("Efficiency Results");
		JLabel jlabelResults = new JLabel("Efficiency Results");
		jlabelResults.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		bottomLowerPanel.add(jlabelResults);
		bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNNIRMMain.energyLebelEfficiency), BorderLayout.CENTER);
		bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNNIRMMain.interationImprovement + "%"));
		bottomLowerPanel.setBorder(raisedetched);
		bottomLowerPanel.setBackground(Color.GRAY);
		panel6.add(bottomLowerPanel, BorderLayout.SOUTH);
		
		topPanel.add(label, BorderLayout.SOUTH);
		label.setText("Date & Time :"+ new Date());
		btnRTRAInvoke.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bottomPanel.repaint();
				bottomPanel.removeAll();
				topPanel.add(label, BorderLayout.SOUTH);
				label.setText("Date & Time :"+ new Date());
				WSNNIRMMain.seriesMap = null;
				WSNNIRMMain.invokeProtocol();
				bottomPanel.setName("NIRM Panel");
				bottomPanel.add(new WSNXYLineGenericChart2(WSNNIRMMain.seriesMap).getChartPanel());
				
				bottomLowerPanel.removeAll();
				bottomLowerPanel.repaint();
				bottomLowerPanel.add(jlabelResults);
				bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNNIRMMain.energyLebelEfficiency), BorderLayout.CENTER);
				bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNNIRMMain.interationImprovement + "%"));
				bottomLowerPanel.setBorder(raisedetched);
			}
		});
	}
	public void pageCAIR()
	{
		panel7 = new JPanel();
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		//topPanel.setBackground(Color.black);
		JButton btnRTRAInvoke = new JButton ("Invoke CAIR");
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		//topPanel.setBorder(raisedetched);
		topPanel.add(btnRTRAInvoke, BorderLayout.EAST);
		JPanel topInnerWestPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		topInnerWestPanel.setSize(10,10);
		topInnerWestPanel.add(new JLabel("Residual Energy          :" + WSNCAIRMain.INITIAL_RESIDUAL_ENERGY));
		topInnerWestPanel.add(new JLabel("Transmission Power :" + WSNCAIRMain.EPS_TE));
		topInnerWestPanel.add(new JLabel("Amplifier Power          :" + WSNCAIRMain.EPS_TA));
		topInnerWestPanel.add(new JLabel("Receiver Power           :" + WSNCAIRMain.EPS_R));
		topInnerWestPanel.add(new JLabel("Number of Nodes        :" + WSNCAIRMain.TOTAL_NUMBER_NODES));
		topPanel.add(topInnerWestPanel, BorderLayout.NORTH);
		JPanel bottomPanel = new JPanel();
		JLabel label = new JLabel("Hello " + Math.random());
		WSNCAIRMain.invokeProtocol();
		WSNXYLineGenericChart2 wSNXYLineGenericChart2 = new WSNXYLineGenericChart2(WSNCAIRMain.seriesMap);
		panel7.setLayout(new BorderLayout());
		panel7.add(topPanel, BorderLayout.NORTH);
		panel7.add(bottomPanel, BorderLayout.CENTER);
		bottomPanel.setName("CAIR Panel");
		bottomPanel.add(new WSNXYLineGenericChart2(WSNCAIRMain.seriesMap).getChartPanel());
		
		JPanel bottomLowerPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		bottomLowerPanel.setName("Efficiency Results");
		JLabel jlabelResults = new JLabel("Efficiency Results");
		jlabelResults.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		bottomLowerPanel.add(jlabelResults);
		bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNCAIRMain.energyLebelEfficiency + "%"), BorderLayout.CENTER);
		bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNCAIRMain.interationImprovement + "%"));
		bottomLowerPanel.setBorder(raisedetched);
		bottomLowerPanel.setBackground(Color.GRAY);
		panel7.add(bottomLowerPanel, BorderLayout.SOUTH);
		
		topPanel.add(label, BorderLayout.SOUTH);
		label.setText("Date & Time :"+ new Date());
		btnRTRAInvoke.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bottomPanel.repaint();
				bottomPanel.removeAll();
				topPanel.add(label, BorderLayout.SOUTH);
				label.setText("Date & Time :"+ new Date());
				WSNCAIRMain.seriesMap = null;
				WSNCAIRMain.invokeProtocol();
				bottomPanel.setName("RTRA Panel");
				bottomPanel.add(new WSNXYLineGenericChart2(WSNCAIRMain.seriesMap).getChartPanel());
				
				bottomLowerPanel.removeAll();
				bottomLowerPanel.repaint();
				bottomLowerPanel.add(jlabelResults);
				bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNCAIRMain.energyLebelEfficiency), BorderLayout.CENTER);
				bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNCAIRMain.interationImprovement + "%"));
				bottomLowerPanel.setBorder(raisedetched);
			}
		});
	}
	public void pageNNEWM()
	{
		panel8 = new JPanel();
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		//topPanel.setBackground(Color.black);
		JButton btnRTRAInvoke = new JButton ("Invoke NNEWM");
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		//topPanel.setBorder(raisedetched);
		topPanel.add(btnRTRAInvoke, BorderLayout.EAST);
		JPanel topInnerWestPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		topInnerWestPanel.setSize(10,10);
		topInnerWestPanel.add(new JLabel("Residual Energy          :" + WSNNNEWMMain.INITIAL_RESIDUAL_ENERGY));
		topInnerWestPanel.add(new JLabel("Transmission Power :" + WSNNNEWMMain.EPS_TE));
		topInnerWestPanel.add(new JLabel("Amplifier Power          :" + WSNNNEWMMain.EPS_TA));
		topInnerWestPanel.add(new JLabel("Receiver Power           :" + WSNNNEWMMain.EPS_R));
		topInnerWestPanel.add(new JLabel("Number of Nodes        :" + WSNNNEWMMain.TOTAL_NUMBER_NODES));
		topPanel.add(topInnerWestPanel, BorderLayout.NORTH);
		JPanel bottomPanel = new JPanel();
		JLabel label = new JLabel("Hello " + Math.random());
		WSNNNEWMMain.invokeProtocol();
		WSNXYLineGenericChart2 wSNXYLineGenericChart2 = new WSNXYLineGenericChart2(WSNNNEWMMain.seriesMap);
		panel8.setLayout(new BorderLayout());
		panel8.add(topPanel, BorderLayout.NORTH);
		panel8.add(bottomPanel, BorderLayout.CENTER);
		
		JPanel bottomLowerPanel = new JPanel(new GridLayout(0, 1, 0, 0));
		bottomLowerPanel.setName("Efficiency Results");
		JLabel jlabelResults = new JLabel("Efficiency Results");
		jlabelResults.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		bottomLowerPanel.add(jlabelResults);
		bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNNNEWMMain.energyLebelEfficiency + "%"), BorderLayout.CENTER);
		bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNNNEWMMain.interationImprovement + "%"));
		bottomLowerPanel.setBorder(raisedetched);
		bottomLowerPanel.setBackground(Color.GRAY);
		panel8.add(bottomLowerPanel, BorderLayout.SOUTH);
		
		topPanel.add(label, BorderLayout.SOUTH);
		label.setText("Date & Time :"+ new Date());
		btnRTRAInvoke.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bottomPanel.repaint();
				bottomPanel.removeAll();
				topPanel.add(label, BorderLayout.SOUTH);
				label.setText("Date & Time :"+ new Date());
				WSNNNEWMMain.seriesMap = null;
				WSNNNEWMMain.invokeProtocol();
				bottomPanel.setName("RTRA Panel");
				bottomPanel.add(new WSNXYLineGenericChart2(WSNNNEWMMain.seriesMap).getChartPanel());
				
				bottomLowerPanel.removeAll();
				bottomLowerPanel.repaint();
				bottomLowerPanel.add(jlabelResults);
				bottomLowerPanel.add(new JLabel("Energy Efficiency          :" + WSNNNEWMMain.energyLebelEfficiency + "%"), BorderLayout.CENTER);
				bottomLowerPanel.add(new JLabel("Iteration Efficiency       :" + WSNNNEWMMain.interationImprovement + "%"));
				bottomLowerPanel.setBorder(raisedetched);
			}
		});
	}

	// Main method to get things started
	public static void main( String args[] )
	{
		// Create an instance of the test application
		WSNMainHomePane mainFrame	= new WSNMainHomePane();
		mainFrame.setVisible( true );
	}
}