package engine;

import java.io.File;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

class GameTexture
{
	static final String FILE_FORMAT_TEX="PNG";
	static final String TEX_FILE_EXTENSION=".png";
	static final String DIR_TEX="img";
	static final String ARCHIVE_TEX="textures.archive";
	
	private Texture vTex;
	private int vID;
	private String vName;
	
	GameTexture(int id, String name)
	{
		try
		{		
			vTex=TextureLoader.getTexture(FILE_FORMAT_TEX, ResourceLoader.getResourceAsStream(DIR_TEX+File.separator+name));
			vID=id;
			vName=name;
		}
		catch(Exception e)
		{
			ErrorReporter.display(e);
			System.exit(1);
		}
	}
	
	Texture getTex()
	{
		return vTex;
	}
	
	String getName()
	{
		return vName;
	}
	
	int getID()
	{
		return vID;
	}
}

