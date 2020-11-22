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

public class FishFood extends SimulatedObject {
	private Ellipse2D.Double body;
	private Color bodyColor;

	public FishFood(Dimension screenSize) {
		this.screenSize = screenSize;
		this.size = new Dimension(10, 10);

		bodyColor = Color.yellow;
		scale = Math.random() * 5 + 1;
		// float x = (float) (Math.random() * (screenSize.width - size.width * scale) +
		// size.width * scale / 2);
		// float y = (float) (Math.random() * (screenSize.height - size.width * scale) +
		// size.width * scale / 2);
		float x = (float) (Math.random() * (screenSize.width -50));
		float y = (float) (Math.random() * (screenSize.height-50));
		pos = new PVector(x, y);
		body = new Ellipse2D.Double(size.width / 2, size.height / 2, size.width, size.height);
		outline = new Area(body);

	}

	public void draw(Graphics2D g2) {
		AffineTransform origin = g2.getTransform();
		g2.translate(pos.x, pos.y);
		g2.scale(scale, scale);
		g2.setColor(bodyColor);
		g2.fill(body);
		g2.setTransform(origin);

	}

	// ----------------------------------
	public void update(ArrayList<SimulatedObject> creatureList) {
		for (int i = 0; i < creatureList.size(); i++) {
			if (creatureList.get(i) instanceof Food) {
				Food fish = (Food) creatureList.get(i);
				if (isEaten(fish)) {
					if (fish.getState() == 3) {
						double x = this.scale / 10;
						fish.setScale(x);
					} else {
						int x = (int) this.scale * 50;
						fish.setEnergy(x);
					}
					creatureList.remove(this);
					creatureList.add(new FishFood(screenSize));
				}
			}
		}
	}

	// -----------------------------------
	public double getScale() {
		return scale;
	}

	// ----------------------------------------------
	public Shape getBoundary() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.scale(scale, scale);
		return at.createTransformedShape(outline);
	}

	// -----------------------------------
	public boolean isEaten(Food fish) {
		return (this.getBoundary().intersects(fish.getBoundary().getBounds2D()));
	}

	// -----------------------------------
	public Shape getOutline() {
		return outline;
	}
}
