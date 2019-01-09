package engine;

class ActionPerTime
{
	ActionPerTime(int timeVal, Runnable action)
	{
		vCounter = 0;
		vCompleted = false;
		vEndVal = timeVal;
		vAction = action;
	}
	
	private int vCounter;
	private int vEndVal;
	private boolean vCompleted;
	private Runnable vAction;

	
	
	void run()
	{
		if(!vCompleted && vCounter<vEndVal)
		{
			vAction.run();
			update();
		}
		else
		{
			finish();
		}
		
	}
	
	void update()
	{
		vCounter++;		
	}
	
	void finish()
	{
		vCompleted = true;
	}
	
	boolean isCompleted()
	{
		if(vCompleted == true)
			return true;
		return false;
	}
}
