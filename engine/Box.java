package engine;

class Box extends GameObject
{
	Box(int leftX, int rightX, int botY, int topY)
	{
		setCoords(leftX, botY, leftX, topY, rightX, topY, rightX, botY);
	}
}
