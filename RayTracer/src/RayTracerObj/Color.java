package RayTracerObj;

public class Color {
	final private double r;
	final private double g;
	final private double b;
	
	/**
	 * This class represent color parameters for every channel
	 * @param r
	 * @param g
	 * @param b
	 */
	public Color(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public Color mul(int i) {
		return new Color(r * i, g * i, b * i);
	}
	
	public byte getBlue() {
		return (byte) b;
	}
	
	public byte getRed() {
		return (byte) r;
	}
	
	public byte getGreen() {
		return (byte) g;
	}
	
	
}
