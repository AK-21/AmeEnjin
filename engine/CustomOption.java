package engine;

class CustomOption
{
	CustomOption(String label, String... options)
	{
		vLabel = label;
		vOptions = options;
		vIndex = -1;
		vValue = -1;
	}
	
	private String vLabel;
	private String[] vOptions;
	
	private int vIndex;
	private int vValue;
	
	void setIndex(int index)
	{
		vIndex = index;
	}
	
	int getIndex()
	{
		return vIndex;
	}
	
	void setValue(int value)
	{
		vValue = value;
	}
	
	int getValue()
	{
		return vValue;
	}
	
	String getLabel()
	{
		return vLabel;
	}
	
	String[] getOptions()
	{
		return vOptions;
	}
}
