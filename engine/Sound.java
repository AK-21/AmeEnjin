package engine;

import java.io.File;

class Sound extends AudioResource
{
	private static final String DIR_SOUND = "sound";
	
	Sound(int id, String fileName)
	{
		super(id, fileName, false, false);
		vPlayed = false;
	}
	
	@Override
	protected String buildPath()
	{
		return super.buildPath().replace(File.separator,File.separator+DIR_SOUND+File.separator);
	}
	
	@Override
	void play()
	{
		if(!wasPlayed())
		{			
			vPlayed = true;
			super.play();
		}
	}
	
	private boolean vPlayed;
	
	boolean wasPlayed()
	{
		if(vPlayed)
			return true;
		return false;
	}
	
	void reset()
	{
		vPlayed = false;
	}
}
