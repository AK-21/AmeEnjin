package engine;

abstract class Ability
{
	static final int DEFAULT_SPEED = 1;
	static final int DEFAULT_SPACING = Series.DEF_IDEOGRAM_LINE_SPACING;
	
	static final int IMG_WIDTH = 300;
	static final int IMG_HEIGHTH = 150;
	static final int SPACE = 5;
	
	static final int IMG_X = Graphics.DEFAULT_WINDOW_WIDTH/2;
	static final int IMG_Y = Graphics.DEFAULT_WINDOW_HEIGHT/2 + IMG_HEIGHTH/2 + SPACE;
	
	Ability(int id, String name, Sound sfx, int imageTex, int power, int speed, int space, int seriesSize, int cost)
	{
		vID = id;
		vName = name;
		if(speed>0)
			vSpeed = speed;
		else
			vSpeed = 1;
		vSpace = space;
		if(seriesSize>0)
			vSeriesSize = seriesSize;
		else
			vSeriesSize=1;
		vPower = power;
		vCost = cost;
		vSfx = sfx;
		if(vSfx != null)
			vHasSfx = true;
		else
			vHasSfx = false;
		if(imageTex<0)
		{
			vHasImage=false;
			vImage = new Box(0,0,0,0);
		}
		else
		{
			vHasImage=true;
			vImage = new Box(IMG_X-IMG_WIDTH/2, IMG_X+IMG_WIDTH/2, IMG_Y-IMG_HEIGHTH/2, IMG_Y+IMG_HEIGHTH/2);
			vImage.setTexNr(imageTex);
		}
	}
	
	Ability(int id, String name, int power, int seriesSize, int cost)
	{
		this(id, name, null, -1, power, DEFAULT_SPEED, DEFAULT_SPACING, seriesSize, cost);
	}
	
	private int vID;
	private String vName;
	private NPC vTarget;
	private int vSeriesSize;
	private int vPower;
	private int vCost;
	private int vSpeed;
	private int vSpace;
	
	private Sound vSfx;
	private boolean vHasSfx;
	private Box vImage;
	private boolean vHasImage;
	
	boolean hasImage()
	{
		if(vHasImage)
			return true;
		return false;
	}
	
	boolean hasSfx()
	{
		if(vHasSfx)
			return true;
		return false;
	}
	
	int getID()
	{
		return vID;
	}
	
	Sound getSfx()
	{
		return vSfx;
	}
	
	void resetSfx()
	{
		vSfx.reset();
	}
	
	Box getImage()
	{
		return vImage;
	}
	
	String getName()
	{
		return vName;
	}
	
	int getSeriesSize()
	{
		return vSeriesSize;
	}
	
	int getCost()
	{
		return vCost;
	}
	
	int getSpeed()
	{
		return vSpeed;
	}
	
	int getSpace()
	{
		return vSpace;
	}
	
	protected NPC getTarget()
	{
		return vTarget;
	}	
	
	
	protected int countEffect(int points)
	{
		int value = 0;
		
		if(points == vSeriesSize)
			value = vPower + vPower/4;		
		else
			value = (vPower * points)/vSeriesSize;
		
		return value;
	}
	
	int getBonus(int points)
	{
		if(points == vSeriesSize)
			return vPower/4;
		return 0;
	}
	
	int getCounted(int points)
	{
		return countEffect(points);
	}
	
	void doAction(int points, NPC target)
	{
		vTarget = target;
	}
}
