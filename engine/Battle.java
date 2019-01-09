package engine;


import static engine.GameFont.DEFAULT_COLOR;
import static engine.Graphics.DEFAULT_WINDOW_HEIGHT;
import static engine.Graphics.DEFAULT_WINDOW_WIDTH;

import java.awt.Color;
import java.util.Random;
import java.util.function.Function;

class Battle
{	
	private static final boolean PLAYER = true;
	private static final boolean AI = false;
	
	private static final int DEFAULT_AP_UP_VALUE = 2;
	private static final int INCREMENT_AP_TURN = 5;
	private static final int INCREMENT_AP_VALUE_PLAYER = 1;
	private static final int INCREMENT_AP_VALUE_AI = 2;
	
	static final int BATTLE_NOT_RESOLVED = -1;
	static final int BATTLE_DRAW = 0;
	static final int BATTLE_WIN = 1;
	static final int BATTLE_LOSE = 2;
	
	static final int BATTLE_JUMP_DEFAULT = -1;
	
	private static final int BORDER_BOT = 0;
	private static final int BATTLE_TEXT_X = DEFAULT_WINDOW_WIDTH/2;
	private static final int BATTLE_TEXT_Y = DEFAULT_WINDOW_HEIGHT/2+10;
	private static final int PLAYERTURN_DISPLAY_TIME = 120;
	private static final int ABILITY_DISPLAY_TIME = 120;
	
	private static final int SERIES_SPEED_DEFAULT = 3;
	
	private static final boolean CHOOSE_HEAL = true;
	private static final boolean CHOOSE_ATK = false;

	
	private static Function<Integer, GameTexture> sTextureGetter;
	
	private int vTurnCounter;
	
	private static Color sFontColor = DEFAULT_COLOR;
	private static GameFont sFont = null;
	
	static void setTextureGetter(Function<Integer, GameTexture> textureGetter)
	{
		sTextureGetter = textureGetter;
	}
	
	static void setFontColor(Color color)
	{
		 sFontColor = color;
	}
	
	static void setBattleFont(GameFont font)
	{
		sFont = font;
	}
	
	Battle(NPC player1, NPC player2, Battlefield arena, IdeogramsArray ideograms, int scenarioJumpIndexVictory, int scenarioJumpIndexLose)
	{
		vSeriesSpeed = SERIES_SPEED_DEFAULT;
		vPlayer1 = player1;		
		vPlayer1.setHpToMax();
		vPlayer1.setStatus(NPC.STATUS_FINE);
		vPlayer1.resetAP();
		vPlayer2 = player2;
		vPlayer2.setHpToMax();
		vPlayer2.setStatus(NPC.STATUS_FINE);
		vPlayer2.resetAP();
		vArena = arena;
		if(vFont==null)
			if(sFont==null)
				vFont = new GameFont(vArena.getFont(), 30, sFontColor);
			else
				vFont = sFont;
		vCurrentPlayer = AI;		
		vIdeograms = ideograms;
		vBattleResult = BATTLE_NOT_RESOLVED;		
		vAPUpValuePlayer = DEFAULT_AP_UP_VALUE;	
		vAPUpValueAI = DEFAULT_AP_UP_VALUE;
		vDoActionOnce = false;
		vPlayerLose = false;
		vAILose = false;
		vSenarioWin = scenarioJumpIndexVictory;
		vSenarioLose = scenarioJumpIndexLose;
		vDiaTime = false;	
		vTurnCounter=0;
	}	
	
	
	
	private NPC vPlayer1;
	private NPC vPlayer2;	
	
	private int vSenarioWin;
	private int vSenarioLose;
	
	private Battlefield vArena;
	
	private int vPoints;
	private int vAPUpValuePlayer;
	private int vAPUpValueAI;
	
	private IdeogramsArray vIdeograms;
	private Series vCurrentSeries;
	
	private int vBattleResult;
	
	private boolean vAttackInProgress;
	
