package engine;

import java.io.File;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import net.java.games.input.Version;

abstract class Graphics
{
	static final int DEFAULT_WINDOW_WIDTH=1024;
	static final int DEFAULT_WINDOW_HEIGHT=768;
	
	static final boolean DISPLAY_MODE_OBJECT = true;
	static final boolean DISPLAY_MODE_TEXT = false;
	
	private static int vWidth;
	private static int vHeight;
	
	private static boolean vDisplayMode;
	private static boolean vFlip;
	private static int vFlipA;
	private static int vFlipB;
	
	static void init(String windowTitle, int width, int height) throws LWJGLException
	{
		Logger.log("Initializing graphics...");
		System.setProperty("org.lwjgl.librarypath", new File("lib"+File.separator+"natives").getAbsolutePath());
		vWidth = width;
		vHeight = height;
		createWindow(windowTitle);
		initGL();
		Logger.log("Graphics succesfully initialized");
		Logger.log("LWJGL Version " + Version.getVersion() + " is working");
	}
	
	static void initGL()
	{	
		Logger.log("Initializing GL...");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, vWidth, vHeight);
		GL11.glOrtho(0,vWidth, 0, vHeight,-1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		vDisplayMode = DISPLAY_MODE_OBJECT;
		vFlip = false;
		vFlipA = 1;
		vFlipB = 0;		
		Logger.log("GL succesfully initialized");
	}
	
	static void flip()
	{
		if(vFlip)
		{
			vFlip = false;
			vFlipA = 1;
			vFlipB = 0;
		}
		else
		{
			vFlip = true;
			vFlipA = 0;
			vFlipB = 1;
		}
	}
	
	static void setWindowTitle(String title)
	{
		Display.setTitle(title);
	}
	
	static void adjustDisplayMode(boolean mode)
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		if(mode == DISPLAY_MODE_OBJECT)
			adjustDisplayModeFofObjects();
		else
			adjustDisplayModeFofText();
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	private static void adjustDisplayModeFofText()
	{
		GL11.glOrtho(0,vWidth,vHeight,0,-1, 1);
		vDisplayMode = DISPLAY_MODE_TEXT;
	}
	
	static boolean isTextMode()
	{
		if(vDisplayMode == DISPLAY_MODE_TEXT)
			return true;
		return false;
	}
	
	static boolean isObjMode()
	{
		if(vDisplayMode == DISPLAY_MODE_OBJECT)
			return true;
		return false;
	}
	
	private static void adjustDisplayModeFofObjects()
	{
		GL11.glOrtho(0,vWidth,0,vHeight,-1, 1);
		vDisplayMode = DISPLAY_MODE_OBJECT;
	}
	
	private static void createWindow(String title, boolean fullScreen) throws LWJGLException
	{		
		Logger.log("Initializing window...");
		Display.setTitle(title);
		if(!fullScreen)
			Display.setDisplayMode(new DisplayMode(vWidth,vHeight));
		else				
			Display.setFullscreen(true);			
		Display.create();
		Display.setVSyncEnabled(true);
		Logger.log("Window succesfully initialized");
	}
	
	private static void createWindow(String title) throws LWJGLException
	{	
		createWindow(title, false);
	}
	
	static void draw(int width, int height, int x, int y, GameTexture tex)
	{
		
		
		if(isTextMode())
			adjustDisplayMode(DISPLAY_MODE_OBJECT);	
		
		Color.white.bind();	
		tex.getTex().bind();
		
		GL11.glBegin(GL11.GL_QUADS);				
		
		GL11.glTexCoord2f(vFlipA,1);		
		(new Point(x+width/2, y-height/2)).draw();
			
		GL11.glTexCoord2f(vFlipA,0);
		(new Point(x+width/2, y+height/2)).draw();
			
		GL11.glTexCoord2f(vFlipB,0);
		(new Point(x-width/2, y+height/2)).draw();
			
		GL11.glTexCoord2f(vFlipB,1);
		(new Point(x-width/2, y-height/2)).draw();
		GL11.glEnd();	
		
		
		
	}
	
	static void writeText(String text, GameFont font, int posX, int posY)
	{		
		if(!isTextMode())
			adjustDisplayMode(DISPLAY_MODE_TEXT);
		int tmp = font.getUFont().getWidth(text);
		tmp = tmp/2;
		font.getUFont().drawString(posX-tmp, posY, text);		
	}
	
	static void writeTextLR(String text, GameFont font, int posX, int posY)
	{		
		if(!isTextMode())
			adjustDisplayMode(DISPLAY_MODE_TEXT);
		font.getUFont().drawString(posX, posY, text);		
	}
	
	static int getWindowWidth()
	{
		return vWidth;
	}
	
	static int getWindowHeight()
	{
		return vHeight;
	}
	
	static void destroy()
	{
		Logger.log("Destroying window...");
		Display.destroy();
		Logger.log("Window destroyed");
	}
	
	static void drawBlackScreen(float alpha)
	{
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Color.white.bind();
		GL11.glColor4f(0f,0f,0f,alpha);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(DEFAULT_WINDOW_WIDTH,0);
		GL11.glVertex2f(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		GL11.glVertex2f(0,DEFAULT_WINDOW_HEIGHT);
		GL11.glVertex2f(0,0);
		GL11.glEnd();		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	static void drawBlackScreen()
	{
		drawBlackScreen(1f);
	}
}
