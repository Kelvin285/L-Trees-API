package kmerrill.ltrees.tester;


public class TestPos {
	public double x, y, z;
	
	public double rotationXY = 90;
	public double rotationZY = 90;
	public double rotationXZ;
	
	public TestPos(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	
	public TestPos forwards() {
		
		TestPos pos2 = new TestPos(Math.cos(Math.toRadians(rotationXY)) * Math.cos(Math.toRadians(rotationXZ)), Math.sin(Math.toRadians(rotationXY + rotationXZ)), Math.cos(Math.toRadians(rotationXZ)) * Math.cos(Math.toRadians(rotationXY)));
		pos2.rotationXY = rotationXY;
		pos2.rotationZY = rotationZY;
		pos2.rotationXZ = rotationXZ;
		pos2.x += getX();
		pos2.y += getY();
		pos2.z += getZ();
		return pos2;
	}
	
	public TestPos forwards(double s) {
		double size = s * 1.0;
		TestPos pos2 = new TestPos(Math.cos(Math.toRadians(rotationXY)) * size, Math.sin(Math.toRadians(rotationXY)) * size, 0);
		TestPos pos3 = new TestPos(0, Math.sin(Math.toRadians(rotationZY)) * size, Math.cos(Math.toRadians(rotationZY)) * size);
		
		pos2 = new TestPos(pos2.x, (pos2.y + pos3.y), pos3.z);
		
		pos2.x = pos2.x * Math.cos(Math.toRadians(rotationXZ)) + pos2.z * Math.sin(Math.toRadians(rotationXZ));
		pos2.z = pos2.z * Math.cos(Math.toRadians(rotationXZ)) + pos2.x * Math.sin(Math.toRadians(rotationXZ));
		
		pos2.rotationXY = rotationXY;
		pos2.rotationZY = rotationZY;
		pos2.rotationXZ = rotationXZ;
		
		pos2.y *= 0.8;
//		pos2.x *= 2;
//		pos2.z *= 2;
		
		pos2.x += getX();
		pos2.y += getY();
		pos2.z += getZ();
		return pos2;
	}
}
