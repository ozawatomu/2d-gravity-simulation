import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Space extends JPanel implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener{
	Timer timer = new Timer(30, this);
	static int screenSize = 1620;
	static ArrayList<Mass> masses;
	static Mass phantomMass;
	static Line phantomLine;
	static XYPair clickPoint;

	public static void main(String[] args) {
		JFrame jFrame = new JFrame();
		jFrame.setTitle("Space");
		jFrame.setSize(screenSize, screenSize);
		//jFrame.setLocation(30, 30);
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		Space space = new Space();
		space.addMouseListener(space);
		space.addMouseMotionListener(space);
		space.addMouseWheelListener(space);
		space.setPreferredSize(new Dimension(screenSize, screenSize));
		jFrame.add(space);
		jFrame.pack();
		
		masses = new ArrayList<Mass>();
		phantomMass = new Mass(new XYPair(0, 0), 30, new XYPair(0, 0), new XYPair(0, 0));
		phantomLine = new Line(new XYPair(0, 0), new XYPair(0, 0));
		clickPoint = new XYPair(0, 0);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, screenSize, screenSize);
		for(int i = 0; i < masses.size(); i++) {
			ArrayList<Mass> otherMasses = (ArrayList<Mass>) masses.clone();
			otherMasses.remove(i);
			masses.get(i).calculateVelocity(otherMasses);
		}
		for(int i = 0; i < masses.size(); i++) {
			ArrayList<Mass> otherMasses = (ArrayList<Mass>) masses.clone();
			otherMasses.remove(i);
			masses.get(i).calculateCollisions(otherMasses);
		}
		for(Mass mass: masses) {
			mass.move();
			mass.draw(g, 100);
		}
		phantomMass.draw(g, 100);
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
	}

	public void mouseReleased(MouseEvent arg0) {
		XYPair velocity = phantomLine.pointB.vectorTo(phantomLine.pointA);
		velocity.scale(0.05);
		masses.add(new Mass(new XYPair(arg0.getX(), arg0.getY()), phantomMass.radius, velocity, new XYPair(0, 0)));
		phantomLine.setPointA(0, 0);
		phantomLine.setPointB(0, 0);
	}

	public void mouseDragged(MouseEvent arg0) {
		phantomLine.setPointA(clickPoint);
		phantomLine.setPointB(arg0.getPoint().getX(), arg0.getPoint().getY());
		phantomMass.changeLocation(arg0.getX(), arg0.getY());
	}

	public void mouseMoved(MouseEvent arg0) {
		phantomMass.changeLocation(arg0.getX(), arg0.getY());
	}

	public void mouseWheelMoved(MouseWheelEvent arg0) {
		phantomMass.radius -= arg0.getWheelRotation()*5;
		if(phantomMass.radius < 1) {
			phantomMass.radius = 1;
		}
	}

}
