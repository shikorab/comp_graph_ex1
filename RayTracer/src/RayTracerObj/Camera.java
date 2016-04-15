package RayTracerObj;

public class Camera {
	
	private final Point position;
	private final Point up;
	private final Point lookAt;
	private final double scDist;
	private final double scWidth;
	private double scHeight;
	
	private Vector towardsVec;
	private Vector lookAtVec;
	private Vector upVec;
	private Vector rightVec;
	
//	private Point scTop;
//	private Point scBottom;
//	private Point scRight;
	private Point scLeft;
	
		
	public Camera(double px, double py, double pz, double lx, double ly, double lz, double ux, double uy, double uz,
			double sc_dist, double sc_width) {
		this.position = new Point(px, py, pz);
		this.lookAt = new Point(lx, ly, lz);
		this.up = new Point(ux, uy, uz);
		this.scDist = sc_dist;
		this.scWidth = sc_width;
		
		
		/*look At vector*/
		this.lookAtVec = this.getLookAt().toVec().normalize();
		
		/*towards vector*/
		this.towardsVec = this.getPosition().toVec();
		this.towardsVec = this.getLookAt().toVec().sub(this.towardsVec).normalize();
		
		/*up vector*/
		this.upVec = this.getUp().toVec();
		/*Fix up vector*/
		this.upVec = this.towardsVec.crossProduct(this.upVec.crossProduct(this.towardsVec)).normalize();
		
		/*Calc right vector*/
		this.rightVec = this.towardsVec.crossProduct(this.upVec).normalize();

		/*Calc screen points*/
//		scTop = this.position.toVec().add(this.towardsVec.mul(this.scDist)).add(this.upVec.mul(this.scHeight)).toPoint();/*
//		scBottom = this.position.toVec().add(this.towardsVec.mul(this.scDist)).sub(this.upVec.mul(this.scHeight)).toPoint();
//		scRight = this.position.toVec().add(this.towardsVec.mul(this.scDist)).add(this.rightVec.mul(this.scWidth)).toPoin*/t();
		
		
		scLeft = position.toVec().add(towardsVec.mul(scDist)).sub(rightVec.mul(scWidth)).toPoint();
	}
	
	public double getScHeight() {
		return scHeight;
	}


	public Vector getTowardsVec() {
		return towardsVec;
	}


	public Vector getLookAtVec() {
		return lookAtVec;
	}


	public Vector getUpVec() {
		return upVec;
	}


	public Vector getRightVec() {
		return rightVec;
	}


//	public Point getScTop() {
//		return scTop;
//	}
//
//
//	public Point getScBottom() {
//		return scBottom;
//	}
//
//
//	public Point getScRight() {
//		return scRight;
//	}


	public Point getScLeft() {
		return scLeft;
	}



	
	
	public Point getPosition() {
		return position;
	}

	public Point getUp() {
		return up;
	}

	public Point getLookAt() {
		return lookAt;
	}

	public double getScDist() {
		return scDist;
	}

	public double getScWidth() {
		return scWidth;
	}


	public void setAspectRatio(double aspectRatio) {
		this.scHeight  = this.scWidth * aspectRatio;		
	}
}