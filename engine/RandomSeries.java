package engine;

import static engine.Ideogram.POS_IDEOGRAM_MAX;

import java.util.Random;

class RandomSeries extends Series
{
	private RandomSeries(int size, IdeogramsArray base)
	{
		super(size, base);
		vCount = base.getSize();
		generateRandom(size);
	}
	
	RandomSeries(int size, int spacing, IdeogramsArray base)
	{
		super(size, spacing, base);
		vCount = base.getSize();
		generateRandom(size);
	}
	
	private int vCount;
	
	private void generateRandom(int size)
	{
		Random rand = new Random();
		
		int maxSize=size;
		int tempArray[] = new int[maxSize];
		int tempArrSize = 0;
		int tmp = 0;;
		
		for(tempArrSize = 0; (tempArrSize<maxSize)	&& size>0; tempArrSize++)
		{
			if(size>=POS_IDEOGRAM_MAX)
				tempArray[tempArrSize]=rand.nextInt(POS_IDEOGRAM_MAX)+1;
			else			
				tempArray[tempArrSize]=rand.nextInt(size)+1;
			size-=tempArray[tempArrSize];			
		}
		
		maxSize=tempArrSize;
		
		tempArrSize=0;
		
		IdeogramsArray tempIdeogramsArray;
	
		boolean positions[] = new boolean[]{false,false,false,false};
		int posNr = 0;
		boolean posFound = false;

		while(tempArrSize<maxSize)
		{
			tempIdeogramsArray=new IdeogramsArray(tempArray[tempArrSize]);
			while(tmp<tempIdeogramsArray.getSize())
			{
				tempIdeogramsArray.add(super.createIdeogramFromExisting(rand.nextInt(vCount)));				
				
				
				while(!posFound)
				{
					posNr=rand.nextInt(POS_IDEOGRAM_MAX)+1;
					posFound=true;
						for(int i=0; i<tmp; i++)
							if(positions[posNr-1])
								posFound=false;
					if(posFound)
						positions[posNr-1]=true;
				}
				posFound=false;
				tempIdeogramsArray.getItemByIndex(tmp).putOnPosition((byte)(posNr));
				tmp++;
			}
			tmp=0;
			
			add(tempIdeogramsArray);
			
			for(int i=0; i<positions.length; i++)
				positions[i]=false;
			
			tempArrSize++;
		}		
	}
}
