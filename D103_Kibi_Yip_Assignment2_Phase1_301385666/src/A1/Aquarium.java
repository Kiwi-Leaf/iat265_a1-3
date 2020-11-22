package A1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;



public class Aquarium extends JPanel implements ActionListener {
	//-------------------------------------------------------------------------------------
	//public ArrayList<Food> fish= new ArrayList<Food>();
	private Eel eel;
	private Food fish,tempFish;
	private static Timer timer;
	public final static int Ocean_W=900;
	public final static int Ocean_H=800;
	public Dimension winSize;
	private Dimension screenSize;
	private int respawn;
	private Sea sea;
	private double fishSize=0.5;
	private double randSize=0;
	private ArrayList<Food> fishList=new ArrayList<Food>();
	private final static int Fish_Max=10;
	//private double pressedTime, releasedTime;
	
	
	public Aquarium(){
		super();
		screenSize= new Dimension(Ocean_W,Ocean_H);
		
		timer = new Timer(33, this);
		
		timer.start();
		respawn=0;
		eel= new Eel(screenSize,2,2,1.1);
		
		tempFish=null;
		for (int i=0; i<5;i++) {
			//Food f=new Food(screenSize.width/2+i*2,screenSize.height/2+i*3,0.5);
			Food f=new Food((int)(Math.random()*screenSize.width),(int)(Math.random()*screenSize.height),0.5);
			fishList.add(f);
		}
		sea=new Sea(screenSize);
		winSize= new Dimension(Ocean_W+40,Ocean_H+40);
		setPreferredSize(winSize);
		addMouseListener(new MyMouseAdapter());
		addMouseMotionListener(new MyMouseMotionAdapter());
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		setBackground(Color.black);
		sea.draw(g2);
		//sea.bubbles(g2);
		
		
		
		for (Food f: fishList) {
			f.draw(g2);
		}
		eel.draw(g2);
		
	}
	public void actionPerformed(ActionEvent e) {
		sea.update();
		//------------------eel-look-for-closest-fish------------------------------
		
//		if(fishList.size()>0) {
//			tempFish=eel.findClosestFood(fishList);
//		}
//		eel.update(tempFish);
//		if(tempFish!=null) {
//			if(eel.getPos().dist(tempFish.getPos())<eel.getSize().width/2+tempFish.getSize().width/2) {
//				fishList.remove(tempFish);
//				tempFish=null;
//				//fishList.add(new Food((int)(Math.random()*screenSize.width),(int)(Math.random()*screenSize.height),0.5));
//			}
//		}
//		
//		if(fishList.size()>0) {
//			for (Food f: fishList) {
//				f.update();
//			}
//		}
		//---------------------------------------------------------------------------------
		// only care about 1st item on fishList
		// Pursue fish regardless of distance--> boolean in eel
		// use tempFish as the 1st item and only check that
		// put else statement to keep code work when list is empty
		if(fishList.size()>0) {
			tempFish=fishList.get(0);
			eel.update(tempFish);
			if(tempFish!=null) {
				if(eel.getPos().dist(tempFish.getPos())<eel.getSize().width/2+tempFish.getSize().width/2) {
					fishList.remove(tempFish);
					tempFish=null;
				}
			}

			}
		else eel.update(null);
		
		if(fishList.size()>0) {
			for (int i=0; i<fishList.size();i++) {
				fishList.get(i).update();
				if(eel.getPos().dist(fishList.get(i).getPos())<eel.getSize().width/2+fishList.get(i).getSize().width/2) {
					fishList.remove(fishList.get(i));
			}
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
	
	private class MyMouseAdapter extends MouseAdapter{
		//double click to add fish
		//mouse press and release to find out duration
		//reset fish size after adding one fish
		public void mouseClicked(MouseEvent e) {
			
				if (e.getClickCount()>1) {
					if (fishList.size()<=Fish_Max) {
						fishList.add(new Food(e.getX(),e.getY(),0.5));
					}
				}
				else {
					if(fishList.size()>0) {
						for(int i=0;i<fishList.size();i++) {
							if(fishList.get(i).checkMouseHit(e)) {
								if (e.isControlDown()) {
									fishList.remove(fishList.get(i));
									
								}
							}
						}
					}
				}
				
				
			}
		public void mousePressed(MouseEvent e) {
			//pressedTime=e.getWhen();
		
			//When fish is clicked, run method to enlarge the fish
			//mouse event is passed into method to get x, y position
			for(Food f: fishList) {
				
				if (f.checkMouseHit(e)) {
					f.enlarge();
				}
				
			}
			
		}
		
		public void mouseReleased(MouseEvent e) {
		
			
			
		}
	}
	
	private class MyMouseMotionAdapter extends MouseMotionAdapter{
		
	}

}
