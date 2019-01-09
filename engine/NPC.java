package engine;

import java.util.List;
import java.util.function.Function;
import java.util.ArrayList;
import static engine.Graphics.*;

class NPC
{
	static final int ID_HERO = 0;
	
	static final int STATUS_NEUTRAL = 0;
	static final int STATUS_SMILE = 1;
	static final int STATUS_HAPPY = 2;
	static final int STATUS_SAD = 3;
	static final int STATUS_CRY = 4;
	static final int STATUS_ANGRY = 5;
	static final int STATUS_FINE = 6;
	static final int STATUS_HURT = 7;
	static final int STATUS_AGONY = 8;
	static final int STATUS_GLORY = 9;	
	static final int STATUS_EVIL = 10;
	
	static final int STATUS_CUSTOM_1 = 11;
	static final int STATUS_CUSTOM_2 = 12;
	static final int STATUS_CUSTOM_3 = 13;
	static final int STATUS_CUSTOM_4 = 14;
	static final int STATUS_CUSTOM_5 = 15;
	
	static final int STATUS_ARROGANCE = 16;
	static final int STATUS_CORRUPT = 17;
	
	static final int STATUS_LIMIT = 18;	
	
	private static final int DEF_HP_MAX = 100;	
	
	static final boolean POS_LEFT = true;
	static final boolean POS_RIGHT = false;
	
	static final int DESELECT_ABILITY = -1;
	
	
	NPC(int id, String name, int hp, int portrait, int effigy, boolean position)
	{
		vID = id;
		vName = name;		
		vHPMax = hp;
		vHP = vHPMax;		
		
		vCurrentPos = position;
		
		setDefaultAppearance(portrait, effigy);
		defaultAppearanceToAll();
		vCurrentStatus = STATUS_NEUTRAL;
		vSelectedAbility = DESELECT_ABILITY;
	}
	
	NPC(int id, String name, int hp, int portrait, int effigy, List<Ability> abilities)
	{
		vID = id;
		vName = name;		
		vHPMax = hp;
		vHP = vHPMax;
		vAP = 0;
		
		if(id == ID_HERO)
			vCurrentPos = POS_LEFT;
		else
			vCurrentPos = POS_RIGHT;
		
		setDefaultAppearance(portrait, effigy);
		defaultAppearanceToAll();
		vCurrentStatus = STATUS_NEUTRAL;
		vAbilities = abilities;
		vSelectedAbility = DESELECT_ABILITY;
		
	}
	
	NPC(int id, String name, int portrait, int effigy, List<Ability> abilities)
	{
		this(id, name, DEF_HP_MAX, portrait, effigy, abilities);
	}
	
	
	private int vID;
	private String vName;
	
	private int vHPMax;
	private int vHP;
	
	private int vAP;
	
	private int vCurrentStatus;	
	
	private boolean vCurrentPos;	
	
	private List<Appearance> vAppearance = new ArrayList<Appearance>(STATUS_LIMIT);
	
	private List<Ability> vAbilities = new ArrayList<Ability>();
	private List<Ability> vAvailableAbilities = new ArrayList<Ability>();
	
	private int vSelectedAbility;	
	
	int getID()
	{
		return vID;
	}
	
	String getName()
	{
		return vName;
	}
	
	void hpUp(int value)
	{
		if(vHP + value < vHPMax)
			vHP += value;
		else
			vHP = vHPMax;
	}
	
	void hpDown(int value)
	{
		if(vHP - value > 0)
			vHP -= value;
		else
			vHP = 0;
	}
	
	private void setDefaultAppearance(int portrait, int effigy)
	{
		addAppearance(new Effigy(effigy, 400,400), new Portrait(portrait), STATUS_NEUTRAL);
		
	}
	
	void placeLeft()
	{
		if(vCurrentPos == POS_RIGHT)
		{			
			vCurrentPos = POS_LEFT;
		}
	}
	
	void placeRight()
	{
		if(vCurrentPos == POS_LEFT)
		{
			vCurrentPos = POS_RIGHT;
		}
	}
	
	boolean getSide()
	{
		return vCurrentPos;
	}
	
	private void defaultAppearanceToAll()
	{
		for(int i=1; i<STATUS_LIMIT; i++)
			vAppearance.add(i,vAppearance.get(STATUS_NEUTRAL));
	}
	
	private void addAppearance(Appearance a, int status)
	{
		vAppearance.add(status,a);
	}
	
	private void addAppearance(Effigy e, Portrait p, int status)
	{
		addAppearance(new Appearance(e, p), status);
	}
	
	protected void addAppearance(Effigy e, int portraitTex, int status)
	{
		addAppearance(e, new Portrait(portraitTex), status);
	}
	
