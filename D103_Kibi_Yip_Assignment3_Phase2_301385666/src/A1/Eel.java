package A1;

import processing.core.PVector;

import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import java.awt.*;

public class Eel extends Creatures {
	/*
	 * Theses are variables and methods are exclusive to this class. Some are
	 * methods from super class that are being override. Methods that are exclusive
	 * to this class are traceBestFood(), attractedBy() and getAttraction().
	 * 
	 */

	private int random, random2, reactTime;

	private Color body, bodyStorage, spot, fin, eye;
	private Polygon head, jaw, tail;
	private QuadCurve2D tailUpperFin, tailLowerFin, frontUpperFin, frontLowerFin;
	private Ellipse2D.Double midBody, headOval, jawOval, eyeOval;
	private ArrayList<Ellipse2D.Double> spotOval;
	private Rectangle2D.Double fBody;
	private double jawA, tailA, jawTimer, tailTimer;

	private Boolean isTargetingFish;

	// -------------------------------------------------------------------------------------
	public Eel(Dimension screenSize, double scale) {

		super();
		vel = new PVector((float) Math.random() * 10 - 20, (float) Math.random() * 10 - 20);

		this.scale = scale;

		size = new Dimension(200, 50);
		scaleH = (Math.random() * 0.2 + 0.8);
		scaleW = (Math.random() * 0.2 + 0.8);

		isTargetingFish = true;
		reactTime = -1;
		topSpeed = 5;

		random = (int) (Math.random() * 4);

		// 10% chance of spawning a fast eel with red eyes
		random2 = (int) (Math.random() * 100);
		if (random2 < 10) {
			eye = new Color(255, 13, 17);
			topSpeed = 7;
		} else {
			eye = Color.black;
		}
		tempTopSpeed = topSpeed;

		spotOval = new ArrayList<Ellipse2D.Double>();

		jawTimer = 0.005;
		tailTimer = 0.005;
		jawA = 0;
		tailA = 0;

		int r = (int) (Math.random() * 255);
		int g = (int) (Math.random() * 255);
		int b = (int) (Math.random() * 255);
		body = new Color(r, g, b);
		bodyStorage = body;
		fin = body.darker();
		spot = new Color(80, 89, 85, 200);

		setShapeAttributes();

	}

	// -------------------------------------------------------------------------------------
	public void updateReport() {
		if (report > 0)
			report--;
		else {
//			System.out.println("Eel state:" + state);
//			System.out.println("Eel energy:" + energy);
//			System.out.println("=====================================:");
			report = 90;
		}
	}

