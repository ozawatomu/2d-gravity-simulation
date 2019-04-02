
public class XYPair {
	public double x;
	public double y;

	public XYPair(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public XYPair clone() {
		return new XYPair(this.x, this.y);
	}

	public String toString() {
		return "[" + x + ", " + y + "]";
	}
	
	public void add(XYPair otherPair) {
		x += otherPair.x;
		y += otherPair.y;
	}
	
	public void scale(double scale) {
		x *= scale;
		y *= scale;
	}
	
	public double distance(XYPair otherPair) {
		return Math.sqrt(Math.pow(x - otherPair.x, 2) + Math.pow(y - otherPair.y, 2));
	}
	
	public double angle(XYPair otherPair) {
		return Math.toDegrees(Math.atan((y-otherPair.y)/(x-otherPair.x)));
	}
	
	public double angleToXAxis() {
		//return Math.atan2(y, x)*180/Math.PI;
		return Math.toDegrees(Math.atan(y/x));
	}
	
	public double getMagnitude() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public XYPair vectorTo(XYPair otherPair) {
		return new XYPair(otherPair.x - x, otherPair.y - y);
	}
	
	public void setMagnitude(double scale) {
		double mag = getMagnitude();
		x = x*scale/mag;
		y = y*scale/mag;
	}
	
	public void rotate(double angleDegrees) {
		double xNew = Math.cos(Math.toRadians(angleDegrees))*x - Math.sin(Math.toRadians(angleDegrees))*y;
		double yNew = Math.sin(Math.toRadians(angleDegrees))*x + Math.cos(Math.toRadians(angleDegrees))*y;
		x = xNew;
		y = yNew;
	}
	
	public void project(XYPair otherPair) {
		double scale = dotProduct(otherPair)/Math.pow(otherPair.getMagnitude(), 2);
		x = otherPair.x*scale;
		y = otherPair.y*scale;
	}
	
	public double dotProduct(XYPair otherPair) {
		return x*otherPair.x + y*otherPair.y;
	}
}
