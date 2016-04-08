package RayTracerObj;

public class Settings {
	final private Color background_color;
	final private double sh_rays;
	final private double rec_max;

	public Settings(double bgr, double bgg, double bgb, double sh_rays, double rec_max) {
		this.background_color = new Color(bgr, bgg, bgb);
		this.sh_rays = sh_rays;
		this.rec_max = rec_max;
	}

}
