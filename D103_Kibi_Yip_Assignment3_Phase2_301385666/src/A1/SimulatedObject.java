package A1;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PVector;

public abstract class SimulatedObject {
	protected PVector pos, vel;
	protected double scale;
	protected Dimension size;
	protected Dimension screenSize;
	protected Area outline;
	protected double scaleW, scaleH;
	protected int report;

	public SimulatedObject() {
		// TODO Auto-generated constructor stub
	}

	public void update(ArrayList<SimulatedObject> creatureList) {

	}

	public abstract void draw(Graphics2D g2);

	public PVector getPos() {
		// TODO Auto-generated method stub
		return pos;
	}

	public void updateReport() {
		if (report > 0)
			report--;
		else {
			System.out.println();
			report = 30;
		}
	}

	public double getScale() {
		return scale;
	}
	
	protected void move() {
		
	}
	public Shape getOutline() {
		return outline;
	}
	public Shape getBoundary() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(vel.heading());
		at.scale(scale * scaleW, scale * scaleH);
		if (vel.x < 0)
			at.scale(1, -1);
		return at.createTransformedShape(outline);
	}
}
