package RayTracerObj;

public class Material {
	public  Color diffuse_color;
	public  Color specular_color;
	public  Color reflection_color;
	public  double phong_coeff;
	public  double transparency;
	
	public Material(double dr, double dg, double db, double sr, double sg, double sb, double rr, double rg, double rb,
			double phong, double trans) {
		this.diffuse_color = new Color(dr, dg, db);
		this.specular_color = new Color(sr, sg, sb);
		this.reflection_color = new Color(rr, rg, rb);
		this.phong_coeff = phong;
		this.transparency = trans;
	}
}