	private boolean vCurrentPlayer;
	
	private Ability vCurrentAbility;
	
	private GameFont vFont=null;
	
	private ActionPerTime vCurrentActionPerTime;
	private ActionPerTime vTurnPlayerDisplay;
	
	private String vInputText;		
	
	boolean isPlayerTurn()
	{
		if(vCurrentPlayer == PLAYER)
			return true;
		return false;
	}
	
	void resetInputText()
	{
		vInputText="";
	}
	
	void addToInputText(String text)
	{
		vInputText+=text;
	}
	
	void removeLastInputChar()
	{
		if(vInputText.length()>0)
			vInputText=vInputText.substring(0, vInputText.length()-1);
	}
	
	String getInputText()
	{
		return vInputText;
	}
	
	void nextTurn()
	{
		if(vCurrentPlayer == PLAYER)
		{
			vTurnCounter++;
			if(vTurnCounter%INCREMENT_AP_TURN==0)
			{
				vAPUpValuePlayer+=INCREMENT_AP_VALUE_PLAYER;
				vAPUpValueAI+=INCREMENT_AP_VALUE_AI;
				Logger.log("Increases AP growth to: "+vPlayer1.getName()+"="+vAPUpValuePlayer+", "+vPlayer2.getName()+"="+vAPUpValueAI+" per turn");
			}
			vCurrentPlayer = AI;
			vPlayer2.apUp(vAPUpValueAI);
			
		}
		else
		{
			vCurrentPlayer = PLAYER;
			vPlayer1.apUp(vAPUpValuePlayer);
		}
		resetInputText();
		vTurnOnceDone = false;
		vAbilityIsReady = false;
		vDoActionOnce = false;
		vSeriesActionTriggerOn = false;
		
	}
	
	void begin()
	{
		vPoints = 0;
		vPlayer2.apUp(vAPUpValueAI);
		vAttackInProgress = false;
		vTurnOnceDone = false;
		vAbilityIsReady = false;
		vSeriesActionTriggerOn = false;
		vPointsDisplayTime = false;
		vTurnCounter=0;
		vTurnPlayerDisplay = new ActionPerTime(0, ()->{});
	}
	
	void run() throws GameException
	{
		if(isSeriesInProgress())
			vArena.draw(sTextureGetter, ()->arenaInternalFunc());
		else
			vArena.drawInsideOnTop(sTextureGetter, ()->arenaInternalFunc());
	}
	
	private Runnable vDisplay;
	
	private boolean vKeyLocker = true;
	boolean isKeyboardLocked()
	{
		if(vKeyLocker)
			return true;
		return false;
	}
	
	void lockKeyboard()
	{
		if(!isKeyboardLocked())
			vKeyLocker = true;
	}
	
	void unlockKeyboard()
	{
		if(isKeyboardLocked())
			vKeyLocker = false;
	}
	
	private void turn()
	{		
		if(!vTurnOnceDone)
		{
			vPoints = 0;
			
			if(vCurrentPlayer == AI)
			{				
				setTurnDisplayAI();
			}
			else
			{				
				setTurnDisplayPlayer();
			}
		}
		
		vTurnOnceDone = true;
		vTurnPlayerDisplay.run();
		if(vTurnPlayerDisplay.isCompleted())
		{			
			
			if(!vAbilityIsReady)
			{
				if(vCurrentPlayer == AI)
				{				
					turnAI();
				}
				else
				{				
					turnPlayer();
				}
					
				turnAbility();			
			}
			else
			{
				vCurrentActionPerTime.run();
				if(vCurrentActionPerTime.isCompleted())
					vAttackInProgress = true;
			}
		}
	}
	
	private void setTurnDisplayPlayer()
	{
		Logger.log("Player: "+vPlayer1.getName()+", Turn: "+(vTurnCounter+1));
		vTurnPlayerDisplay = new ActionPerTime
		(			
			PLAYERTURN_DISPLAY_TIME,
			() ->{lockKeyboard(); displayPlayerTurnInfo(vPlayer1);}					
		);
	}
	
