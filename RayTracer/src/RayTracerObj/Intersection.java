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
			boolean occluded = isOccluded(lightRay, scene);
			
			Color diffuseColor = getDiffuseColor(light);
			curColor = curColor.add(diffuseColor);
			
			Color specularColor = getSpecularColor(light);
			curColor = curColor.add(specularColor);
			
			/*Take into account hard shadow intensity - in case point occluded*/
			if (scene.set.getShRays() == 1) {
				if (occluded) {
					curColor = curColor.mul( 1 - light.getShadowIntesity());
				}
			} else {
				double softShadowCoeff = getSoftShadowCoeff(light, scene);
				curColor = curColor.mul(softShadowCoeff);
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
		return reflectionColor;
	}

	private Color calcTransColor(Scene scene, int depth) {
		Ray transRay = new Ray(point, ray.getVec());
		/*look for next intersection*/
		Color backgroundColor;
		Intersection background = RayCast.findIntersection(transRay, scene, current);
		if (background != null) {
			background.computeColor(scene, depth + 1);
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
		Vector planeVecY = planeVecX.crossProduct(lightNormal).normalize();
		int length = (int) Math.floor(Math.sqrt(n) / 2);
		Random rand = new Random();
		int occludedCount = 0;
		int totalCount = 0;
		for (int i = -length; i < length; i++) {
			for (int j = -length; j < length; j++) {
				
				double xdist = rand.nextDouble() * nradious + i * nradious;
				double ydist = rand.nextDouble() * nradious + j * nradious;
				
				Point lightSoftShadowPoint = lightPoint.toVec().add(planeVecX.mul(xdist)).add(planeVecY.mul(ydist)).toPoint();
				Ray softShadowRay = new Ray(lightSoftShadowPoint, point);
				
				boolean occluded  = isOccluded(softShadowRay, scene);
				if (!occluded) { 
					occludedCount++;
				}
				totalCount++;
			}
		}
		
		return (double) (occludedCount) / (double) (totalCount);
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
		Vector V = ray.getVec();
		Color Il = light.getLightColor();
		
		/*Calc specular color*/
		Color Ks = material.specularColor;
		Vector Rm = N.mul(2 * (L.dotProduct(N))).sub(L);
		double cosalpha = V.dotProduct(Rm);
		
		Color specularColor = Ks.mul(Math.pow(cosalpha, material.phongCoeff)).mul(Il);
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
	private boolean isOccluded(Ray lightRay, Scene scene) {
		/*Calc Ray and distance from light to point*/
		boolean distPos;
		double distance = point.toVec().dotProduct(lightRay.getP0().toVec());
		distPos = distance > 0;
		
		/*Check if occluded by any other surface*/
		/*Assume not occluded*/
		boolean occluded = false;
		for (Surface surface: scene) {
			if (surface == current) continue;
			
			Intersection intersection = surface.getIntersection(lightRay);
			if (intersection == null) continue; //No intersection
			
			/*Check if occluded*/
			double distance2 = intersection.getPoint().toVec().dotProduct(lightRay.getP0().toVec());
			if (distPos && distance2 < 0) continue;
			if (!distPos && distance2 > 0) continue;
			
			if (Math.abs(distance) > Math.abs(distance2)) {
				occluded = true;
				break;
			}
		}
		
		return occluded;
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
