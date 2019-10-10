package kmerrill.ltrees.tester;


public class TestVec {
	public double x, y, z;
	public TestVec(double x, double y, double z) {
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
	
	public String toString() {
		return x + ", " + y + ", " + z;
	}
}
