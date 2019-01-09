package engine;

import java.util.function.Function;

class Appearance
{		
	
	private Effigy vEffigy;
	private Portrait vPortrait;
	
	
	Appearance(Effigy e, Portrait p)
	{
		vEffigy = e;
		vPortrait = p;
	}
	
	int getEffigyTex()
	{
		return vEffigy.getTexNr();
	}
	
	void drawEffigy(Function<Integer, GameTexture> textureGetter)
	{
		vEffigy.draw(textureGetter);
	}
	
	
	int getPortraitTex()
	{
		return vPortrait.getTexNr();
	}
	
	void drawPortrait(Function<Integer, GameTexture> textureGetter)
	{
		vPortrait.draw(textureGetter);
	}
	
	void setPortraitPos(int x, int y)
	{
		vPortrait.setPos(x, y);
	}
}
