package engine;

class Background extends GameObject
{
	Background(int id, int texNr)
	{
		setID(id);
		setTexNr(texNr);
		setCoords(
				0, 0,
				0, Graphics.getWindowHeight(),
				Graphics.getWindowWidth(), Graphics.getWindowHeight(),
				Graphics.getWindowWidth(), 0
		);
	}	
}
