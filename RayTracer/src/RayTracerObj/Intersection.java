package RayTracerObj;

import java.util.ArrayList;

public class Intersection {

	public Material material;
	public Point point;
	public Color color;
	
	public Intersection(Point p, Material material) {
		point = p;
		this.material = material;
	}
	
	public void computeColor(Scene scene) {
		color = new Color(0, 0, 0);
		
		ArrayList<Light> lights = scene.lightList;
		for (Light light: lights) {
			/*Calc Ray and distance from light to point*/
			Ray  ray = new Ray(light.getPosition(), point);
			double distance = point.distance(light.getPosition());
			
			/*Check if occluded by any other surface*/
			/*Assume not occluded*/
			boolean occluded = false;
			for (Surface surface: scene) {
				Intersection intersection = surface.getIntersection(ray);
				if (intersection == null) continue; //No intersection
				
				/*Check if occluded*/
				Vector interVec = intersection.getPoint().toVec().sub(light.getPosition().toVec());
				double distance2 = Math.sqrt(interVec.dotProduct(interVec));
				if (distance > distance2) {
					occluded = true;
					break;
				}
			}
			
			/*in case not occluded - add to color*/
			if (!occluded) {
				Color addedColor = 
						material.diffuseColor.add(material.specularColor).
						mul(light.getLightColor()).
						mul(light.getSpecularIntesity() * (1 - material.transparency));
				color = color.add(addedColor);
			}
		}
		
		color = color.mul(255).mul(0.5);
	}

	public Color getColor() {
		return color;
	}
	
	public Point getPoint() {
		return point;
	}

}
