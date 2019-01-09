package engine;

public final class DialogueElement
{
	DialogueElement(NPC sayer, String content, Runnable evt)
	{
		vLine = new DialogueLine(sayer, content);
		vEvt = evt;
	}
	
	DialogueElement(NPC sayer, String content)
	{
		vLine = new DialogueLine(sayer, content);
		vEvt = ()->{};
	}
	
	private DialogueLine vLine;
	private Runnable vEvt;
	
	DialogueLine getLine()
	{
		return vLine;
	}
	
	Runnable getEvt()
	{
		return vEvt;
	}
}
