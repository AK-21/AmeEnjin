package engine;

import static engine.Dialogue.*;

class Effigy extends GameObject
{	
	private static final int EFF_ORIGIN_X = 10;
	private static final int EFF_ORIGIN_Y = DIA_BOX_HEIGHT - 1;
	
	Effigy(int tex, int width, int height)
	{
		vEndPointX = EFF_ORIGIN_X+width;
		vEndPointY = EFF_ORIGIN_Y+height;
		
		place();
		setTexNr(tex);
	}	
	
	private int vEndPointX;
	private int vEndPointY;
	
	private void place()
	{
		setCoords(EFF_ORIGIN_X, EFF_ORIGIN_Y, EFF_ORIGIN_X, vEndPointY, vEndPointX, vEndPointY, vEndPointX, EFF_ORIGIN_Y);
	}
	
	void setPos(int x, int y)
	{
		setRelPos(x, y);
	}
}