	// -------------------------------------------------------------------------------------
	private void setShapeAttributes() {
		// for drawing
		midBody = new Ellipse2D.Double(-size.height / 10 * 4, -size.height / 10 * 4, size.height / 5 * 4,
				size.height / 5 * 4);
		headOval = new Ellipse2D.Double(-size.height / 4 * 3 / 2, -size.height / 4 * 3 / 2, size.height / 4 * 3,
				size.height / 4 * 3);
		jawOval = new Ellipse2D.Double(-size.height / 2 / 2, -size.height / 2 / 2, size.height / 2, size.height / 2);
		eyeOval = new Ellipse2D.Double(-size.width / 30 / 2, -size.height / 5 / 2, size.width / 30, size.height / 5);

		fBody = new Rectangle2D.Double(0, -size.height / 10 * 4, size.width / 2 / 3 * 2, size.height / 5 * 4);

		for (int i = 0; i < random; i++) {
			int t = size.width / 30 * 3;
			Ellipse2D.Double temp;
			temp = new Ellipse2D.Double(-size.width / 30 / 2 - t * i, -(size.height / 3 - i * 2) / 2, size.width / 30,
					size.height / 3 - i * 2);
			spotOval.add(temp);
		}

		int[] xPoint = { 0, 0, size.width / 20 * 3, size.width / 20 * 3 };
		int[] yPoint = { -size.height / 4 * 3 / 2, size.height / 4 * 3 / 2, size.height / 4 * 3 / 2, 0 };
		for (int i = 0; i < xPoint.length; i++) {
			xPoint[i] += size.height / 5 + size.width / 2 / 3 * 2;
			yPoint[i] += -size.height / 10;
		}

		head = new Polygon(xPoint, yPoint, xPoint.length);

		int[] xPoint2 = { 0, 0, size.width / 6, size.width / 6 };
		int[] yPoint2 = { 0, size.height / 4, size.height / 8, 0 };

		for (int j = 0; j < xPoint.length; j++) {
			xPoint2[j] += +size.height / 5 + size.width / 2 / 3 * 2 - size.width / 30;
			yPoint2[j] += -size.height / 10 + size.height / 5 * 2;
		}

		jaw = new Polygon(xPoint2, yPoint2, xPoint2.length);

		tail = new Polygon();
		tail.addPoint(0, -size.height / 10 * 4);
		tail.addPoint(0, size.height / 10 * 4);
		tail.addPoint(-size.width / 100 * 49, size.height / 10);
		tail.addPoint(-size.width / 100 * 49, -size.height / 10);

		tailUpperFin = new QuadCurve2D.Float();
		tailUpperFin.setCurve(0, -size.height / 10 * 4, -size.width / 10, -size.height / 5 * 4, -size.width / 100 * 49,
				-size.height / 10);

		tailLowerFin = new QuadCurve2D.Float();
		tailLowerFin.setCurve(0, size.height / 10 * 4, -size.width / 10, size.height / 5 * 4, -size.width / 100 * 49,
				size.height / 10);

		frontUpperFin = new QuadCurve2D.Float();
		frontUpperFin.setCurve(0, -size.height / 10 * 4 + 5, size.width / 5 + 5, -size.height, size.width / 5 * 2,
				-size.height / 10 * 4);

		frontLowerFin = new QuadCurve2D.Float();
		frontLowerFin.setCurve(0, size.height / 10 * 4 - 5, size.width / 5 - 5, size.height, size.width / 3,
				size.height / 10 * 4);

		fov = new Arc2D.Double(-size.width / 3, -size.width / 2, size.width * 1.5, size.width, -45, 90, Arc2D.PIE);

		outline = new Area(head);
		outline.add(new Area(tail));
		outline.add(new Area(midBody));
		outline.add(new Area(headOval));
		outline.add(new Area(jawOval));
		outline.add(new Area(jaw));
		outline.add(new Area(fBody));
		outline.add(new Area(tailUpperFin));
		outline.add(new Area(tailLowerFin));
		outline.add(new Area(frontUpperFin));
		outline.add(new Area(frontLowerFin));

	}

	// -------------------------------------------------------------------------------------
	public void draw(Graphics2D g2) {
		// Graphics2D g2= (Graphics2D) g;

		AffineTransform origin = g2.getTransform();

		// Default face right
		g2.translate(pos.x, pos.y);
		g2.rotate(vel.heading());
		// g2.scale(scale, scale);
		g2.scale(scale * scaleW, scale * scaleH);
		if (vel.x < 0)
			g2.scale(1, -1);
		AffineTransform eelMid = g2.getTransform();
		// draw the eel
		// tail
		g2.setTransform(eelMid);
		g2.rotate(tailA);
		g2.setColor(fin);
		g2.fill(tailUpperFin);
		g2.fill(tailLowerFin);
		g2.setColor(body);
		g2.fillPolygon(tail);

		// fBody
		g2.setTransform(eelMid);
		g2.setColor(fin);
		g2.fill(frontUpperFin);
		g2.fill(frontLowerFin);
		g2.setColor(body);
		g2.fill(midBody);
		g2.fill(fBody);

		// head
		g2.setColor(body);
		g2.fillPolygon(head);
		AffineTransform foo = g2.getTransform();
		g2.rotate(jawA);
		g2.fillPolygon(jaw);
		g2.setTransform(foo);
		g2.translate(size.width / 2 / 3 * 2, 0);

		g2.translate(size.height / 5, -size.height / 10);
		g2.fill(headOval);

		// eye
		g2.setColor(eye);
		g2.fill(eyeOval);

		// Jaw
		g2.rotate(jawA);
		g2.setColor(body);
		g2.translate(-size.width / 30, size.height / 5 * 2);
		g2.fill(jawOval);

		g2.setTransform(eelMid);

		// drawSpot
		g2.setColor(spot);
		g2.translate(size.width / 30 * 3 * 3, 0);
		for (Ellipse2D.Double o : spotOval) {
			g2.fill(o);
		}

		g2.setTransform(eelMid);

		g2.setTransform(origin);

		if (display)
			drawInfo(g2);

	}

