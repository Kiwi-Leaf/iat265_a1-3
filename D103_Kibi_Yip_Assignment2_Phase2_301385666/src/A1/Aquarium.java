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

	private ArrayList<Eel> eelList= new ArrayList<Eel>();

	private static Timer timer;
	public final static int Ocean_W=1700;
	public final static int Ocean_H=900;
	public Dimension winSize;
	private Dimension screenSize;
	private Sea sea;
	private ArrayList<Food> fishList=new ArrayList<Food>();

	private final static int Fish_Max=20;

	
	
	public Aquarium(){
		super();
		screenSize= new Dimension(Ocean_W,Ocean_H);
		
		timer = new Timer(33, this);
		
		timer.start();

		sea=new Sea(screenSize);
		winSize= new Dimension(Ocean_W+100,Ocean_H+100);
		
		for (int i=0; i<5;i++) {
			Eel e= new Eel(screenSize,2,2,Math.random()*0.6+0.5);
			
			eelList.add(e);
		}
		
		for (int j=0; j<15;j++) {
			Food f=new Food((int)(Math.random()*screenSize.width),(int)(Math.random()*screenSize.height),Math.random()*0.2+0.5);
		fishList.add(f);
		}
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
		for (Eel eel:eelList) {
		eel.draw(g2);
		}
		
	}
	public void actionPerformed(ActionEvent e) {
		

		for (int i=0;i<eelList.size();i++) {
			Eel eel;
			eel = eelList.get(i);

		if(fishList.size()>0) {
			for (int q=0; q<fishList.size();q++) {
				Food f;
				f=fishList.get(q);
				f.update(eel,fishList);
					for(int w=q+1;w<fishList.size();w++) {
						Food f2;
						f2= fishList.get(w);
						if(f.collideWithEachOther(f2)) {

							if (f.getScale()>f2.getScale()) {
								f2.reverseSmaller(f);

								}
							else if (f.getScale()<f2.getScale()) {
								f.reverseSmaller(f2);

								}
							else {
								f.reverseBoth(f2);

								}
							}
						}
				}
			}
		eel.update(fishList);
		for (int j=i+1; j<eelList.size();j++) {
			Eel eel2;
			eel2=eelList.get(j);
			
			if(eel.collideWithEachOther(eel2)) {
				//Prioritize coming back to screen than escaping from larger eel.
				
				if (eel.getScale()>eel2.getScale()&&!eel2.isOutOfBound()) {
					eel2.reverseSmaller(eel);
					
				}
				else if (eel.getScale()<eel2.getScale()&&!eel.isOutOfBound()) {
					eel.reverseSmaller(eel2);
					
				}
				else {
					if(!eel2.isOutOfBound()&&!eel.isOutOfBound())
					eel.reverseBoth(eel2);
					
				}
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
		frame.pack();
		frame.setVisible(true);
		

	}
	
	private class MyMouseAdapter extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			
				if (e.getClickCount()>1) {
					if (fishList.size()<=Fish_Max) {
						fishList.add(new Food(e.getX()-50,e.getY()-50,0.5));
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
			for(Food f: fishList) {
				
				if (f.checkMouseHit(e)) {
					f.enlarge();
				}
				
			}
			
		}
	}
	
	private class MyMouseMotionAdapter extends MouseMotionAdapter{
		
	}

}
