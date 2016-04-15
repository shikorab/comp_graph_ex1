package RayTracerObj;

import java.util.ArrayList;

public class Scene {
	public Camera cam;
	public Settings set;
	public ArrayList<Material> material_list = new ArrayList<Material>(); 
	public ArrayList<Sphere> Sphere_list = new ArrayList<Sphere>();
	public ArrayList<Plane> plane_list = new ArrayList<Plane>();
	public ArrayList<Cylinder> cylinder_list = new ArrayList<Cylinder>();
	public ArrayList<Light> light_list = new ArrayList<Light>();
	
}
