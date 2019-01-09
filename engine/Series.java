package engine;

import static engine.Ideogram.IDEOGRAM_HEIGHT;

class Series extends GameArray
{		
	static final int DEF_IDEOGRAM_LINE_SPACING=200;
	
	Series(int size, int spacing, IdeogramsArray base)
	{
		setSize(size);
		vIdeogramsCounter = size;
		vArray=new IdeogramsArray[size];
		vLineSpacing=spacing;
		vBase = base;
	}
	
	Series(int size, IdeogramsArray base)
	{
		this(size, DEF_IDEOGRAM_LINE_SPACING, base);
	}
		
	
	private int vIdeogramsCounter;
	private IdeogramsArray[] vArray;
	private int vLineSpacing;
	private IdeogramsArray vBase;
	
	void print()
	{
		if(!isEmpty())
			for(int counter=0; counter<getElemCounter();counter++)
				vArray[counter].print();
		else
			System.out.println(LABEL_ARR_EMPTY);	
	}
	
	boolean add(IdeogramsArray array)
	{
		if(!isFreeSpace())
			return false;
		vArray[getElemCounter()]=array;
		vArray[getElemCounter()].setVerticalPositionForAll((getElemCounter())*(IDEOGRAM_HEIGHT+vLineSpacing));
		if(!incElemCounter())
			return false;	
		return true;
	}
	
	IdeogramsArray getByIndex(int index)
	{
		if(!isEmpty())
			if(index<getSize()&&index>=0)
				return vArray[index];
		return null;
	}
	
	boolean findAndRemoveFirst(String transcription)
	{
		int tmpIndex=VAL_WRONG;
		if(!isEmpty())
			for(int counter=0; counter<getElemCounter();counter++)
			{
				tmpIndex=vArray[counter].getIndexByTransciption(transcription);		
				if(tmpIndex!=VAL_WRONG)
				{
					if(vArray[counter].getItemByIndex(tmpIndex).isVisible())
					{
						vArray[counter].remove(tmpIndex);
						vIdeogramsCounter--;
						return true;
					}
				}
			}
		return false;
	}
	
	Ideogram createIdeogramFromExisting(int index)
	{
		return new Ideogram(vBase.getItemByIndex(index));
	}	
	
	int getIdeogramsCounter()
	{
		return vIdeogramsCounter;
	}
	
	void decIdeogramsCounter()
	{
		vIdeogramsCounter--;
	}
}

