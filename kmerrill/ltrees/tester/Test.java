package kmerrill.ltrees.tester;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

public class Test {

	public static void main(String[] args) {
		new Test();
	}
	
	public Test() {
		JFrame frame = new JFrame();
		
		frame.setSize(500, 500);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image image = frame.createVolatileImage(128, 128);
		
		
		Random rand = new Random();
		
//		for (int i = 0; i < rand.nextInt(5) + 5; i++) {
			tree(64);
//		}
		
		
		
		while (true) {
			Graphics g = image.getGraphics();
			render(g);
			g = frame.getGraphics();
			g.drawImage(image, 0, 0, frame.getHeight(), frame.getHeight(),null);
			g.dispose();
		}
	}
	public int SIZE = 80;
	Color[][][] stuff = new Color[SIZE][SIZE][SIZE];
	
	double rotation = 0;
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 128, 128);
		g.setColor(Color.BLACK);
		
		rotation +=0.1;
		
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				for (int z = 0; z < SIZE; z++) {
					if (stuff[x][y][z] != null) {
						
						g.setColor(stuff[x][y][z]);
						if (stuff[x][y][z] == Color.black ) {
							
							double col = (int)((z - 32) * Math.sin(Math.toRadians(rotation))) + 32;
							
							col = (int)(col / 8) * 8;
							col /= 64;
							col *= 255;
							if (col < 0) col = 0;
							if (col > 255) col = 255;
							g.setColor(new Color((int)col, (int)col, (int)col));
						} 
						else {
							double col = (int)((z - 32) * Math.sin(Math.toRadians(rotation))) + 32;
							
							col = (int)(col / 8) * 8;
							col /= 64;
							col *= 255;
							int R = stuff[x][y][z].getRed();
							int G = stuff[x][y][z].getGreen();
							int B = stuff[x][y][z].getBlue();
							
							R += col;
							R /= 2;
							G += col;
							G /= 2;
							B += col;
							B /= 2;
							
							if (R < 0) R = 0;
							if (R > 255) R = 255;
							if (G < 0) G = 0;
							if (G > 255) G = 255;
							if (B < 0) B = 0;
							if (B > 255) B = 255;
							g.setColor(new Color(R, G, B));
						}
						double X = (x - SIZE/2) * Math.cos(Math.toRadians(rotation)) + (z - SIZE/2) * Math.sin(Math.toRadians(rotation));
						double Z = (z - SIZE/2) * Math.sin(Math.toRadians(rotation));
						g.fillRect((int)X + SIZE/2 + 20, (y - 4), 1, 1);
					}
				}
			}
		}
	}
	
	public void tree(int x) {
		Random rand = new Random();
		
//		LSystem system = new LSystem(pos, 1, false,4.0, "place", "place", "place", "splitrand");
//   	 	system.run(rand, 1);
   	 	
   	 	
   	 	for (int i = 0; i < 5; i++)
		{
	   	 	TestPos pos3 = new TestPos(rand.nextInt(SIZE), 0, rand.nextInt(SIZE));
	   	 	
	   	 	String[] tree = {"splitrand","place","place<25"};
	   	 	
	   	 	LSystem stem = new LSystem(pos3, 10, 3, 5.0, 2,tree);
		 	stem.run(rand, 10);
		}
//   	 	}
   	 	
	}
	
	

	   private class LSystem {
		   public ArrayList<L> system = new ArrayList<L>();
		   
		   public int leaves;
		   public double leafSize;
		   
		   public LSystem(TestPos pos, int maxIterations, int leaves, double leafSize, double size, String...system) {
			   this.system.add(new L(pos, this, system, maxIterations, size, true));
			   this.leaves = leaves;
			   this.leafSize = leafSize;
		   }
		   
		   public void run(Random rand, int iterations) {
			   
			   for (int i = 0; i < iterations; i++) {
				   int A = system.size();
				   
				   for (int a = 0; a < A; a++) {
					   if (system.get(a).iterations <= 0) {
						   system.remove(a);
						   break;
					   }
					   system.get(a).runFunction(rand);
					   system.get(a).size *= 0.9;
				   }
			   }
		   }
	   }
	   
	   private class L {
		   public TestPos pos = new TestPos(0, 0, 0);
		   public boolean xy;
		   public int dir;
		   
		   public boolean main = false;
		   
		  
		   public String[] system;
		   
		   public int last = 0;
		   
		   public LSystem parent;
		   
		   public int iterations = 0;
		   
		   public double size = 1.0f;
		   
		   
		   public L(TestPos pos, LSystem parent, String[] system, int iterations, double size, boolean main) {
			   this.pos = pos;
			   this.system = system;
			   this.iterations = iterations;
			   this.parent = parent;
			   this.size = size;
		   }
		   
		   public void runFunction(Random rand) {
			   if (iterations <= 0) return;
			   	if (main) iterations+=0;
			   else
				   iterations--;
			   
			   for (String function : system) {
				   run(function, rand);
			   }
		   }
		   
		   private void run(String function, Random rand) {
			  
			   if (function.contains("<")) {
				   if (function.split("<").length > 0) {
					   int r = rand.nextInt(100);
					   if (r >= Integer.parseInt(function.split("<")[1])) {
						   return;
					   }
					   function = function.split("<")[0];
				   }
			   }
			   if (function.contains(">")) {
				   if (function.split(">").length > 0) {
					   int r = rand.nextInt(100);
					   if (r <= Integer.parseInt(function.split(">")[1])) {
						   return;
					   }
					   function = function.split(">")[0];
				   }
			   }
			   
			   if (function.contentEquals("place")) place(rand);
			   if (function.contentEquals("up")) move(0); 
			   if (function.contentEquals("down")) move(2);
			   if (function.contentEquals("left")) move(3);
			   if (function.contentEquals("right")) move(1);
			   if (function.contentEquals("turnaround")) rot3(6);

			   if (function.contentEquals("turnright")) rot3(1);
			   if (function.contentEquals("turnleft")) rot3(-1);
			   if (function.contentEquals("roteast")) rot(1);
			   if (function.contentEquals("rotwest")) rot(-1);
			   if (function.contentEquals("rotnorth")) rot2(1);
			   if (function.contentEquals("rotsouth")) rot2(-1);
			   
			   if (function.contentEquals("rot90east")) rot(3);
			   if (function.contentEquals("rot90west")) rot(-3);
			   if (function.contentEquals("rot90north")) rot2(3);
			   if (function.contentEquals("rot90south")) rot2(-3);
			   
			   if (function.contentEquals("--")) iterations--;
			   if (function.contentEquals("++")) iterations++;
			   
			   if (function.contentEquals("flip")) {
				   pos.rotationXY += 180;
				   pos.rotationZY += 180;
			   }

			   
			   if (function.contentEquals("rot90rand")) rot3((rand.nextInt(3) - 1) * 3);

			   if (function.contentEquals("rotrand")) {
				   rot3(rand.nextInt(3) - 1);
				   rot2(rand.nextInt(3) - 1);
				   rot(rand.nextInt(3) - 1);
				   }
			   
			   
			   if (function.contentEquals("spliteast")) splitRight(rand);
			   if (function.contentEquals("splitwest")) splitLeft(rand);
			   if (function.contentEquals("splitnorth")) splitNorth(rand);
			   if (function.contentEquals("splitsouth")) splitSouth(rand);
			   if (function.contentEquals("casesplit"))if (rand.nextBoolean()) splitRand(rand);
			   if (function.contentEquals("splitrand")) splitRand(rand);
			   if (function.contentEquals("splitrand2d")) splitRand2d(rand);

			   if (true) {
				   if (function.contentEquals("shrink")) {
					   iterations--;
					   size *=0.7;
				   }
				   if (function.contentEquals("grow")) {
					   iterations++;
					   size *=1.3;
				   }
			   }
			   
			   if (function.contentEquals("z")) xy = !xy;
		   }
		   
		   public void splitRand2d(Random rand) {
			   if (rand.nextBoolean())
				   splitLeft(rand);
			   else
				   splitRight(rand);
		   }
		   public void splitRand(Random rand) {
			   splitTrueRand(rand);
		   }
		  double sizeDown = 0.8;
		   public void splitTrueRand(Random rand) {
			   L l = new L(new TestPos(pos.getX() + 0,pos.getY() + 0, pos.getZ() + 0), parent, system, iterations-1, size*sizeDown,false);
			   l.pos.rotationXY = pos.rotationXY;
			   l.pos.rotationXZ = pos.rotationXZ;
			   l.pos.rotationZY = pos.rotationZY;
			   l.rot(rand.nextInt(2) * 2 - 1);
			   l.rot2(rand.nextInt(2) * 2 - 1);
			   l.rot3(rand.nextInt(24) - 12);
			   l.place(rand);
			   parent.system.add(l);
//			   iterations--;
		   }
		   
		   public void splitNorth(Random rand) {
			   L l = new L(new TestPos(pos.getX() + 0,pos.getY() + 0, pos.getZ() + 0), parent, system, iterations-1, size*sizeDown,false);
			   l.pos.rotationXY = pos.rotationXY;
			   l.pos.rotationXZ = pos.rotationXZ;
			   l.pos.rotationZY = pos.rotationZY;
			   l.rot2(-1);
			   l.place(rand);
			   parent.system.add(l);
//			   iterations--;
		   }
		   
		   public void splitSouth(Random rand) {
			   L l = new L(new TestPos(pos.getX() + 0,pos.getY() + 0, pos.getZ() + 0), parent, system, iterations-1, size*sizeDown,false);
			   l.pos.rotationXY = pos.rotationXY;
			   l.pos.rotationXZ = pos.rotationXZ;
			   l.pos.rotationZY = pos.rotationZY;
			   l.rot2(1);
			   l.place(rand);
			   parent.system.add(l);
//			   iterations--;
		   }
		   
		   public void splitLeft(Random rand) {
			   L l = new L(new TestPos(pos.getX() + 0,pos.getY() + 0, pos.getZ() + 0), parent, system, iterations-1, size*sizeDown,false);
			   l.pos.rotationXY = pos.rotationXY;
			   l.pos.rotationXZ = pos.rotationXZ;
			   l.pos.rotationZY = pos.rotationZY;
			   l.rot(-1);
			   l.place(rand);
			   parent.system.add(l);
//			   iterations--;
		   }
		   
		   public void splitRight(Random rand) {
			   L l = new L(new TestPos(pos.getX() + 0,pos.getY() + 0, pos.getZ() + 0), parent, system, iterations-1, size*sizeDown,false);
			   l.pos.rotationXY = pos.rotationXY;
			   l.pos.rotationXZ = pos.rotationXZ;
			   l.pos.rotationZY = pos.rotationZY;
			   l.rot(1);
			   l.place(rand);
			   parent.system.add(l);
//			   iterations--;
		   }
		   
		   public TestVec[] cloneVecArray(TestVec[] a) {
			   TestVec[] b = new TestVec[a.length];
			   for (int i = 0; i < a.length; i++) {
				   b[i] = cloneVec(a[i]);
			   }
			   return b;
		   }
		   
		   public TestVec cloneVec(TestVec a) {
			   return new TestVec(a.getX() + 0, a.getY() + 0, a.getZ() + 0);
		   }
		   
		   public void place(Random rand) {
			   int x = (int)pos.getX();
			   int y = (int)pos.getY();
			   int z = (int)pos.getZ();
			   y = (SIZE-1) - y;
			   
			   for (int xx = (int)(- size); xx < size; xx++) {
				   for (int zz = (int)(- size); zz < size; zz++) {
					   for (int yy = (int)(-size); yy < size; yy++) {
						   double dist = Math.sqrt(xx * xx + zz * zz);
//						   double dist2 = Math.sqrt(xx * xx + zz * zz);
						   if (dist <= size) {
							   placeWood(xx + x, y + yy, zz + z, rand);
						   }
					   }
					   
				   }
			   }
			   
//			   worldIn.setBlockState(pos, BlocksT.ICE.getDefaultState(), 2);
			   move(last);
		   }
		   
		   public void placeWood(int x, int y, int z, Random rand) {
			   if (x >= 0 && y >= 0 && z >= 0 && x < SIZE && y < SIZE && z < SIZE) {
				   stuff[x][y][z] = Color.black;
				   if (iterations < parent.leaves) {
					   double size = parent.leafSize * this.size;
					   for (int xx = (int)(- size); xx < size; xx++) {
						   for (int zz = (int)(- size); zz < size; zz++) {
							   for (int yy = (int)(-size); yy < size; yy++) {
								   double dist = Math.sqrt(xx * xx + zz * zz + yy * yy);
//									   double dist2 = Math.sqrt(xx * xx + zz * zz);
								   if (dist <= size) {
									   if (rand.nextInt(10)<=3)
									   placeLeaf(xx + x, y + yy, zz + z);
								   }
							   }
							   
						   }
					   }
					   
//						   placeLeaf(x + rand.nextInt(3) - 1, y + rand.nextInt(3) - 1, z + rand.nextInt(3) - 1);
					   
				   }
			   }
		   }
		   
		   public void placeLeaf(int x, int y, int z) {
			   if (x >= 0 && y >= 0 && z >= 0 && x < SIZE && y < SIZE && z < SIZE) {
				   if (stuff[x][y][z] == null)
				   stuff[x][y][z] = Color.green;
			   }
		   }
		   
		   public void move(int face) {
			   pos = pos.forwards(size);
			   last = face;
		   }
		   public void rot(int rot) {
			   pos.rotationXY -= 30 * rot;
		   }
		   public void rot2(int rot) {
			   pos.rotationZY -= 30 * rot;
		   }
		   public void rot3(int rot) {
			   pos.rotationXZ -= 30 * rot;
		   }
	   }

}
