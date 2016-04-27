package RayTracerObj;

import java.util.ArrayList;
import java.util.Random;

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
		for (Light light: lights) {
			
			Color curColor = new Color(0, 0, 0);
			Ray  lightRay = new Ray(light.getPosition(), point);
			
			Color diffuseColor = getDiffuseColor(light);
			curColor = curColor.add(diffuseColor);
			
			Color specularColor = getSpecularColor(light);
			curColor = curColor.add(specularColor);
			
			/*Take into account hard shadow intensity - in case point occluded*/
			if (scene.set.getShRays() == 1) {
//				boolean occluded = isOccluded(lightRay, scene);
//				if (occluded) {
//					curColor = curColor.mul( 1 - light.getShadowIntesity());
//				}
			} else {
				double softShadowCoeff = getSoftShadowCoeff(light, scene);
				//curColor = curColor.mul(softShadowCoeff);
				if (softShadowCoeff < 1) {
					curColor = curColor.mul((1 - light.getShadowIntesity()) + softShadowCoeff*light.getShadowIntesity());
				}	
			}
			
			color = color.add(curColor);
		}
		/*Take into account transparency coeff*/
		color = color.mul(1 - material.transparency);
			
			
		/*transparency*/
		if (material.transparency > 0 ) { /* transparent or partially transparent*/ 
			Color transColor = calcTransColor(scene, depth);
			color = color.add(transColor.mul(material.transparency));
		}
		
		
		/*reflection*/
		Color reflectColor = calcReflectColor(scene, depth);
		color = color.add(reflectColor);
		
	}

	private Color calcReflectColor(Scene scene, int depth) {
		Vector V = ray.getVec();
		Vector N = normal;
		double costeta = V.dotProduct(N);
		if (costeta >= 0) return new Color(0, 0, 0);
		
		Vector R = V.sub(N.mul(2.0 * (costeta))).normalize();
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
		return reflectionColor;
	}

	private Color calcTransColor(Scene scene, int depth) {
		Ray transRay = new Ray(point, ray.getVec());
		/*look for next intersection*/
		Color backgroundColor;
		Intersection background = RayCast.findIntersection(transRay, scene, current);
		if (background != null) {
			background.computeColor(scene, 0);
			backgroundColor = background.getColor();
		} else {
			backgroundColor = scene.set.getBackgroundColor();
		}
			       
		return backgroundColor;
	}

	private double getSoftShadowCoeff(Light light, Scene scene) {
		/*Soft shadow*/
		Vector lightNormal = ray.getVec();
		Point lightPoint  = light.getPosition();
		double radious  = light.getLightWidth();
		double n = scene.set.getShRays();
		double nradious = radious / n;
		Vector planeVecX = new Point(lightNormal.toPoint().getY(), - lightNormal.toPoint().getX(), 0).toVec().normalize();
		Vector planeVecY = lightNormal.crossProduct(planeVecX).normalize();
		int length = (int) Math.floor(n / 2);
		Random rand = new Random();
		double intensityCount = 0;
		int totalCount = 0;
		for (int i = -length; i <= length; i++) {
			for (int j = -length; j <= length; j++) {
				
				double xdist = rand.nextDouble() * nradious + ((double) i) * nradious;
				double ydist = rand.nextDouble() * nradious + ((double) j) * nradious;
				
				Point lightSoftShadowPoint = lightPoint.toVec().add(planeVecX.mul(xdist)).add(planeVecY.mul(ydist)).toPoint();
				Ray softShadowRay = new Ray(lightSoftShadowPoint, point);
				
				double intensity  = isOccluded(softShadowRay, scene);
				intensityCount += intensity;
				
				totalCount++;
			}
		}
		
		return ((double) intensityCount) / ((double) totalCount);
	}

	/**
	 *  Calc the specular color for a given light in the intersection
	 * @param light
	 * @return
	 */
	private Color getSpecularColor(Light light) {
		/*Calc diffuse color*/
		Vector N = normal;
		Vector L = light.getPosition().toVec().sub(point.toVec()).normalize();
		Vector V = ray.getVec().mul(-1);
		Color Il = light.getLightColor();
		
		/*Calc specular color*/
		Color Ks = material.specularColor;
		double costeta = L.dotProduct(N);
		if (costeta <= 0) return new Color(0,0,0);
		Vector Rm = N.mul(2 * (costeta)).sub(L);
		double cosalpha = V.dotProduct(Rm);
		if (cosalpha < 0) cosalpha = 0;
		
		Color specularColor = Ks.mul(Math.pow(cosalpha, material.phongCoeff)).mul(Il).mul(light.getSpecularIntesity());
		return specularColor;
	}

	/**
	 * Calc the diffuse color in the relevant intersection for a given light
	 * @param light
	 * @return
	 */
	private Color getDiffuseColor(Light light) {
		/*Calc diffuse color*/
		Vector N = normal;
		Color Kd = material.diffuseColor;
		Vector L = light.getPosition().toVec().sub(point.toVec()).normalize();
		
		double costeta = N.dotProduct(L);
		if (costeta < 0) costeta = 0;
		Color Il = light.getLightColor();
		Color diffuseColor = Kd.mul(costeta).mul(Il);
		
		return diffuseColor;
	}

	/**
	 * Given light Ray and point return true iff the point is occluded by another surface.
	 * @param lightRay
	 * @param point
	 * @return
	 */
	private double isOccluded(Ray lightRay, Scene scene) {
		/*Calc Ray and distance from light to point*/
		double distance = point.distance(lightRay.getP0());
		
		/*Check if occluded by any other surface*/
		/*Assume not occluded*/
		double intensity = 1;
		for (Surface surface: scene) {
			if (surface == current) continue;
			
			Intersection intersection = surface.getIntersection(lightRay);
			if (intersection == null) continue; //No intersection
			
			/*Check if occluded*/
			double distance2 = intersection.getPoint().distance(lightRay.getP0());
			
			if (distance > distance2) {
				intensity *= intersection.material.transparency;
			}
		}
		
		return intensity;
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
