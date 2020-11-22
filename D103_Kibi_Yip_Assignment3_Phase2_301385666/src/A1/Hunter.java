package A1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import processing.core.PVector;

public class Hunter extends Creatures {
	protected Ellipse2D.Double bodyOval, windowOval, topOval, tailOval;
	protected Color mainColor, windowColor, secondaryColor;
	protected ArrayList<Missile> missileList = new ArrayList<Missile>();
	protected PVector pos, vel;
	private int health;

	public Hunter() {
		super();
		health = 100;
		size = new Dimension(100, 60);
		pos = new PVector(screenSize.width - this.size.width - this.size.width / 5, screenSize.height / 2);
		mainColor = new Color(224, 148, 58);
		bodyOval = new Ellipse2D.Double(-size.width / 2, -size.height / 2, size.width, size.height);
		tailOval = new Ellipse2D.Double(size.width / 2, -size.height / 2, size.width / 5, size.height);
		windowColor = new Color(58, 170, 224);
		windowOval = new Ellipse2D.Double(-size.width / 8 - size.width / 8, -size.width / 8, size.width / 4,
				size.width / 4);
		secondaryColor = new Color(148, 92, 24);
		topOval = new Ellipse2D.Double(-size.width / 8, -size.height / 1.25, size.width / 4, size.height / 2);
		vel = new PVector(0, 5);
		outline = new Area(bodyOval);
		outline.add(new Area(tailOval));
		outline.add(new Area(topOval));

	}

	@Override
	public void stateScript(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		AffineTransform origin = g.getTransform();

		// Default face right
		g.translate(pos.x, pos.y);

		g.setColor(secondaryColor);
		g.fill(topOval);

		g.setColor(mainColor);
		g.fill(bodyOval);

		g.setColor(windowColor);
		g.fill(windowOval);

		g.setColor(secondaryColor);
		g.setStroke(new BasicStroke(5));
		g.draw(windowOval);
		g.fill((tailOval));

		g.setTransform(origin);
		if (display)
			drawInfo(g);
		if (missileList != null) {
			for (int i = 0; i < missileList.size(); i++) {
				missileList.get(i).draw(g);
			}
		}
	}

	@Override
	protected boolean eatable(SimulatedObject food) {
		// TODO Auto-generated method stub
		return false;
	}

	public void update(ArrayList<SimulatedObject> cList) {
		move();
		seeEdge();
		// System.out.println("is here");
		if (missileList != null) {
			for (int i = 0; i < missileList.size(); i++) {
				missileList.get(i).update(cList, missileList);

			}
		}
	}

	protected void move() {

		pos.add(vel);
//		
//		if(pos.y<0+size.height/2||pos.y>screenSize.height-size.height/2) {
//			vel.y*=-1;
//		}

	}

	public void fire() {
		
		missileList.add(new Missile(pos.x, pos.y));
	}

	public Shape getBoundary() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);

		at.scale(scale * scaleW, scale * scaleH);

		return at.createTransformedShape(outline);
	}

	protected void seeEdge() {

		float coef = .2f;
		PVector accel = new PVector();

		if (getBoundary().intersects(left) || pos.x < 0) {
			accel.add(1, 0);

		} else if (getBoundary().intersects(right) || pos.x > screenSize.width) {
			accel.add(-1, 0);

		}
		if (getBoundary().intersects(top) || pos.y < 0) {
			accel.add(0, 1);

		} else if (getBoundary().intersects(bottom) || pos.y > screenSize.height) {
			accel.add(0, -1);

		}

		accel.mult(coef * topSpeed);
		vel.add(accel);
	}

	public void clearMissile() {

		missileList = new ArrayList<Missile>();
	}

	protected void drawInfo(Graphics2D g) {
		AffineTransform at = g.getTransform();
		g.translate(pos.x, pos.y);

		String st3 = "Health : " + String.format("%d", health);

		Font f = new Font("Courier", Font.PLAIN, 12);
		FontMetrics metrics = g.getFontMetrics(f);
		g.setFont(f);
		float textWidth = metrics.stringWidth(st3);
		float textHeight = metrics.getHeight();
		float margin = 25, spacing = 6;

		g.setColor(Color.black);
		g.drawString(st3, -textWidth / 2, (float) (-size.height * scale * .75f - margin - (textHeight + spacing) * 1f));

		g.setTransform(at);
	}
}
