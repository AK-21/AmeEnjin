package engine;

abstract class Logger
{
	private static boolean sActive=false;
	
	private static void init()
	{
		ujavniacz.Tool.get().enableLog();
		ujavniacz.Tool.get().enableOutputWindow();
		log("Output window ready");
	}
	
	static void log(String text)
	{
		if(sActive)
			ujavniacz.Tool.get().report(null, text);
	}
	
	static void activate()
	{
		sActive=true;
		init();
	}
	
	static boolean isActive()
	{
		if(sActive)
			return true;
		return false;
	}
}
