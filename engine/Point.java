package engine;

import org.lwjgl.opengl.GL11;

class Point
{
	private int vX;
	private int vY;
	
	Point(int x, int y)
	{
		vX = x;
		vY = y;
	}
	
	void draw()
	{
		GL11.glVertex2f(vX,vY);
	}
}
