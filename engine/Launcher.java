package engine;

import static engine.GameFont.ARCHIVE_FNT;
import static engine.GameFont.DIR_FONT;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

class Launcher
{
	private static final boolean VERTICAL = true;
	private static final boolean HORIZONTAL = false;
	
	private static final int MIN_WIDTH = 350;
	private static final int MIN_HEIGHT = 10;
	private static final int MAX_HEIGHT_DESCR = 100;
	
	private static final Border BORDER_MAIN = new EmptyBorder(10,10,10,10);
	private static final Border BORDER_SECTION = new EmptyBorder(10,10,10,10);	
	private static final Border BORDER_BUTTON_AREA = new EmptyBorder(20,5,0,5);
	private static final Border BORDER_TOP = new EmptyBorder(0,0,10,0);
	private static final Border BORDER_COPYRIGHT = new EmptyBorder(10,0,0,0);
	private static final Border BORDER_DESCRIPTION_SECTION = new EmptyBorder(10, 5, 10, 5);
	private static final Border BORDER_DESCRIPTION = new EmptyBorder(5, 5, 5, 5);
	private static final Border BORDER_DESCRIPTION_PANEL = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	

	
	private static final String LABEL_TITLE = "Title";
	private static final String LABEL_DESCRIPTION = "Description";
	private static final String LABEL_CATEGORY = "Category";
	private static final String LABEL_KANA_TABLE = "Show kana table";
	private static final String LABEL_BTN_LAUNCH= "Launch";
	private static final String LABEL_OPTIONS = "Options";
	private static final String LABEL_LOAD = "Load";
	
	private static final float ALIGNMENT = Container.LEFT_ALIGNMENT;
	
	private static final String NAME_LAUNCHER = " AmeEnjin 0.2 Game Launcher ";
	private static final String NAME_LAUNCHER_JP = " 雨エンジン";
	private static final String HEADER_LAUNCHER = "Launcher";
	
	private static final String COPYRIGHT = " \u00A9 2019 Arkadiusz Kostyra ";
		
	Launcher(Project gameProject)
	{
		vReady = false;
		vUseLoadFuncs = false;
		if(gameProject != null)
			build(gameProject);
	}
	
	private boolean vReady;	
	
	private void buildLauncher(Project gameProject)
	{
		loadFonts();
		initWindow();
		
		initCustomFonts(gameProject.getCustomTitleFont(), gameProject.getCustomTitleSize(), gameProject.getCustomDescFont(), gameProject.getCustomDescSize());
		
		addTop();
		addTitle(gameProject.getTitle());
		
		addDescription(gameProject.getDescription());
				
		if (new File(SaveItem.SAVE_DIR).exists())
		{
			buildList();
		
			if(vSaves!=null&&vSaves.length>0)	
			{
				addLoad();
			}
		}
		if(gameProject.isUsingDefaultIdeogramsArray())
			addIdeogramsChoice();
		
		if(gameProject.getCustomOptionsCounter()>0)
			buildCustomOptions();
		
		addTools();		
		addLaunchButton();
		addCopyright();
		pack();
		putWindowToCenter();
		vReady = true;		
		
	}
	
	private void build(Project gameProject)
	{
		vProject = gameProject;
		buildLauncher(vProject);
	}
	
	private Project vProject;
	
	/*-----------------------------------
		Window
	-----------------------------------*/
	
	private JFrame vWindow;
	private JPanel vMainPanel;
	
	private void initWindow()
	{
		vWindow = new JFrame(HEADER_LAUNCHER);
		if(vFntReady)
			vWindow.setFont(vFont);
		vWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		vWindow.setResizable(false);
		vWindow.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		vMainPanel = new JPanel();
		vWindow.add(vMainPanel);			
		vMainPanel.setBorder(BORDER_MAIN);
		setLayout(vMainPanel, VERTICAL);
	}
	
	private void addToWindow(Container container)
	{
		addTo(container, vMainPanel);
	}
	
	private void putWindowToCenter()
	{
		vWindow.setLocationRelativeTo(null);
	}
	
	private void pack()
	{
		vWindow.pack();
	}
	
