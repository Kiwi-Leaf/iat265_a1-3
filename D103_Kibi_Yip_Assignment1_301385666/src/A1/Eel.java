package A1;
import processing.core.PVector;

import java.awt.geom.AffineTransform;
import java.awt.geom.QuadCurve2D;
import java.awt.*;



public class Eel{
	private PVector pos,vel,path;
	private double scale;
	private float angle,dir,rangle;
	private int bodyW=200;
	private int bodyH=50;
	private int random;
	//private Dimension screenSize;
	private Dimension size;
	private Color body,spot,fin;
	private Polygon head,jaw,tail;
	private QuadCurve2D tuFin,tlFin,fuFin,flFin;
	private double jawA,tailA,jawTimer,tailTimer;
	
	//-------------------------------------------------------------------------------------
	public Eel(Dimension screenSize,float velx, float vely,double scale) {
		int w,h;
		
		pos=new PVector(Aquarium.Ocean_W/3,Aquarium.Ocean_H/5*4);
		
		vel=new PVector((float)Math.random()*10-20,(float)Math.random()*10-20);
		
		this.scale=scale;
		
		bodyW*=scale;
		bodyH*=scale;
		size= new Dimension(bodyW,bodyH);
		random=(int)(Math.random()*4);

		angle=0;
		dir=(float)1;
		jawTimer=0.01;
		tailTimer=0.01;
		jawA=0;
		tailA=0;
		
		int r= (int)(Math.random()*255);
		int g= (int)(Math.random()*255);
		int b= (int)(Math.random()*255);
		body= new Color(r,g,b);
		
		fin=body.darker();
		
		//body=new Color(90,214,158);
		spot= new Color(80,89,85,200);
		//fin=new Color(69,163,120);
		
		//for drawing
		head= new Polygon();
		head.addPoint(0, -size.height/4*3/2);
		head.addPoint(0, size.height/4*3/2);
		head.addPoint(size.width/20*3, size.height/4*3/2);
		head.addPoint(size.width/20*3, 0);
		
		jaw= new Polygon();
		jaw.addPoint(0, 0);
		jaw.addPoint(0, size.height/4);
		jaw.addPoint(size.width/6, size.height/8);
		jaw.addPoint(size.width/6, 0);
		
		tail= new Polygon();
		tail.addPoint(0, -size.height/10*4);
		tail.addPoint(0, size.height/10*4);
		tail.addPoint(-size.width/100*49, size.height/10);
		tail.addPoint(-size.width/100*49, -size.height/10);
		
		tuFin = new QuadCurve2D.Float();
		tuFin.setCurve(0,-size.height/10*4,-size.width/10,-size.height/5*4,-size.width/100*49,-size.height/10);
		
		tlFin = new QuadCurve2D.Float();
		tlFin.setCurve(0,size.height/10*4,-size.width/10,size.height/5*4,-size.width/100*49,size.height/10);
		
		fuFin=new QuadCurve2D.Float();
		fuFin.setCurve(0,-size.height/10*4,size.width/5,-size.height,size.width/5*2,-size.height/10*4);
		
		flFin=new QuadCurve2D.Float();
		flFin.setCurve(0,size.height/10*4,size.width/5,size.height,size.width/3,size.height/10*4);
		
	}
	//-------------------------------------------------------------------------------------
	public void draw(Graphics g) {
		Graphics2D g2= (Graphics2D) g;
		
		AffineTransform origin = g2.getTransform();
		
		//Default face right
		g2.translate(pos.x,pos.y);
		g2.rotate(vel.heading());
		if (vel.x < 0) g2.scale(1,-1);
		AffineTransform eelMid= g2.getTransform();
		//draw the eel
		//tail
		g2.setTransform(eelMid);
		g2.rotate(tailA);
		g2.setColor(fin);
		g2.fill(tuFin);
		g2.fill(tlFin);
		g2.setColor(body);
		g2.fillPolygon(tail);
		
		//fBody
		g2.setTransform(eelMid);
		g2.setColor(fin);
		g2.fill(fuFin);
		g2.fill(flFin);
		g2.setColor(body);
		g2.fillOval(-size.height/10*4, -size.height/10*4, size.height/5*4, size.height/5*4);
		g2.fillRect(0, -size.height/10*4, size.width/2/3*2,size.height/5*4);
		
		
		//head
		g2.translate(size.width/2/3*2, 0);
		g2.setColor(body);
		g2.fillOval(-size.height/10*4, -size.height/10*9/2, size.height/5*4, size.height/10*9);
		
		g2.translate(size.height/5, -size.height/10);
		g2.fillOval(-size.height/4*3/2,-size.height/4*3/2,size.height/4*3, size.height/4*3);
		g2.fillPolygon(head);
		//eye
		g2.setColor(Color.black);
		g2.fillOval(-size.width/30/2,-size.height/5/2,size.width/30,size.height/5);
		//Jaw
		g2.rotate(jawA);
		g2.setColor(body);
		g2.translate(-size.width/30, size.height/5*2);
		g2.fillOval(-size.height/2/2, -size.height/2/2, size.height/2, size.height/2);
		g2.fillPolygon(jaw);
		
		g2.setTransform(eelMid);
		
		//drawSpot
		g2.setColor(spot);
		g2.translate(size.width/30*3*3, 0);
		for(int i=0; i<random;i++) {
			int t=size.width/30*3;
			g2.fillOval(-size.width/30/2-t*i,-(size.height/3-i*2)/2,size.width/30, size.height/3-i*2);
			
		}

		g2.setTransform(origin);
	}
	//-------------------------------------------------------------------------------------
	public void update(Food d) {
		move();
		seeEdge();
		
		//Animation
		jawA+=jawTimer;
		tailA+=tailTimer*vel.mag();
		if(jawA>0.2||jawA<-0.2){jawTimer*=-1;}
		if (tailA>0.5) tailTimer=-0.01;
		else if (tailA<-0.5) tailTimer=0.01;
		
		if (d==null || !seeFood(d)){
			vel.limit(3);
			randomIdle();

		}
		else {
			path=PVector.sub (pos,d.getPos() );
			angle= path.heading();
			vel=PVector.fromAngle(angle);
			vel.mult(-10);
			vel.limit(7);
		}
	
		
	}
	//-------------------------------------------------------------------------------------
	public PVector getPos() {
		return pos;
	}
	//-------------------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------------------
	private void move() {
		pos.add(vel);

	}
	//-------------------------------------------------------------------------------------
	public Dimension getSize() {
		return size;
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
	private void randomIdle() {
		rangle =(float) 0.005 * dir; 
	    if ( Math.random()*100 < 1) {
	      dir *= -1;
	    }
	    vel.rotate(rangle);
	}
	//-------------------------------------------------------------------------------------
	private boolean seeFood(Food d) {
		if(d!=null) {
			if (pos.dist(d.getPos())< this.bodyW*1.5)  {
			//see if fish in range /2 body width & height from mid
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	
}
