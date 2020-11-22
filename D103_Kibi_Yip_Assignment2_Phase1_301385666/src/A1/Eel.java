package A1;
import processing.core.PVector;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.awt.*;



public class Eel{
	private PVector pos,vel,path;
	private double scale;
	private float angle,dir,rangle;
	private int bodyW=200;
	private int bodyH=50;
	private int random;
	private Dimension screenSize;
	private Dimension size;
	private Color body,spot,fin;
	private Polygon head,jaw,tail;
	private QuadCurve2D tuFin,tlFin,fuFin,flFin;
	private Ellipse2D.Double midBody, headOval,jawOval,eyeOval;
	private ArrayList<Ellipse2D.Double> spotOval;
	private Rectangle2D.Double fBody,top,bottom,left, right;
	private double jawA,tailA,jawTimer,tailTimer;
	private Area outline;
	
	//private Path2D.Double headPath,jawPath,tailPath;
	
	//-------------------------------------------------------------------------------------
	public Eel(Dimension screenSize,float velx, float vely,double scale) {
		
		
		pos=new PVector(Aquarium.Ocean_W/3,Aquarium.Ocean_H/5*4);
		
		vel=new PVector((float)Math.random()*10-20,(float)Math.random()*10-20);
		
		this.scale=scale;
		screenSize=new Dimension(Aquarium.Ocean_W,Aquarium.Ocean_H);
		//bodyW*=scale;
		//bodyH*=scale;
		size= new Dimension(bodyW,bodyH);
		random=(int)(Math.random()*4);
		spotOval=new ArrayList<Ellipse2D.Double>();
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
		
		setShapeAttributes();
		

 
		 top = new Rectangle2D.Double(0,-10, screenSize.width, 10);
		 bottom = new Rectangle2D.Double(0, screenSize.height-10, screenSize.width, 10);
		 left = new Rectangle2D.Double(-20, 0, 20, screenSize.height);
		 right = new Rectangle2D.Double(screenSize.width ,0, 10, screenSize.height );

		
	}
	//-------------------------------------------------------------------------------------
	private void setShapeAttributes() {
		//for drawing
		midBody= new Ellipse2D.Double(-size.height/10*4, -size.height/10*4, size.height/5*4, size.height/5*4);
		headOval=new Ellipse2D.Double(-size.height/4*3/2,-size.height/4*3/2,size.height/4*3, size.height/4*3);
		jawOval= new Ellipse2D.Double(-size.height/2/2, -size.height/2/2, size.height/2, size.height/2);
		eyeOval=new Ellipse2D.Double(-size.width/30/2,-size.height/5/2,size.width/30,size.height/5);
		
		
		fBody=new Rectangle2D.Double(0, -size.height/10*4, size.width/2/3*2,size.height/5*4);
		
		for(int i=0; i<random;i++) {
			//System.out.println("code reach here");
			int t=size.width/30*3;
			Ellipse2D.Double temp;
			temp=new Ellipse2D.Double(-size.width/30/2-t*i,-(size.height/3-i*2)/2,size.width/30, size.height/3-i*2);
			spotOval.add(temp);
		}

		int[] xPoint= {0+size.height/5+size.width/2/3*2,0+size.height/5+size.width/2/3*2,size.width/20*3+size.height/5+size.width/2/3*2,size.width/20*3+size.height/5+size.width/2/3*2};
		int[] yPoint= {-size.height/4*3/2+-size.height/10,size.height/4*3/2+-size.height/10, size.height/4*3/2+-size.height/10,0+-size.height/10};
		
		head=new Polygon(xPoint,yPoint,xPoint.length);
//		head= new Polygon();
//		head.addPoint(0, -size.height/4*3/2);
//		head.addPoint(0, size.height/4*3/2);
//		head.addPoint(size.width/20*3, size.height/4*3/2);
//		head.addPoint(size.width/20*3, 0);
		//headPath=new Path2D.Double(head);
		
		int[] xPoint2= {0+size.height/5+size.width/2/3*2-size.width/30,0+size.height/5+size.width/2/3*2-size.width/30,size.width/6+size.height/5+size.width/2/3*2-size.width/30,size.width/6+size.height/5+size.width/2/3*2-size.width/30};
		int[] yPoint2= {0+-size.height/10+size.height/5*2,size.height/4+-size.height/10+size.height/5*2,size.height/8+-size.height/10+size.height/5*2,0+-size.height/10+size.height/5*2};
		
		jaw=new Polygon(xPoint2,yPoint2,xPoint2.length);
//		jaw= new Polygon();
//		jaw.addPoint(0, 0);
//		jaw.addPoint(0, size.height/4);
//		jaw.addPoint(size.width/6, size.height/8);
//		jaw.addPoint(size.width/6, 0);
		//jawPath=new Path2D.Double(jaw);
		
		tail= new Polygon();
		tail.addPoint(0, -size.height/10*4);
		tail.addPoint(0, size.height/10*4);
		tail.addPoint(-size.width/100*49, size.height/10);
		tail.addPoint(-size.width/100*49, -size.height/10);
		//tailPath=new Path2D.Double(tail);
		
		tuFin = new QuadCurve2D.Float();
		tuFin.setCurve(0,-size.height/10*4,-size.width/10,-size.height/5*4,-size.width/100*49,-size.height/10);
		
		tlFin = new QuadCurve2D.Float();
		tlFin.setCurve(0,size.height/10*4,-size.width/10,size.height/5*4,-size.width/100*49,size.height/10);
		
		fuFin=new QuadCurve2D.Float();
		fuFin.setCurve(0,-size.height/10*4+5,size.width/5+5,-size.height,size.width/5*2,-size.height/10*4);
		
		flFin=new QuadCurve2D.Float();
		flFin.setCurve(0,size.height/10*4-5,size.width/5-5,size.height,size.width/3,size.height/10*4);
		
		outline= new Area(head);
		outline.add(new Area(tail));
		outline.add(new Area(midBody));
		outline.add(new Area(headOval));
		outline.add(new Area(jawOval));
		outline.add(new Area(jaw));
		outline.add(new Area(fBody));
		outline.add(new Area(tuFin));
		outline.add(new Area(tlFin));
		outline.add(new Area(fuFin));
		outline.add(new Area(flFin));
		
	}
	
