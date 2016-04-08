package RayTracerObj;

import java.awt.Point;

public class Ray {

	/**
	 *  Get camera settings and create Vector from camera to (x, y) in screen
	 * @param camera
	 * @param x
	 * @param y
	 */
	public Ray(Camera camera, int x, int y) {
			Vector position = new Vector(camera.getPosition());
			Vector lookAt = new Vector(camera.getLookAt());
			
			Vector towards = lookAt.sub(position);
			Vector up = new Vector(camera.getLookAt());
			
			/*Fix up vector*/
			up = up.crossProduct(towards).crossProduct(towards).normalize();
			
			/*TBD*/
	}

}
