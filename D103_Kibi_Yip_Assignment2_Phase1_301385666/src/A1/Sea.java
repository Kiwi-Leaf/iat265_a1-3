package A1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

import processing.core.PVector;


public class Sea {
	private Color s1,s2,s3,s4,sand,c1,c2;
	private Dimension size;
	private PVector pos;
	private Rectangle2D.Double[] sea=new Rectangle2D.Double[4];
	private Rectangle2D.Double sandRect;
	private Arc2D.Double lCoral, sCoral;
	
	public Sea(Dimension size) {
		
		s1= new Color(202,255,247);
		s2= new Color(161,246,233);
		s3= new Color(133,160,186);
		s4= new Color(121,112,163);
		c1= new Color(226,100,124);
		c2= new Color(86,76,111);
		sand= new Color(232,202,141);
		this.size=size;
		pos= new PVector(0,0);
		int newTop= (int)(size.height-30);
		int sect= newTop/4;
		for (int i =0;i<sea.length;i++) {
			sea[i]=new Rectangle2D.Double(0, sect*i, size.width, sect);
		}
		sandRect=new Rectangle2D.Double(0, newTop-10, size.width, size.height-newTop);
		lCoral= new Arc2D.Double(size.width/5, newTop-size.height/3/2,size.height/3,size.height/3, 0, 180,Arc2D.CHORD);
		sCoral=new Arc2D.Double(size.width/5*3, newTop-size.height/5/3,size.height/5,size.height/6, 0, 180,Arc2D.CHORD);
	}
	
	public void draw(Graphics g) {
		
		Graphics2D g2= (Graphics2D) g;
		AffineTransform origin = g2.getTransform();
		
		
		g2.translate(20, 20);
		int newTop= (int)(size.height-30);
		int sect= newTop/4;
		
		g2.setColor(s1);
		//g2.fillRect(0, 0, size.width, sect);
		g2.fill(sea[0]);
		g2.setColor(s2);
		//g2.fillRect(0, sect,size.width, sect);
		g2.fill(sea[1]);
		g2.setColor(s3);
		//g2.fillRect(0, sect*2,size.width, sect);
		g2.fill(sea[2]);
		g2.setColor(s4);
		//g2.fillRect(0,sect*3,size.width,sect);
		g2.fill(sea[3]);
		g2.setColor(sand);
		//g2.fillRect(0, newTop-10, size.width, size.height-newTop);
		g2.fill(sandRect);
		
		g2.setColor(c1);
		//g2.fillArc(size.width/5, newTop-size.height/3/2,size.height/3,size.height/3, 0, 180);
		g2.fill(lCoral);

		
		//g2.setTransform(origin);
		g2.setColor(c2);
		//g2.fillArc(size.width/5*3, newTop-size.height/5/3,size.height/5,size.height/6, 0, 180);
		g2.fill(sCoral);
		
		
		
	}
	
	public void update() {
		
	}
	
	public void bubbles(Graphics g) {
		Graphics2D g2= (Graphics2D) g;
		AffineTransform origin = g2.getTransform();
		
		g2.translate(0,0);
		
		
		g2.setTransform(origin);
		
	}

}