	private void setTurnDisplayAI()
	{
		Logger.log("Player: "+vPlayer2.getName()+", Turn: "+(vTurnCounter+1));
		vTurnPlayerDisplay = new ActionPerTime
		(
			PLAYERTURN_DISPLAY_TIME,
			() ->{lockKeyboard(); displayPlayerTurnInfo(vPlayer2);}
		);
	}
	
	int getJumpIndex()
	{
		if(getResult() == BATTLE_WIN)
			return vSenarioWin;
		if(getResult() == BATTLE_LOSE)
			return vSenarioLose;
		return BATTLE_JUMP_DEFAULT;
	}
	
	private boolean vAbilityIsReady;
	
	private void turnPlayer()
	{		
		chooseAbility();		
	}
	
	private void turnAI()
	{										
		getAIAbility();			
	}
	
	private void turnAbility()
	{
		vDisplay = ()->
		{
			if(vCurrentAbility.hasSfx())
				vCurrentAbility.getSfx().play();
			if(vCurrentAbility.hasImage())
				vCurrentAbility.getImage().draw(sTextureGetter);
			Graphics.writeText(vCurrentAbility.getName(), vFont, BATTLE_TEXT_X, BATTLE_TEXT_Y);
		};
		vCurrentActionPerTime = new ActionPerTime(ABILITY_DISPLAY_TIME, vDisplay);
		generateRandomSeries(vCurrentAbility.getSeriesSize(), vCurrentAbility.getSpace());
		vSeriesSpeed = vCurrentAbility.getSpeed();
	}
	
	private void displayPlayerTurnInfo(NPC player)
	{		
		player.drawPortraitCopyInCenter(sTextureGetter);
		Graphics.writeText(player.getName(), vFont, BATTLE_TEXT_X, BATTLE_TEXT_Y);
	}
	
	private boolean vTurnOnceDone = false;
	
	private boolean vPlayerLose = false;
	private boolean vAILose = false;
	
	void EvtVictory(Runnable evt)
	{
		if(vAILose)
		{
			evt.run();
			vBattleResult = BATTLE_WIN;
		}	
	}
	
	void EvtLose(Runnable evt)
	{
		if(vPlayerLose)
		{
			evt.run();
			vBattleResult = BATTLE_LOSE;
		}
	}
	
	private boolean vDiaTime;
	
	boolean isDiaTime()
	{
		if(vDiaTime)
			return true;
		return false;
	}
	
	void setDiaTime()
	{
		vDiaTime = true;
	}
	
	void unsetDiaTime()
	{
		vDiaTime = false;
	}
	
	private void checkBattleStatus()
	{
		if(vPlayer1.getHP()<=0)
		{
			vPlayerLose = true;
			Logger.log(vPlayer2.getName()+"(Enemy) wins");
		}
		else if(vPlayer2.getHP()<=0)
		{
			vAILose = true;			
			Logger.log(vPlayer1.getName()+"(Player) wins");
		}
	}
	
	int getResult()
	{				
		return vBattleResult;		
	}
	
	private void getAIAbility()
	{
		try
		{
			generateTempAbilityArray();
			selectAbilityAI();
		}
		catch(GameException e)
		{
			ErrorReporter.display(e);
			System.exit(1);
		}
		
		vPlayer2.apDown(vCurrentAbility.getCost());	
		vAbilityIsReady = true;
		Logger.log(vPlayer2.getName()+"(Enemy) uses ability: "+vCurrentAbility.getName());
	}
	
	private void chooseAbility()
	{
		if(!generatedArray)
		{
			try
			{
				generateTempAbilityArray();
			}
			catch(GameException e)
			{
				ErrorReporter.display(e);
				System.exit(1);
			}
			vPlayer1.selectDefault();
			generatedArray = true;
		}
		selectAbilityPlayer();
	}
	
