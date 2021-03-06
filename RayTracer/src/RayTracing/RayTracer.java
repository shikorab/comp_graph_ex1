package RayTracing;

import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import RayTracerObj.Camera;
import RayTracerObj.Cylinder;
import RayTracerObj.Light;
import RayTracerObj.Material;
import RayTracerObj.Plane;
import RayTracerObj.RayCast;
import RayTracerObj.Scene;
import RayTracerObj.Settings;
import RayTracerObj.Sphere;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {

	public int imageWidth;
	public int imageHeight;
	private Scene scene;
	
	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as input.
	 */
	public static void main(String[] args) {

		try {

			RayTracer tracer = new RayTracer();

			// Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			// Optional params
			if (args.length > 3)
			{
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}


			// Parse scene file:
			tracer.parseScene(sceneFileName);
			//Update Screen Height according aspect Ratio
			double aspectRatio = tracer.imageHeight / tracer.imageWidth;
			tracer.setAspectRatio(aspectRatio);
			
			// Render scene:
			RayCast rayCast = new RayCast(tracer.imageWidth, tracer.imageHeight);
			rayCast.renderScene(tracer.scene.cam, tracer.scene, outputFileName);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (RayTracerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	private void setAspectRatio(double aspectRatio) {
		scene.cam.setAspectRatio(aspectRatio);
		
	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException
	{
		scene = new Scene();
		
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);



		while ((line = r.readLine()) != null)
		{
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#'))
			{  // This line in the scene file is a comment
				continue;
			}
			else
			{
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

				if (code.equals("cam"))
				{
					// Add code here to parse camera parameters
					
					double px = Double.parseDouble(params[0]);
					double py = Double.parseDouble(params[1]);
					double pz = Double.parseDouble(params[2]);
					double lx = Double.parseDouble(params[3]);
					double ly = Double.parseDouble(params[4]);
					double lz = Double.parseDouble(params[5]);
					double ux = Double.parseDouble(params[6]);
					double uy = Double.parseDouble(params[7]);
					double uz = Double.parseDouble(params[8]);
					double sc_dist = Double.parseDouble(params[9]);
					double sc_width = Double.parseDouble(params[10]);
					scene.cam = new Camera(px, py, pz, lx, ly, lz, ux, uy, uz, sc_dist, sc_width);
					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
				}
				else if (code.equals("set"))
				{
					// Add code here to parse general settings parameters
					double bgr = Double.parseDouble(params[0]);
					double bgg = Double.parseDouble(params[1]);
					double bgb = Double.parseDouble(params[2]);
					double sh_rays = Double.parseDouble(params[3]);
					double rec_max = Double.parseDouble(params[4]);
					scene.set = new Settings(bgr, bgg, bgb, sh_rays, rec_max);
					System.out.println(String.format("Parsed general settings (line %d)", lineNum));
				}
				else if (code.equals("mtl"))
				{
					// Add code here to parse material parameters
					double dr = Double.parseDouble(params[0]);
					double dg = Double.parseDouble(params[1]);
					double db = Double.parseDouble(params[2]);
					double sr = Double.parseDouble(params[3]);
					double sg = Double.parseDouble(params[4]);
					double sb = Double.parseDouble(params[5]);
					double rr = Double.parseDouble(params[6]);
					double rg = Double.parseDouble(params[7]);
					double rb = Double.parseDouble(params[8]);
					double phong = Double.parseDouble(params[9]);
					double trans = Double.parseDouble(params[10]);
					Material mtl = new Material(dr, dg, db, sr, sg, sb, rr, rg, rb, phong, trans);
					scene.materialList.add(mtl);
					System.out.println(String.format("Parsed material (line %d)", lineNum));
				}
				else if (code.equals("sph"))
				{
					// Add code here to parse sphere parameters
					double cx = Double.parseDouble(params[0]);
					double cy = Double.parseDouble(params[1]);
					double cz = Double.parseDouble(params[2]);
					double radius = Double.parseDouble(params[3]);
					int mat_idx = Integer.parseInt(params[4]);
					Sphere sphere = new Sphere(cx, cy, cz, radius, scene.materialList.get(mat_idx - 1));
					scene.sphereList.add(sphere);
					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				}
				else if (code.equals("pln"))
				{
					// Add code here to parse plane parameters
					double nx = Double.parseDouble(params[0]);
					double ny = Double.parseDouble(params[1]);
					double nz = Double.parseDouble(params[2]);
					double offset = Double.parseDouble(params[3]);
					int mat_idx = Integer.parseInt(params[4]);
					Plane plane = new Plane(nx, ny, nz, offset, scene.materialList.get(mat_idx - 1));
					scene.planeList.add(plane);
					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				}
				else if (code.equals("cyl"))
				{
					// Add code here to parse cylinder parameters
					double cx = Double.parseDouble(params[0]);
					double cy = Double.parseDouble(params[1]);
					double cz = Double.parseDouble(params[2]);
					double len = Double.parseDouble(params[3]);
					double radius = Double.parseDouble(params[4]);
					double rx = Double.parseDouble(params[5]);
					double ry = Double.parseDouble(params[6]);
					double rz = Double.parseDouble(params[7]);
					int mat_idx = Integer.parseInt(params[8]);
					Cylinder cylinder = new Cylinder(cx, cy, cz, len, radius, rx, ry, rz, scene.materialList.get(mat_idx - 1));
					scene.cylinderList.add(cylinder);
					System.out.println(String.format("Parsed cylinder (line %d)", lineNum));
				}
				else if (code.equals("lgt"))
				{
					double px = Double.parseDouble(params[0]);
					double py = Double.parseDouble(params[1]);
					double pz = Double.parseDouble(params[2]);
					double lr = Double.parseDouble(params[3]);
					double lg = Double.parseDouble(params[4]);
					double lb = Double.parseDouble(params[5]);
					double specular_intesity = Double.parseDouble(params[6]);
					double shadow_intesity = Double.parseDouble(params[7]);
					double light_width = Double.parseDouble(params[7]);
					// Add code here to parse light parameters
					Light light = new Light(px, py, pz, lr, lg, lb, specular_intesity, shadow_intesity, light_width);
					scene.lightList.add(light);
					System.out.println(String.format("Parsed light (line %d)", lineNum));
				}
				else
				{
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}
			}
		}

		// It is recommended that you check here that the scene is valid,
		// for example camera settings and all necessary materials were defined.

		System.out.println("Finished parsing scene file " + sceneFileName);

	}

	
	public static class RayTracerException extends Exception {
		public RayTracerException(String msg) {  super(msg); }
	}


}
