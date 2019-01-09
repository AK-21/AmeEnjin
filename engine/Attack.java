package engine;

class Attack extends Ability
{
	static final int ID_ATK_DEFAULT = -1;
	static final String NAME_ATK_DEFAULT = "ATK";
	static final int PWR_ATK_DEFAULT = 100;
	static final int SIZE_ATK_DEFAULT = 10;
	static final int COST_ATK_DEFAULT = 1;
	
	
	
	static Attack defaultATK()
	{
		return new Attack(ID_ATK_DEFAULT, NAME_ATK_DEFAULT, PWR_ATK_DEFAULT, SIZE_ATK_DEFAULT, COST_ATK_DEFAULT);
	}	
	
	Attack(int id, String name, int power, int seriesSize, int cost)
	{
		super(id, name, power, seriesSize, cost);
	}
	
	Attack(int id, String name, Sound sfx, int imageTex, int power, int speed, int space, int seriesSize, int cost)
	{
		super(id, name, sfx, imageTex, power, speed, space, seriesSize, cost);
	}
	
	@Override
	void doAction(int points, NPC target)
	{
		super.doAction(points, target);
		int effect = countEffect(points);
		getTarget().hpDown(effect);
		if(Logger.isActive())
			Logger.log(target.getName()+" takes "+effect+"HP damage");
	}
}