package A1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Aquarium extends JPanel implements ActionListener {
	// -------------------------------------------------------------------------------------

	// private ArrayList<Eel> eelList= new ArrayList<Eel>();

	private static Timer timer;
	public final static int Ocean_W = 1700;
	public final static int Ocean_H = 900;
	public Dimension winSize;
	private Dimension screenSize;
	private Sea sea;
	// private ArrayList<Food> fishList=new ArrayList<Food>();
	private ArrayList<SimulatedObject> creatureList = new ArrayList<SimulatedObject>();

	private final static int Fish_Max = 20;
	private Creatures hunter;
	private final int FISH_MAX = 12;
	private final int EEL_MAX = 6;
	private boolean activateHunter = false;
	private boolean isFired = false;
	private int delayTimer = 150;
	private boolean displayInfo = false;
	private String stringToDisplay = "Testing";
	private int textTimer = -1;
	private boolean text1=false;

	public Aquarium() {
		super();
		screenSize = new Dimension(Ocean_W, Ocean_H);

		timer = new Timer(33, this);

		timer.start();
		hunter = new Hunter();
		sea = new Sea(screenSize);
		winSize = new Dimension(Ocean_W + 100, Ocean_H + 100);

		for (int i = 0; i < EEL_MAX; i++) {
			Eel e = new Eel(screenSize, Math.random() * 0.8 + 0.5);

			creatureList.add(e);
		}

		for (int j = 0; j < FISH_MAX; j++) {
			Food f = new Food((int) (Math.random() * screenSize.width), (int) (Math.random() * screenSize.height),
					Math.random() * 0.2 + 0.5);
			// fishList.add(f);

			creatureList.add(f);
			creatureList.add(new FishFood(screenSize));
		}
		for (int r = 0; r < 10; r++) {

			creatureList.add(new FishFood(screenSize));
		}

		setPreferredSize(winSize);
		addMouseListener(new MyMouseAdapter());
		addMouseMotionListener(new MyMouseMotionAdapter());
		addKeyListener(new MyKeyAdapter());
		setFocusable(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		setBackground(Color.black);
		sea.draw(g2);

		displayStat(g2);
		for (SimulatedObject c : creatureList) {

			c.draw(g2);
		}
		if (activateHunter == true)
			hunter.draw(g2);

	}

	public void actionPerformed(ActionEvent e) {
		activateHunter();
		changeTextDisplay();
		for (int c = 0; c < creatureList.size(); c++) {
			creatureList.get(c).update(creatureList);
			if (creatureList.get(c) instanceof Creatures) {
				if (creatureList.get(c) instanceof Eel) {

				}

				for (int b = c + 1; b < creatureList.size(); b++) {
					if ((creatureList.get(c) instanceof Eel || creatureList.get(c) instanceof Food)
							&& (creatureList.get(b) instanceof Eel || creatureList.get(b) instanceof Food)) {
						Creatures f = (Creatures) creatureList.get(c);
						Creatures f2 = (Creatures) creatureList.get(b);
						if (activateHunter) {
							if (f.collideWithEachOther(hunter)) {
								f.reverseSmaller(hunter);
							}
						}
						if (creatureList.get(c).getClass() == creatureList.get(b).getClass()) {
//							Creatures f = (Creatures) creatureList.get(c);
//							Creatures f2 = (Creatures) creatureList.get(b);

							if (f.collideWithEachOther(f2)) {

								if (f.getScale() > f2.getScale()) {
									f2.reverseSmaller(f);
								} else if (f.getScale() < f2.getScale()) {

									f.reverseSmaller(f2);
								} else {

									f.reverseBoth(f2);
								}
							}
						} else {
							Eel eelTemp;
							Food fishTemp;
							if (creatureList.get(c) instanceof Eel) {
								eelTemp = (Eel) creatureList.get(c);
								fishTemp = (Food) creatureList.get(b);
							} else {
								eelTemp = (Eel) creatureList.get(b);
								fishTemp = (Food) creatureList.get(c);
							}
							if (fishTemp.collideWithEachOther(eelTemp)) {

								fishTemp.reverseSmaller(eelTemp);
							}
							if (fishTemp.isEaten(eelTemp)) {
								creatureList.remove(fishTemp);
								creatureList.add(new Food((int) (Math.random() * screenSize.width),
										(int) (Math.random() * screenSize.height), Math.random() * 0.2 + 0.5));
							}

						}
					}
				}
			}
		}
		if (activateHunter == true) {
			hunter.update(creatureList);
		}
//		System.out.println(countItem(creatureList, Food.class));
		repaint();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Underwater A1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Aquarium ocean = new Aquarium();
		frame.add(ocean);
		frame.pack();
		frame.setVisible(true);

	}

	private class MyMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1) {
				for (int i = 0; i < creatureList.size(); i++) {
					if (creatureList.get(i) instanceof Food) {
						Food food = (Food) creatureList.get(i);
						if (e.isControlDown() && food.checkMouseHit(e)) {
							creatureList.remove(food);
						}
					}
				}
			} else {
				creatureList.add(new Food(e.getX() - 50, e.getY() - 50, 0.5));
			}

		}

		public void mousePressed(MouseEvent e) {
			for (int i = 0; i < creatureList.size(); i++) {
				if (creatureList.get(i) instanceof Food) {
					Food food = (Food) creatureList.get(i);
					if (food.checkMouseHit(e)) {
						food.isEnlarge = true;
					} else {
						food.isEnlarge = false;
					}
				}
			}

		}

		public void mouseReleased(MouseEvent e) {
			for (int i = 0; i < creatureList.size(); i++) {
				if (creatureList.get(i) instanceof Food) {
					Food food = (Food) creatureList.get(i);
					food.isEnlarge = false;
				}
			}
		}
	}

	private class MyMouseMotionAdapter extends MouseMotionAdapter {

	}

	private class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_D) {
				if (displayInfo == false)
					displayInfo = true;
				else
					displayInfo = false;
				for (int i = 0; i < creatureList.size(); i++) {
					if (creatureList.get(i) instanceof Creatures) {
						Creatures c = (Creatures) creatureList.get(i);
						c.setDisplay(displayInfo);
					}
				}
				hunter.setDisplay(displayInfo);
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {

				if (isFired == false) {
					Hunter hunter2 = (Hunter) hunter;
					hunter2.fire();
					isFired = true;
				}

			}

		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				isFired = false;
			// fire = false;
		}
	}

	private int countItem(ArrayList<SimulatedObject> cList, Class<?> cls) {
		int count = 0;
		for (int i = 0; i < cList.size(); i++) {
			if (cls == Eel.class) {
				if (cList.get(i) instanceof Eel) {
					count++;
				}
			} else if (cls == Food.class) {
				if (cList.get(i) instanceof Food) {
					count++;
				}
			}
		}
		return count;
	}

	private void activateHunter() {
		if (countItem(creatureList, Food.class) <= FISH_MAX / 2) {
			activateHunter = true;
			if(textTimer<0&& text1==false) {
				textTimer=90;
				text1=true;
			}
			if (countItem(creatureList, Eel.class) <= EEL_MAX / 2) {
				Hunter hunter2 = (Hunter) hunter;
				hunter2.clearMissile();
				activateHunter = false;
				text1=false;
				delayTimer--;
				if (delayTimer < 0) {
					repopulateCreatures();
					delayTimer = 150;
				}
			}
		}

	}

	private void repopulateCreatures() {
		int fishNeeded = FISH_MAX - countItem(creatureList, Food.class);
		for (int i = 0; i < fishNeeded; i++) {
			Food f = new Food((int) (Math.random() * screenSize.width), (int) (Math.random() * screenSize.height),
					Math.random() * 0.2 + 0.5);
			creatureList.add(f);
		}
		System.out.println(countItem(creatureList, Food.class));
		int EelNeeded = EEL_MAX - countItem(creatureList, Eel.class);
		for (int j = 0; j < EelNeeded; j++) {
			Eel e = new Eel(screenSize, Math.random() * 0.8 + 0.5);
			creatureList.add(e);
		}
		System.out.println(countItem(creatureList, Eel.class));
	}

	private void changeTextDisplay() {

		if (textTimer > 0) {
			textTimer--;
			stringToDisplay="Submarine is here!";
		}
		else if(activateHunter) {
			stringToDisplay="Eel to remove:"+(countItem(creatureList,Eel.class)-EEL_MAX/2);
		}
		else {
			stringToDisplay="Submarine is not here.";
		}
	}

	private void displayStat(Graphics2D g) {
		Font statFont = new Font("Courier", Font.BOLD, 20);
		Rectangle2D.Double statBox = new Rectangle2D.Double(0, screenSize.height - 40, screenSize.width, 30);
		g.setColor(new Color(255, 255, 255, 150));
		g.fill(statBox);

		g.setFont(statFont);
		// String stringToDisplay="";
		g.setColor(Color.black);
		g.drawString(stringToDisplay, 10, screenSize.height - 20);
	}

}