	private boolean generatedArray = false;
	
	void choseSelectedAbility()
	{
		vPlayer1.apDown(vCurrentAbility.getCost());
		vAbilityIsReady = true;
		generatedArray = false;
		Logger.log(vPlayer1.getName()+"(Player) uses ability: "+vCurrentAbility.getName());
	}
	
	/*------------------------------------
		DRAW RANDOM IDEOGRAMS
	-----------------------------------*/
	
	private void generateRandomSeries(int size, int space)
	{		
		vCurrentSeries = new RandomSeries(size, space, vIdeograms);
	}
	
	private void arenaInternalFunc()
	{
		if(vAttackInProgress)
		{			
			seriesDisplay(vCurrentSeries);
			Graphics.writeText( vInputText, vFont, DEFAULT_WINDOW_WIDTH/2, engine.Battlefield.DEF_YPOS_TEXT_INPUT);
		}
		else
		{
			if(vBattleResult == BATTLE_NOT_RESOLVED)
				turn();
		}
	}
	
	boolean isAttackInProgress()
	{
		if(vAttackInProgress)
			return true;
		return false;
	}
	
	/*------------------------------------
		SERIES DISPLAY
	-----------------------------------*/

	private int vSeriesSpeed;
	
	private void seriesDisplay(Series series)
	{
		int extLoopCounter = 0;
		int intLoopCounter = 0;	
		
		while(extLoopCounter<series.getElemCounter())
		{
			while(intLoopCounter<series.getByIndex(extLoopCounter).getElemCounter())
			{					
				if(series.getByIndex(extLoopCounter).getItemByIndex(intLoopCounter).isVisible())
					(series.getByIndex(extLoopCounter).getItemByIndex(intLoopCounter)).draw(sTextureGetter);
				series.getByIndex(extLoopCounter).getItemByIndex(intLoopCounter).down(vSeriesSpeed);
				
				if(series.getByIndex(extLoopCounter).getItemByIndex(intLoopCounter).outOfLineTop(BORDER_BOT))
				{
					vPoints++;
					
					series.getByIndex(extLoopCounter).remove(intLoopCounter);
					series.decIdeogramsCounter();
					if(series.getIdeogramsCounter()==0)
					{						
						
						vSeriesActionTriggerOn = true;
					}
						
				}
				intLoopCounter++;
			}
			extLoopCounter++;
			intLoopCounter = 0;			
		}		
		
		if(vSeriesActionTriggerOn)
		{
			doAction();
		}
	}
	
	void seriesTrigger()
	{
		if(vCurrentSeries.getIdeogramsCounter()==0)
		{						
			
			vSeriesActionTriggerOn = true;
		}		
	}
	
	boolean isSeriesInProgress()
	{
		if(!vSeriesActionTriggerOn)
		{
			return true;
		}
		return false;
	}
	
	private boolean vDoActionOnce = false;
	private boolean vSeriesActionTriggerOn = false;

	
	private void generateTempAbilityArray() throws GameException
	{
		
		int size;
		
		if(vCurrentPlayer == PLAYER)
		{
			vPlayer1.resetAvailableAbilities();
			generateTempAbilityArrayPlayer();
			size = vPlayer1.getAvaliableAbilitiesCount();
		}
		else
		{
			vPlayer2.resetAvailableAbilities();
			generateTempAbilityArrayAI();
			size = vPlayer2.getAvaliableAbilitiesCount();
		}
		
		if(size<1)
			throw new GameException("No available abilities!");
		
	}
	
	private void generateTempAbilityArrayPlayer()
	{
		Ability a;
		for(int i=0; i<vPlayer1.getAbilitiesCount(); i++)
		{
			a = vPlayer1.getAbilityByIndex(i);
			if(vPlayer1.getAP()>=a.getCost())
				vPlayer1.addToAvailable(a);
		}
		
		a = null;
	}
	
