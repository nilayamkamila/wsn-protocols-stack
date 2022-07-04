package com.ssu.wsn.graphics.protocols.nirm;


import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;

import com.ssu.wsn.graphics.protocols.mnf.MNFProtocolWSNGraphics;
 
public class NIRMProtocolWSNGraphics extends JPanel {
    int[] rtra = {
    		3637,3189,2905,2785,2539,2393,2171,1875,1513,1233,987,789,534,314,19
    };
    int[] piss = {
    		3637,3189,2905,2785,2539,2429,2411,2213,2066,1846,1623,1436,1298,976,698,424,302,197,01
    };
    int[] nirm = {
    		3637,3089,2712,2567,2311,2247,2133,2077,1933,1841,1635,1507,1378,1183,1036,846,641,433,291,187,61
    };
    final int PAD = 20;
 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        //drawData(g2, pegasis, Color.green.darker(), true, "PEGASIS Lifetime");
        drawData(g2, rtra, Color.gray.darker(), true, "RTRA Lifetime");
        drawData(g2, piss, Color.blue.darker(), true, "PISS Lifetime");
        drawData(g2, nirm, Color.green.darker(), true, "NIRM Lifetime");
        //drawDataPoints(g2, data2, Color.gray.darker());
    }

	private void drawData(Graphics2D g2, int[] data, Paint paint, boolean isLine, String message) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Draw ordinate.
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
        // Draw labels.
        Font font = g2.getFont();
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics("0", frc);
        float sh = lm.getAscent() + lm.getDescent();
        // Ordinate label.
        //g2.setFont(new Font("default", Font.BOLD, 12));
        String s = "Residual Energy";
        float sy = PAD + ((h - 2*PAD) - s.length()*sh)/2 + lm.getAscent();
        for(int i = 0; i < s.length(); i++) {
            String letter = String.valueOf(s.charAt(i));
            float sw = (float)font.getStringBounds(letter, frc).getWidth();
            float sx = (PAD - sw)/2;
            g2.drawString(letter, sx, sy);
            sy += sh;
        }
        // Abcissa label.
        s = "Network Lifetime";
        sy = h - PAD + (PAD - sh)/2 + lm.getAscent();
        float sw = (float)font.getStringBounds(s, frc).getWidth();
        float sx = (w - sw)/2;
        g2.drawString(s, sx, sy);
        // Draw lines.
        double xInc = (double)(w - 2*PAD)/(nirm.length-1);
        double scale = (double)(h - 2*PAD)/getMax();
        g2.setPaint(paint);
        //g2.drawLine((int)(PAD + 7*xInc), (int)(h - PAD - scale*data[1]), (int)(PAD + 9*xInc), (int)(h - PAD - scale*data[1]));
        //g2.drawString(message, (int)(PAD + 10*xInc), (int)(h - PAD - scale*data[1]));
        if(message.equalsIgnoreCase("RTRA Lifetime")){
        	g2.drawLine((int)(PAD + 7*xInc), (int)(h - PAD - scale*3112), (int)(PAD + 9*xInc), (int)(h - PAD - scale*3112));
            g2.drawString(message, (int)(PAD + 10*xInc), (int)(h - PAD - scale*3112));
        }else if(message.equalsIgnoreCase("PISS Lifetime")){
        	g2.drawLine((int)(PAD + 7*xInc), (int)(h - PAD - scale*3312), (int)(PAD + 9*xInc), (int)(h - PAD - scale*3312));
            g2.drawString(message, (int)(PAD + 10*xInc), (int)(h - PAD - scale*3312));
        }else if(message.equalsIgnoreCase("NIRM Lifetime")){
        	g2.drawLine((int)(PAD + 7*xInc), (int)(h - PAD - scale*3512), (int)(PAD + 9*xInc), (int)(h - PAD - scale*3512));
            g2.drawString(message, (int)(PAD + 10*xInc), (int)(h - PAD - scale*3512));
        }
        if(isLine){
        for(int i = 0; i < data.length-1; i++) {
            double x1 = PAD + i*xInc;
            double y1 = h - PAD - scale*data[i];
            double x2 = PAD + (i+1)*xInc;
            double y2 = h - PAD - scale*data[i+1];
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
        }
	}
        // Mark data points.
        g2.setPaint(Color.red);
        for(int i = 0; i < data.length; i++) {
            double x = PAD + i*xInc;
            double y = h - PAD - scale*data[i];
            g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
        }
	}
 
	
	
	
	
	
    private int getMax() {
        int max = -Integer.MAX_VALUE;
        for(int i = 0; i < nirm.length; i++) {
            if(nirm[i] > max)
                max = nirm[i];
        }
        return max;
    }
 
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new NIRMProtocolWSNGraphics());
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}
