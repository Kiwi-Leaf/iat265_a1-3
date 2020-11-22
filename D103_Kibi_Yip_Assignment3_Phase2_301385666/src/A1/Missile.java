package A1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import processing.core.PVector;

public class Missile extends Creatures {
	private Ellipse2D.Double body;

	public Missile(float x, float y) {
		size = new Dimension(10, 10);
		body = new Ellipse2D.Double(-size.width / 2, -size.height / 2, size.width, size.height);
		pos = new PVector(x, y);
		vel = new PVector(0, -3);
		fov = null;
		outline = new Area(body);
	}

	public void update(ArrayList<SimulatedObject> cList, ArrayList<Missile> mList) {
		move();
		ArrayList<SimulatedObject> fList = filterTargetList(cList);
		traceBestFood(fList);
		for (int i = 0; i < cList.size(); i++) {
			if (cList.get(i) instanceof Eel) {
				Eel a = (Eel) cList.get(i);
				if (hitTarget(a)) {
					cList.remove(a);
					for (int j = 0; j < mList.size(); j++) {
						mList.remove(this);
					}
				}

			}
		}
		for (int j = 0; j < mList.size(); j++) {
			if (this.pos.x < 0 || pos.x > screenSize.width && pos.y < 0 || pos.y > screenSize.height)
				mList.remove(this);
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		// TODO Auto-generated method stub
		AffineTransform origin = g2.getTransform();

		g2.translate(pos.x, pos.y);
		g2.setColor(Color.black);
		g2.fill(body);
		g2.setTransform(origin);
	}

	public void move() {
		pos.add(vel);
	}

	public Shape getBoundary() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);

		return at.createTransformedShape(outline);
	}

	@Override
	public void stateScript(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean eatable(SimulatedObject target) {
		return (target instanceof Eel);
	}

	private boolean hitTarget(Eel e) {
		return (this.getBoundary().intersects(e.getBoundary().getBounds2D())
				&& e.getBoundary().intersects(this.getBoundary().getBounds2D()));
	}

	protected void approach(SimulatedObject target) {
		float coef = .1f; // coefficient of acceleration relative to maxSpeed
		PVector direction = PVector.sub(target.getPos(), pos).normalize();
		PVector accel = PVector.mult(direction, vel.mag() * coef);
		vel.add(accel);
		vel.limit(10);
	}

}
