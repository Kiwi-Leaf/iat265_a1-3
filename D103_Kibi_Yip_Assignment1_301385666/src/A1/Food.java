package A1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import processing.core.PVector;

public class Food {
	private PVector pos,vel;
	private double scale;
	private int bodyW=100;
	private int bodyH=50;//bodyW:bodyH = 2:1
	private Dimension size;
	private Color fin,body;
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
		
	}
	//-------------------------------------------------------------------------------------

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
	}
	//-------------------------------------------------------------------------------------
	public void draw(Graphics g) {
		Graphics2D g2= (Graphics2D) g;
		AffineTransform origin = g2.getTransform();
		g2.translate(pos.x,pos.y);
		g2.rotate(vel.heading());
		if (vel.x < 0) g2.scale(1,-1);
		//body
		g2.setColor(fin);
		g2.fillArc(-size.width/10*9, -size.height/2, size.width/2, size.height, -90, 180);
		
		g2.setColor(body);
		g2.fillOval(-size.width/10*8/2, -size.height/2, size.width/10*9, size.height);
		g2.setColor(Color.black);
		g2.fillOval(size.width/5,-size.height/3/2,size.width/10,size.height/3);
		
		
		g2.setTransform(origin);
	}
	//-------------------------------------------------------------------------------------
	private void move() {
		pos.add(vel);
	}
	//-------------------------------------------------------------------------------------
	private void seeEdge() {
		if (pos.x<bodyW/2||pos.x>Aquarium.Ocean_W-bodyW/2) {
			vel.x*=-1;
		}
		if(pos.y<bodyW/2||pos.y>Aquarium.Ocean_H-bodyW/2) {
			vel.y*=-1;
		}
	}
	//-------------------------------------------------------------------------------------
}
