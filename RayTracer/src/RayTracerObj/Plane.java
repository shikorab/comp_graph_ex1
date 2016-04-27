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
		this.normal = new Vector(nx, ny, nz).normalize();
		this.offset = offset;
		this.material = material;
	}

	@Override
	public Intersection getIntersection(Ray ray) {
		Point P0 = ray.getP0();
		Vector V = ray.getVec();
		
		/*t = - (P0 . N - C) / (V . N) */
		double vdotn = V.dotProduct(normal);
		if (vdotn == 0) return null; /*no intersection*/
		
		double t = - (P0.toVec().dotProduct(normal) - offset) / (vdotn);
		if (t < 0) return null; /*the plane is behind us*/
		Point p = P0.toVec().add(V.mul(t)).toPoint();
		
		return new Intersection(p, material, ray, this, this.getNormal(V));
	}

	
	public Vector getNormal(Vector v) {
		if (v.dotProduct(normal) < 0)
			return normal;
		return normal.mul(-1);
	}

}
