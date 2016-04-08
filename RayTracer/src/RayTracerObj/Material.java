package RayTracerObj;

public class Material {
	final private Color diffuse_color;
	final private Color specular_color;
	final private Color reflection_color;
	final private double phong_coeff;
	final private double transparency;
	
	public Material(double dr, double dg, double db, double sr, double sg, double sb, double rr, double rg, double rb,
			double phong, double trans) {
		this.diffuse_color = new Color(dr, dg, db);
		this.specular_color = new Color(sr, sg, sb);
		this.reflection_color = new Color(rr, rg, rb);
		this.phong_coeff = phong;
		this.transparency = trans;
	}
}
