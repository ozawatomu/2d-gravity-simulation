import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Mass {
	XYPair location;
	double radius;
	XYPair velocity;
	XYPair accelaration;
	boolean visibility;
	boolean highlight;
	
	public Mass(XYPair location, double radius, XYPair velocity, XYPair accelaration) {
		this.location = location;
		this.radius = radius;
		this.velocity = velocity;
		this.accelaration = accelaration;
		visibility = true;
		highlight = false;
	}
	
	public void move() {
		location.add(velocity);
	}
	
	public void changeLocation(double x, double y) {
		location.x = x;
		location.y = y;
	}
	
	public void draw(Graphics g, int opacity, boolean isDrawVelocity) {
		if(visibility) {
			g.setColor(new Color(255, 255, 255, opacity));
			g.fillOval((int) (location.x - radius), (int) (location.y - radius), (int) (2*radius), (int) (2*radius));
			if(highlight) {
				g.setColor(new Color(255, 0, 0, opacity));
				g.drawOval((int) (location.x - radius), (int) (location.y - radius), (int) (2*radius), (int) (2*radius));
			}
			if (isDrawVelocity) {
				drawVelocity(g, opacity);
			}
		}
	}
	
	public void drawVelocity(Graphics g, int opacity) {
		g.setColor(new Color(255, 100, 100, opacity));
		g.drawLine((int) location.x, (int) location.y, (int) (location.x + velocity.x*75), (int) (location.y + velocity.y*75));
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
				/*XYPair c2c1 = mass.location.minus(location);
				XYPair norm = new XYPair(c2c1.x/mass.location.distance(location), c2c1.y/mass.location.distance(location));
				double p = (2*(velocity.dotProduct(norm) - mass.velocity.dotProduct(norm)))/(getMass() + mass.getMass());
				velocity.x = velocity.x - p*getMass()*norm.x;
				velocity.y = velocity.y - p*getMass()*norm.y;*/
				
				/*double d = Math.sqrt(Math.pow(cx1 - cx2, 2) + Math.pow(cy1 - cy2, 2)); 
				double nx = (cx2 - cx1) / d; 
				double ny = (cy2 - cy1) / d; 
				double p = 2 * (circle1.vx * nx + circle1.vy * n_y - circle2.vx * nx - circle2.vy * n_y) / 
				        (circle1.mass + circle2.mass); 
				vx1 = circle1.vx - p * circle1.mass * n_x; 
				vy1 = circle1.vy - p * circle1.mass * n_y; 
				vx2 = circle2.vx + p * circle2.mass * n_x; 
				vy2 = circle2.vy + p * circle2.mass * n_y;*/
				
				double d = Math.sqrt(Math.pow(location.x - mass.location.x, 2) + Math.pow(location.y - mass.location.y, 2));
				double nx = (mass.location.x - location.x) / d;
				double ny = (mass.location.y - location.y) / d;
				double p = 2 * (velocity.x * nx + velocity.y * ny - mass.velocity.x * nx - mass.velocity.y * ny) / (getMass() + mass.getMass());
				velocity.x = velocity.x - p * getMass() * nx;
				velocity.y = velocity.y - p * getMass() * ny;
			}
		}
	}
	
	public void calculateCollision(Mass mass) {
		if(location.distance(mass.location) <= radius + mass.radius) {
			//Remove overlap
			double overlapDist = radius + mass.radius - location.distance(mass.location);
			XYPair norm = new Line(location, mass.location).toXYPair();
			norm.setMagnitude(Math.ceil(overlapDist/2));
			mass.location.add(norm);
			norm.negate();
			location.add(norm);
			
			System.out.println("COLLISION");
			
			XYPair normPerp = norm;
			normPerp.rotate(90);
			XYPair v1x = velocity;
			XYPair v2x = mass.velocity;
			XYPair v1y = velocity;
			XYPair v2y = mass.velocity;
			v1x.project(norm);
			v2x.project(norm);
			v1y.project(normPerp);
			v2y.project(normPerp);
			double v1xfMag = ((getMass() - mass.getMass())*v1x.getMagnitude() + 2*mass.getMass()*v2x.getMagnitude())/(getMass() + mass.getMass());
			double v2xfMag = ((mass.getMass() - getMass())*v2x.getMagnitude() + 2*getMass()*v1x.getMagnitude())/(getMass() + mass.getMass());
			System.out.println(v1xfMag);
			System.out.println(v2xfMag);
			XYPair v1xf = v1x;
			XYPair v2xf = v2x;
			v1xf.setMagnitude(v1xfMag);
			v2xf.setMagnitude(v2xfMag);
			XYPair v1f = v1xf;
			XYPair v2f = v2xf;
			v1f.add(v1y);
			v2f.add(v2y);
			velocity = v1f;
			//mass.velocity = v2f;
			
			
			//Calculate collision response
			/*double d = Math.sqrt(Math.pow(location.x - mass.location.x, 2) + Math.pow(location.y - mass.location.y, 2));
			double nx = (mass.location.x - location.x) / d;
			double ny = (mass.location.y - location.y) / d;
			double p = 2 * (velocity.x * nx + velocity.y * ny - mass.velocity.x * nx - mass.velocity.y * ny) / (getMass() + mass.getMass());
			velocity.x = velocity.x - p * getMass() * nx;
			velocity.y = velocity.y - p * getMass() * ny;*/
		}
	}
	
	public boolean isOver(XYPair point) {
		if(point.distance(location) <= radius) {
			return true;
		}else {
			return false;
		}
	}

	public Mass copy() {
		return new Mass(location, radius, velocity, accelaration);
	}
}