	protected void addAppearance(int effigyTex, int portraitTex, int status)
	{
		addAppearance(new Effigy(effigyTex, 400,400), new Portrait(portraitTex), status);
	}
	
	protected void setAppearance(int effigyTex, int portraitTex, int status)
	{
		vAppearance.set(status, new Appearance(new Effigy(effigyTex, 400,400), new Portrait(portraitTex)));

	}
	
	void drawPortrait(Function<Integer, GameTexture> textureGetter)
	{
		vAppearance.get(vCurrentStatus).drawPortrait(textureGetter);
	}
	
	void drawPortraitInCenter(Function<Integer, GameTexture> textureGetter)
	{
		vAppearance.get(vCurrentStatus).drawPortrait(textureGetter);
	}
	
	void drawEffigy(Function<Integer, GameTexture> textureGetter)
	{
		vAppearance.get(vCurrentStatus).drawEffigy(textureGetter);
	}
	
	void setPortraitPos(int x, int y)
	{
		for(int i=0; i<STATUS_LIMIT; i++)
			vAppearance.get(i).setPortraitPos(x, y);
	}
	
	void drawPortraitCopyInCenter(Function<Integer, GameTexture> textureGetter)
	{
		Portrait copy = new Portrait(vAppearance.get(vCurrentStatus).getPortraitTex());
		copy.setPos(DEFAULT_WINDOW_WIDTH/2, DEFAULT_WINDOW_HEIGHT/2+Portrait.PORTRAIT_HEIGHT/2);
		copy.draw(textureGetter);
	}
	
	int getHP()
	{
		return vHP;
	}
	
	int getHPMax()
	{
		return vHPMax;
	}
	
	void apUp(int value)
	{
		vAP += value;
	}
	
	void apDown(int value)
	{
		vAP -= value;
	}
	
	int getAP()
	{
		return vAP;
	}	
	
	Ability getAbility(int id)
	{
		for(int i=0; i<vAbilities.size(); i++)
			if(vAbilities.get(i).getID()==id)
				return vAbilities.get(i);
		return null;
	}
	
	Ability getAbilityByIndex(int index)
	{
		if(index>=0 && index < vAbilities.size())
			return vAbilities.get(index);
		return null;
	}
	
	int getAbilitiesCount()
	{
		return vAbilities.size();
	}
	
	void resetAvailableAbilities()
	{
		vAvailableAbilities = new ArrayList<Ability>();
	}
	
	void addToAvailable(Ability ability)
	{
		vAvailableAbilities.add(ability);
	}
	
	Ability getAvailableAbilityByIndex(int index)
	{
		if(index>=0 && index < vAvailableAbilities.size())
			return vAvailableAbilities.get(index);
		return null;
	}
	
	int getAvailableAbilityIndex(Ability a)
	{
		for(int i=0; i<vAvailableAbilities.size(); i++)
			if(a.getID()==vAvailableAbilities.get(i).getID())
				return i;
		return -1;
	}
	
	int getAvaliableAbilitiesCount()
	{
		return vAvailableAbilities.size();
	}
	
	void deselectAbility()
	{
		vSelectedAbility = DESELECT_ABILITY;
	}
	
	void selectDefault()
	{
		vSelectedAbility = 0;
	}
	
	void selectNextAbility()
	{
		if(vSelectedAbility<getAvaliableAbilitiesCount()-1)
			vSelectedAbility++;
		else
			vSelectedAbility = 0;
	}
	
	void selectPrevAbility()
	{
		if(vSelectedAbility>0)
			vSelectedAbility--;
		else
			vSelectedAbility = getAvaliableAbilitiesCount()-1;
	}
	
	void selectAbility(int index)
	{
		if(index<0)			
			vSelectedAbility = 0;
		else if(index>=getAvaliableAbilitiesCount())
			vSelectedAbility = getAvaliableAbilitiesCount()-1;
		else
			vSelectedAbility = index;
	}
	
	int getSelectedIndex()
	{
		return vSelectedAbility;
	}
	
	void setStatus(int status)
	{
		if(vCurrentStatus!=status)
		{
			if(status<0)
				vCurrentStatus=0;
			else if(status>=STATUS_LIMIT)
				status=STATUS_LIMIT-1;
			else
				vCurrentStatus = status;
			Logger.log("Changed NPC("+vName+") status to "+vCurrentStatus);
		}
	}
	
	int getStatus()
	{
		return vCurrentStatus;
	}
	
	void setHpToMax()
	{
		vHP = vHPMax;
	}
	
	void setHpMax(int value)
	{
		if(value>0)
			vHP = vHPMax = value;
		else
			vHP = vHPMax = 1;
	}
	
	void resetAP()
	{
		vAP = 0;
	}
}
