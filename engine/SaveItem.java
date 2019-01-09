package engine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class SaveItem
{
	static final String FILE_EXT=".save";
	static final String SAVE_DIR = "save";
	static final int LIMIT = 15;
	
	/*-----------------------------------
		Save
	-----------------------------------*/
	
	private static String sTextEmpty = "File name cannot be empty!";
	private static String sTextLong = "File name too long!";
	
	static void setWarnings(String emptyWarning, String tooLongWarning)
	{
		sTextEmpty = emptyWarning;
		sTextLong = tooLongWarning;
	}
	
	SaveItem(int currentScene, String name)
	{
		CurrentTime time = new CurrentTime();
		vTimeString = time.toString();
		vTime = time.getTime();
		vName = name;
		vScene = currentScene;
	}
	
	private String vName;
	private String vTimeString;
	private long vTime;
	private int vScene;
	
	String getName()
	{
		return vName;
	}
	
	String getTimeString()
	{
		return vTimeString;
	}
	
	long getTime()
	{
		return vTime;
	}
	
	int getScene()
	{
		return vScene;
	}
	
	void save() throws GameException
	{
		try
		{
			saveToFile();
		}
		catch(IOException e)
		{
			throw new GameException(e.getMessage());
		}
	}
	
	private void saveToFile() throws IOException, GameException
	{
		if(vName==null || vName.length()==0)
			throw new GameException(sTextEmpty);
		else if(vName.length()>LIMIT)
			throw new GameException(sTextLong);
		else
		{
			DataOutputStream outputStream = null;
			try
			{
				outputStream = new DataOutputStream(new FileOutputStream(SAVE_DIR+File.separator+vName.toLowerCase()+FILE_EXT));
				outputStream.writeUTF(vName);
				outputStream.writeUTF(vTimeString);
				outputStream.writeLong(vTime);
				outputStream.writeInt(vScene);		    
			}
			finally
			{
				if(outputStream != null)
				{
			        outputStream.close();
				}
			}
		}
	}
	
	/*-----------------------------------
		Load
	-----------------------------------*/
	
	SaveItem(String file)
	{
		try
		{
			load(file);
		}
		catch(GameException e)
		{			
			ErrorReporter.display(e);
		}
	}
	
	private void load(String file) throws GameException
	{		
		try
		{
			loadFromFile(file);
		}
		catch(IOException e)
		{
			throw new GameException("Error: "+e.getMessage());
		}
	}
	
	private void loadFromFile(String file) throws IOException
	{
		DataInputStream inputStream = null;
		 
		try
		{
			inputStream = new DataInputStream(new FileInputStream(SAVE_DIR+File.separator+file));
		    vName = inputStream.readUTF();
		    vTimeString = inputStream.readUTF();
			vTime = inputStream.readLong();
			vScene = inputStream.readInt();		  
		}
		finally
		{
			if(inputStream != null)
			{
				inputStream.close();
			}
		}
	}
}
