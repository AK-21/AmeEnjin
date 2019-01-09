package engine;

import java.util.function.Function;

abstract class GameObject
{
	private final byte COORDS_ARR_SIZE_LR=2;
	private final byte COORDS_ARR_SIZE_BT=2;
	private final byte COORDS_ARR_SIZE_XY=2;
	
	static final byte COORDS_X=0;
	static final byte COORDS_Y=1;
	static final byte COORDS_L=0;
	static final byte COORDS_R=1;
	static final byte COORDS_B=0;
	static final byte COORDS_T=1;
	
	
	private int vID;
	
	private int vCoords[][][] = new int[COORDS_ARR_SIZE_LR][COORDS_ARR_SIZE_BT][COORDS_ARR_SIZE_XY];
	
	private int vTexNr;
	
	void setID(int nrIdent)
	{
		vID=nrIdent;
	}
	
	int getID()
	{
		return vID;
	}
	
	void setCoords(int lbX, int lbY, int ltX, int ltY, int rtX, int rtY, int rbX, int rbY)
	{
		vCoords[COORDS_L][COORDS_B][COORDS_X]=lbX;
		vCoords[COORDS_L][COORDS_B][COORDS_Y]=lbY;
		vCoords[COORDS_L][COORDS_T][COORDS_X]=ltX;
		vCoords[COORDS_L][COORDS_T][COORDS_Y]=ltY;
		vCoords[COORDS_R][COORDS_T][COORDS_X]=rtX;
		vCoords[COORDS_R][COORDS_T][COORDS_Y]=rtY;
		vCoords[COORDS_R][COORDS_B][COORDS_X]=rbX;
		vCoords[COORDS_R][COORDS_B][COORDS_Y]=rbY;
	}
	
	void updatePosX(int newX)
	{
		vCoords[COORDS_L][COORDS_B][COORDS_X]+=newX;
		vCoords[COORDS_L][COORDS_T][COORDS_X]+=newX;
		vCoords[COORDS_R][COORDS_T][COORDS_X]+=newX;
		vCoords[COORDS_R][COORDS_B][COORDS_X]+=newX;
	}
	
	void updatePosY(int newY)
	{
		vCoords[COORDS_L][COORDS_B][COORDS_Y]+=newY;
		vCoords[COORDS_L][COORDS_T][COORDS_Y]+=newY;
		vCoords[COORDS_R][COORDS_T][COORDS_Y]+=newY;
		vCoords[COORDS_R][COORDS_B][COORDS_Y]+=newY;
	}
	
	int getCoord(int lr, int bt, int xy)
	{
		return vCoords[lr][bt][xy];
	}
	
	void setTexNr(int texNr)
	{
		vTexNr=texNr;
	}
	
	int getTexNr()
	{
		return vTexNr;
	}
	
	int getWidth()
	{
		int vb, ve;
		if(vCoords[COORDS_L][COORDS_B][COORDS_X] <= vCoords[COORDS_L][COORDS_T][COORDS_X])
			vb = vCoords[COORDS_L][COORDS_B][COORDS_X];
		else
			vb = vCoords[COORDS_L][COORDS_T][COORDS_X];
		
		if(vCoords[COORDS_R][COORDS_B][COORDS_X] >= vCoords[COORDS_R][COORDS_T][COORDS_X])
			ve = vCoords[COORDS_R][COORDS_B][COORDS_X];
		else
			ve = vCoords[COORDS_R][COORDS_T][COORDS_X];
		
		return ve - vb;		
	}
	
	int getHeight()
	{
		int vb, ve;
		if(vCoords[COORDS_L][COORDS_B][COORDS_Y] <= vCoords[COORDS_R][COORDS_B][COORDS_Y])
			vb = vCoords[COORDS_L][COORDS_B][COORDS_Y];
		else
			vb = vCoords[COORDS_R][COORDS_B][COORDS_Y];
		
		
		if(vCoords[COORDS_L][COORDS_T][COORDS_Y] >= vCoords[COORDS_R][COORDS_T][COORDS_Y])
			ve = vCoords[COORDS_L][COORDS_T][COORDS_Y];
		else
			ve = vCoords[COORDS_R][COORDS_T][COORDS_Y];
		return ve - vb;		
	}
	
	int getRelX()
	{
		return vCoords[COORDS_L][COORDS_B][COORDS_X] + getWidth()/2;
	}
	
	int getRelY()
	{
		return vCoords[COORDS_L][COORDS_B][COORDS_Y] + getHeight()/2;
	}
	
	void setRelPos(int x, int y)
	{
		int w = getWidth()/2;
		int h =  getHeight()/2;
		setCoords
		(
			vCoords[COORDS_L][COORDS_B][COORDS_X] = x - w,
			vCoords[COORDS_L][COORDS_B][COORDS_Y] = y - h,
			vCoords[COORDS_L][COORDS_T][COORDS_X] = x - w,
			vCoords[COORDS_L][COORDS_T][COORDS_Y] = y + h,
			vCoords[COORDS_R][COORDS_T][COORDS_X] = x + w,
			vCoords[COORDS_R][COORDS_T][COORDS_Y] = y + h,
			vCoords[COORDS_R][COORDS_B][COORDS_X] = x + w,
			vCoords[COORDS_R][COORDS_B][COORDS_Y] = y - h
		);
	}
	
	void draw(Function<Integer, GameTexture> textureGetter)
	{
		if(Graphics.isTextMode())
			Graphics.adjustDisplayMode(Graphics.DISPLAY_MODE_OBJECT);
		Graphics.draw(getWidth(), getHeight(), getRelX(), getRelY(), textureGetter.apply(getTexNr()));

	}
}
