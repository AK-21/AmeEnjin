package engine;

import static engine.Dialogue.*;
import static engine.Graphics.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class DialogueLine
{
	private static final int DIA_X = 20;
	private static final int DIA_Y_HEADER = DIA_BOX_HEIGHT -10;	
	private static final int DIA_Y_LINE = 50;
	private static final int DIA_MAX_LINE_WIDTH = DIA_BOX_WIDTH - 2*DIA_X;
	
	DialogueLine(NPC sayer, String content)
	{
		vSayer = sayer;
		vContent = content;
		vContentLines = new ArrayList<String>();
		split();
		build();
	}
	
	private NPC vSayer;
	private String vContent;
	
	private List<String> vContentLines;
	
	private String[] vWords;
	
	private void split()
	{
		vWords = vContent.split(" ");
	}
	
	private void build()
	{
		String tmp = "";
		int length = 0;
		
		for(int i = 0; i < vWords.length; i++)
		{
			length += getDiaFnt().getUFont().getWidth(" "+vWords[i]);
			
			if(length <= DIA_MAX_LINE_WIDTH)
			{
				if(i!=0)
					tmp += " ";
				tmp += vWords[i];
			}
			else
			{				
				vContentLines.add(tmp);
				tmp = vWords[i];
				length = getDiaFnt().getUFont().getWidth(tmp);
			}				
			
		}
		
		if(!tmp.isEmpty())
		{
			vContentLines.add(tmp);
		}
	}

	void draw(Function<Integer, GameTexture> textureGetter)
	{
		vSayer.drawEffigy(textureGetter);
		Graphics.writeTextLR(vSayer.getName()+":", getDiaFnt(), DIA_X, DEFAULT_WINDOW_HEIGHT - DIA_Y_HEADER);
		
		for(int i=0; i<vContentLines.size(); i++)
		{
			Graphics.writeTextLR(vContentLines.get(i), getDiaFnt(), DIA_X, DEFAULT_WINDOW_HEIGHT - (DIA_Y_HEADER - ((i+1)*DIA_Y_LINE)));
		}
	}
}
