package RayTracerObj;

public class Sphere {
	final private Point center;
	final private double radius;
	final private Material material;
	
	/**
	 * This class represents sphere with its: center, radius and material
	 * @param cx
	 * @param cy
	 * @param cz
	 * @param radius
	 * @param material
	 */
	public Sphere(double cx, double cy, double cz, double radius, Material material) {
		this.center = new Point(cx, cy, cz);
		this.radius = radius;
		this.material = material;
	}

}
