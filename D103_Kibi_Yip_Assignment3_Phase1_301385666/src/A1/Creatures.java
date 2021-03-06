package A1;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


import processing.core.PVector;

public abstract class Creatures extends SimulatedObject{
	// The method moved here are the one shared by both eel and food class, or the nature of the method applies for both of them, but not necessarily used for now. 
	protected PVector pos,vel;
	protected double scale;
	protected float topSpeed,tempTopSpeed;
	protected Dimension size;
	protected Arc2D.Double fov;
	protected Rectangle2D.Double top,bottom,left, right;
	protected Dimension screenSize;
	protected Area outline;
	protected double scaleW, scaleH;
	protected int reactTime;
	public Creatures() {
		// TODO Auto-generated constructor stub
		this.screenSize=new Dimension(Aquarium.Ocean_W,Aquarium.Ocean_H);
		float x= (float)(Math.random()*screenSize.width);
		
		float y= (float)(Math.random()*screenSize.height);
		pos= new PVector(x,y);
		scaleW=1;
		scaleH=1;
		reactTime=-1;
		
		 top = new Rectangle2D.Double(0,-10, screenSize.width, 10);
		 bottom = new Rectangle2D.Double(0, screenSize.height-10, screenSize.width, 10);
		 left = new Rectangle2D.Double(-20, 0, 20, screenSize.height);
		 right = new Rectangle2D.Double(screenSize.width ,0, 10, screenSize.height );
	}
	//-----------------------------------------------
	public void update(ArrayList<SimulatedObject> cList) {
	
	}
	//-------------------------------------------
	public void draw(Graphics2D g) {
		
	}
	//-----------------------------------------------
	public PVector getPos() {
		return pos;
		}
	//-------------------------------------------
	protected void move() {
		vel.normalize().mult(topSpeed);
		pos.add(vel);

	}
	//----------------------------------
	public Shape getBoundary() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(vel.heading());
		at.scale(scale*scaleW, scale*scaleH);
		if (vel.x < 0) at.scale(1,-1);
		return at.createTransformedShape(outline);
	}
	//-----------------------------------
	public Shape getOutline() {
		return outline;
	}
	//------------------------------------
	protected Shape getFOV() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(vel.heading());
		at.scale(scale, scale);
		return at.createTransformedShape(fov);
	}
	//--------------------------------------------
	public void reverseSmaller(Creatures e) {
		System.out.println("is here!1");
		float angle =(float)Math.atan2(pos.y-e.pos.y, pos.x-e.pos.x);
	      float aveSpeed= (vel.mag()+e.vel.mag())/2;
	      float coef = .2f;
	      PVector accel = new PVector();
	      accel.set((float)(aveSpeed*Math.cos(angle)),(float)(aveSpeed*Math.sin(angle)));
	      accel.mult(coef*topSpeed);
	      vel.add(accel);
	      setReactTime();
	}
	//-------------------------------------------------------------------------------------
	public void reverseBoth(Creatures e) {
		System.out.println("is here!2");
		float angle =(float)Math.atan2(pos.y-e.pos.y, pos.x-e.pos.x);
	      float aveSpeed= (vel.mag()+e.vel.mag())/2;
	      float coef = .2f;
	      PVector accel = new PVector();
	      accel.set((float)(aveSpeed*Math.cos(angle)),(float)(aveSpeed*Math.sin(angle)));
	      accel.mult(coef*topSpeed);
	      vel.add(accel);
	      this.setReactTime(); 
	      
	      accel.set((float)(aveSpeed*Math.cos(-angle)),(float)(aveSpeed*Math.sin(-angle)));
	      accel.mult(coef*topSpeed);
	      e.vel.add(accel);
	      e.setReactTime();
	}
	//--------------------------------------------------------------------
	public double getScale() {
		return scale;
	}
	//---------------------------------------------------------------------
	public Boolean collideWithEachOther(Creatures e) {
		if ((this.getBoundary().intersects(e.getBoundary().getBounds2D()) && e.getBoundary().intersects(this.getBoundary().getBounds2D()))||this.getFOV().intersects(e.getBoundary().getBounds2D()))
		{
			//System.out.println("Collide with each other. Class is"+this.getClass());
			return true;
		}
		else return false;
	}
	//------------------------------------------------
	public Dimension getSize() {
		return size;
	}
	//------------------------------------------------
	protected void seeEdge() {

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
	//-------------------------------------------------
	public boolean isOutOfBound() {
		if(pos.x<50||pos.x>screenSize.width+50||pos.y<50||pos.y<0||pos.y>screenSize.height+50
				||getBoundary().intersects(top)||getBoundary().intersects(bottom)||getBoundary().intersects(left)||getBoundary().intersects(right)) {
			
			return true;
		}
		else return false;
	}
	//--------------------------------------------------
	protected ArrayList<SimulatedObject> filterTargetList(ArrayList<SimulatedObject> fList) {
		ArrayList<SimulatedObject> list = new ArrayList<>();
		for (SimulatedObject f:fList) if (eatable(f)) list.add(f);
		return list;
	}
	//----------------------------------------------------
	protected void approach(SimulatedObject target) {
		float coef = .3f;	// coefficient of acceleration relative to maxSpeed
		PVector direction = PVector.sub(target.getPos(), pos).normalize();
		PVector accel = PVector.mult(direction, vel.mag()*coef);
		vel.add(accel);
	}
	//-------------------------------------------------------
	protected void traceBestFood(ArrayList<SimulatedObject> fList) {	
		if (fList.size()>0) {	

			SimulatedObject target = fList.get(0);
			float distToTarget = PVector.dist(pos, target.getPos());
			
			for (SimulatedObject f:fList) if (PVector.dist(pos, f.getPos()) < distToTarget) {
				target = f;
				distToTarget = PVector.dist(pos, target.getPos());
			}

			this.approach(target);
		}	
	}
	//-------------------------------------
	public void setReactTime() {
		reactTime = 60;
	}
	//-------------------------------------
	protected abstract boolean eatable(SimulatedObject food);

}
