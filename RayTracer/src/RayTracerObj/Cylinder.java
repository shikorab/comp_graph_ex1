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
	private double offset1;
	private double offset2;
	private Plane plane1;
	private Plane plane2;
	private Vector axisOfCylV;
	private Vector upCenterV;
	private Vector downCenterV;

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
		
		/* Get the cylinder body intersections */
		
		Vector deltaP = ray.getP0().toVec().sub(downCenterV);
		Vector rayProjectionOnAxis = axisOfCylV.mul(ray.direction.dotProduct(axisOfCylV));
		Vector rayOriginToDownCenterProjection = axisOfCylV.mul(deltaP.dotProduct(axisOfCylV));
		
		double a = ray.direction.toPoint().distance2(rayProjectionOnAxis.toPoint());
		double b = 2 * (ray.direction.sub(rayProjectionOnAxis)).dotProduct(deltaP.sub(rayOriginToDownCenterProjection));
		double c = deltaP.toPoint().distance2(rayOriginToDownCenterProjection.toPoint()) - radius * radius;
		
		double discriminant = b * b - 4 * a * c;
		if (discriminant >= 0){
		
			discriminant = Math.sqrt(discriminant);
			double t1 = ((-1 * b) + discriminant) / (2 * a);
			double t2 = ((-1 * b) - discriminant) / (2 * a);
			
			if (t1 > 0){
				Point p1 = ray.getP0().toVec().add(ray.direction.mul(t1)).toPoint();
				if (axisOfCylV.dotProduct(p1.toVec().sub(downCenterV)) > 0 &&
						axisOfCylV.dotProduct(p1.toVec().sub(upCenterV)) < 0){
					Vector downCenterToP = p1.toVec().sub(downCenterV);
					Vector normal = downCenterToP.add(axisOfCylV.mul(downCenterToP.dotProduct(axisOfCylV))).normalize();
					Intersection i1 = new Intersection(p1, this.material, ray, this, normal);
					intersections.add(i1);
//					System.out.println("i1");
				}
			}
			if (t2 > 0){
				Point p2 = ray.getP0().toVec().add(ray.direction.mul(t2)).toPoint();
				if (axisOfCylV.dotProduct(p2.toVec().sub(downCenterV)) > 0 &&
						axisOfCylV.dotProduct(p2.toVec().sub(upCenterV)) < 0){
					Vector downCenterToP = p2.toVec().sub(downCenterV);
					Vector normal = downCenterToP.add(axisOfCylV.mul(downCenterToP.dotProduct(axisOfCylV))).normalize();
					Intersection i2 = new Intersection(p2, this.material, ray, this, normal);
					intersections.add(i2);
//					System.out.println("i2");
				}
			}
		}
		
		/* Get the plain and intersections of each cap */
		Intersection i3 = plane1.getIntersection(ray);
		if (i3 != null)
			/* The intersection point is not in the cylinder */
			if (i3.getPoint().distance2(upCenterV.toPoint()) <= this.radius * this.radius){
//				System.out.println("i3");
				intersections.add(i3);
			}

		Intersection i4 = plane2.getIntersection(ray);
		if (i4 != null)
			/* The intersection point is not in the cylinder */
			if (i4.getPoint().distance2(downCenterV.toPoint()) <= this.radius * this.radius){
//				System.out.println("i4");
				intersections.add(i4);
			}
		
		/* No intersection point has been detected */
		if (intersections.size() == 0)
			return null;
		
		/* Sort the intersections */
		Collections.sort(intersections);
//		System.out.println("intersection point: " + intersections.get(0).getPoint());
		
		/* Get the minimal intersection point */
		return new Intersection(intersections.get(0).getPoint(), intersections.get(0).material, ray, this, intersections.get(0).getNormal());
	}
	
	/**
	 * This function set 3 vectors:
	   the axis of the cylinder, up center vector, down center vector
	 */
	private void setVectors() {
		Vector localUpCenterV = new Vector(0, 0, 1).mul(this.length/2);
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
		
		// Get the offsets - the Vertical of each cap
		offset1 = upCenterV.dotProduct(axisOfCylV);
		offset2 = downCenterV.dotProduct(axisOfCylV);
		// Set plane1 and plane2
		Point axisOfCylP = axisOfCylV.toPoint();
		plane1 = new Plane(axisOfCylP.getX(), axisOfCylP.getY(), axisOfCylP.getZ(), offset1, this.material);
		plane2 = new Plane(axisOfCylP.getX(), axisOfCylP.getY(), axisOfCylP.getZ(), offset2, this.material);

//		System.out.println("axisOfCylV " + axisOfCylV.toString() + " offset1: " + offset1 + " offset2: " + offset2 + " upCenter: " + upCenterV + " downCenter: " + downCenterV);
	}
	
}
