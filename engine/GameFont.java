package engine;


import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.ShadowEffect;

class GameFont
{
	static final String FONT_PL="FiraSans-Regular.otf";
	static final String FONT_JP="font_1_ant-kaku.ttf";
	static final String DIR_FONT="font";
	static final String ARCHIVE_FNT="fonts.archive";
	
	static final Color DEFAULT_COLOR = Color.orange;
	
	private UnicodeFont vFont;
	private Color vColor;
	
	private static String sFont = FONT_PL;
	
	private static int s1stGlyph = 0;
	private static int sLastGlyph = 3000;	
	
	static void selectFont(String font, int firstGlyph, int lastGlyph)
	{
		sFont = font;
		s1stGlyph = firstGlyph;
		sLastGlyph = lastGlyph;
	}
	
	static void selectFont(String font)
	{
		sFont = font;
	}
	
	static void selectFont(String font, String glyphs)
	{
		sFont = font;
	}
	
	static String getFontName()
	{
		return sFont;
	}
	
	static GameFont specialFont(String name, String txt, Archive source)
	{
		return new GameFont(name, 60, source, txt, Color.RED);
	}
	
	GameFont(String name, int size, Archive source)
	{
		try
		{
			vFont=new UnicodeFont(convertFromStream(name, source), size, false, false);
			vFont.addAsciiGlyphs();
			vFont.addGlyphs(s1stGlyph,sLastGlyph);
			
			setColor(DEFAULT_COLOR);
			adjust();
			vFont.loadGlyphs();
		}
		catch(Exception e)
		{
			ErrorReporter.display(e);
		}
	}
	
	GameFont(String name, int size, Archive source, int firstGlyph, int lastGlyph, Color color)
	{
		try
		{
			vFont=new UnicodeFont(convertFromStream(name, source), size, false, false);
			vFont.addGlyphs(firstGlyph,lastGlyph);
			setColor(color);
			adjust();
			vFont.loadGlyphs();
		}
		catch(Exception e)
		{
			ErrorReporter.display(e);
		}
	}
	
	GameFont(String name, int size, Archive source, String text, Color color)
	{
		try
		{
			vFont=new UnicodeFont(convertFromStream(name, source), size, false, false);
			vFont.addGlyphs(text);
			setColor(color);
			adjust();
			vFont.loadGlyphs();
		}
		catch(Exception e)
		{
			ErrorReporter.display(e);
		}
	}
	
	GameFont(Font fnt, int size, Color color)
	{
		try
		{
			vFont=new UnicodeFont(fnt, size, false, false);
			vFont.addAsciiGlyphs();
			vFont.addGlyphs(0,fnt.getNumGlyphs());
			setColor(color);
			adjust();
			vFont.loadGlyphs();
		}
		catch(Exception e)
		{
			ErrorReporter.display(e);
		}
	}
	
	GameFont(Font fnt, int size)
	{
		this(fnt, size, DEFAULT_COLOR);
	}
		
	private Font convertFromStream(String resource, Archive source)
	{
		try
		{		
			return Font.createFont(Font.TRUETYPE_FONT, source.getResourceAsStream(resource));		
		}
		catch(Exception e)
		{
			ErrorReporter.display(e);			
		}
		return null;
	}
	
	UnicodeFont getUFont()
	{
		return vFont;
	}
	
	Font getFont()
	{
		return vFont.getFont();
	}
	
	private void setColor(Color color)
	{
		vColor = color;
	}
	
	Color getColor()
	{
		return vColor;
	}
	
	@SuppressWarnings("unchecked")
	void adjust()
	{
		vFont.getEffects().add(new ColorEffect(Color.black));
		vFont.getEffects().add(new ShadowEffect(vColor,1,1,(float)0.75));
	}	
}

