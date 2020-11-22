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

public class Food extends Creatures {
	/*
	 * Theses are variables and methods are exclusive to this class. Some are
	 * methods from super class that are being override. Methods that are exclusive
	 * to this class are setTargetedColor() and setOriginalColor().
	 * 
	 */

	private int bodyW = 100;
	private int bodyH = 50;// bodyW:bodyH = 2:1

	private Color fin, body, altFin, altBody, outlineColor;

	private Ellipse2D.Double bodyOval, eyeOval;
	private Arc2D.Double finArc;
	public boolean isEnlarge = false;
	protected boolean isLookingForFood = true;

	// -------------------------------------------------------------------------------------
	public Food(int x, int y, double scale) {
		super();
		this.scale = scale;
		bodyW *= scale;
		bodyH *= scale;
		size = new Dimension(bodyW, bodyH);
		topSpeed = 3;
		tempTopSpeed = topSpeed;
		screenSize = new Dimension(Aquarium.Ocean_W, Aquarium.Ocean_H);

		pos = new PVector(x, y);
		vel = new PVector((float) Math.random() * 10 - 5, (float) Math.random() * 10 - 5);

		fov = new Arc2D.Double(-size.width / 2, -size.width / 2, size.width * 2, size.width, -60, 120, Arc2D.PIE);

		setShapeAttributes();
	}

	// -------------------------------------------------------------------------------------
	private void setShapeAttributes() {
		int r = (int) (Math.random() * 255);
		int g = (int) (Math.random() * 255);
		int b = (int) (Math.random() * 255);
		body = new Color(r, g, b);
		altBody = new Color(r, g, b);
		outlineColor = new Color(0, 0, 0, 0);
		r = (int) (Math.random() * 255);
		g = (int) (Math.random() * 255);
		b = (int) (Math.random() * 255);
		fin = new Color(r, g, b);
		altFin = new Color(r, g, b);

		finArc = new Arc2D.Double(-size.width / 10 * 9, -size.height / 2, size.width / 2, size.height, -90, 180,
				Arc2D.CHORD);
		bodyOval = new Ellipse2D.Double(-size.width / 10 * 8 / 2, -size.height / 2, size.width / 10 * 9, size.height);
		eyeOval = new Ellipse2D.Double(size.width / 5, -size.height / 3 / 2, size.width / 10, size.height / 3);
		outline = new Area(bodyOval);
		outline.add(new Area(finArc));
	}

	// -------------------------------------------------------------------------------------
	public void update(ArrayList<SimulatedObject> cList) {
		super.update(cList);
		ArrayList<SimulatedObject> fList = filterTargetList(cList);
		move();
		seeEdge();
		topSpeed = tempTopSpeed;
		isLookingForFood = true;
		if (reactTime > -1) {
			reactTime--;
			topSpeed = 2 + tempTopSpeed;
			isLookingForFood = false;
		}
		// vel.limit(1);
		if (isEnlarge && this.scale < 2.0) {
			enlarge();
		}
		if (isLookingForFood) {
			traceBestFood(fList);
		}
		for (int i = 0; i < fList.size(); i++) {

		}
		stateScript(state);
//		updateReport();
	}

	// -------------------------------------------------------------------------------------
	public void draw(Graphics2D g2) {
//		Graphics2D g2= (Graphics2D) g;
		AffineTransform origin = g2.getTransform();
		g2.translate(pos.x, pos.y);
		g2.scale(scale, scale);
		g2.rotate(vel.heading());
		if (vel.x < 0)
			g2.scale(1, -1);
		// body
		g2.setColor(fin);

		g2.fill(finArc);
		g2.setColor(body);

		g2.fill(bodyOval);
		g2.setColor(Color.black);

		g2.fill(eyeOval);

		g2.setTransform(origin);
		g2.setColor(outlineColor);
		g2.draw(getBoundary());

		if (display)
			drawInfo(g2);
	}
	// -------------------------------------------------------------------------------------

