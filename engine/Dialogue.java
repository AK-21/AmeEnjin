package engine;

import static engine.Graphics.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class Dialogue
{
	
	static final int DIA_BOX_WIDTH = DEFAULT_WINDOW_WIDTH;
	static final int DIA_BOX_HEIGHT = 250;	
	
	private static GameFont gDiaFnt;
	private static Box gBox;
	
	static void setDiaFnt(GameFont fnt)
	{
		gDiaFnt = fnt;
	}
	
	static void generateDiaBox(int texNr)
	{
		gBox = new Box(0, DIA_BOX_WIDTH, 0, DIA_BOX_HEIGHT);
		gBox.setTexNr(texNr);
	}
	
	static GameFont getDiaFnt()
	{
		return gDiaFnt;		
	}
	
	Dialogue(int id, int backgroundID)
	{
		vID = id;
		vLines = new ArrayList<DialogueLine>();
		vDiaEvents = new ArrayList<Runnable>();
		vCurrentLine = -1;
		vCurrentEvt = -1;
		vEvtFlag = false;
		vOnce = false;
		vBackgroundID = backgroundID;
	}
	
	private int vBackgroundID;
	
	private int vID;
	
	private List<DialogueLine>vLines;
	private int vCurrentLine;
	
	private List<Runnable> vDiaEvents;
	private int vCurrentEvt;
	
	private boolean vEvtFlag;
	
	int getBackgroundID()
	{
		return vBackgroundID;
	}
	
	void reset()
	{
		vCurrentLine = 0;
		vCurrentEvt = 0;
		vOnce = false;
		flagDown();
	}
	
	void flagUp()
	{
		vEvtFlag = vOnceFlag = true;
	}
	
	void flagDown()
	{
		vEvtFlag = false;
	}
	
	int getID()
	{
		return vID;		
	}
	
	void addLine(NPC sayer, String content)
	{
		vLines.add(new DialogueLine(sayer, content));
		if(vCurrentLine == -1)
			nextLine();
	}
	
	void addLine(DialogueLine line)
	{
		vLines.add(line);
		if(vCurrentLine == -1)
			nextLine();
	}
	
	void addEvt(Runnable evt)
	{
		vDiaEvents.add(evt);
		if(vCurrentEvt == -1)
			nextEvt();
	}	
	
	void nextEvt()
	{
		vCurrentEvt++;
	}
	
	void nextLine()
	{
		vCurrentLine++;
	}
	
	void nextLineEvt()
	{
		nextLine();
		nextEvt();
	}
	
	private boolean vOnce;
	
	void play()
	{
		if(!vOnce)
		{
			Logger.log("Displaying dialogue "+vID);
			vOnce = true;
		}
		if(vEvtFlag)
		{
			nextEvt();
			flagDown();
		}
		vDiaEvents.get(vCurrentEvt).run();
	}
	
	void draw(Function<Integer, GameTexture> textureGetter)
	{
		gBox.draw(textureGetter);
		
		vLines.get(vCurrentLine).draw(textureGetter);

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
	
}
