package A1;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PVector;

public class SimulatedObject {
	protected PVector pos,vel;
	protected double scale;
	protected Dimension size;
	protected Dimension screenSize;
	protected Area outline;
	protected double scaleW, scaleH;
	
	public SimulatedObject() {
		// TODO Auto-generated constructor stub
	}

	public void update(ArrayList<SimulatedObject> creatureList) {

	}


	public void draw(Graphics2D g2) {
		// TODO Auto-generated method stub
		
	}

	public PVector getPos() {
		// TODO Auto-generated method stub
		return pos;
	}



}
