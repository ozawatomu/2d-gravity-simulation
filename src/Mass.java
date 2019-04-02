import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Mass {
	XYPair location;
	double radius;
	XYPair velocity;
	XYPair accelaration;
	boolean visibility;
	
	public Mass(XYPair location, double radius, XYPair velocity, XYPair accelaration) {
		this.location = location;
		this.radius = radius;
		this.velocity = velocity;
		this.accelaration = accelaration;
		visibility = true;
	}
	
	public void move() {
		location.add(velocity);
	}
	
	public void changeLocation(double x, double y) {
		location.x = x;
		location.y = y;
	}
	
	public void draw(Graphics g, int opacity) {
		if(visibility) {
			g.setColor(new Color(255, 255, 255, opacity));
			g.fillOval((int) (location.x - radius), (int) (location.y - radius), (int) (2*radius), (int) (2*radius));
		}
	}
	
	public double getMass() {
		double r3b = (Math.pow(radius, 3))/8000;
		return (4*Math.PI*r3b)/3;
	}
	
	public void setAccelaration(ArrayList<Mass> masses) {
		accelaration.x = 0;
		accelaration.y = 0;
		for(Mass mass: masses) {
			double fG = getMass()*mass.getMass()/Math.pow(location.distance(mass.location), 2);
			//f = ma      a = f/m
			XYPair gravVector = location.vectorTo(mass.location);
			gravVector.setMagnitude(fG/getMass());
			accelaration.add(gravVector);
		}
	}
	
	public void calculateVelocity(ArrayList<Mass> masses) {
		setAccelaration(masses);
		velocity.add(accelaration);
		//Bad collision
		/*for(Mass mass: masses) {
			if(location.distance(mass.location) <= radius + mass.radius) {
				XYPair perpVect = location.vectorTo(mass.location);
				perpVect.rotate(90);
				velocity.project(perpVect);
			}
		}*/
	}

	public void calculateCollisions(ArrayList<Mass> masses) {
		for(Mass mass: masses) {
			if(location.distance(mass.location) <= radius + mass.radius) {
				/*double v1 = velocity.getMagnitude();
				double v2 = mass.velocity.getMagnitude();
				double m1 = getMass();
				double m2 = mass.getMass();
				double t1 = velocity.angleToXAxis();
				double t2 = mass.velocity.angleToXAxis();
				double ph = location.vectorTo(mass.location).angleToXAxis();
				velocity.x = ((v1*Math.cos(t1 - ph)*(m1 - m2) + 2*m2*v2*Math.cos(t2 - ph))/(m1 + m2))*Math.cos(ph) + v1*Math.sin(t1 - ph)*Math.sin(ph);
				velocity.y = ((v1*Math.cos(t1 - ph)*(m1 - m2) + 2*m2*v2*Math.cos(t2 - ph))/(m1 + m2))*Math.sin(ph) + v1*Math.sin(t1 - ph)*Math.cos(ph);*/
				
				
				
				/*double xm1 = getMass();
				double xm2 = mass.getMass();
				double xv1 = velocity.x;
				double xv2 = mass.velocity.x;
				double xVel = xv2 + ((2*xm1*xv1 + xm2*xv2 - xm1*xv2)/(xm1 + xm2)) - xv1;
				velocity.x = xVel;
				
				double ym1 = getMass();
				double ym2 = mass.getMass();
				double yv1 = velocity.y;
				double yv2 = mass.velocity.y;
				double yVel = yv2 + ((2*ym1*yv1 + ym2*yv2 - ym1*yv2)/(ym1 + ym2)) - yv1;
				velocity.y = yVel;*/
			}
		}
	}
}
