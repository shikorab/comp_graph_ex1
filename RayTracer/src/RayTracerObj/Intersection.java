package RayTracerObj;

public class Intersection {

	public Material material;
	public Point point;
	
	public Intersection(Point p, Material material) {
		point = p;
		this.material = material;
	}

	public Color getColor() {
		Color dcol = material.diffuse_color;
		return dcol.mul(256);
	}
	
	public Point getPoint() {
		return point;
	}

}
