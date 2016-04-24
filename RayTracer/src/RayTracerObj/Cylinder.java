package RayTracerObj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Cylinder implements Surface {
	final private Point center;
	final private Angle rotationAngles;
	final private double length;
	final private double radius;
	final private Material material;
	private static Vector axisOfCylV;
	private static Vector upCenterV;
	private static Vector downCenterV;

	public Cylinder(double cx, double cy, double cz, double len, double radius, double rx, double ry, double rz,
			Material material) {
		this.center = new Point(cx, cy, cz);
		this.rotationAngles = new Angle(rx, ry, rz);
		this.length = len;
		this.radius = radius;
		this.material = material;
		setVectors();
	}

	@Override
	public Intersection getIntersection(Ray ray) {
		ArrayList<Intersection> intersections = new ArrayList<Intersection>();
		
		Point axisOfCylP = axisOfCylV.toPoint();
		/* Get the offsets - the Vertical of each cap */
		Vector upCenter = ray.getP0().toVec().sub(upCenterV);
		double offset1 = upCenter.dotProduct(axisOfCylV);
		Vector downCenter = ray.getP0().toVec().sub(downCenterV);
		double offset2 = downCenter.dotProduct(axisOfCylV);

		/* Get the plain and intersections of each cap */
		Plane plane1 = new Plane(axisOfCylP.getX(), axisOfCylP.getY(), axisOfCylP.getZ(), offset1, this.material);
		if (plane1 != null){
			Intersection i1 = plane1.getIntersection(ray);
			if (i1 != null)
				/* The intersection point is not in the cylinder */
				if (i1.getPoint().distance(upCenterV.toPoint()) <= this.radius){
					System.out.println("point1");
					intersections.add(i1);
				}
		}
		Plane plane2 = new Plane(axisOfCylP.getX(), axisOfCylP.getY(), axisOfCylP.getZ(), offset2, this.material);
		if (plane2 != null){
			Intersection i2 = plane2.getIntersection(ray);
			if (i2 != null)
				/* The intersection point is not in the cylinder */
				if (i2.getPoint().distance(downCenterV.toPoint()) <= this.radius){
					System.out.println("point2");
					intersections.add(i2);
				}	
		}
		/* Get the sphere and its intersection */
		Sphere sphr = new Sphere(this.center.getX(), this.center.getY(), this.center.getZ(), this.radius,
				this.material);
		if (sphr != null){
			Intersection i3 = sphr.getIntersection(ray);
			if (i3 != null){
				System.out.println("point3");
				intersections.add(i3);
			}
		}
		
		/* No intersection point has been detected */
		if (intersections.size() == 0)
			return null;
		
		/* Sort the intersections */
		Collections.sort(intersections);
		
		/* Get the minimal intersection point */
		return new Intersection(intersections.get(0).getPoint(), intersections.get(0).material, ray, this);
	}
	
	/**
	 * This function set 3 vectors:
	   the axis of the cylinder, up center vector, down center vector
	 */
	private void setVectors() {
		Vector localUpCenterV = new Vector(0, 1, 0).mul(this.length/2);
		Point upCenterP = localUpCenterV.toPoint();
		
		// Rotate by X
		double new_x = upCenterP.getX();
		double new_y = upCenterP.getY() * Math.cos(Math.toRadians(rotationAngles.getRx())) - upCenterP.getZ() * Math.sin(Math.toRadians(rotationAngles.getRx()));
		double new_z = upCenterP.getY() * Math.sin(Math.toRadians(rotationAngles.getRx())) + upCenterP.getZ() * Math.cos(Math.toRadians(rotationAngles.getRx())); 
		
		double x = new_x;
		double y = new_y;
		double z = new_z;
		
		// Rotate by Y
		new_x = x * Math.cos(Math.toRadians(rotationAngles.getRy())) + z * Math.sin(Math.toRadians(rotationAngles.getRy()));
		new_y = y;
		new_z = - x * Math.sin(Math.toRadians(rotationAngles.getRy())) + z * Math.cos(Math.toRadians(rotationAngles.getRy()));
		
		x = new_x;
		y = new_y;
		z = new_z;
		
		// Rotate by Z
		new_x = x * Math.cos(Math.toRadians(rotationAngles.getRz())) - y * Math.sin(Math.toRadians(rotationAngles.getRz()));
		new_y = x * Math.sin(Math.toRadians(rotationAngles.getRz())) + y * Math.cos(Math.toRadians(rotationAngles.getRz()));
		new_z = z;
		
		x = new_x;
		y = new_y;
		z = new_z;
		
		// Set upCenter Vector
		upCenterV = new Vector(x, y, z).add(this.center.toVec());
		// Set downCenter Vector
		Vector down = this.center.toVec().sub(upCenterV).normalize();
		downCenterV = this.center.toVec().add(down.mul(this.length / 2));
		// Set axis of the cylinder
		axisOfCylV = upCenterV.sub(downCenterV).normalize();

	}
	
	/**
	 * This function returns 2D point from a 3D point by Mercator projection 
	 */
//	public Point getTwoProjection(Point p, double radius){
//		
//	}

}
