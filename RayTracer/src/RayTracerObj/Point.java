package RayTracerObj;

public class Point {
	private double x;
	private double y;
	private double z;
	
	/**
	 * This class represent Point in Cartesian coordinates
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point(double x, double y, double z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}
	
	/*Point to vector*/
	public Vector toVec() {
		return new Vector(this);
	}
	
	public String toString() {
		return Double.toString(getX()) + ", " + Double.toString(getY()) + ", " + Double.toString(getZ());
	}

	public double distance(Point other) {
		double x = getX() * other.getX();
		double y = getY() * other.getY();
		double z = getZ() * other.getZ();
		
		return Math.sqrt(x + y + z);
	}
	
}
