package RayTracerObj;


public class Ray {

	Vector direction;
	/**
	 *  Get camera settings and create Vector from camera to (x, y) in screen
	 * @param camera
	 * @param x
	 * @param y
	 */
	public Ray(Camera camera, int x, int y) {
			Vector right = camera.getRightVec().mul((x/camera.getScWidth() - 1) *camera.getScWidth());
			Vector up = camera.getUpVec().mul(y);
			direction = camera.getScLeft().toVec().add(right).add(up);
		}

}
