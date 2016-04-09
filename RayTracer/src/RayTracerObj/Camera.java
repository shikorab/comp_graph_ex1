package RayTracerObj;

public class Camera {
	private final Point position;
	private final Point up;
	private final Point lookAt;
	
	
	public Point getPosition() {
		return position;
	}

	public Point getUp() {
		return up;
	}

	public Point getLookAt() {
		return lookAt;
	}

	public double getScDist() {
		return scDist;
	}

	public double getScWidth() {
		return scWidth;
	}

	final private double scDist;
	final private double scWidth;

	public Camera(double px, double py, double pz, double lx, double ly, double lz, double ux, double uy, double uz,
			double sc_dist, double sc_width) {
		this.position = new Point(px, py, pz);
		this.lookAt = new Point(lx, ly, lz);
		this.up = new Point(ux, uy, uz);
		this.scDist = sc_dist;
		this.scWidth = sc_width;
	}

}
