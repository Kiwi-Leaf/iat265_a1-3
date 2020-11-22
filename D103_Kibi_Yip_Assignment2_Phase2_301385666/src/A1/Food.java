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
import java.util.ArrayList;

import processing.core.PVector;

public class Food {
	private PVector pos,vel;
	private double scale;
	private int bodyW=100;
	private int bodyH=50;//bodyW:bodyH = 2:1
	private Dimension size;
	private Color fin,body,altFin,altBody,outlineColor;
	private Area outline;
	private Ellipse2D.Double bodyOval,eyeOval;
	private Arc2D.Double finArc,fov;
	private Rectangle2D.Double top,bottom,left, right;
	private Dimension screenSize;
	private float topSpeed;
	//-------------------------------------------------------------------------------------
	public Food(int x, int y, double scale) {

		this.scale=scale;
		bodyW*=scale;
		bodyH*=scale;
		size= new Dimension(bodyW,bodyH);
		topSpeed=1;
		int r= (int)(Math.random()*255);
		int g= (int)(Math.random()*255);
		int b= (int)(Math.random()*255);
		body= new Color(r,g,b);
		altBody= new Color(r,g,b);
		outlineColor=null;
		r= (int)(Math.random()*255);
		g= (int)(Math.random()*255);
		b= (int)(Math.random()*255);
		fin=new Color(r,g,b);
		altFin=new Color(r,g,b);
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
		fov=new Arc2D.Double(-size.width/2,-size.width/2,size.width*2,size.width,-60,120,Arc2D.PIE);
		
		
		outline=new Area(bodyOval);
		outline.add(new Area(finArc));
		 top = new Rectangle2D.Double(0,-10, screenSize.width, 10);
		 bottom = new Rectangle2D.Double(0, screenSize.height-10, screenSize.width, 10);
		 left = new Rectangle2D.Double(-20, 0, 20, screenSize.height);
		 right = new Rectangle2D.Double(screenSize.width ,0, 10, screenSize.height );
		
	}
	//-------------------------------------------------------------------------------------
	public Shape getBoundary() {
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
	public void update( Eel e,ArrayList<Food> f) {
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
		g2.setColor(outlineColor);
		g2.draw(getBoundary());
	}
	//-------------------------------------------------------------------------------------
	private void move() {
		vel.normalize().mult(topSpeed);
		pos.add(vel);
	}
	//-------------------------------------------------------------------------------------
	private void seeEdge() {

		float coef = .3f;
		PVector accel = new PVector();
		
		if (getBoundary().intersects(left)||getFOV().intersects(left)||pos.x<0) {
			accel.add(1,0);
			
		}
		else if (getBoundary().intersects(right)||getFOV().intersects(right)||pos.x>screenSize.width) {
			accel.add(-1,0);
		
		}
		if (getBoundary().intersects(top)||getFOV().intersects(top)||pos.y<0) {
			accel.add(0,1);
			
		}
		else if (getBoundary().intersects(bottom)||getFOV().intersects(bottom)||pos.y>screenSize.height) {
			accel.add(0,-1);
			
		}
		
		accel.mult(coef*topSpeed);
		vel.add(accel);
	}

	//-------------------------------------------------------------------------------------
	public boolean checkMouseHit(MouseEvent e) {
//		System.out.println("Check fish click");
//		System.out.println(getBoundary().contains(e.getX(),e.getY()));
//		System.out.println("Mouse location:"+(e.getX()-50)+","+(e.getY()-50));
//		System.out.println("Fish location:"+pos);
//		System.out.println("===================================================");
		
		return getBoundary().contains(e.getX()-50,e.getY()-50);
		
	}
	//-------------------------------------------------------------------------------------
	public void enlarge() {
		if (scale<1.0) scale+=0.1;
	}
	//-------------------------------------------------------------------------------------
	public Shape getOutline() {
		return outline;
	}
	//-------------------------------------------------------------------------------------
	public void setTargetedColor() {
		this.body=new Color(255,255,255);
		this.fin=body.darker();
		this.outlineColor=Color.red;
	}
	//-------------------------------------------------------------------------------------
	public void setOriginalColor() {
		body=altBody;
		fin=altFin;
		outlineColor=new Color(0,0,0,0);
	}
	//-------------------------------------------------------------------------------------
	private Shape getFOV() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(vel.heading());
		at.scale(scale, scale);
		return at.createTransformedShape(fov);
	}
	//-------------------------------------------------------------------------------------
	public Boolean collideWithEachOther(Food e) {
		return (this.getBoundary().intersects(e.getBoundary().getBounds2D()) && e.getBoundary().intersects(this.getBoundary().getBounds2D()))||this.getFOV().intersects(e.getBoundary().getBounds2D());
	}
	//-------------------------------------------------------------------------------------
	public void reverseSmaller(Food e) {
		float angle =(float)Math.atan2(pos.y-e.pos.y, pos.x-e.pos.x);
	      float aveSpeed= (vel.mag()+e.vel.mag())/2;
	      float coef = .1f;
	      PVector accel = new PVector();
	      accel.set((float)(aveSpeed*Math.cos(angle)),(float)(aveSpeed*Math.sin(angle)));
	      accel.mult(coef*topSpeed);
	      vel.add(accel);
	
	}
	//-------------------------------------------------------------------------------------
	public void reverseBoth(Food e) {
		float angle =(float)Math.atan2(pos.y-e.pos.y, pos.x-e.pos.x);
	      float aveSpeed= (vel.mag()+e.vel.mag())/2;
	      float coef = .1f;
	      PVector accel = new PVector();
	      accel.set((float)(aveSpeed*Math.cos(angle)),(float)(aveSpeed*Math.sin(angle)));
	      accel.mult(coef*topSpeed);
	      vel.add(accel);
	      accel.set((float)(aveSpeed*Math.cos(-angle)),(float)(aveSpeed*Math.sin(-angle)));
	      accel.mult(coef*topSpeed);
	      e.vel.add(accel);
	
	}
	//-------------------------------------------------------------------------------------
	public double getScale() {
		return scale;
	}
}
