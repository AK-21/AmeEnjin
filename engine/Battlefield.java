package engine;

import static engine.Graphics.*;

import java.awt.Font;
import java.util.function.Function;

class Battlefield
{
	static int DEF_YPOS_TEXT_INPUT = DEFAULT_WINDOW_HEIGHT - 50;
	
	Battlefield(int id, Background bg, Background panel, Font font)
	{
		vID = id;
		vBG = bg;
		vPanel = panel;
		vPlayersReady = false;
		vFont = font;
	}
	
	private int vID;
	private Background vBG;
	private Background vPanel;
	
	private NPC vPlayer1, vPlayer2;
	private boolean vPlayersReady;
	
	private ArenaBox vBoxL, vBoxR;
	
	private Font vFont;
	
	int getID()
	{
		return vID;
	}
	
	Font getFont()
	{
		return vFont;
	}
	
	void setPlayers(NPC player1, NPC player2)
	{
		vPlayer1 = player1;
		vPlayer1.placeLeft();
		vBoxL = new ArenaBox(vPlayer1, vFont);
		
		vPlayer2 = player2;				
		vPlayer2.placeRight();
		vBoxR = new ArenaBox(vPlayer2, vFont);
		
		vPlayersReady = true;
	}
	
	private void drawPlayers(Function<Integer, GameTexture> textureGetter) throws GameException
	{
		if(vPlayersReady)
		{
			vBoxL.draw(textureGetter);
			vBoxR.draw(textureGetter);
		}
		else
			throw new GameException("Players not ready");
	}
	
	void draw(Function<Integer, GameTexture> textureGetter, Runnable insideEvt) throws GameException
	{
		vBG.draw(textureGetter);
		
		insideEvt.run();
		vPanel.draw(textureGetter);
		drawPlayers(textureGetter);
	}
	
	void drawInsideOnTop(Function<Integer, GameTexture> textureGetter, Runnable insideEvt) throws GameException
	{
		vBG.draw(textureGetter);
		vPanel.draw(textureGetter);
		drawPlayers(textureGetter);
		insideEvt.run();
	}	
}
