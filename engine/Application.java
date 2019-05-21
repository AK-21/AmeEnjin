package engine;

import static engine.Graphics.DEFAULT_WINDOW_HEIGHT;
import static engine.Graphics.DEFAULT_WINDOW_WIDTH;

import java.util.Calendar;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;

public abstract class Application
{
	private static final String DIR_REPORTS = "reports";
	
	public static void run(Project project)
	{
		init();	
		Launcher launcher = new Launcher(project);
		launcher.start();
		boolean ready = false;
		
		Calendar time = Calendar.getInstance();
		long beginning = time.getTimeInMillis();
		
		while(!ready)
		{
			ready = launcher.isReadyToBuild();
			time = Calendar.getInstance();
		}		
		
		Logger.log("Program starting! Time spent in launcher: " + (time.getTimeInMillis() - beginning)/1000 + " seconds.");
		
		init();	
		initPlatform();
		Game game;
		try
		{
			if(project.isUsingDefaultIdeogramsArray())
				game=project.build(launcher.getIdeogramChoice());
			else
				game=project.build();
			Graphics.setWindowTitle(game.getTitle());
			
			game.load(launcher.getLoaded());
			
			game.runLoop();
			
		} catch (GameException e)
		{
			ErrorReporter.display(e);
			close(1);
		}
		close();
	}
	
	private static void init()
	{
		ErrorReporter.setDirectory(DIR_REPORTS);
	}
	
	private static void close()
	{
		Graphics.destroy();
		AL.destroy();
		System.exit(0);
	}
	
	private static void close(int status)
	{
		Graphics.destroy();
		AL.destroy();
		System.exit(status);
	}
	
	private static void initGraphics() throws LWJGLException
	{ 
		Graphics.init("", DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
	}
	
	private static void initPlatform()
	{ 
		try
		{
			initGraphics();
		}
		catch(LWJGLException e)
		{
			ErrorReporter.display(e);
			System.exit(1);
		}
	}
}
