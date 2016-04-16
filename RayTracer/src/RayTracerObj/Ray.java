package RayTracerObj;


public class Ray {

	Vector direction;
	private Point p0;
	/**
	 *  Get camera settings and create Vector from camera to (x, y) in screen
	 * @param camera
	 * @param x
	 * @param y
	 */
	public Ray(Camera camera, double x, double y) {
		double screenWidth = camera.getScWidth();
		Vector rightVec = camera.getRightVec();
		
		double rightDist = (x/screenWidth + 1.0) * screenWidth;
		Vector right = rightVec.mul(rightDist);
		Vector up = camera.getUpVec().mul(y);
		p0 = camera.getPosition();
		direction = camera.getScLeft().toVec().sub(p0.toVec()).add(right).add(up).normalize();
	}
	
	public Vector getVec() {
		return direction;
	}

	/**
	 * @return the p0
	 */
	public Point getP0() {
		return p0;
	}

	
}
