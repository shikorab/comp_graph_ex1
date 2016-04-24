package RayTracerObj;

import java.util.ArrayList;

public class Intersection implements Comparable<Intersection>{

	public Material material;
	public Point point;
	public Color color;
	public Ray ray;
	public Surface current;
	public Vector normal;
	
	public Intersection(Point p, Material material, Ray ray, Surface current, Vector normal) {
		point = p;
		this.material = material;
		this.ray = ray;
		this.current = current;
		this.normal = normal;
	}
	
	public void computeColor(Scene scene, int depth) {
		color = new Color(0, 0, 0);
		if (depth > scene.set.getRecMax()) {
			color = scene.set.getBackgroundColor();
			return;
		}
		
		ArrayList<Light> lights = scene.lightList;
		int count = 0;
		for (Light light: lights) {
			/*Calc Ray and distance from light to point*/
			Ray  lightRay = new Ray(light.getPosition(), point);
			double distance = point.distance(light.getPosition());
			
			/*Check if occluded by any other surface*/
			/*Assume not occluded*/
			boolean occluded = false;
			for (Surface surface: scene) {
				if (surface == current) continue;
				
				Intersection intersection = surface.getIntersection(lightRay);
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
				
				/*Calc diffuse color*/
				Vector N = normal;
				Color Kd = material.diffuseColor;
				Vector L = light.getPosition().toVec().sub(point.toVec()).normalize();
				Vector V = ray.getVec();
				
				double costeta = N.dotProduct(L);
				Color Il = light.getLightColor();
				Color diffuseColor = Kd.mul(costeta).mul(Il);
				color = color.add(diffuseColor);
				
				/*Calc specular color*/
				Color Ks = material.specularColor;
				Vector Rm = N.mul(2 * (L.dotProduct(N))).sub(L);
				double cosalpha = V.dotProduct(Rm);
				
				Color specularColor = Ks.mul(Math.pow(cosalpha, material.phongCoeff)).mul(Il);
				color = color.add(specularColor);
				
				count = count + 1;
			}	
			color = color.mul(1 - material.transparency);
			
			/*emission*/
			
			/*ambient*/
			
			/*transparency*/
			if (material.transparency > 0 ) { /* transparent or partially transparent*/ 
				Ray newray = new Ray(point, ray.getVec());
				/*look for next intersection*/
				Color backgroundColor;
				Intersection background = RayCast.findIntersection(newray, scene, current);
				if (background != null) {
					background.computeColor(scene, depth + 1);
					backgroundColor = background.getColor();
				} else {
					backgroundColor = scene.set.getBackgroundColor();
				}
					
				color = color.add(backgroundColor.mul(material.transparency));
			}
			
			
			/*reflection*/
			Vector V = ray.getVec();
			Vector N = normal;
			Vector R = V.sub(N.mul(2 * (V.dotProduct(N)))).normalize();
			Ray reflactionRay = new Ray(point, R);
			Intersection background = RayCast.findIntersection(reflactionRay, scene, current);
			Color backgroundColor;
			if (background != null) {
				background.computeColor(scene, depth + 1);
				backgroundColor = background.getColor();
			} else {
				backgroundColor = scene.set.getBackgroundColor();
			}
			Color reflectionColor = material.reflectionColor.mul(backgroundColor);
			color = color.add(reflectionColor);
			
			
			/*shadow*/
			
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

	public Vector getNormal() {
		return normal;
	}

}
