package RayTracerObj;

public class Plane {
	final private Point normal;
	final private double offset;
	final private Material material;

	/**
	 * This class represents plane
	 * @param nx
	 * @param ny
	 * @param nz
	 * @param offset
	 * @param material
	 */
	public Plane(double nx, double ny, double nz, double offset, Material material) {
		this.normal = new Point(nx, ny, nz);
		this.offset = offset;
		this.material = material;
	}

}
