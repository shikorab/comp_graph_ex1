package RayTracerObj;

public class Color{
	/**
	 * This class represent color parameters for every channel
	 * @param r
	 * @param g
	 * @param b
	 */
	public Vector vec;
	
	public Color(double r, double g, double b) {
		vec = new Vector(r, g, b);
	}
	
	public Color(Vector vector) {
		vec = vector;
	}
	
	public byte getBlue() {
		return (byte) vec.toPoint().getZ();
	}
	
	public byte getRed() {
		return (byte) vec.toPoint().getX();
	}
	
	public byte getGreen() {
		return (byte) vec.toPoint().getY();
	}
	
	public Color mul(double i) {
		return new Color(vec.mul(i));
	}
	
	public Color mul(Color other) {
		return new Color(vec.mul(other.vec));
	}
	
	public Color add(Color other) {
		return new Color(vec.add(other.vec));
	}
	
	
}
