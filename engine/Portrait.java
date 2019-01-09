package engine;

class Portrait extends GameObject
{
	static final int PORTRAIT_WIDTH = 140;
	static final int PORTRAIT_HEIGHT = 190;	
	
	Portrait(int tex)
	{
		setOrigin();
		setTexNr(tex);
	}	
	
	private void setOrigin()
	{
		setCoords(0, 0, 0, PORTRAIT_HEIGHT, PORTRAIT_WIDTH, PORTRAIT_HEIGHT, PORTRAIT_WIDTH, 0);
	}
	
	void setPos(int x, int y)
	{
		setRelPos(x, y);
	}
}
