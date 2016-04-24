package RayTracerObj;

public class Settings {
	final private Color backgroundColor;
	final private double shRays;
	final private double recMax;

	public Settings(double bgr, double bgg, double bgb, double sh_rays, double rec_max) {
		this.backgroundColor = new Color(bgr, bgg, bgb);
		this.shRays = sh_rays;
		this.recMax = rec_max;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public double getShRays() {
		return shRays;
	}

	public double getRecMax() {
		return recMax;
	}


}
