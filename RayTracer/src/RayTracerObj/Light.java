package RayTracerObj;

public class Light {
	final private Point position;
	final private Color lightColor;
	final private double specularIntesity;
	final private double shadowIntesity;
	final private double lightWidth;

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
		this.lightColor = new Color(lr, lg, lb);
		this.specularIntesity = specular_intesity;
		this.shadowIntesity = shadow_intesity;
		this.lightWidth = light_width;
	}

	public Point getPosition() {
		return position;
	}

	public Color getLightColor() {
		return lightColor;
	}

	public double getSpecularIntesity() {
		return specularIntesity;
	}

	public double getShadowIntesity() {
		return shadowIntesity;
	}

	public double getLightWidth() {
		return lightWidth;
	}

}
