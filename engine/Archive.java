package engine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

class Archive
{
	private InputStream vStream = null;
	private ZipFile vContent = null;
	
	void getContent(String dir, String zipFile)
	{
		try
		{
			vContent = new ZipFile(dir+File.separator+zipFile);		  
		}
		catch (IOException e)
		{
			ErrorReporter.display(e);
			System.exit(1);
		}
	}
	
	InputStream getResourceAsStream(String resource)
	{
		try
		{
			return vContent.getInputStream(vContent.getEntry(resource));	  
		}
		catch (Exception e)
		{
			ErrorReporter.display(new GameException("Problem with resource "+resource+": "+e.getMessage()));
			System.exit(1);
		}
		return null;
	}
	
	void convertResourceToStream(String resource)
	{
		try
		{
			vStream = vContent.getInputStream(vContent.getEntry(resource));
		}
		catch (IOException e)
		{
			ErrorReporter.display(new GameException("Problem with resource "+resource+": "+e.getMessage()));
			System.exit(1);
		}
	}
	
	InputStream getStream()
	{
		return vStream;
	}
}

