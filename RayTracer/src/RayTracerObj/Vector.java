package RayTracerObj;


/**
 * 
 * Vector 
 * Should support operations such as:
 *  dot products, cross products, vector additions and
 * multiplications (either with a scalar or multiplying two vectors component
 * wise for RGB color calculation)
 */
public class Vector {
	private Point point;
	
	public Vector(Point point) {
		this.point = point;
	}
	
	public Vector(double x, double y, double z) {
		point  = new Point(x, y, z);
	}

	public Vector crossProduct(Vector other) {
		
		double x = point.getY() * other.point.getZ() - point.getZ() * other.point.getY();
		double y = point.getZ() * other.point.getX() - point.getX() * other.point.getZ();
		double z = point.getX() * other.point.getY() - point.getY() * other.point.getX();
		
		return new Vector(x, y, z);
	}
	
	public double dotProduct(Vector other) {
		
		double x = point.getX() * other.point.getX();
		double y = point.getY() * other.point.getY();
		double z = point.getZ() * other.point.getZ();
		
		return x + y + z;
	}

	public Vector normalize() {
		double h = Math.sqrt(Math.pow(point.getX(), 2) + 
							 Math.pow(point.getY(), 2) + 
							 Math.pow(point.getZ(), 2));
		double x = (point.getX() / h);
		double y = (point.getY() / h);
		double z = (point.getZ() / h);
		
		return new Vector(x, y, z);
	}

	public Vector sub(Vector other) {
		double x = (point.getX() - other.point.getX());
		double y = (point.getY() - other.point.getY());
		double z = (point.getZ() - other.point.getZ());
		
		return new Vector(x, y, z);
	}
	
	public Point toPoint() {
		return this.point;
	}

	public Vector add(Vector other) {
		double x = (point.getX() + other.point.getX());
		double y = (point.getY() + other.point.getY());
		double z = (point.getZ() + other.point.getZ());
		
		return new Vector(x, y, z);		
	}

	public Vector mul(double d) {
		double x = (point.getX() * d);
		double y = (point.getY() * d);
		double z = (point.getZ() * d);
		
		return new Vector(x, y, z);
	}
	
	public String toString() {
		return point.toString();
	}
}
