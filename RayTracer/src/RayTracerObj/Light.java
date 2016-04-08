package RayTracerObj;

public class Light {
	final private Point position;
	final private Color light_color;
	final private double specular_intesity;
	final private double shadow_intesity;
	final private double light_width;

	/**
	 * This class represents light and its properties are: position, light_color, specular_intesity, shadow_intesity, light_width
	 * @param px
	 * @param py
	 * @param pz
	 * @param lr
	 * @param lg
	 * @param lb
	 * @param specular_intesity
	 * @param shadow_intesity
	 * @param light_width
	 */
	public Light(double px, double py, double pz, double lr, double lg, double lb, double specular_intesity,
			double shadow_intesity, double light_width) {
		this.position = new Point(px, py, pz);
		this.light_color = new Color(lr, lg, lb);
		this.specular_intesity = specular_intesity;
		this.shadow_intesity = shadow_intesity;
		this.light_width = light_width;
	}

}
