package engine;

class GameException extends Exception
{
	GameException()
	{
		super();
	}
	
	GameException(String gripe)
	{
		super(gripe);
	}
}
