package A1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Aquarium extends JPanel implements ActionListener {
	//-------------------------------------------------------------------------------------
	//public ArrayList<Food> fish= new ArrayList<Food>();
	private Eel eel;
	private Food fish;
	private static Timer timer;
	public final static int Ocean_W=900;
	public final static int Ocean_H=800;
	public Dimension winSize;
	private Dimension screenSize;
	private int respawn;
	private Sea sea;
	private double randSize=0;
	
	public Aquarium(){
		super();
		screenSize= new Dimension(Ocean_W,Ocean_H);
		
		timer = new Timer(33, this);
		timer.start();
		respawn=0;
		eel= new Eel(screenSize,2,2,1);
		fish=new Food(screenSize.width/2,screenSize.height/2,0.5);
		sea=new Sea(screenSize);
		winSize= new Dimension(Ocean_W+40,Ocean_H+40);
		setPreferredSize(winSize);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		setBackground(Color.black);
		sea.draw(g2);
		//sea.bubbles(g2);
		
		eel.draw(g2);
		
		if(fish!=null) {
		fish.draw(g2);
		}
		
		
		
	}
	public void actionPerformed(ActionEvent e) {
		sea.update();
		eel.update(fish);
		
		if(fish!=null) {
			fish.update();
			if(eel.getPos().dist(fish.getPos())<eel.getSize().width/2+fish.getSize().width/2) {
				fish=null;
			}
		}
		else {
			if (respawn<150) respawn++;
			else {
				respawn=0;
				
				while(randSize<=0) {
					randSize=Math.random();
				}
				fish= new Food((int)(Math.random()*screenSize.width),(int)(Math.random()*screenSize.height),randSize);
			}
		}

		repaint();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Underwater A1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Aquarium ocean = new Aquarium();
		frame.add(ocean);
		//frame.setPreferredSize(new Dimension(Ocean_W+20,Ocean_H+20));
		frame.pack();
		frame.setVisible(true);
		

	}

}