	//-------------------------------------------------------------------------------------
	public void draw(Graphics g) {
		Graphics2D g2= (Graphics2D) g;
		
		AffineTransform origin = g2.getTransform();
		
		//Default face right
		g2.translate(pos.x,pos.y);
		g2.rotate(vel.heading());
		g2.scale(scale, scale);
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
		g2.fill(midBody);
		g2.fill(fBody);
		
		//head
		g2.setColor(body);
		g2.fillPolygon(head);
		g2.fillPolygon(jaw);
		g2.translate(size.width/2/3*2, 0);
		
		g2.translate(size.height/5, -size.height/10);
		g2.fill(headOval);
		//g2.setColor(Color.red);
		
		
		//eye
		g2.setColor(Color.black);
		g2.fill(eyeOval);
		
		//Jaw
		g2.rotate(jawA);
		g2.setColor(body);
		g2.translate(-size.width/30, size.height/5*2);
		g2.fill(jawOval);
		
		
		g2.setTransform(eelMid);
		
		//drawSpot
		g2.setColor(spot);
		g2.translate(size.width/30*3*3, 0);
		for(Ellipse2D.Double o: spotOval) {
			g2.fill(o);
		}
		
		g2.setTransform(eelMid);
	

		g2.setTransform(origin);
//		g2.setColor(Color.black);
//		g2.draw(getBoundary().getBounds2D());
//		g2.setColor(Color.white);
//		g2.draw(top);
//		g2.setColor(Color.red);
//		g2.draw(left);
//		g2.setColor(Color.green);
//		g2.draw(right);
//		g2.setColor(Color.blue);
//		g2.draw(bottom);
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
//		if (pos.x<bodyW/2||pos.x>Aquarium.Ocean_W-bodyW/2) {
//			vel.x*=-1;
//		}
//		if(pos.y<bodyW/2||pos.y>Aquarium.Ocean_H-bodyW/2) {
//			vel.y*=-1;
//		}
		
		
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
	private void randomIdle() {
		rangle =(float) 0.005 * dir; 
	    if ( Math.random()*100 < 1) {
	      dir *= -1;
	    }
	    vel.rotate(rangle);
	}
	//-------------------------------------------------------------------------------------
	private boolean seeFood(Food d) {
		//--disable distance search--------------------------
		
		
//		if(d!=null) {
//			if (pos.dist(d.getPos())< this.bodyW*1.5)  {
//			//see if fish in range /2 body width & height from mid
//				return true;
//			}
//			else {
//				return false;
//			}
//		}
//		else {
//			return false;
//		}
		
		return true;
	}
	//-------------------------------------------------------------------------------------
	private Shape getBoundary() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(vel.heading());
		at.scale(scale, scale);
		return at.createTransformedShape(outline);
	}

	public Food findClosestFood(ArrayList<Food> fList) {
		Food closestFood = null;
		
		if (fList.size() > 0) {
			closestFood = fList.get(0);
			float closestDist = PVector.dist(this.getPos(), closestFood.getPos());

			for (Food f : fList)
				if (PVector.dist(this.getPos(), f.getPos()) < closestDist) {
					closestFood = f;
					closestDist = PVector.dist(this.getPos(), closestFood.getPos());
				}
		}
		

		return closestFood;
	}
	
}
