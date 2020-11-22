package A1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import processing.core.PVector;

public class Food {
	private PVector pos,vel;
	private double scale;
	private int bodyW=100;
	private int bodyH=50;//bodyW:bodyH = 2:1
	private Dimension size;
	private Color fin,body;
	private Area outline;
	private Ellipse2D.Double bodyOval,eyeOval;
	private Arc2D.Double finArc;
	private Rectangle2D.Double top,bottom,left, right;
	private Dimension screenSize;
	//-------------------------------------------------------------------------------------
	public Food(int x, int y, double scale) {
		
		
		this.scale=scale;
		bodyW*=scale;
		bodyH*=scale;
		size= new Dimension(bodyW,bodyH);
		int r= (int)(Math.random()*255);
		int g= (int)(Math.random()*255);
		int b= (int)(Math.random()*255);
		body= new Color(r,g,b);
		r= (int)(Math.random()*255);
		g= (int)(Math.random()*255);
		b= (int)(Math.random()*255);
		fin=new Color(r,g,b);
		screenSize=new Dimension (Aquarium.Ocean_W,Aquarium.Ocean_H);
		
		if (x<size.width/2) {
			x=size.width;
		}
		else if (x>Aquarium.Ocean_W-size.width/2) {
			x=Aquarium.Ocean_W-size.width;
		}
		
		if (y<size.height/2) {
			y=size.width;
		}
		else if (y>Aquarium.Ocean_H-size.height/2) {
			y=Aquarium.Ocean_H-size.width;
		}
		pos= new PVector(x,y);
		vel=new PVector((float)Math.random()*10-5,(float)Math.random()*10-5);
		finArc=new Arc2D.Double(-size.width/10*9, -size.height/2, size.width/2, size.height, -90, 180,Arc2D.CHORD);
		bodyOval=new Ellipse2D.Double(-size.width/10*8/2, -size.height/2, size.width/10*9, size.height);
		eyeOval=new Ellipse2D.Double(size.width/5,-size.height/3/2,size.width/10,size.height/3);
		
		
		outline=new Area(bodyOval);
		outline.add(new Area(finArc));
		 top = new Rectangle2D.Double(0,-10, screenSize.width, 10);
		 bottom = new Rectangle2D.Double(0, screenSize.height-10, screenSize.width, 10);
		 left = new Rectangle2D.Double(-20, 0, 20, screenSize.height);
		 right = new Rectangle2D.Double(screenSize.width ,0, 10, screenSize.height );
		
	}
	//-------------------------------------------------------------------------------------
	private Shape getBoundary() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(vel.heading());
		at.scale(scale, scale);
		return at.createTransformedShape(outline);
	}

	//-------------------------------------------------------------------------------------
	public PVector getPos() {
		return pos;
	}
	//-------------------------------------------------------------------------------------
	public Dimension getSize() {
		return size;
	}
	//-------------------------------------------------------------------------------------
	public void update() {
		move();
		seeEdge();
		vel.limit(1);
	}
	//-------------------------------------------------------------------------------------
	public void draw(Graphics g) {
		Graphics2D g2= (Graphics2D) g;
		AffineTransform origin = g2.getTransform();
		g2.translate(pos.x,pos.y);
		g2.scale(scale, scale);
		g2.rotate(vel.heading());
		if (vel.x < 0) g2.scale(1,-1);
		//body
		g2.setColor(fin);
	
		g2.fill(finArc);
		g2.setColor(body);
		
		g2.fill(bodyOval);
		g2.setColor(Color.black);
		
		g2.fill(eyeOval);
		
		
		g2.setTransform(origin);
//		g2.setColor(Color.black);
//		g2.draw(getBoundary().getBounds2D());
	}
	//-------------------------------------------------------------------------------------
	private void move() {
		pos.add(vel);
	}
	//-------------------------------------------------------------------------------------
	private void seeEdge() {
		if (getBoundary().intersects(left) && vel.x < 0) {
			vel.x *= -1;
		}

		if (getBoundary().intersects(right) && vel.x > 0) {
			vel.x *= -1;
		}
		if (getBoundary().intersects(top) && vel.y < 0) {
			vel.y *= -1;
		}
		if (getBoundary().intersects(bottom) && vel.y > 0) {
			vel.y *= -1;
		}
	}
	//-------------------------------------------------------------------------------------
	public boolean checkMouseHit(MouseEvent e) {
		//if (Math.abs(pos.x-e.getX())<size.width/2&&Math.abs(pos.y-e.getY())<size.height/2) return true;
		PVector temp= new PVector(e.getX(),e.getY());
		if (pos.dist(temp)<size.width) 
			{
			System.out.println("fish is hit");
			return true;
			}
		
		else {
			return false;
		}
	}
	//-------------------------------------------------------------------------------------
	public void enlarge() {
		if (scale<1.0) scale+=0.1;
	}
}
