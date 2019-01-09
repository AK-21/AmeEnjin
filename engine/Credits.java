package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class Credits
{
	private Credits(boolean useBackground, int background)
	{
		vUseBackground = useBackground;
		vBackground = background;
		vElements = new ArrayList<CreditsElement>();
		vSpeed = 1;
		vLast = 0;
		vMargin = 50;
	}
	
	Credits()
	{
		this(false, -1);
	}
	
	Credits(int background)
	{
		this(true, background);
	}	
	
	private boolean vUseBackground;
	private int vBackground;
	
	boolean hasBackground()
	{
		if(vUseBackground)
			return true;
		return false;
	}
	
	int getBackground()
	{
		return vBackground;
	}
	
	private List<CreditsElement> vElements;
	
	void add(CreditsElement element)
	{
		vElements.add(element);
	}
	
	boolean isCompleted()
	{
		if(vElements.get(vElements.size()-1).awayScreen())
			return true;
		return false;
	}
	
	
	
	CreditsElement getElement(int index)
	{
		return vElements.get(index);
	}
	
	int getElementsCount()
	{
		return vElements.size();
	}
	
	int vLast;
	
	int vMargin;
	
	void setMargin(int margin)
	{
		vMargin = margin;
	}
	
	void up(int value)
	{
		for(int i=0; i<vLast+1 && i<vElements.size(); i++)
		{			
			vElements.get(i).up(value);
			if(vLast<vElements.size())
				if(vElements.get(vLast).getDist()>(vMargin+vElements.get(vLast).getHeight()))
					vLast++;			
		}
	}
	
	void draw(Function<Integer, GameTexture> textureGetter)
	{
		for(int i=0; i<vElements.size(); i++)
			vElements.get(i).draw(textureGetter);
	}
	
	private int vSpeed;
	
	int getSpeed()
	{
		return vSpeed;
	}
	
	void setSpeed(int speed)
	{
		vSpeed = speed;
	}
	
	void gotoOrigin()
	{
		vLast = 0;
		for(int i=0; i<getElementsCount(); i++)
			getElement(i).gotoOrigin();
	}
}
