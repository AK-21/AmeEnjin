package engine;

import ujavniacz.Tool;

abstract class ErrorReporter
{	
	static void display(Exception e)
	{
		Tool.get().report(e);
		sErrStatus = true;
	}
	
	private static boolean sErrStatus = false;
	
	static boolean error()
	{
		if(sErrStatus)
			return true;
		return false;
	}
	
	static void setDirectory(String name)
	{
		Tool.get().setReportsDirectory(name);
	}
}

