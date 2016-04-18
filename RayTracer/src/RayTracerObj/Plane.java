package RayTracerObj;

public class Plane implements Surface {
	final private Vector normal;
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
		this.normal = new Vector(nx, ny, nz);
		this.offset = offset;
		this.material = material;
	}

	@Override
	public Intersection getIntersection(Ray ray) {
		Point P0 = ray.getP0();
		Vector V = ray.getVec();
		
		/*t = - (P0 . N + C) / (V . N) */
		double vdotn = V.dotProduct(normal);
		if (vdotn > 0) return null; /*no intersection*/
		
		double t = - (P0.toVec().dotProduct(normal) + offset) / (vdotn);
		Point p = P0.toVec().add(V.mul(t)).toPoint();
		
		return new Intersection(p, material);
	}

}
