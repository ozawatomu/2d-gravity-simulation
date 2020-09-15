import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Space extends JPanel implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener{
	Timer timer = new Timer(30, this);
	static int screenSizeWidth = 1620;
	static int screenSizeHeight = 1620;
	static boolean isDrawVelocity = false;
	static ArrayList<Mass> masses;
	static Mass phantomMass;
	static Line phantomLine;
	static XYPair clickPoint;
	static XYPair clickDistance;
	static XYPair mousePrePoint;
	static XYPair vel;
	static Mass selectedMass;

	public static void main(String[] args) {
		JFrame jFrame = new JFrame();
		jFrame.setTitle("Space");
		jFrame.setSize(screenSizeWidth, screenSizeHeight);
		//jFrame.setLocation(30, 30);
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		Space space = new Space();
		space.addMouseListener(space);
		space.addMouseMotionListener(space);
		space.addMouseWheelListener(space);
		jFrame.addKeyListener(space);
		space.setPreferredSize(new Dimension(screenSizeWidth, screenSizeHeight));
		jFrame.add(space);
		jFrame.pack();
		
		masses = new ArrayList<Mass>();
		phantomMass = new Mass(new XYPair(0, 0), 30, new XYPair(0, 0), new XYPair(0, 0));
		phantomLine = new Line(new XYPair(0, 0), new XYPair(0, 0));
		clickPoint = new XYPair(0, 0);
		mousePrePoint = new XYPair(0, 0);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, screenSizeWidth, screenSizeHeight);
		for(int i = 0; i < masses.size(); i++) {
			ArrayList<Mass> otherMasses = (ArrayList<Mass>) masses.clone();
			otherMasses.remove(i);
			masses.get(i).calculateVelocity(otherMasses);
		}
		for(Mass mass: masses) {
			mass.move();
		}
		/*ArrayList<Mass> otherMasses = new ArrayList<Mass>();
		for(Mass mass: masses) {
			otherMasses.add(mass.copy());
		}*/
		/*for(int i = 0; i < masses.size(); i++) {
			for(int j = i + 1; j < masses.size(); j++) {
				masses.get(i).calculateCollision(masses.get(j));
			}
		}*/
		for(Mass mass: masses) {
			mass.draw(g, 200, isDrawVelocity);
		}
		phantomMass.draw(g, 32, true);
		g.drawLine((int) phantomLine.pointA.x, (int) phantomLine.pointA.y, (int) phantomLine.pointB.x, (int) phantomLine.pointB.y);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	public void mouseClicked(MouseEvent arg0) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		phantomMass.visibility = true;
	}

	public void mouseExited(MouseEvent arg0) {
		phantomMass.visibility = false;
	}

	public void mousePressed(MouseEvent arg0) {
		clickPoint.x = arg0.getX();
		clickPoint.y = arg0.getY();
		if(selectedMass != null) {
			clickDistance = selectedMass.location.minus(clickPoint);
			phantomMass.visibility = false;
			selectedMass.velocity = new XYPair(0, 0);
		}
	}

	public void mouseReleased(MouseEvent arg0) {
		if(selectedMass != null) {
			//vel.scale(0.5);
			selectedMass.velocity = vel;
			phantomMass.visibility = true;
		}else {
			XYPair velocity = phantomLine.pointB.vectorTo(phantomLine.pointA);
			velocity.scale(0.05);
			masses.add(new Mass(new XYPair(arg0.getX(), arg0.getY()), phantomMass.radius, velocity, new XYPair(0, 0)));
			phantomLine.setPointA(0, 0);
			phantomLine.setPointB(0, 0);
			phantomMass.visibility = true;
		}
	}

	public void mouseDragged(MouseEvent arg0) {
		if(selectedMass != null) {
			XYPair loc = new XYPair((double) arg0.getPoint().getX(), (double) arg0.getPoint().getY());
			loc.add(clickDistance);
			selectedMass.location = loc;
			vel = (mousePrePoint.vectorTo(new XYPair(arg0.getPoint().getX(), arg0.getPoint().getY())));
			mousePrePoint = new XYPair(arg0.getPoint().getX(), arg0.getPoint().getY());
		}else {
			phantomLine.setPointA(clickPoint);
			phantomLine.setPointB(arg0.getPoint().getX(), arg0.getPoint().getY());
			phantomMass.changeLocation(arg0.getX(), arg0.getY());
		}
	}

	public void mouseMoved(MouseEvent arg0) {
		boolean isSel = false;
		for(Mass mass: masses) {
			if(mass.isOver(new XYPair(arg0.getX(), arg0.getY()))) {
				isSel = true;
				mass.highlight = true;
				selectedMass = mass;
			}else {
				mass.highlight = false;
			}
		}
		if(!isSel) {
			selectedMass = null;
		}
		phantomMass.changeLocation(arg0.getX(), arg0.getY());
	}

	public void mouseWheelMoved(MouseWheelEvent arg0) {
		phantomMass.radius -= arg0.getWheelRotation()*5;
		if(phantomMass.radius < 1) {
			phantomMass.radius = 1;
		}
	}

	@Override
	public void keyPressed(KeyEvent e){
		switch (e.getKeyCode()) {
		case KeyEvent.VK_V:
			isDrawVelocity = !isDrawVelocity;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e){
		
	}

	@Override
	public void keyTyped(KeyEvent e){
	}

}