	// -------------------------------------------------------------------------------------
	public void update(ArrayList<SimulatedObject> cList) {
		super.update(cList);
		move();
		seeEdge();
		ArrayList<SimulatedObject> fList = filterTargetList(cList);
		updateReport();
		// Animation
		jawA += jawTimer;
		tailA += tailTimer * vel.mag();
		if (jawA > 0.06 || jawA < -0.05) {
			jawTimer *= -1;
		}
		if (tailA > 0.5)
			tailTimer = -0.01;
		else if (tailA < -0.5)
			tailTimer = 0.01;
		topSpeed = tempTopSpeed;

		for (int c = 0; c < cList.size(); c++) {
			if (cList.get(c) instanceof Food) {

				Food fish = (Food) cList.get(c);
//				if (this.collideWithEachOther(fish)) {
//					isTargetingFish = true;
//				} else {
//					isTargetingFish = true;
//				}

				if (this.getBoundary().intersects(fish.getBoundary().getBounds2D())
						&& fish.getBoundary().intersects(this.getBoundary().getBounds2D())) {
					if (this.state <= 2) {
						energy += (int) (cList.get(c).getScale() * 70);
//						System.out.println("Eel ate food and gain energy" + ((int) (cList.get(c).getScale() * 70)));
//						System.out.println("New energy:" + energy);
					} else {
						if (this.scale < 3) {
							this.scale += cList.get(c).getScale() / 2;
						}
//						System.out.println("Eel get larger" + (cList.get(c).getScale() / 2));
//						System.out.println("New scale:" + scale);
					}
					cList.remove(c);
//					cList.add(new Food((int) (Math.random() * screenSize.width),
//							(int) (Math.random() * screenSize.height), Math.random() * 0.5 + 0.5));
				}
				if (isTargetingFish) {
					topSpeed = tempTopSpeed + 1;
					traceBestFood(fList);
				}
				if (reactTime > -1) {
					reactTime--;
					isTargetingFish = false;
				}

			}
		}

	}
	// -------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------

	public void reverseSmaller(Eel e) {

		float angle = (float) Math.atan2(pos.y - e.pos.y, pos.x - e.pos.x);
		float aveSpeed = (vel.mag() + e.vel.mag()) / 2;
		float coef = .2f;
		PVector accel = new PVector();
		accel.set((float) (aveSpeed * Math.cos(angle)), (float) (aveSpeed * Math.sin(angle)));
		accel.mult(coef * topSpeed);
		vel.add(accel);
		setReactTime();

	}

	// -------------------------------------------------------------------------------------
	public void reverseBoth(Eel e) {

		float angle = (float) Math.atan2(pos.y - e.pos.y, pos.x - e.pos.x);
		float aveSpeed = (vel.mag() + e.vel.mag()) / 2;
		float coef = .2f;
		PVector accel = new PVector();
		accel.set((float) (aveSpeed * Math.cos(angle)), (float) (aveSpeed * Math.sin(angle)));
		accel.mult(coef * topSpeed);
		vel.add(accel);
		this.setReactTime();

		accel.set((float) (aveSpeed * Math.cos(-angle)), (float) (aveSpeed * Math.sin(-angle)));
		accel.mult(coef * topSpeed);
		e.vel.add(accel);
		e.setReactTime();

	}
	// -----------------------------------------------------------------------

	// -----------------------------------------------------------------------
	protected float getAttraction(Food f) {
		return (float) f.getScale() * 10 / PVector.dist(pos, f.getPos());
	}

	// -----------------------------------------------------------------------
	protected void attractedBy(Food target) {
		float coef = .2f; // coefficient of acceleration relative to maxSpeed
		PVector direction = PVector.sub(target.getPos(), pos).normalize();
		PVector acceleration = PVector.mult(direction, topSpeed * coef);
		vel.add(acceleration);
	}
	// -----------------------------------------------

	// -------------------------------------------------
	public void setReactTime() {
		reactTime = 90;
	}

	// --------------------------------------------------
	protected boolean eatable(SimulatedObject food) {
		return (food instanceof Food);
	}

	// ---------------------------------------
	@Override
	public void stateScript(int energy) {
		switch (energy) {
		case (SICK):
			body = Color.white;
			break;
		case (CONTENT):
			body = bodyStorage;
			break;
		case (FULL):
			body = bodyStorage;
			break;
		case (DEAD):
			body = new Color(255, 255, 255, 50);
			break;
		}

	}
}
