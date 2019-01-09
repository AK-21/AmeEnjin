package engine;

import java.util.ArrayList;
import java.util.List;

class Scenario
{
	static final int DEF_INIT_VAL = 100;
	static final int ARR_NULL = -1;
	static final int EVT_START = 0;
	
	Scenario()
	{		
		init();
	}
	
	private void init()
	{
		initBFs();
		initNPCs();
		initEvts();
		initDias();
	}	
	
	/*------------------------------------
		EVENTS
	-----------------------------------*/
	
	private List<Runnable> vEvents;
	private int vCurrentEvt;
	
	private boolean vEvtFlag;
	
	private boolean vDeep;
	
	private void initEvts()
	{
		vEvents = new ArrayList<Runnable>(DEF_INIT_VAL);
		vCurrentEvt = ARR_NULL;
		vEvtFlag = false;
		vDeep = false;
		vOnceFlag = false;
	}
	
	void play() throws GameException
	{
		if((vCurrentEvt < EVT_START || vCurrentEvt >= vEvents.size()))
			throw new GameException("Incorrect event number: \""+vCurrentEvt+"\"");
		else
		{			
			if(vEvtFlag && !vDeep)
			{				
				next();
				vEvtFlag = false;
			}
			vEvents.get(vCurrentEvt).run();			
		}		
	}
	
	void goDeep()
	{
		vDeep = true;
	}
		
	void next()
	{
		vCurrentEvt++;
		if(vDiaReset)
			vDiaReset = false;
	}
	
	String getNext()
	{
		String evt = ""+(vCurrentEvt+1);
		if(vCurrentEvt+1==vEvents.size()-1)
			evt+= "(last)";
		return evt;
	}
	
	void jump(int eventIndex)
	{
		vCurrentEvt = eventIndex;
	}
	
	void flagUp()
	{		
		vEvtFlag = vOnceFlag = true;
	}
	
	void flagDown()
	{
		vEvtFlag = false;
	}
	
	void addEvt(Runnable evt)
	{
		vEvents.add(evt);
		if(vCurrentEvt == ARR_NULL)
			vCurrentEvt = EVT_START;
	}
	
	private boolean vOnceFlag;
	
	void once(Runnable r)
	{
		if(vOnceFlag)
		{
			r.run();
			vOnceFlag = false;
		}
	}
	
	int getCurrent()
	{
		return vCurrentEvt;
	}
	
	/*------------------------------------
		BATTLE ARENAS
	-----------------------------------*/
	
	private List<Battlefield> vBattlefields;
	
	private void initBFs()
	{
		vBattlefields = new ArrayList<Battlefield>(DEF_INIT_VAL);
	}
	
	void addBF(Battlefield bf)
	{
		vBattlefields.add(bf);
	}
	
	
	
	
	
	/*------------------------------------
		NPCS
	-----------------------------------*/
	
	private List<NPC> vNPCs;
	
	private void initNPCs()
	{
		vNPCs = new ArrayList<NPC>(DEF_INIT_VAL);
	}
	
	void addNPC(NPC npc)
	{
		vNPCs.add(npc);
	}
	
	int findNPC(int id) throws GameException
	{
		if(vNPCs.size()<1)
			throw new GameException("NPCs list is empty");
		for(int i = 0; i<vNPCs.size(); i++)
			if(vNPCs.get(i).getID() == id)
				return i;
		throw new GameException("NPC nr: "+id+" not exists");
	}
	
	/*------------------------------------
		DIALOGUES
	-----------------------------------*/

	private List<Dialogue> vDialogues;
	
	private Dialogue vCurrentDia;
	
	private void initDias()
	{
		vDialogues = new ArrayList<Dialogue>(DEF_INIT_VAL);		
	}
	
	void addDia(Dialogue dia)
	{
		vDialogues.add(dia);
	}
	
	void findDia(int id) throws GameException
	{
		if(vDialogues.size()<1)
			throw new GameException("Dialogues list is empty");
		for(int i = 0; i<vDialogues.size(); i++)
			if(vDialogues.get(i).getID() == id)
			{
				if(!vDiaReset)	
				{
					vDialogues.get(i).reset();
					vDiaReset = true;
				}
				vCurrentDia = vDialogues.get(i);
			}
	}
	
	private boolean vDiaReset = false;
	
	void playDia() throws GameException
	{
		if(!vDeep)
			goDeep();
		if(vEvtFlag)
		{
			vCurrentDia.flagUp();
			flagDown();
		}
		vCurrentDia.play();
	}	
		
	void nextLine()
	{		
		vCurrentDia.nextLine();
	}
}
