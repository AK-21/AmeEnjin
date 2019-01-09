package engine;

abstract class GameArray
{	
	static final int VAL_WRONG=-1;
	
	static final String STR_DIV="------------------------";
	static final String LABEL_ARR_EMPTY="<<EMPTY>>";
	
	private int vSize=0;
	private int vElemCounter=0;
	
	void setSize(int size)
	{
		if(size<0)
			size=0;
		vSize=size;
	}
	
	int getSize()
	{
		return vSize;
	}
	
	boolean incElemCounter()
	{
		if(isFreeSpace())
		{
			vElemCounter++;
			return true;
		}
		return false;
	}
	
	boolean decElemCounter()
	{
		if(!isEmpty())
		{
			vElemCounter--;
			return true;
		}
		return false;
	}
	
	int getElemCounter()
	{
		return vElemCounter;
	}
	
	boolean isFreeSpace()
	{
		if(vElemCounter<vSize)
			return true;
		return false;
	}
	
	boolean isEmpty()
	{
		if(vElemCounter>0)
			return false;
		return true;
	}
}