	public boolean checkMouseHit(MouseEvent e) {
		return getBoundary().contains(e.getX() - 50, e.getY() - 50);
	}

	// -------------------------------------------------------------------------------------
	public void enlarge() {
		if (scale < 2.0)
			scale += 0.1;
	}
	// -------------------------------------------------------------------------------------

//	public void setTargetedColor() {
//		this.body = new Color(255, 255, 255);
//		this.fin = body.darker();
//		this.outlineColor = Color.red;
//	}

	// -------------------------------------------------------------------------------------
//	public void setOriginalColor() {
//		body = altBody;
//		fin = altFin;
//		outlineColor = new Color(0, 0, 0, 0);
//	}
	// -------------------------------------------------------------------------------------

	public void reverseSmaller(Food e) {
		float angle = (float) Math.atan2(pos.y - e.pos.y, pos.x - e.pos.x);
		float aveSpeed = (vel.mag() + e.vel.mag()) / 2;
		float coef = .1f;
		PVector accel = new PVector();
		accel.set((float) (aveSpeed * Math.cos(angle)), (float) (aveSpeed * Math.sin(angle)));
		accel.mult(coef * topSpeed);
		vel.add(accel);

	}

	// -------------------------------------------------------------------------------------
	public void reverseBoth(Food e) {
		float angle = (float) Math.atan2(pos.y - e.pos.y, pos.x - e.pos.x);
		float aveSpeed = (vel.mag() + e.vel.mag()) / 2;
		float coef = .1f;
		PVector accel = new PVector();
		accel.set((float) (aveSpeed * Math.cos(angle)), (float) (aveSpeed * Math.sin(angle)));
		accel.mult(coef * topSpeed);
		vel.add(accel);
		accel.set((float) (aveSpeed * Math.cos(-angle)), (float) (aveSpeed * Math.sin(-angle)));
		accel.mult(coef * topSpeed);
		e.vel.add(accel);

	}

	// -------------------------------------------------------------------------------------
	public boolean isEaten(Eel e) {
		if (this.getBoundary().intersects(e.getBoundary().getBounds2D())
				&& e.getBoundary().intersects(this.getBoundary().getBounds2D())) {

			return true;
		}
		return false;

	}

	// ------------------------------------------------
	protected boolean eatable(SimulatedObject food) {
		return (food instanceof FishFood);
	}

	// --------------------------------------------
	protected float getAttraction(FishFood f) {
		return (float) f.getScale() * 10 / PVector.dist(pos, f.getPos());
	}

	// ----------------------------------------
	protected void attractedBy(FishFood target) {
		float coef = .2f; // coefficient of acceleration relative to maxSpeed
		PVector direction = PVector.sub(target.getPos(), pos).normalize();
		PVector acceleration = PVector.mult(direction, topSpeed * coef);
		vel.add(acceleration);
	}

	// --------------------------------------
	@Override
	public void stateScript(int state) {
//		switch (state) {
//		case (SICK):
//			deadTimer=90;
//			body = Color.red;
//			break;
//		case (CONTENT):
//			body = altBody;
//			break;
//		case (FULL):
//			body = altBody;
//			break;
//		case (DEAD):
//			deadTimer--;
//			body = new Color(255, 255, 255, 50);
//			break;
//		}

		if (state == FULL) {
			body = altBody;
		} else if (state == CONTENT) {
			body = altBody;
		} else if (state == SICK) {
//			System.out.println("is here");
			deadTimer = 90;
			body = new Color(255, 0, 0);
		} else if (state == DEAD) {
//			System.out.println("is here2");
			deadTimer--;
			body = new Color(255, 255, 255, 50);
		}
	}

	// -----------------------------------------------
	public void updateReport() {
		if (report > 0)
			report--;
		else {
			System.out.println("Fish energy" + this.energy);
			System.out.println("Fish state:" + this.state);
			System.out.println("=================");
			report = 30;
		}
	}

	// ---------------------------------
	public void setScale(double x) {
		if (this.scale < 5)
			this.scale += x;
	}
}
