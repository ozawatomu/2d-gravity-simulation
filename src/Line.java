
public class Line {
	XYPair pointA;
	XYPair pointB;
	
	public Line(XYPair a, XYPair b) {
		pointA = a;
		pointB = b;
	}
	
	public void setPointA(XYPair point) {
		pointA.x = point.x;
		pointA.y = point.y;
	}
	
	public void setPointA(double x, double y) {
		pointA.x = x;
		pointA.y = y;
	}
	
	public void setPointB(XYPair point) {
		pointB.x = point.x;
		pointB.y = point.y;
	}
	
	public void setPointB(double x, double y) {
		pointB.x = x;
		pointB.y = y;
	}
}
