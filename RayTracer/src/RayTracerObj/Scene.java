package RayTracerObj;

import java.util.ArrayList;
import java.util.Iterator;

public class Scene implements Iterable<Surface>{
	public Camera cam;
	public Settings set;
	public ArrayList<Material> materialList = new ArrayList<Material>(); 
	public ArrayList<Sphere> sphereList = new ArrayList<Sphere>();
	public ArrayList<Plane> planeList = new ArrayList<Plane>();
	public ArrayList<Cylinder> cylinderList = new ArrayList<Cylinder>();
	public ArrayList<Light> lightList = new ArrayList<Light>();
	@Override
	public Iterator<Surface> iterator() {
		
		return new Iterator<Surface>() {
			//Spheres, Planes, Cylinders
			int surfaceIndex = -1;
			
			@Override
			public boolean hasNext() {
				boolean res = (surfaceIndex + 1) < (sphereList.size() + planeList.size() + cylinderList.size()) ;
				return res;
			}

			@Override
			public Surface next() {
				surfaceIndex++;
				
				if (surfaceIndex < sphereList.size()) {
					return sphereList.get(surfaceIndex);
				} else if (surfaceIndex < sphereList.size() + planeList.size()) {
					return planeList.get(surfaceIndex - sphereList.size());
				} else if (surfaceIndex < sphereList.size() + planeList.size() + cylinderList.size()) {
					return cylinderList.get(surfaceIndex - sphereList.size() - planeList.size());
				}
				return null;
			}
		};
	}
	
}
