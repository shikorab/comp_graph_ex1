package RayTracerObj;

public class Material {
	public  Color diffuseColor;
	public  Color specularColor;
	public  Color reflectionColor;
	public  double phongCoeff;
	public  double transparency;
	
	public Material(double dr, double dg, double db, double sr, double sg, double sb, double rr, double rg, double rb,
			double phong, double trans) {
		this.diffuseColor = new Color(dr, dg, db);
		this.specularColor = new Color(sr, sg, sb);
		this.reflectionColor = new Color(rr, rg, rb);
		this.phongCoeff = phong;
		this.transparency = trans;
	}
}
