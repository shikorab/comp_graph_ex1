package RayTracerObj;

public class Camera {
	final private Point position;
	final private Point up;
	final private Point look_at;
	final private double sc_dist;
	final private double sc_width;

	public Camera(double px, double py, double pz, double lx, double ly, double lz, double ux, double uy, double uz,
			double sc_dist, double sc_width) {
		this.position = new Point(px, py, pz);
		this.look_at = new Point(lx, ly, lz);
		this.up = new Point(ux, uy, uz);
		this.sc_dist = sc_dist;
		this.sc_width = sc_width;
	}

}
