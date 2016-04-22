package RayTracerObj;

public class Angle {
	final private double rx;
	final private double ry;
	final private double rz;
	final static private double ang = 90;

	public Angle(double rx, double ry, double rz) {
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}
	
	public double getRx() {
		return rx;
	}



	public double getRy() {
		return ry;
	}



	public double getRz() {
		return rz;
	}



	/**
	 * This function returns the normal from the rotation angels
	 * @return
	 */
	public Point getNormalPoint(){
		double x = this.rx / Angle.ang;
		double y = this.ry / Angle.ang;
		double z = this.rz / Angle.ang;
		return new Point(x, y, z);
	}
}
