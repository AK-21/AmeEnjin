package engine;

class Heal extends Ability
{
	Heal(int id, String name, int power, int seriesSize, int cost)
	{
		super(id, name, power, seriesSize, cost);
	}
	
	Heal(int id, String name, Sound sfx, int imageTex, int power, int speed, int space, int seriesSize, int cost)
	{
		super(id, name, sfx, imageTex, power, speed, space, seriesSize, cost);
	}
		
	@Override
	void doAction(int points, NPC target)
	{
		super.doAction(points, target);
		int effect = countEffect(points);
		getTarget().hpUp(effect);
		if(Logger.isActive())
			Logger.log(target.getName()+" heals self by "+effect+"HP");
	}
}
