package engine;


import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;


import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;


public class KanaTable
{
	static final String SOURCE="Source: Wikipedia.org";
	
	private static KanaTable sInstance;
	
	private static boolean sReady = false;
		
		private static void init(Archive resources)
		{
			sResources = resources;
			sInstance = new KanaTable();
			sReady=true;
		}
		
		static void invoke(Archive resources)
		{
			if(!sReady)
				init(resources);
			sInstance.vDialog.setVisible(true);
		}
		
		static void hide()
		{
			if(sReady)
				sInstance.vDialog.dispose();	
		}
		
		private static Archive sResources;
		
		private JPanel table()
		{
			String[][] array = vContent();
			JPanel table = new JPanel();
			table.setLayout(new BoxLayout(table, BoxLayout.Y_AXIS));
			int current=0;
			{
				for(int i=0; i<vSize.length; i++)
				{						
					table.add(line(vSize[i], array, current));
					current+=vSize[i];
				}
			}
			return table;
		}
		
		private JPanel line(int size, String[][] array, int firstLine)
		{
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			
			if(size==1)
			{
				panel.add(emptySingle(300));
				panel.add(single(array[firstLine][0], array[firstLine][1], array[firstLine][2]));
				panel.add(emptySingle(300));
			}
			else if(size==2)
			{
				panel.add(single(array[firstLine][0], array[firstLine][1], array[firstLine][2]));
				panel.add(emptySingle(450));
				panel.add(single(array[firstLine+1][0], array[firstLine+1][1], array[firstLine+1][2]));
			}			
			else if(size==3)
			{
				panel.add(single(array[firstLine][0], array[firstLine][1], array[firstLine][2]));
				panel.add(emptySingle(150));
				panel.add(single(array[firstLine+1][0], array[firstLine+1][1], array[firstLine+1][2]));
				panel.add(emptySingle(150));
				panel.add(single(array[firstLine+2][0], array[firstLine+2][1], array[firstLine+2][2]));
			}
			else
			{
				for(int i=0; i<size; i++)
				{
					panel.add(single(array[firstLine+i][0], array[firstLine+i][1], array[firstLine+i][2]));
				}
			}
			return panel;
		}
		
		private JPanel single(String s1, String s2, String s3)
		{
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			panel.setBorder(new LineBorder(Color.BLACK, 1));
			panel.setPreferredSize(new Dimension(150, 40));
			panel.setMinimumSize(panel.getPreferredSize());
			panel.setMaximumSize(panel.getPreferredSize());
			panel.add(romajiField(s1));
			panel.add(romajiField("-"));
			panel.add(kanaField(s2));
			panel.add(romajiField("-"));
			panel.add(kanaField(s3));
			return panel;
		}
		
		private JPanel emptySingle(int size)
		{
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			panel.setBorder(new LineBorder(Color.BLACK, 1));
			panel.setPreferredSize(new Dimension(size, 40));
			panel.setMinimumSize(panel.getPreferredSize());
			panel.setMaximumSize(panel.getPreferredSize());
			panel.add(romajiField(" "));
			panel.add(kanaField(" "));
			return panel;
		}
		
		private JLabel romajiField(String s)
		{
			JLabel label = new JLabel(s);
			label.setBorder(new EmptyBorder(5,5,5,5));
			if(vFontsReady)
				label.setFont(vRomajiFnt);
			return label;
		}
		
		private JLabel kanaField(String s)
		{
			JLabel label = new JLabel(s);
			label.setBorder(new EmptyBorder(5,5,5,5));
			if(vFontsReady)
				label.setFont(vKanaFnt);
			return label;
		}
		
		private Font vRomajiFnt;
		private Font vKanaFnt;
		private boolean vFontsReady;
		
		private void setFonts(Font kana, Font romaji)
		{
			vRomajiFnt = romaji;
			vKanaFnt = kana;
			vFontsReady = true;
		}
		
		private JPanel horizontalPanel(String text, Font fnt)
		{
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			
			panel.setBorder(new LineBorder(Color.BLACK, 1));
			
			panel.setPreferredSize(new Dimension(750, 40));
			panel.setMinimumSize(panel.getPreferredSize());
			panel.setMaximumSize(panel.getPreferredSize());
			panel.setAlignmentX(Component.CENTER_ALIGNMENT);
			JLabel label = new JLabel("<html><div style=\"text-align:center; width:"+(panel.getPreferredSize().getWidth()-10)+";\">"+text+"</div></html>");
			
			label.setBorder(new EmptyBorder(5,5,5,5));
			label.setBackground(Color.BLUE);
			
			if(vFontsReady)
				label.setFont(fnt);
			
			panel.add(label);
			
			return panel;
		}
		
		private JPanel header(String text)
		{
			return horizontalPanel(text, vRomajiFnt);						
		}
		
		private JPanel footer(String text)
		{
			Font fnt = vRomajiFnt;
			if(vFontsReady)
				fnt = new Font(vRomajiFnt.getFontName(), Font.BOLD, 12);
			return horizontalPanel(text, fnt);
		}
		private KanaTable()
		{
			vDialog = new JDialog();
			Container mainC = vDialog.getContentPane();
			
			JPanel mainP = new JPanel();
			mainP.setLayout(new BoxLayout(mainP, BoxLayout.Y_AXIS));
			mainP.setBorder(new EmptyBorder(8, 3, 0, 3));
			mainC.add(mainP);
			vFontsReady=false;
			
			try
			{
				Font  kf;
				kf = Font.createFont(Font.TRUETYPE_FONT, sResources.getResourceAsStream(GameFont.FONT_JP));
				kf = new Font(kf.getFontName(), Font.PLAIN, 24);
				Font  rf;
				rf = Font.createFont(Font.TRUETYPE_FONT, sResources.getResourceAsStream(GameFont.FONT_PL));
				rf = new Font(rf.getFontName(), Font.BOLD, 18);
				 setFonts(kf, rf);
			}
			catch (Exception e)
			{
				ErrorReporter.display(new GameException(e.getClass().getSimpleName()+": "+e.getMessage()));
			}
			
			mainP.add(header(vHeaders[0]+" - "+vHeaders[1]+" - "+vHeaders[2]));
			mainP.add(table());
			mainP.add(footer(SOURCE));
			
			
			vDialog.pack();
			vDialog.setAlwaysOnTop(true);
			vDialog.setResizable(false);
			vDialog.setLocationRelativeTo(null);
		}
		
		private String[] vHeaders = {"Rōmaji", "Hiragana", "Katakana"};
		
		private int vSize[] = {5,5,5,5,5,5,5,3,5,2,1,5,5,5,5,5};
		
		private String[][] vContent()
		{
			int length = Ideogram.ROMAJI.length;
			String[][] o = new String[length][vHeaders.length];			
			
			for(int i=0; i<length; i++)
			{
				o[i][0]= Ideogram.ROMAJI[i];
				o[i][1]= Ideogram.HIRAGANA[i];
				o[i][2]= Ideogram.KATAKANA[i];
			}
			
			return o;
		}
		
		private JDialog vDialog;
}
