package engine;

class IdeogramsArray extends GameArray
{
	static final int ALL_IDEOGRAMS=71;
	
	IdeogramsArray(int size)
	{
		setSize(size);
		vArray=new Ideogram[size];
	}
	
	private Ideogram[] vArray;
	
	void print()
	{
		System.out.println(STR_DIV);
		if(!isEmpty())
			for(int counter=0; counter<getElemCounter();counter++)
				System.out.println(counter+". "+vArray[counter].getTranscription());
		else
			System.out.println(LABEL_ARR_EMPTY);
	}
	
	boolean add(Ideogram item)
	{
		if(!isFreeSpace())
			return false;
		vArray[getElemCounter()]=item;
		if(!incElemCounter())
			return false;	
		return true;
	}
	
	Ideogram getItemByIndex(int index)
	{
		if(!isEmpty())
			if(index<getSize()&&index>=0)
				return vArray[index];
		return null;
	}
	
	Ideogram getItemByID(int id)
	{
		if(!isEmpty())
			for(int counter=0; counter<getElemCounter();counter++)
				if(vArray[counter].getID()==id)
					return vArray[counter];
		return null;
	}
	
	int getIDByIndex(int index)
	{
		if(!isEmpty())
			if(index<getSize()&&index>=0)
				return vArray[index].getID();
		return VAL_WRONG;
	}
	
	int getIndexByID(int id)
	{
		if(!isEmpty())
			for(int counter=0; counter<getElemCounter();counter++)
				if(vArray[counter].getID()==id)
					return counter;
		return VAL_WRONG;
	}
	
	int getIndexByTransciption(String transcription)
	{
		if(!isEmpty())
			for(int counter=0; counter<getElemCounter();counter++)
				if(vArray[counter].getTranscription().equals(transcription))
					return counter;
		return VAL_WRONG;
	}
	
	boolean remove(int index)
	{
		if(decElemCounter())
		{
			int valF=getElemCounter();
			if(!isEmpty()&&index<=valF&&index>=0)
			{
				int i=index;			
				while(i<valF)
				{
					vArray[i]=vArray[i+1];
					i++;
				}				
				return true;
			}
		}
		return false;
	}
	
	void setVerticalPositionForAll(int value)
	{
		if(!isEmpty())
			for(int counter=0; counter<getElemCounter();counter++)
				vArray[counter].updatePosY(value);
	}
}
