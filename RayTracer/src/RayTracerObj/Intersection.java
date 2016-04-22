package RayTracerObj;

import java.util.ArrayList;

public class Intersection implements Comparable<Intersection>{

	public Material material;
	public Point point;
	public Color color;
	private Ray ray;
	
	public Intersection(Point p, Material material, Ray ray) {
		point = p;
		this.material = material;
		this.ray = ray;
	}
	
	public void computeColor(Scene scene) {
		color = new Color(0, 0, 0);
		
		ArrayList<Light> lights = scene.lightList;
		int count = 0;
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
						material.specularColor.
						mul(light.getSpecularIntesity()).
						add(material.diffuseColor).
						mul(light.getLightColor());
				color = color.add(addedColor);
				count = count + 1;
			}
		}	
		color = color.mul(255 / count).mul(1 - material.transparency);
		
		if (material.transparency > 0 ) { /* transparent or partially transparent*/ 
			/*look for next intersection*/
			Color backgroundColor;
			Ray newray = new Ray(point, ray.getVec());
			Intersection background = RayCast.findIntersection(newray, scene);
			if (false && background != null) {
				background.computeColor(scene);
				backgroundColor = background.getColor();
			} else {
				backgroundColor = scene.set.getBackgroundColor().mul(255);
			}
			
			color = color.add(backgroundColor.mul(material.transparency));
		}
	}

	public Color getColor() {
		return color;
	}
	
	public Point getPoint() {
		return point;
	}
	
	public int compareTo(Intersection other){
		return (int)((this.point).distance(this.ray.getP0()) - (other.point).distance(this.ray.getP0())); 
	}

}