	/*-----------------------------------
		Custom options
	-----------------------------------*/
	
	private int vFirstCustom = -1;
	
	private void buildCustomOption(int projectIndex)
	{
		String[] options = vProject.getCustomOptionChoices(projectIndex);
		
		int index = vChoices.size();
		if(vFirstCustom == -1)
			vFirstCustom = index;
		
		JComboBox<String> dropdown = dropdown(options, 0, index);		
		
		addVerticalSection(vProject.getCustomOptionLabel(projectIndex), dropdown);
		
		vProject.setCustomOption(projectIndex, index);
	}
	
	private void buildCustomOptions()
	{
		for(int i=0; i<vProject.getCustomOptionsCounter(); i++)
		{
			buildCustomOption(i);
		}
	}
	
	/*-----------------------------------
		Section
	-----------------------------------*/
	
	private void addSection(String header, boolean configuration, Container... elements)
	{
		addLabel(header);
		
		JPanel section = new JPanel();
		setLayout(section, configuration);
		section.setBorder(BORDER_SECTION);
		section.setAlignmentX(ALIGNMENT);		
		
		for(int i=0; i<elements.length; i++)
		{
			addTo(elements[i], section);
		}
		
		addToWindow(section);
	}
	
	private void addVerticalSection(String header, Container... elements)
	{
		addSection(header, VERTICAL, elements);
	}
	
	private void addHorizontalSection(String header, Container... elements)
	{
		addSection(header, HORIZONTAL, elements);
	}
	
	/*-----------------------------------
		Title
	-----------------------------------*/
	
	private void addTitle(String title)
	{		
		JLabel label = new JLabel(title);
		label.setFont(vTitleFnt);
		label.setAlignmentX(ALIGNMENT);
		addVerticalSection(LABEL_TITLE, label);
	}
	
	/*-----------------------------------
		Description
	-----------------------------------*/	
	
	private JPanel descriptionSection()
	{
		JPanel p;
		p = new JPanel();
		setLayout(p, VERTICAL);
		p.setBorder(BORDER_DESCRIPTION_SECTION);
		p.setAlignmentX(ALIGNMENT);
		return p;
	}
	
	private JTextArea description(String content)
	{
		JTextArea a = new JTextArea(content);
		a.setWrapStyleWord(true);			
		a.setLineWrap(true);
		a.setBackground(new Color(238,238,238));
		a.setEditable(false);
		a.setCursor(null);
		a.setFocusable(false);
		vDescFnt = new Font(vDescFnt.getFontName(), Font.BOLD, vDescFnt.getSize());
		a.setFont(vDescFnt);
		a.setAlignmentX(ALIGNMENT);
		a.setBorder(BORDER_DESCRIPTION);
		return a;
	}
	
	private void addDescription(String content)
	{		
		
		if(!content.equals(""))
		{
			addLabel(LABEL_DESCRIPTION);
			
			JPanel section = descriptionSection();
						
			JTextArea descr = description(content);
			
			JScrollPane panel = new JScrollPane(descr);
			panel.setBorder(BORDER_DESCRIPTION_PANEL);
			
			addTo(panel, section);
			
			section.setPreferredSize(new Dimension(section.getWidth(),MAX_HEIGHT_DESCR));
			addToWindow(section);			
		}
	}
	
	/*-----------------------------------
		Load
	-----------------------------------*/
	
	private int vLoad=-1;
	private JCheckBox vLoadBox;
	private int vLoadChoice;
	
	private boolean vUseLoadFuncs;
	
	int getLoaded()
	{
		if(vLoad>=0)
			return vLoad;
		return-1;
	}
	
	private void addLoad()
	{
		vLoadBox = new JCheckBox("Load game");
		vLoadBox.setFocusPainted(false);
		vLoadBox.setSelected(false);
		
		

		String[] options=new String[0];
		
		if(vSaves!=null&&vSaves.length>0)
		{		
			options=new String[vSaves.length];
			for(int i=0; i<vSaves.length; i++)
				options[i]=vSaves[i].getName()+" ("+vSaves[i].getTimeString()+")";
			vUseLoadFuncs=true;
		}
		
		vLoadChoice = vChoices.size();
		
		JComboBox<String> dropdown = dropdown(options, 0, vLoadChoice);
		dropdown.setEnabled(false);
		
		ActionListener listener = new ActionListener()
		{
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if(vLoadBox.isSelected())
            	{
            		dropdown.setEnabled(true);
            	}
            	else
            	{
            		dropdown.setEnabled(false);
            	}
            }
        };
        
