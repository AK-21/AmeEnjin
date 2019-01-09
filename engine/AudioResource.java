package engine;

import java.io.File;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

abstract class AudioResource
{
	protected static final String DIR_AUDIO = "audio";
	
	AudioResource(int id, String fileName, boolean looped, boolean isMusic)
	{
		vID = id;
		vFileName=fileName;
		vIsMusic = isMusic;
		vLooped = looped;
		vPath = buildPath();
		
		buildPath();
		
		try
		{
			vAudio=AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(vPath));
		}
		catch(Exception e)
		{
			ErrorReporter.display(e);
			Display.destroy();
			AL.destroy();
			System.exit(1);
		}
		
		vLooped = looped;
	}
	
	private int vID;
	private boolean vLooped;
	private boolean vIsMusic;
	private String vFileName;
	private String vPath;
	private Audio vAudio;
	
	protected String buildPath()
	{
		return DIR_AUDIO + File.separator + vFileName;
	}
	
	void play()
	{
		if(vIsMusic)
		{
			vAudio.playAsMusic(1.0f, 1.0f, vLooped);
			if(Logger.isActive())
				Logger.log("Playing music ["+vID+"]"+vFileName);
		}
		else
		{
			vAudio.playAsSoundEffect(1.0f, 1.0f, vLooped);
			if(Logger.isActive())
				Logger.log("Playing sound ["+vID+"]"+vFileName);
		}
		
	}
	
	void stop()
	{
		vAudio.stop();
	}
	
	int getID()
	{
		return vID;
	}
	
	String getFileName()
	{
		return vFileName;
	}
}
