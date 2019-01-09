package engine;

import java.io.File;

class Music extends AudioResource
{
	private static int sAlreadyPlayed = -1;
	
	private static final String DIR_MUSIC = "music";
	
	Music(int id, String fileName)
	{
		super(id, fileName, true, true);
		vStopped = true;
	}
	
	@Override
	protected String buildPath()
	{
		return super.buildPath().replace(File.separator,File.separator+DIR_MUSIC+File.separator);
	}
	
	private boolean vStopped;
	
	@Override
	void play()
	{
		if(this.getID()!=sAlreadyPlayed)
		{
			sAlreadyPlayed = this.getID();
			vStopped=false;
			super.play();
		}
	}
	
	@Override
	void stop()
	{
		if(!vStopped)
		{
			super.stop();
			vStopped=true;
			Logger.log("Music ["+getID()+"]"+getFileName()+" stopped");
		}
	}
}