        vLoadBox.addActionListener(listener);
		
		
		JPanel filler=new JPanel();
		
		filler.setPreferredSize(new Dimension(20, filler.getHeight()));
		filler.setMaximumSize((filler.getPreferredSize()));
		
		addHorizontalSection(LABEL_LOAD, vLoadBox, filler, dropdown);
	}
	
	
	SaveItem[] vSaves=null;
	
	private void buildList()
	{
		FilenameFilter filter = new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				if(name.endsWith(SaveItem.FILE_EXT))
				{
					return true;
				} 
				else
				{
					return false;
				}
			}
		};		
				
		String[] list= new File(SaveItem.SAVE_DIR).list(filter);
		vSaves = new SaveItem[list.length];
		for(int i=0; i<list.length; i++)
			addSaveItem(new SaveItem(list[i]), i);
	}
	
	private void addSaveItem(SaveItem item, int currentIndex)
	{
		long time = item.getTime();
		if(currentIndex==0)
			vSaves[currentIndex]=item;
		else if(time<=vSaves[currentIndex-1].getTime())
			vSaves[currentIndex]=item;
		else if(time>vSaves[0].getTime())
		{
			moveArrayItems(currentIndex, 0);
			vSaves[0]=item;
		}
		else
		{
			for(int i=0; i<currentIndex; i++)
				if(time>vSaves[i].getTime())
				{
					moveArrayItems(currentIndex, i);
					vSaves[i]=item;
					break;
				}
		}
			
	}
	
	private void moveArrayItems(int currentIndex, int limit)
	{
		for(int i=currentIndex; i>limit; i--)
			vSaves[i]=vSaves[i-1];
	}
	
	/*-----------------------------------
		Output
	-----------------------------------*/	
	
	private JCheckBox vEnableOutputWindowOption;
	
	private void addTools()
	{		
		vEnableOutputWindowOption = new JCheckBox("Enable log output window");
		vEnableOutputWindowOption.setFocusPainted(false);
		vEnableOutputWindowOption.setSelected(false);
		addVerticalSection(LABEL_OPTIONS, vEnableOutputWindowOption);
	}
	
	/*-----------------------------------
		Ideograms choice
	-----------------------------------*/	

	private int vIdeogramChoice = -1;
	
	private void addIdeogramsChoice()
	{		
		String[] options = new String[] {"Hiragana", "Katakana"};
		
		vIdeogramChoice = vChoices.size();
		
		JComboBox<String> dropdown = dropdown(options, 0, vIdeogramChoice);
		
		ActionListener listener = new ActionListener()
		{
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	KanaTable.invoke(vResources);
            }
        };
		
		JButton showTable = new JButton(LABEL_KANA_TABLE);
		showTable.addActionListener(listener);
		
		JPanel filler=new JPanel();
		
		filler.setPreferredSize(new Dimension(30, filler.getHeight()));
		filler.setMaximumSize((filler.getPreferredSize()));
		
		addHorizontalSection(LABEL_CATEGORY, dropdown, filler, showTable);
	}
	
	boolean getIdeogramChoice() throws GameException
	{
		if(vIdeogramChoice != -1)
			if(getValue(vIdeogramChoice)==0)
				return true;
			else
				return false;
		else
			throw new GameException("Incorrect category settings!");
	}
	
	/*-----------------------------------
		Button
	-----------------------------------*/
	
	private void addLaunchButton()
	{
		JPanel section = new JPanel();		
		section.setLayout(new GridLayout());
		section.setBorder(BORDER_BUTTON_AREA);
		section.setAlignmentX(ALIGNMENT);	
		
		JButton btn = new JButton(LABEL_BTN_LAUNCH);
		
		btn.setFocusPainted(false);
		
		
		ActionListener listener = new ActionListener()
		{
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	if(vProject.getCustomOptionsCounter()>0)
            	{
            		for(int i=vFirstCustom; i<vChoices.size(); i++)
            		{
            			vProject.setCustomValue(vFirstCustom, getValue(vFirstCustom));
            		}
            	}
            	
            	if(vEnableOutputWindowOption.isSelected())
            		Logger.activate();
            	
            	if(vUseLoadFuncs && vLoadBox.isSelected())
            	{
            		vLoad=vSaves[getValue(vLoadChoice)].getScene();
            		if(Logger.isActive())
            			Logger.log("Loading: \""+vSaves[getValue(vLoadChoice)].getName()+"\"...");
            	}
            	else
            	{
            		Logger.log("Running new game...");
            	}
            	readyToBuild=true;;
            	KanaTable.hide();
            	vWindow.dispose();            	
            }
        };
        
        btn.addActionListener(listener);
		
		addTo(btn,section);
		
		addToWindow(section);
		
		
		
		
		
	}
	
	
	
	
	/*-----------------------------------
		Label
	-----------------------------------*/	
	private void addLabel(String header)
	{
		addToWindow(sectionLabel(header));
	}
	
	private JPanel sectionLabel(String label)
	{
		JPanel panel = new JPanel();
		setLayout(panel, HORIZONTAL);
		panel.setAlignmentX(ALIGNMENT);
		
		label = label.toUpperCase();
		
		JLabel labelHeader = new JLabel(label+" ");
		setLabelFont(labelHeader, 10, Font.BOLD);
		labelHeader.setForeground(Color.LIGHT_GRAY);
		addTo(labelHeader, panel);
		addTo(sectionSeparator(), panel);
		return panel;
	}
	
	/*-----------------------------------
		Separator
	-----------------------------------*/	
	
	private JPanel sectionSeparator()
	{
		JPanel tmp = new JPanel();
		setLayout(tmp, VERTICAL);
		
		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		separator.setForeground(Color.LIGHT_GRAY);
		
		JLabel emptyLabelT, emptyLabelB; 
		emptyLabelT = new JLabel(" ");
		emptyLabelB = new JLabel(" ");
		
		setLabelFont(emptyLabelT,5);
		setLabelFont(emptyLabelB,5);
		
		tmp.add(emptyLabelT);
		tmp.add(separator);
		tmp.add(emptyLabelB);
		
		return tmp;
	}
	
	/*-----------------------------------
		Configuration/Layout
	-----------------------------------*/
	
	private void setLayout(Container container, boolean configuration)
	{
		int axis = getAxis(configuration);
		container.setLayout(new BoxLayout(container, axis));
	}
	
	private int getAxis(boolean configuration)
	{
		if(configuration == VERTICAL)
			return BoxLayout.Y_AXIS;
		else
			return BoxLayout.X_AXIS;
	}
	
	private void addTo(Container what, Container where)
	{
		where.add(what);
	}
	
	private void setLabelFont(JLabel label, int size, int style)
	{
		Font font = label.getFont();
		label.setFont(new Font(font.getName(), style, size));
	}
	
	private void setLabelFont(JLabel label, int size)
	{
		setLabelFont(label, size, Font.PLAIN);
	}
	
	private void addCopyright()
	{
		JPanel cpr = copyright();
		cpr.setBorder(BORDER_COPYRIGHT);
		addToWindow(cpr);
	}
	
	private JPanel copyright()
	{		
		return labelInLine(COPYRIGHT);
	}
	
	private void addTop()
	{
		JPanel top = top();
		top.setBorder(BORDER_TOP);
		addToWindow(top);
	}
	
	/*
	private JPanel top()
	{
		return labelInLine(NAME_LAUNCHER);
	}
	*/
	private JPanel top()
	{
		JPanel panel = new JPanel();
		
		setLayout(panel, HORIZONTAL);
		panel.setAlignmentX(ALIGNMENT);
		
		JLabel textLabel = new JLabel(NAME_LAUNCHER);

		setLabelFont(textLabel, 10, Font.BOLD);
		textLabel.setForeground(Color.LIGHT_GRAY);		
				
		JLabel textLabel2 = new JLabel(NAME_LAUNCHER_JP);
		textLabel2.setFont(vFontJP);
		setLabelFont(textLabel2, 10, Font.BOLD);
		textLabel2.setForeground(Color.LIGHT_GRAY);
				
		
		addTo(sectionSeparator(), panel);		
		addTo(textLabel2, panel);
		addTo(textLabel, panel);
		addTo(sectionSeparator(), panel);
		
		
		
		return panel;
	}
	
	private JPanel labelInLine(String text)
	{
		JPanel panel = new JPanel();
		
		setLayout(panel, HORIZONTAL);
		panel.setAlignmentX(ALIGNMENT);
		
		JLabel textLabel = new JLabel(text);
		setLabelFont(textLabel, 10, Font.BOLD);
		textLabel.setForeground(Color.LIGHT_GRAY);		
		
		addTo(sectionSeparator(), panel);
		addTo(textLabel, panel);
		addTo(sectionSeparator(), panel);
		
		
		
		return panel;
	}
	
	private JComboBox<String> dropdown(String[] options, int defaultSelected, int indexInOptionArray)
	{
		JComboBox<String> dropdown = new JComboBox<String>(options);
		vChoices.add(defaultSelected);
		dropdown.setSelectedIndex(defaultSelected);
		
		ActionListener listener = new ActionListener()
		{
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	int selected = dropdown.getSelectedIndex();
            	vChoices.set(indexInOptionArray, selected);
            }
        };
        
        dropdown.addActionListener(listener);
		
		return dropdown;
	}
	
	/*-----------------------------------
		System
	-----------------------------------*/
	
	private List<Integer> vChoices = new ArrayList<Integer>(0);
	
	int getValue(int index)
	{
		if(index>=0 && index < vChoices.size())
			return vChoices.get(index);
		return -1;
	}
		
	void start()
	{
		if(vReady)
			vWindow.setVisible(true);
		else
			ErrorReporter.display(new GameException("Launcher is not ready to run!"));
	}	
	
	private boolean readyToBuild=false;
	
	boolean isReadyToBuild()
	{
		if(readyToBuild)
			return true;
		return false;
	}
	
	private Archive vResources;
	private Font vFont;
	private Font vFontJP;
	private boolean vFntReady=false;
	
	private Font vTitleFnt;
	private Font vDescFnt;
	
	void setTitleFont(String font, int size)
	{
		try
		{		
			vTitleFnt= Font.createFont(Font.TRUETYPE_FONT, vResources.getResourceAsStream(font));		
			vTitleFnt=vTitleFnt.deriveFont((float)size);
		}
		catch(Exception e)
		{
			ErrorReporter.display(e);			
		}
	}
	
	void setDescriptionFont(String font, int size)
	{
		try
		{		
			vDescFnt= Font.createFont(Font.TRUETYPE_FONT, vResources.getResourceAsStream(font));		
			vDescFnt=vDescFnt.deriveFont((float)size);
		}
		catch(Exception e)
		{
			ErrorReporter.display(e);			
		}
	}
	
	private void loadFonts()
	{
		vResources = new Archive();
		vResources.getContent(DIR_FONT,ARCHIVE_FNT);		
		try
		{		
			vFont= Font.createFont(Font.TRUETYPE_FONT, vResources.getResourceAsStream(GameFont.FONT_PL));		
			vFont=vFont.deriveFont(12f);
			vFontJP=Font.createFont(Font.TRUETYPE_FONT, vResources.getResourceAsStream(GameFont.FONT_JP));
			vFontJP=vFontJP.deriveFont(12f);
			vTitleFnt=vFont;
			vDescFnt=vFont;
			vFntReady=true;			
		}
		catch(Exception e)
		{
			ErrorReporter.display(e);		
		}
	}	
	
	private void initCustomFonts(String title, int titleSize, String desc, int descSize)
	{
		if(!title.equals("") && titleSize>=0)
		{
			setTitleFont(title, titleSize);
		}
		if(!desc.equals("") && descSize>=0)
		{
			setDescriptionFont(desc, descSize);
		}
	}
}
