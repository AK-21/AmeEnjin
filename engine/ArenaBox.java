package engine;

import static engine.GameFont.*;
import static engine.Ideogram.*;
import static engine.NPC.*;
import static engine.Portrait.*;

import java.awt.Color;
import java.awt.Font;
import java.util.function.Function;

class ArenaBox
{	
	static final int PORTRAIT_MARGIN_X = 70;
	static final int PORTRAIT_MARGIN_Y = 50;	
	
	private static final int PORTRAIT_X_DIST = PORTRAIT_WIDTH/2 + PORTRAIT_MARGIN_X;
	private static final int PORTRAIT_Y_DIST = PORTRAIT_HEIGHT/2 + PORTRAIT_MARGIN_Y;
	
	
	
	static final int LINE_CORRECTOR = 20;
	
	static final int NPC_NAME_TEXT_Y = 250;
	static final int NPC_HP_TEXT_Y = 280;
	static final int NPC_AP_TEXT_Y = 300;
	
	private static final int NPC_ABILITIES_LIST_Y = NPC_AP_TEXT_Y + 80;
	private static final int NPC_ABILITIES_LIST_Y_DIST = 40;
	
	private static Color sFontColor = DEFAULT_COLOR;
	private static Color sFontColorDisabled = Color.gray;
	
	static void setFontColor(Color color)
	{
		sFontColor = color;
	}
	
	static void setDisabledFontColor(Color color)
	{
		sFontColorDisabled = color;
	}
	
	private static boolean sFontsReady=false;
	
	static void setFonts(GameFont font)
	{
		sFnts = new GameFont[]
		{
			new GameFont(font.getFont(), 20, sFontColor),
			new GameFont(font.getFont(), 16, sFontColor),
			new GameFont(font.getFont(), 16, sFontColor),
			new GameFont(font.getFont(), 16, sFontColorDisabled),
			new GameFont(font.getFont(), 16, sFontColor)
		};
		sFontsReady=true;		
	}
	
	private static GameFont[] sFnts;
	
	ArenaBox(NPC player, Font font)
	{		
		if(player.getSide() == POS_LEFT)
		{
			vRefLine = 0;
			centerX = IDEOGRAM_DEF_START_X/2;
			centerX = 140;			
		}
		else
		{
			vRefLine = Graphics.getWindowWidth() - IDEOGRAM_DEF_START_X + LINE_CORRECTOR;
			centerX = Graphics.getWindowWidth() - 140;
		}
		
		vPlayer = player;
		vFont = font;
		
		setFnts();
		setBox();
	}
	
	private void setFnts()
	{
		if(sFontsReady)
		{
			vNameFnt = sFnts[0];
			vHPFnt = sFnts[1];
			vAPFnt = sFnts[2];
			vAbilitiesFnt = sFnts[3];
			vAbilitiesActivedFnt = sFnts[4];
		}
		else
		{
			vNameFnt = new GameFont(vFont, 20, sFontColor);
			vHPFnt = new GameFont(vFont, 16, sFontColor);
			vAPFnt = vHPFnt;
			vAbilitiesFnt = new GameFont(vFont, 16, sFontColorDisabled);
			vAbilitiesActivedFnt = new GameFont(vFont, 16, sFontColor);
		}
	}
	
	private void setBox()
	{
		vPlayer.setPortraitPos(vRefLine + PORTRAIT_X_DIST, Graphics.getWindowHeight() - PORTRAIT_Y_DIST);

	}
	
	private int vRefLine;
	private NPC vPlayer;
	
	private Font vFont;
	private GameFont vNameFnt;
	private GameFont vHPFnt;
	private GameFont vAPFnt;
	private GameFont vAbilitiesFnt;
	private GameFont vAbilitiesActivedFnt;
	
	private int centerX;
	
	static int[] vPositions = new int[]
	{
			NPC_ABILITIES_LIST_Y,
			NPC_ABILITIES_LIST_Y+NPC_ABILITIES_LIST_Y_DIST,
			NPC_ABILITIES_LIST_Y+(2*NPC_ABILITIES_LIST_Y_DIST),
			NPC_ABILITIES_LIST_Y+(3*NPC_ABILITIES_LIST_Y_DIST),
			NPC_ABILITIES_LIST_Y+(4*NPC_ABILITIES_LIST_Y_DIST),
			NPC_ABILITIES_LIST_Y+(5*NPC_ABILITIES_LIST_Y_DIST),
			NPC_ABILITIES_LIST_Y+(6*NPC_ABILITIES_LIST_Y_DIST)
	};
	
	void drawOnPosition(int position, int ability, GameFont font)
	{
		Graphics.writeText(
				vPlayer.getAvailableAbilityByIndex(ability).getName()+
				" ("+vPlayer.getAvailableAbilityByIndex(ability).getCost()+" AP)",
				font, centerX, 
				vPositions[position]);
	}
	int vStartIndex=0;
	void draw(Function<Integer, GameTexture> textureGetter)
	{		
		if(vStartIndex>=vPlayer.getAvaliableAbilitiesCount())
			vStartIndex=0;
		
		vPlayer.drawPortrait(textureGetter);
		Graphics.writeText(vPlayer.getName(), vNameFnt, centerX, NPC_NAME_TEXT_Y);
		Graphics.writeText("HP: "+vPlayer.getHP()+"/"+vPlayer.getHPMax(), vHPFnt, centerX, NPC_HP_TEXT_Y);
		Graphics.writeText("AP: "+vPlayer.getAP(), vAPFnt, centerX, NPC_AP_TEXT_Y);
		
		GameFont font;
		
		for(int i=0; i+vStartIndex<vPlayer.getAvaliableAbilitiesCount() && i<vPositions.length; i++)
		{			
			if(i+vStartIndex==vPlayer.getSelectedIndex())
				font = vAbilitiesActivedFnt;
			else
				font = vAbilitiesFnt;
			if(vPlayer.getSelectedIndex()>=vStartIndex+vPositions.length)
			{
				vStartIndex++;
			}
			else if(vPlayer.getSelectedIndex()<vStartIndex)
			{
				vStartIndex--;
			}
			
			drawOnPosition(i, i+vStartIndex, font);
		}
	}
}