	private void selectAbilityPlayer()
	{
		unlockKeyboard();
		int selected = vPlayer1.getSelectedIndex();
		vCurrentAbility = vPlayer1.getAvailableAbilityByIndex(selected);
		if(vCurrentAbility.hasSfx())
			vCurrentAbility.resetSfx();
	}
	
	void nextAbility()
	{
		vPlayer1.selectNextAbility();
	}
	
	void prevAbility()
	{
		vPlayer1.selectPrevAbility();
	}
	
	private void generateTempAbilityArrayAI()
	{
		Ability a;
		for(int i=0; i<vPlayer2.getAbilitiesCount(); i++)
		{
			a = vPlayer2.getAbilityByIndex(i);
			if(vPlayer2.getAP()>=a.getCost())
				vPlayer2.addToAvailable(a);
		}
		a=null;
	}
	
	private void selectAbilityAI() throws GameException
	{
		Random r = new Random();
		
		int[] hTab;
		int[] aTab;
		
		int count = vPlayer2.getAvaliableAbilitiesCount();
		
		int hCount=0;
		int aCount=0;
		
		for(int i=0; i<count; i++)
			if(vPlayer2.getAvailableAbilityByIndex(i) instanceof Heal)
				hCount++;
			else if(vPlayer2.getAvailableAbilityByIndex(i) instanceof Attack)
				aCount++;
		
		if(hCount==0 && aCount==0)
			throw new GameException(vPlayer2.getName()+" has not any available abilities!");
		else if(hCount==0 || aCount==0)
		{
			int selected = r.nextInt(vPlayer2.getAvaliableAbilitiesCount());
			vPlayer2.selectAbility(selected);
			vCurrentAbility = vPlayer2.getAvailableAbilityByIndex(selected);
		}
		else
		{			
			boolean choice = getAIChoice();
			if(choice == CHOOSE_HEAL)
			{
				hTab = new int[hCount];
				int index=0;
				
				
				for(int i=0; i<count && index<=hCount; i++)					
				{
					if(vPlayer2.getAvailableAbilityByIndex(i) instanceof Heal)
					{
						hTab[index]=i;
						index++;						
					}					
				}
				
				int chosen = r.nextInt(hCount);
				chosen = hTab[chosen];
				
				vPlayer2.selectAbility(chosen);				
				vCurrentAbility = vPlayer2.getAvailableAbilityByIndex(chosen);
			}
			else
			{
				aTab = new int[aCount];
				int index=0;
				
				for(int i=0; i<count && index<=aCount; i++)					
				{
					if(vPlayer2.getAvailableAbilityByIndex(i) instanceof Attack)
					{
						aTab[index]=i;
						index++;						
					}					
				}
				
				int chosen = r.nextInt(aCount);
				chosen = aTab[chosen];
				
				vPlayer2.selectAbility(chosen);				
				vCurrentAbility = vPlayer2.getAvailableAbilityByIndex(chosen);
			}
		}
	}
	
	private boolean getAIChoice()
	{
		int chance = 0;
		Random r = new Random();
		
		chance = getAIHealChooseChance();
		int choseArrSize=4;
		boolean[] choseArray = new boolean[choseArrSize];
		
		for(int i=0; i<choseArrSize; i++)
		{
			if(i<chance)
				choseArray[i]=CHOOSE_HEAL;
			else
				choseArray[i]=CHOOSE_ATK;
		}		
		int chosen = r.nextInt(choseArrSize);
		
		return choseArray[chosen];
	}
	
	private int getAIHealChooseChance()
	{
		int max = vPlayer2.getHPMax();
		int heroMax =  vPlayer1.getHPMax();
		
		if(vPlayer2.getHP()==max)
			return 0;
		
		else if(vPlayer2.getHP()>calculate(max, 75))
			if(vPlayer1.getHP() < calculate(heroMax, 25))
				return 0;
			else
				return 1;
		else if(vPlayer2.getHP()>calculate(max, 50))
				if(vPlayer1.getHP() < calculate(heroMax, 25))
					return 1;
				else
					return 2;
		else if(vPlayer2.getHP()>calculate(max, 25))
			if(vPlayer1.getHP() < calculate(heroMax, 25))
				return 2;
			else
				return 3;
		else if(vPlayer2.getHP()>=calculate(max, 10))
			if(vPlayer1.getHP() < calculate(heroMax, 10))
				return 2;
			else
				return 4;
		return 2;
	}
		
