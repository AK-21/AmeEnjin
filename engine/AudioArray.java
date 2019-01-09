package engine;

class AudioArray extends GameArray
{
	static final int AUDIO_ARRAY_SIZE=100;
	
	AudioArray(int size)
	{
		setSize(size);
		vArray=new AudioResource[size];		
	}
	
	private AudioResource[] vArray;
	
	
	
	void print()
	{
		System.out.println(STR_DIV);
		if(!isEmpty())
			for(int counter=0; counter<getElemCounter();counter++)
				System.out.println(counter+". Nr "+vArray[counter].getID()+" - "+vArray[counter].getFileName());
		else
			System.out.println(LABEL_ARR_EMPTY);
	}
	
	boolean add(AudioResource audio)
	{
		if(!isFreeSpace())
			return false;
		vArray[getElemCounter()]=audio;
		if(!incElemCounter())
			return false;
		return true;
	}
	
	AudioResource getItemByIndex(int index)
	{
		if(!isEmpty())
			if(index<getSize()&&index>=0)
				return vArray[index];
		return null;
	}
	AudioResource getItemByID(int id)
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
}