package engine;

import java.util.function.Function;

public final class CreditsElement
{
	static final int CAT_EMPTY = 0;
	static final int CAT_TEXT = 1;
	static final int CAT_IMG = 2;
	
	static final int CENTER = Graphics.DEFAULT_WINDOW_WIDTH/2;
	
	CreditsElement(int height)
	{
		vCategory = CAT_EMPTY;
		vHeight = height;
		vPosition = 0;
		vOrigin = vPosition;
		setMargins();
		vAwayScreen = false;
		vDist=0;
	}
	
	CreditsElement(GameFont headerFont, String header, GameFont textFont, String... texts)
	{
		vCategory = CAT_TEXT;
		vHeader = header;
		vTexts = texts;
		vHeaderFont = headerFont;
		vTextFont = textFont;
		vHeight = vHeaderFont.getFont().getSize();
		vHeight += vHeaderFont.getFont().getSize()/2;
		vHeight += getTextLength()*vTextFont.getFont().getSize();
		vHeight += (getTextLength()-1)*(vTextFont.getFont().getSize()/2);
		vDist = 0;
		
		vPosition = Graphics.getWindowHeight()+vHeight/2;
		vOrigin = vPosition;
		setMargins();
		vAwayScreen = false;
		
	}
	
	CreditsElement(int width, int height, int texture)
	{
		vCategory = CAT_IMG;
		vHeight = height;
		vPosition = 0-vHeight/2;
		vOrigin = vPosition;
		vDist=0;
		
		vBox = new Box(CENTER-width/2, CENTER+width/2, vPosition-vHeight/2, vPosition+vHeight/2);
		
		vBox.setTexNr(texture);
		setMargins();
		vAwayScreen = false;		
	}
	
	private int vCategory;
	
	int getCategory()
	{
		if(vCategory == CAT_TEXT)
			return CAT_TEXT;
		if(vCategory == CAT_IMG)
			return CAT_IMG;
		return CAT_EMPTY;
	}
	
	boolean isText()
	{
		if(getCategory()==CAT_TEXT)
			return true;
		return false;
	}
	
	boolean isImg()
	{
		if(getCategory()==CAT_IMG)
			return true;
		return false;
	}
	
	boolean isEmpty()
	{
		if(getCategory()==CAT_EMPTY)
			return true;
		return false;
	}
	
	private String vHeader;
	private String[] vTexts;
	
	String getHeader()
	{
		if(isText())
			return vHeader;
		return "";
	}
	
	int getTextLength()
	{
		if(isText())
			return vTexts.length;
		return 0;
	}
	
	String getText(int index)
	{
		if(index>=0 && index<getTextLength() && isText())
			return vTexts[index];
		return "";
	}
	
	private int vHeight;
	
	int getHeight()
	{
		return vHeight;
	}
	
	private Box vBox;
	
	Box getBox()
	{
		if(isImg())
			return vBox;
		return new engine.Box(CENTER-vHeight/2, CENTER+vHeight/2, vPosition-vHeight/2, vPosition+vHeight/2);
	}
	
	private int vPosition;
	
	int getPosition()
	{
		return vPosition;
	}
	
	void setPosition(int position)
	{
		vPosition = position;
	}
	
	private int vTextMarginBot;
	private int vTextMarginTop;
	
	private int vImgMarginBot;
	private int vImgMarginTop;
	
	private void setMargins()
	{
		vTextMarginBot = Graphics.DEFAULT_WINDOW_HEIGHT+vHeight/2;
		vTextMarginTop = 0-vHeight;		
		vImgMarginBot = vTextMarginTop;
		vImgMarginTop = vTextMarginBot;
	}
	
	private boolean vAwayScreen;
		
	private int vDist;
	
	void up(int value)
	{
		if(isText())
		{
			vPosition-=value;
			if(vPosition<vTextMarginTop)
				vAwayScreen	= true;
		}
		else
		{			
			if(isImg())
				vBox.updatePosY(value);
			vPosition+=value;
			if(vPosition>vImgMarginTop)
				vAwayScreen	= true;
		}
		vDist+=value;
	}
	
	
	
	int getDist()
	{
		return vDist;
	}
	
	boolean isOutScreen()
	{		
		
		if(isText())
		{
			if(vPosition<vTextMarginTop || vPosition>vTextMarginBot)
				return true;
			return false;
		}
		else
		{
			if(vPosition<vImgMarginBot || vPosition>vImgMarginTop)
				return true;
			return false;
		}
	}
	
	boolean awayScreen()
	{
		if(vAwayScreen)
			return true;
		return false;
	}
	
	GameFont vHeaderFont;
	GameFont vTextFont;
	
	void draw(Function<Integer, GameTexture> textureGetter)
	{
		if(!isOutScreen())
		{
			if(isText())
			{
				int pos = vPosition - vHeight/2;
				Graphics.writeText(vHeader, vHeaderFont, CENTER, pos);
				for(int i=0; i<getTextLength(); i++)
				{
					if(i==0)
						pos += vHeaderFont.getFont().getSize()/2;
					else
						pos += vTextFont.getFont().getSize()/2;
					pos+=vTextFont.getFont().getSize();
					Graphics.writeText(getText(i), vTextFont, CENTER, pos);
				}
			}
			else if(isImg())
			{
				vBox.draw(textureGetter);
			}
		}
	}
	
	private int vOrigin;
	
	void gotoOrigin()
	{
		vAwayScreen = false;
		vDist=0;
		vPosition=vOrigin;
		if(isImg())
			vBox.setRelPos(CENTER, vOrigin);
	}
}