	private NPC vTarget;
	private boolean vPointsDisplayTime;
	boolean isPointsDisplayTime()
	{
		if(vPointsDisplayTime)
			return true;
		return false;
	}
	
	private boolean isAttack()
	{
		if(vCurrentAbility instanceof Attack)
			return true;
		return false;
	}
	
	private void doAction()
	{		
		
		if(!vDoActionOnce)
		{
			vPointsDisplayTime = true;
			resetInputText();
			if(Logger.isActive())
			{
				String perfect="";
				if(vPoints==vCurrentAbility.getSeriesSize() && vCurrentPlayer == AI)
					perfect=(" (Enemy gains +25% Perfect Bonus)");
				if(vPoints==0 && vCurrentPlayer == PLAYER)
					perfect=(" (Player gains +25% Perfect Bonus)");
				Logger.log("Score: "+(vCurrentAbility.getSeriesSize()-vPoints)+"/"+vCurrentAbility.getSeriesSize()+perfect);
			}
			
			if(vCurrentPlayer == PLAYER)
			{
				if(isAttack())
					vTarget = vPlayer2;
				else
					vTarget = vPlayer1;
				vPoints = vCurrentAbility.getSeriesSize() - vPoints;				
			}
			else
			{			
				if(isAttack())
					vTarget = vPlayer1;
				else
					vTarget = vPlayer2;
			}		
			
			int val = vPoints;
			
			vCurrentActionPerTime = new ActionPerTime
			(
				ABILITY_DISPLAY_TIME,				
				() ->
				{
					Graphics.writeText(vTarget.getName()+":", vFont, BATTLE_TEXT_X, BATTLE_TEXT_Y-(vFont.getFont().getSize()+vFont.getFont().getSize()/4));
					Graphics.writeText(getPointDisplay(val), vFont, BATTLE_TEXT_X, BATTLE_TEXT_Y);
				}	
			);
			
			vDoActionOnce = true;
		}
				
		vCurrentActionPerTime.run();
		
		if(vCurrentActionPerTime.isCompleted())
		{
			vCurrentAbility.doAction(vPoints, vTarget);
			setTargetHealthStatus(vTarget);
			vPointsDisplayTime = false;
			vAttackInProgress = false;
			checkBattleStatus();

			
			EvtLose(()->{/*DO NOTHING*/});
			EvtVictory(()->{/*DO NOTHING*/});
			
			nextTurn();
		}
	}
	
	private String getPointDisplay(int points)
	{
		String s="";
		if(isAttack())
			s+="-";
		else
			s+="+";
		s+=vCurrentAbility.getCounted(points)+" HP";
		return s;
	}
	
	private void setTargetHealthStatus(NPC target)
	{
		int hpMax = target.getHPMax();
		if(target.getHP()<calculate(hpMax, 66))
			if(target.getHP()<calculate(hpMax, 33))
				target.setStatus(NPC.STATUS_AGONY);
			else
				target.setStatus(NPC.STATUS_HURT);
		else
			target.setStatus(NPC.STATUS_FINE);
		
	}
	
	private int calculate(int value, int perc)
	{
		return (value*perc)/100;
	}
	
	boolean verify()
	{
		return vCurrentSeries.findAndRemoveFirst(vInputText);
	}
	
	void player1Damage(int damage)
	{
		vPlayer1.hpDown(damage);
	}
	
	void player2Damage(int damage)
	{
		vPlayer2.hpDown(damage);
	}	
}
