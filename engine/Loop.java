package engine;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

class Loop
{
	private boolean vPause;	
	
	private Runnable vBeginFunc;
	private Runnable vEndFunc;
	private Runnable vUsrEvt;
	private Runnable vGameEvt;
	Loop(Runnable beginFunc, Runnable usrEvt, Runnable gameEvt, Runnable endFunc)
	{
		vIsLoopShuttingDown = false;
		vBeginFunc = beginFunc;
		vUsrEvt = usrEvt;
		vGameEvt = gameEvt;
		vEndFunc = endFunc;
		vCounter = 0;
	}
		
	private int vCounter;
	
	private static boolean vIsLoopShuttingDown;
	
	private static boolean exitLoop()
	{
		if(Display.isCloseRequested())
			return true;
		return vIsLoopShuttingDown;
	}
	
	private void loopEndPermanentFunc()
	{
		vEndFunc.run();				
		
		Display.update();
        Display.sync(60);   
	}
	
	private void loopBeginPermanentFunc()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		vBeginFunc.run();
	}
	
	boolean isPaused()
	{
		if(vPause)
			return true;
		return false;
	}
	
	void pause()
	{
		vPause = true;
	}
	
	void resume()
	{
		vPause = false;
	}
	
	int getCounter()
	{
		return vCounter;
	}
	
	int run()
	{
		while (!exitLoop())
		{
			loopBeginPermanentFunc();			
			if(!isPaused())
			{
				vGameEvt.run();
			}
			vUsrEvt.run();	
			loopEndPermanentFunc();
		}
		
		return 0;
	}
}
