package RayTracerObj;

public class Sphere implements Surface{
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

	@Override
	public Intersection getIntersection(Ray ray) {
		
		Point P0 = ray.getP0();
		Vector L = center.toVec().sub(P0.toVec());
		Vector V = ray.getVec();
		
		double tca = L.dotProduct(V);
		if (tca < 0) return null; //The sphere behind us
		
		
		double dpow = L.dotProduct(L) - Math.pow(tca, 2);
		if (dpow > Math.pow(radius, 2)) return null; // No intersection
		
		double thc = Math.sqrt(Math.pow(radius, 2) - dpow);
		double t  = tca - thc;
		if (t < 0)  return null;//FIXME: consider t = tca + thc; //part of the sphere behind us
		
		Point P = V.mul(t).add(P0.toVec()).toPoint();
		return new Intersection(P, material, ray, this);
	}

}
