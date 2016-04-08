package RayTracerObj;

public class Cylinder {
	final private Point position;
	final private Angle angle;
	final private double length;
	final private double radius;
	final private Material material;

	public Cylinder(double cx, double cy, double cz, double len, double radius, double rx, double ry, double rz,
			Material material) {
		this.position = new Point(cx, cy, cz);
		this.angle = new Angle(rx, ry, rz);
		this.length = len;
		this.radius = radius;
		this.material = material;
	}

}
