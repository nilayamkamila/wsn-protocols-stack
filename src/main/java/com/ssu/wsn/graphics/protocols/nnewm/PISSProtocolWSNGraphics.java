package com.ssu.wsn.graphics.protocols.nnewm;


import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;
 
public class PISSProtocolWSNGraphics extends JPanel {
    /*int[] leach = {
        400, 350, 301, 244, 189, 113, 67, 3
    };
    
    int[] lea2c = {
            400, 366, 356, 321, 289, 244, 189, 160, 101, 31
        };
    int[] nnewm = {
    		400,381, 366, 323, 291,264, 247,203, 178, 177, 175, 167, 145, 107, 41
    };
    int[] ebcs = {
    		400,398, 381, 361, 302,256, 231,188, 156, 131, 101, 89, 57
    };*/
	
	int[] leach = {
	        200, 171, 144, 112, 89, 27, 13, 3
	    };
	    
	    int[] lea2c = {
	            200, 181, 175, 166, 144, 118, 89, 60, 37, 12
	        };
	    int[] nnewm = {
	    		200, 189, 181, 169, 151, 139, 101, 79, 77, 75, 73, 71, 37, 03
	    };
	    int[] ebcs = {
	    		200, 198, 189, 181, 171, 137, 95,  69, 51, 26, 15, 1
	    };
    final int PAD = 20;
 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        //drawData(g2, leach, Color.green.darker(), true, "LEACH Lifetime");
        drawData(g2, nnewm, Color.gray.darker(), true, "NNEWM Lifetime");
        drawData(g2, lea2c, Color.blue.darker(), true, "LEA2C Lifetime");
        drawData(g2, ebcs, Color.MAGENTA.darker(), true, "EBCS Lifetime");
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
        s = "Network Rounds";
        sy = h - PAD + (PAD - sh)/2 + lm.getAscent();
        float sw = (float)font.getStringBounds(s, frc).getWidth();
        float sx = (w - sw)/2;
        g2.drawString(s, sx, sy);
        // Draw lines.
        double xInc = (double)(w - 2*PAD)/(nnewm.length-1);
        double scale = (double)(h - 2*PAD)/getMax();
        g2.setPaint(paint);
        g2.drawLine((int)(PAD + 7*xInc), (int)(h - PAD - scale*data[1]), (int)(PAD + 9*xInc), (int)(h - PAD - scale*data[1]));
        g2.drawString(message, (int)(PAD + 10*xInc), (int)(h - PAD - scale*data[1]));
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
        for(int i = 0; i < leach.length; i++) {
            if(leach[i] > max)
                max = leach[i];
        }
        return max;
    }
 
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new PISSProtocolWSNGraphics());
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}
