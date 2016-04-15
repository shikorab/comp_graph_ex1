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
		Vector right = rightVec.mul((x/screenWidth + 1) * screenWidth);
		Vector up = camera.getUpVec().mul(y);
		direction = camera.getScLeft().toVec().add(right).add(up).normalize();
			
		p0 = camera.getPosition();
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
