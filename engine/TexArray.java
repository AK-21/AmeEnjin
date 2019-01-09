package engine;

class TexArray extends GameArray
{
	static final int TEX_ARRAY_SIZE=100;	
	
	TexArray(int size)
	{
		setSize(size);
		vArray=new GameTexture[size];
	}
	
	private GameTexture[] vArray;
	
	void print()
	{
		System.out.println(STR_DIV);
		if(!isEmpty())
			for(int counter=0; counter<getElemCounter();counter++)
				System.out.println(counter+". Nr "+vArray[counter].getID()+" - "+vArray[counter].getName());
		else
			System.out.println(LABEL_ARR_EMPTY);
	}
	
	boolean add(GameTexture tex)
	{
		if(!isFreeSpace())
			return false;
		vArray[getElemCounter()]=tex;
		if(!incElemCounter())
			return false;
		return true;
	}
	
	GameTexture getItemByIndex(int index) throws GameException
	{
		if(!isEmpty())
			if(index<getSize()&&index>=0)
				return vArray[index];
		throw new GameException("Cannot find texture (index="+index+")");
	}
	GameTexture getItemByID(int id) throws GameException
	{
		if(!isEmpty())
			for(int counter=0; counter<getElemCounter();counter++)
				if(vArray[counter].getID()==id)
					return vArray[counter];
		throw new GameException("Cannot find texture (id="+id+")");
	}
	
	GameTexture getItemByName(String name) throws GameException
	{
		if(!isEmpty())
			for(int counter=0; counter<getElemCounter();counter++)
				if(vArray[counter].getName().equals(name))
					return vArray[counter];
		throw new GameException("Cannot find texture (name="+name+")");
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
