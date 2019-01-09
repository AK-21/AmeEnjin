package engine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>&#38632&#12456&#12531&#12472&#12531</h1><h2>AmeEnjin v0.2</h2>
 * <h3>Distributed under the Apache License, Version 2.0</h3>
 * <blockquote>
	Copyright 2019 Arkadiusz Kostyra

	Licensed under the Apache License, Version 2.0 (the "License");
   	you may not use this file except in compliance with the License.
   	You may obtain a copy of the License at
   	<br>
	<br>
    http://www.apache.org/licenses/LICENSE-2.0
	<br>
	<br>
   	Unless required by applicable law or agreed to in writing, software
   	distributed under the License is distributed on an "AS IS" BASIS,
   	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   	See the License for the specific language governing permissions and
   	limitations under the License.
   </blockquote>
 */
public abstract class Project
{
	/*######################
		Project constants
	######################*/
	
	/*--------------------
		Display
	--------------------*/
	
	public static final Color DEFAULT_COLOR = GameFont.DEFAULT_COLOR;
	
	public static final int CENTER_X = Graphics.DEFAULT_WINDOW_WIDTH/2;
	public static final int CENTER_Y = Graphics.DEFAULT_WINDOW_HEIGHT/2;
	public static final int WINDOW_WIDTH = Graphics.DEFAULT_WINDOW_WIDTH;
	public static final int WINDOW_HEIGHT = Graphics.DEFAULT_WINDOW_HEIGHT;
	
	public static boolean X_AXIS = true;
	public static boolean Y_AXIS = false;
	
	/*--------------------
		Abilities
	--------------------*/
	
	public static final int ATTACK = 0;
	public static final int HEAL = 1;
	public static final int ABILITY_DEFAULT = ATTACK;
	
	/*--------------------
		Statuses
	--------------------*/
	
	public static final int NEUTRAL = NPC.STATUS_NEUTRAL;
	public static final int SMILE = NPC.STATUS_SMILE;
	public static final int HAPPY = NPC.STATUS_HAPPY;
	public static final int SAD = NPC.STATUS_SAD;
	public static final int CRY = NPC.STATUS_CRY;
	public static final int ANGRY = NPC.STATUS_ANGRY;
	
	public static final int FINE = NPC.STATUS_FINE;
	public static final int HURT = NPC.STATUS_HURT;
	public static final int AGONY = NPC.STATUS_AGONY;
	public static final int GLORY = NPC.STATUS_GLORY;
	public static final int EVIL = NPC.STATUS_EVIL;
	public static final int CUSTOM_STATUS_1 = NPC.STATUS_CUSTOM_1;
	public static final int CUSTOM_STATUS_2 = NPC.STATUS_CUSTOM_2;
	public static final int CUSTOM_STATUS_3 = NPC.STATUS_CUSTOM_3;
	public static final int CUSTOM_STATUS_4 = NPC.STATUS_CUSTOM_4;
	public static final int CUSTOM_STATUS_5 = NPC.STATUS_CUSTOM_5;
	
	public static final int ARROGANCE = NPC.STATUS_ARROGANCE;
	public static final int CORRUPT = NPC.STATUS_CORRUPT;
	
	/*--------------------
		SFX
	--------------------*/
	
	public static final int NO_SOUND = -1;
	public static final int NO_TEXTURE = -1;
	
	/*--------------------
		Private
	--------------------*/
	
	private static final int CREDITS_SPEED = 1;
	private static final int CREDITS_SPACE = 30;	
	
	private static final int WRONG_VALUE = -1;
	private static final String DEFAULT_TITLE = "Game";

	/*##########################################
		Project initial functions & variables
	##########################################*/
	
	/*--------------------
		Constructors
	--------------------*/
	
	/**
	 * Constructor for game using default ideograms (hiragana and katakana), with default title ("Game")
	 */
	protected Project()
	{
		this(DEFAULT_TITLE);
	}
	
	/**
	 * Constructor for game using default ideograms (hiragana and katakana).	 
	 * @param title game title
	 */
	protected Project(String title)
	{
		vTitle = title;
		vCustomIdeograms = false;
		vIdeogramsTexDir = "";
		vTranscriptionArray = Ideogram.ROMAJI;
		initLocalVars();
		selectLauncherFonts();
		createDescription();
		addCustomOptions();
	}	
	
	/**
	 * Constructor for game using custom ideograms.
	 * @param title game title
	 * @param ideogramsTexDirectory directory with ideograms textures (must exists in "img" directory)
	 * @param transcriptionArray transcription/translation for custom ideograms
	 */
	protected Project(String title, String ideogramsTexDirectory, String[] transcriptionArray)
	{
		vTitle = title;
		vCustomIdeograms = true;
		vIdeogramsTexDir = ideogramsTexDirectory;
		vTranscriptionArray = transcriptionArray;
		initLocalVars();
		selectLauncherFonts();
		createDescription();
		addCustomOptions();
	}
	
	/*--------------------
		Variables
	--------------------*/
	
	private boolean vCustomIdeograms;
	private String vTitle;
	private String vIdeogramsTexDir;
	private String[] vTranscriptionArray;
	
	/*--------------------
		Functions
	--------------------*/
	
	private void initLocalVars()
	{
		vCurrentMusic = WRONG_VALUE;
		vCustomOptions = new ArrayList<CustomOption>();
		vSoundTrack = new ArrayList<Music>();
		vSoundFX = new ArrayList<Sound>();
		vAbilities = new ArrayList<Ability>();
		vNPCs = new ArrayList<NPC>();
		vBackgrounds = new ArrayList<Background>();
		vDialogues = new ArrayList<Dialogue>();
		vScenario = new Scenario();
		vTitleFnt="";
		vDescFnt="";
		vTitleSize=-1;
		vDescSize=-1;
		vDescription="";
	}
	
	/*#############################################################
		Private functions & variables - to build game structure
	#############################################################*/
	
	/*--------------------
		System
	--------------------*/
	
	private Game vGamePointer;
	private int vTexOffset=0;
	
	private List<CustomOption> vCustomOptions;
	
	boolean isUsingDefaultIdeogramsArray()
	{
		if(vCustomIdeograms)
			return false;
		return true;
	}
	
	String getTitle()
	{
		return vTitle;
	}	
	
	private void setGamePointer(Game game)
	{		
		vGamePointer = game;		
	}
	
	private void setTextureNumberOffset()
	{
		
		vTexOffset = vGamePointer.getTextureNumberOffset();
	}
	
	private void setStartMode()
	{
		vGamePointer.setMode(Game.FOCUS_STARTGAME);
	}
	
	private int tex(int texture)
	{
		if(texture<0)
			return NO_TEXTURE;
		return texture+vTexOffset;
	}
	
	private void createGame(Game game)
	{		
		Logger.log("Creating from project...");
		setGamePointer(game);
		
		Logger.log("Setting system properties...");
		setSystemProperties();
		Logger.log("Setting system properties completed");
		
		setTextureNumberOffset();
		
		Logger.log("Creating tracklist...");
		buildTrackList();		
		addTrackList();
		Logger.log("Creating tracklist completed");
		
		Logger.log("Creating sounds list...");
		buildSoundsList();
		addSFXList();
		Logger.log("Creating sounds list completed");		
		
		Logger.log("Creating Abilities list...");
		buildAbilitiesList();
		Logger.log("Creating Abilities list completed");
		
		Logger.log("Creating NPCs list...");
		buildNpcList();		
		addNpcList();
		Logger.log("Creating NPCs list completed");
		
		Logger.log("Setting NPCs attributes...");
		setNpcAttributes();
		Logger.log("Setting NPCs attributes completed");
		
		Logger.log("Creating Backgrounds list...");
		buildBackgroundsList();		
		addBackgroundsList();
		Logger.log("Creating Backgrounds list completed");
		
		Logger.log("Creating dialogue panel...");
		buildDialoguePanel();
		Logger.log("Creating dialogue panel completed");
		
		Logger.log("Creating Dialogues list...");
		buildDialoguesList();	
		addDialoguesList();
		Logger.log("Creating Dialogues list completed");
		
		Logger.log("Creating Arenas list...");
		buildArenasList();
		Logger.log("Creating Arenas list completed");
		
		Logger.log("Creating game credits...");
		buildGameCredits();
		Logger.log("Creating game credits completed");
		
		Logger.log("Creating game scenario...");
		buildScenario();
		addScenario();
		Logger.log("Creating game scenario completed");
		
		setStartMode();
		Logger.log("Creating from project finished successful");
	}
	
	private void setExitDialogWindow(String question, String yes, String no)
	{
		Logger.log("Setting custom exit window");
		ExitDialog.setLabels(question, yes, no);
		Logger.log("Custom exit window created");
	}
	
	private void setSaveDialogWindow(String content, String btnSaveLabel, String btnCancelLabel, String overwriteLabel, String btnYesLabel, String btnNoLabel, String btnOKLabel, String successLabel)
	{
		Logger.log("Setting custom save window");
		SaveDialog.setLabels(content, btnSaveLabel, btnCancelLabel, overwriteLabel, btnYesLabel, btnNoLabel, btnOKLabel, successLabel);
		Logger.log("Custom save window created");
	}
	
	private void setMultiDialogWindow(String content, String btnSaveLabel, String btnExitLabel, String btnBackLabel)
	{
		Logger.log("Setting custom menu window");
		MultiDialog.setLabels(content, btnSaveLabel, btnExitLabel, btnBackLabel);
		Logger.log("Custom menu window created");
	}
	
	private void setTextWarnings(String saveEmptyNameWarning, String saveTooLongNameWarning)
	{
		Logger.log("Setting custom warnings");
		SaveItem.setWarnings(saveEmptyNameWarning, saveTooLongNameWarning);
		Logger.log("Custom warnings created");
	}
	
	private void exceptionShutdown(Exception e)
	{
		ErrorReporter.display(e);
		forceShutdown();
	}
	
	private void forceShutdown()
	{
		vGamePointer.close(1);
	}
	
	Game build() throws GameException
	{
		Logger.log("Building game...");
		
		Logger.log("Setting game font...");
		selectGameFontFile();
		Logger.log("Setting game font completed");
		
		Game newGame;
		if(isUsingDefaultIdeogramsArray())
			throw new GameException("Project settings error");
		else
		{
			newGame = new Game(vTitle, vIdeogramsTexDir, vTranscriptionArray);
			createGame(newGame);
			Logger.log("Game is ready");
			return newGame;			
		}
	}
	
	Game build(boolean category) throws GameException
	{
		Logger.log("Building game...");
		
		Logger.log("Setting game font...");
		selectGameFontFile();
		Logger.log("Setting game font completed");
		
		Game newGame;
		if(isUsingDefaultIdeogramsArray())
		{
			newGame = new Game(vTitle, category);
			createGame(newGame);
			Logger.log("Game is ready");
			return newGame;
		}
		else
			throw new GameException("Project settings error");
	}
	
	int getCustomOptionsCounter()
	{
		return vCustomOptions.size();
	}
	
	int getCustomOptionLauncherIndex(int index) throws GameException
	{
		if(index>=0 && index<getCustomOptionsCounter())
			return vCustomOptions.get(index).getIndex();
		throw new GameException("Index out of range!");
	}
	
	String getCustomOptionLabel(int localIndex)
	{
		return vCustomOptions.get(localIndex).getLabel();
	}
	
	String[] getCustomOptionChoices(int localIndex)
	{
		return vCustomOptions.get(localIndex).getOptions();
	}
	
	void setCustomOption(int localIndex, int launcherIndex)
	{
		if(localIndex >=0 && localIndex < vCustomOptions.size())
			vCustomOptions.get(localIndex).setIndex(launcherIndex);
	}
	
	void setCustomValue(int launcherIndex, int value)
	{
		for(int i=0; i<vCustomOptions.size(); i++)
			if(vCustomOptions.get(i).getIndex()==launcherIndex)
			{
				vCustomOptions.get(i).setValue(value);
				break;
			}
	}
	
	void setOptionValue(int index, int value)
	{
		vCustomOptions.get(index).setValue(value);
	}
	
	/*--------------------
		Fonts
	--------------------*/

	private String vTitleFnt;
	private String vDescFnt;
	private int vTitleSize;
	private int vDescSize;
	
	private void setLauncherTitleFnt(String fnt, int size)
	{
		vTitleFnt=fnt;
		vTitleSize=size;
	}
	
	private void setLauncherDescFnt(String fnt, int size)
	{
		vDescFnt=fnt;
		vDescSize=size;
	}
	
	String getCustomTitleFont()
	{
		return vTitleFnt;
	}
	
	String getCustomDescFont()
	{
		return vDescFnt;
	}
	
	int getCustomTitleSize()
	{
		return vTitleSize;
	}
	
	int getCustomDescSize()
	{
		return vDescSize;
	}
	
	private void selectFont(String fontName)
	{
		Logger.log("Setting font file ("+fontName+")");
		GameFont.selectFont(fontName);
		Logger.log(fontName+" set as default font file");
	}
	
	private void selectFont(String fontName, int firstGlyph, int lastGlyph)
	{
		Logger.log("Setting font file ("+fontName+")");
		GameFont.selectFont(fontName, firstGlyph, lastGlyph);
		Logger.log(fontName+" set as default font file");
	}
	
	private void selectFont(String fontName, String glyphs)
	{
		Logger.log("Setting font file ("+fontName+")");
		GameFont.selectFont(fontName, glyphs);
		Logger.log(fontName+" set as default font file");
	}
	
	private void setSceneFontColor(Color color)
	{		
		Logger.log("Setting scene font color ("+color.toString()+")");
		vGamePointer.setSceneFont(color);
		Logger.log("Scene font color updated");
	}
	
	private void setSceneFont(Color color, int size)
	{
		Logger.log("Setting scene font properties (color={"+color.toString()+"}, size="+size+")");
		vGamePointer.setSceneFont(color, size);
		Logger.log("Scene font updated");
	}
	
	private void setBattleFont(Color color)
	{
		Logger.log("Setting battle font color ("+color.toString()+")");
		vGamePointer.setBattleFont(color);
		Logger.log("Battle font color updated");
	}
	
	private void setDisabledFont(Color color)
	{
		Logger.log("Setting disabled font color ("+color.toString()+")");
		vGamePointer.setDisabledFont(color);
		Logger.log("Disabled font color updated");
	}
	
	private void setDialogueFont(Color color)
	{
		Logger.log("Setting dialogue font color ("+color.toString()+")");
		vGamePointer.setDialogueFont(color);
		Logger.log("Dialogue font color updated");
	}
	
	private void setCreditsFontColor(Color color)
	{
		Logger.log("Setting credits font color ("+color.toString()+")");
		vGamePointer.setCreditsFont(color);
		Logger.log("Credits font color updated");
	}
	
	private void setCreditsFont(Color color, int sizeHeader, int sizeText)
	{
		Logger.log("Setting Credits font properties (color={"+color.toString()+"}, size={header="+sizeHeader+", text="+sizeText+"})");
		vGamePointer.setCreditsFont(color, sizeHeader, sizeText);
		Logger.log("Credits font updated");
	}
	
	/*--------------------
		Description
	--------------------*/
	
	private String vDescription;
	
	private void setDescriptionContent(String description)
	{
		if(description!=null)
			vDescription=description;
		else
			vDescription="";
	}
	
	String getDescription()
	{
		return vDescription;
	}
	
	/*--------------------
		Music
	--------------------*/
	
	private List<Music> vSoundTrack;
	
	private void addTrackList()
	{		
		vGamePointer.addMusicTracks(vSoundTrack);		
	};
	
	private int vCurrentMusic;
	
	private void playMusicByID(int id)
	{
		try
		{
			vGamePointer.getMusicByID(id).play();
			vCurrentMusic = id;
		}
		catch(GameException e)
		{
			ErrorReporter.display(e);
			vGamePointer.close(1);
		}		
	}
	
	private void stopCurrentMusic()
	{
		if(vCurrentMusic!=WRONG_VALUE)
			try
			{				
				vGamePointer.getMusicByID(vCurrentMusic).stop();
			}
			catch(GameException e)
			{
				ErrorReporter.display(e);
				vGamePointer.close(1);
			}		
	}
	
	/*--------------------
		Sounds
	--------------------*/
	
	private List<Sound> vSoundFX;
	
	private void addSFXList()
	{
		vGamePointer.addSFX(vSoundFX);		
	};
	
	private void playSFXByID(int id)
	{
		vGamePointer.playSound(id);
	}
	
	private Sound getSfx(int id)
	{
		if(id==NO_SOUND)
			return null;
		Sound sfx;
		
		try
		{
			sfx = vGamePointer.getSoundByID(id);
		}
		catch(GameException e)
		{
			sfx=null;
			ErrorReporter.display(e);
			vGamePointer.close(1);
		}
		return sfx;
	}
	
	/*--------------------
		Abilities
	--------------------*/
	
	private List<Ability> vAbilities;
	
	private int getAbilityIndex(int abilityID)
	{
		for(int i=0; i<vAbilities.size(); i++)
			if(vAbilities.get(i).getID()==abilityID)
				return i;
		return WRONG_VALUE;
	}
	
	private ArrayList<Ability> addNpcAbilities(int... id)
	{
		ArrayList<Ability> list = new ArrayList<Ability>();
		
		for(int i=0; i< id.length; i++)
		{
			int index = getAbilityIndex(id[i]);
			if(index!=WRONG_VALUE)
				list.add(vAbilities.get(index));				
		}		
		return list;
	}
	
	/*--------------------
		NPCs
	--------------------*/
	
	private List<NPC> vNPCs;
	
	private void addNpcList()
	{
		vGamePointer.addNPCs(vNPCs);
	}
	
	private NPC newNPC(int id, String name, int hp, int portrait, int effigy, List<Ability> abilities)
	{
		if(Logger.isActive())
		{	
			String abl="{";
			for(int i=0; i<abilities.size(); i++)
			{
				abl+="["+abilities.get(i).getID()+"]";
				abl+=abilities.get(i).getName();
				if(i<abilities.size()-1)
					abl+=", ";
			}
			abl+="}";
			
			Logger.log("Creating new NPC ["+id+"]"+name+"(hp="+hp+", portrait="+(portrait)+", effigy="+(effigy)+", abilities="+abl+")");
		}
		return new NPC(id, name, hp, portrait+vTexOffset, effigy+vTexOffset, abilities);
	}
	
	private NPC getNPCByID(int id)
	{
		return vGamePointer.getNPCByID(id);
	}
	
	private void setNpcStatusVisual(int npcID, int status, int effigyTex, int portraitTex)
	{
		Logger.log("Setting npc(id="+npcID+") status("+status+") new visual(effigy="+effigyTex+", portrait="+portraitTex+")");
		getNPCByID(npcID).setAppearance(effigyTex+vTexOffset, portraitTex+vTexOffset, status);
		Logger.log("NPC "+getNPCByID(npcID).getName()+" updated");
	}
	
	/*--------------------
		Backgrounds
	--------------------*/
	
	private List<Background> vBackgrounds;
	
	private void addBackgroundsList()
	{
		vGamePointer.addBackgroundsList(vBackgrounds);
	}
	
	private Background newBackground(int id, int texture)
	{
		Logger.log("Creating new Background id="+id+", texture="+texture);
		return new Background(id, texture+vTexOffset);
	}
	
	private void setBackground(int backgroundID)
	{		
		vGamePointer.changeCurrentBackground(backgroundID);
	}
	
	/*--------------------
		Dialogues
	--------------------*/
	
	private List<Dialogue> vDialogues;
	
	private void setDialogueBox(int panelTexture)
	{
		Logger.log("Setting new dialoge box texture ("+panelTexture+")");
		vGamePointer.setDiaBox(panelTexture);
	}
	
	private Dialogue newDialogue(int id, int background, DialogueElement...elements)
	{		
		Logger.log("Creating new Dialogue("+elements.length+" elements) id="+id+", background="+background);
		return vGamePointer.build(id, background, elements);
	}
	
	private DialogueElement newDiaPart(int sayerID, String content, Runnable evt)
	{
		Logger.log("Creating new "+getNPCByID(sayerID).getName()+" dialogue line with event");
		return new DialogueElement(getNPCByID(sayerID), content, evt);
	}
	
	private DialogueElement newDiaPart(int sayerID, String content)
	{
		Logger.log("Creating new "+getNPCByID(sayerID).getName()+" dialogue line");
		return new DialogueElement(getNPCByID(sayerID), content);
	}
	
	private void addDialoguesList()
	{
		for(int i=0; i<vDialogues.size(); i++)
			vScenario.addDia(vDialogues.get(i));
	}
	
	private void playDia(int diaID)
	{
		try
		{
			vScenario.findDia(diaID);
			vScenario.playDia();
		}
		catch(GameException e)
		{
			ErrorReporter.display(e);
			vGamePointer.close(1);
		}
	}
	
	/*--------------------
		Battles
	--------------------*/
	
	private void addBattlefield(int id, int bgID, int hudID)
	{
		Logger.log("Creating new Arena id="+id+", background="+bgID+", hud="+hudID);
		vGamePointer.addBattlefield(id, bgID, hudID);
	}
	
	private void startBattle(int heroID, int enemyID, int arenaID, int scenarioJumpIndexVictory, int scenarioJumpIndexLose)
	{
		vGamePointer.startBattle(heroID, enemyID, arenaID, scenarioJumpIndexVictory, scenarioJumpIndexLose);
	}
	
	/*--------------------
		Static scenes
	--------------------*/	
	
	private void drawStaticTextScene(int background, int textPosX, int textPosY, boolean lr, String... texts)
	{
		try
		{
			vGamePointer.drawStaticTextScene(background, textPosX, Graphics.DEFAULT_WINDOW_HEIGHT-textPosY, lr, texts);
		}
		catch(GameException e)
		{
			ErrorReporter.display(e);
			vGamePointer.close(1);
		}
	}
	
	private void drawBlackScene(int size, Color color, String... text)
	{
		vGamePointer.drawBlackScene(size, color, text);
	}
	
	private void drawBlackScene(int width, int height, int texture)
	{
		vGamePointer.drawBlackScene(width, height, texture);
	}
	
	/*--------------------
		Credits
	--------------------*/
	private void buildNewCredits(int background, int speed, int distance, CreditsElement...elements)
	{
		Logger.log("Creating new credits ("+elements.length+" elements)");
		vGamePointer.buildCredits(background, speed, distance, elements);
	}
	
	private void buildNewCredits(int background, CreditsElement...elements)
	{
		Logger.log("Creating new credits ("+elements.length+" elements)");
		vGamePointer.buildCredits(background, CREDITS_SPEED, CREDITS_SPACE, elements);
	}
	
	private void buildNewCredits(int speed, int distance, CreditsElement...elements)
	{
		Logger.log("Creating new credits ("+elements.length+" elements)");
		vGamePointer.buildCredits(speed, distance, elements);
	}
	
	private void buildNewCredits(CreditsElement...elements)
	{
		Logger.log("Creating new credits ("+elements.length+" elements)");
		vGamePointer.buildCredits(CREDITS_SPEED, CREDITS_SPACE, elements);
	}
	
	private void playCreditsScene()
	{
		vGamePointer.playCredits();
	}
	
	private CreditsElement newCreditsElement(String header, String...texts)
	{
		Logger.log("Creating credits text element");
		return vGamePointer.createCreditsElement(header, texts);
	}
	
	private CreditsElement newCreditsElement(int width, int height, int texture)
	{
		Logger.log("Creating credits image element");
		return vGamePointer.createCreditsElement(width, height, texture);
	}
	
	private CreditsElement newCreditsElement(int height)
	{
		Logger.log("Creating credits empty element");
		return vGamePointer.createCreditsElement(height);
	}
	
	/*--------------------
		Scenario
	--------------------*/
	
	private Scenario vScenario;
	
	private void addScenario()
	{
		vGamePointer.setScenario(vScenario);
	}
	
	/*##########################################
		Protected functions - To create game
	##########################################*/
	
	/*--------------------
		System
	--------------------*/
	
	/**
	 * Empty function.<br>
	 * You can set system properties and windows labels here, overriding this function using:<br>
	 * <code>
	 * setExitDialog(String question, String yes, String no);</br>
	 * setSaveWindow(String content, String btnSaveLabel, String btnCancelLabel, String overwriteLabel, String btnYesLabel, String btnNoLabel, String btnOKLabel, String successLabel);</br>
	 * setExitDialog(String question, String yes, String no);</br>
	 * setMenuWindow(String content, String btnSaveLabel, String btnExitLabel, String btnBackLabel);</br>
	 * setWarnings(String saveEmptyNameWarning, String saveTooLongNameWarning)
	 * </code>
	 * You can check custom options values here too.
	 */
	protected void setSystemProperties()
	{
		//EMPTY
	}
	
	/**
	 * Apply custom descriptions to Exit window elements
	 * @param question label
	 * @param yes accept button
	 * @param no reject button
	 */
	protected void setExitDialog(String question, String yes, String no)
	{
		setExitDialogWindow(question, yes, no);
	}
	
	/**
	 * Apply custom descriptions for Save window elements
	 * @param content label
	 * @param btnSaveLabel save button
	 * @param btnCancelLabel cancel button
	 * @param overwriteLabel save overwriting confirmation label
	 * @param btnYesLabel accept button
	 * @param btnNoLabel reject button
	 * @param btnOKLabel confirm (OK) button
	 * @param successLabel Saving success information
	 */
	protected void setSaveWindow(String content, String btnSaveLabel, String btnCancelLabel, String overwriteLabel, String btnYesLabel, String btnNoLabel, String btnOKLabel, String successLabel)
	{
		setSaveDialogWindow(content, btnSaveLabel, btnCancelLabel, overwriteLabel, btnYesLabel, btnNoLabel, btnOKLabel, successLabel);
	}
	
	/**
	 * Apply custom descriptions for Menu window elements
	 * @param content label
	 * @param btnSaveLabel save button
	 * @param btnExitLabel exit game button
	 * @param btnExitLabel back to game button
	 */
	protected void setMenuWindow(String content, String btnSaveLabel, String btnExitLabel, String btnBackLabel)
	{
		setMultiDialogWindow(content, btnSaveLabel, btnExitLabel, btnBackLabel);
	}
	
	/**
	 * Set custom wrong save name warnings
	 * @param saveEmptyNameWarning when save name string has no chars
	 * @param saveTooLongNameWarning when save name string has too many chars
	 */
	protected void setWarnings(String saveEmptyNameWarning, String saveTooLongNameWarning)
	{
		setTextWarnings(saveEmptyNameWarning, saveTooLongNameWarning);
	}
	
	/**
	 * Game closing function
	 */
	protected void exit()
	{
		vGamePointer.close();
	}
	
	/**
	 * Function closing game with code 1
	 */
	protected void shutdown()
	{
		forceShutdown();
	}
		
	/**
	 * Function closing game with code 1
	 * Display exception information before shutting down.
	 */
	protected void shutdown(Exception e)
	{
		exceptionShutdown(e);
	}
	
	/**
	 * Empty function.<br>
	 * You can add custom launcher options here, overriding this function using:<br>
	 * <code>
	 * addOption(String label, String... options)
	 * </code>
	 */
	protected void addCustomOptions()
	{
		//EMPTY
	}
	
	
	protected int getCustomOptionValue(int index)
	{
		return vCustomOptions.get(index).getValue();
	}
	
	/**
	 * Add new custom option to game launcher. Options will be added to ArrayList on successive indices, starting from 0<br><br>
	 * Example:<br>
	 * <code>
	 * addOption("My option 1", "choice", "other choice");<br>
	 * addOption("My option 2", choices[]);
	 * </code>
	 * @param label launcher option label
	 * @param options options strings displayed in combo box on successive indices, starting from 0. Indices are used to check chosen option.
	 */
	protected void addOption(String label, String... options)
	{
		vCustomOptions.add(new CustomOption(label, options));
	}
	
	/*--------------------
		Fonts
	--------------------*/
	
	/**
	 * Empty function.<br>
	 * You can change default font file, overriding this function using:<br>
	 * setFontFile(String fontName)<br>
	 * selectFontFile(String fontName, int firstGlyph, int lastGlyph)
	 * </code>
	 */
	protected void selectGameFontFile()
	{
		//EMPTY
	}
	
	/**
	 * Set new font as default
	 * @param fontName font file (file must exists inside zip archive "fonts.archive")
	 */
	protected void setFontFile(String fontName)
	{
		selectFont(fontName);
	}
	
	/**
	 * Set new font as default with loading custom glyphs range
	 * @param fontName font file (file must exists inside zip archive "fonts.archive")
	 * @param firstGlyph first glyph
	 * @param laseGlyph	last glyph
	 */
	protected void selectFontFile(String fontName, int firstGlyph, int lastGlyph)
	{
		selectFont(fontName, firstGlyph, lastGlyph);
	}
	
	/**
	 * Set new font as default with loading custom glyphs collection
	 * @param fontName font file (file must exists inside zip archive "fonts.archive")
	 * @param glyphs glyphs collection to load
	 */
	protected void selectFontFile(String fontName, String glyphs)
	{
		selectFont(fontName, glyphs);
	}
		
	/**
	 * Set color of font displayed in story scenes
	 * @param color java.awt.Color
	 */
	protected void setSceneTextColor(Color color)
	{
		setSceneFontColor(color);
	}
	
	/**
	 * Set color and size of font displayed in story scenes
	 * @param color java.awt.Color
	 * @param size font size
	 */
	protected void setSceneTextProperties(Color color, int size)
	{
		setSceneFont(color, size);
	}
	
	/**
	 * Set color of font displayed in battle mode
	 * @param color java.awt.Color
	 */
	protected void setBattleTextColor(Color color)
	{
		setBattleFont(color);
	}
	
	/**
	 * Set color of font for inactive abilities in battle mode
	 * @param color java.awt.Color
	 */
	protected void setDisabledTextColor(Color color)
	{
		setDisabledFont(color);
	}
	
	/**
	 * Set color of dialogue font
	 * @param color java.awt.Color
	 */
	protected void setDialogueTextColor(Color color)
	{
		setDialogueFont(color);
	}
	
	/**
	 * Set color of font displayed in credits
	 * @param color java.awt.Color
	 */
	protected void setCreditsTextColor(Color color)
	{
		setCreditsFontColor(color);
	}
	
	/**
	 * Set color and size of font displayed in credits
	 * @param color java.awt.Color
	 * @param sizeHeader size of font for section header
	 * @param sizeText size of font for section items
	 */
	protected void setCreditsTextProperties(Color color, int sizeHeader, int sizeText)
	{
		setCreditsFont(color, sizeHeader, sizeText);
	}
	
	/**
	 * Empty function.<br>
	 * You can change default launcher fonts, overriding this function using:<br>
	 * setLauncherTitleFont(String font, int size)<br>
	 * setLauncherDescriptionFont(String font, int size)
	 * </code>
	 */
	protected void selectLauncherFonts()
	{
		//EMPTY
	}
	
	/**
	 * Set new font for game title which will be displayed in launcher
	 * @param font font file (file must exists inside zip archive "fonts.archive")
	 * @param size font size
	 */
	protected void setLauncherTitleFont(String font, int size)
	{
		setLauncherTitleFnt(font, size);
	}
	
	/**
	 * Set new font for game description which will be displayed in launcher
	 * @param font font file (file must exists inside zip archive "fonts.archive")
	 * @param size font size
	 */
	protected void setLauncherDescriptionFont(String font, int size)
	{
		setLauncherDescFnt(font, size);
	}
	
	/*--------------------
		Description
	--------------------*/
	
	/**
	 * Empty function.<br>
	 * You can add game description here, overriding this function using:<br>
	 * <code>
	 * setDescription(String description)
	 * </code>
	 * Description will be displayed in game launcher.
	 */
	protected void createDescription()
	{
		//EMPTY
	}
	
	/**
	 * Set game description, which will be displayed in game launcher
	 * @param description game description
	 */
	protected void setDescription(String description)
	{
		setDescriptionContent(description);
	}
	
	/*--------------------
		Music
	--------------------*/
	
	/**
	 * Empty function.<br>
	 * You can add game soundtrack here, overriding this function using:<br>
	 * <code>
	 * addMusicTrack(int id, String file)
	 * </code>
	 */
	protected void buildTrackList()
	{
		//EMPTY
	}
	
	/**
	 * Add new Music to game
	 * @param id Track ID
	 * @param file OGG file name
	 */
	protected void addMusicTrack(int id, String file)
	{
		vSoundTrack.add(new Music(id, file));
	}
	
	/**
	 * Play Music by ID.</br>
	 * May be used in scenario/dialogue events</br>
	 * Only one music track may be played at the same time.<br>
	 * If other track is played, it will be stopped after using function.</br>
	 * If same track is played, function cause no effect.
	 * @param id Track ID
	 */
	protected void playMusic(int id)
	{
		playMusicByID(id);
	}
	
	/**
	 * Stop current music</br>
	 * May be used in scenario/dialogue events
	 */
	protected void stopMusic()
	{
		stopCurrentMusic();
	}
	
	/*--------------------
		Sounds
	--------------------*/
	
	/**
	 * Empty function.<br>
	 * You can add game sound effects here, overriding this function using:<br>
	 * <code>
	 * addSound(int id, String file)
	 * </code>
	 */
	protected void buildSoundsList()
	{
		//EMPTY
	}
	
	/**
	 * Add new Sound to game
	 * @param id sound ID
	 * @param file OGG file name
	 */
	protected void addSound(int id, String file)
	{
		vSoundFX.add(new Sound(id, file));
	}
	
	/**
	 * play Sound by ID.</br>
	 * May be used in scenario/dialogue events</br>
	 * Multiple different sounds may be played at the same time.
	 * @param id Sound ID
	 */
	protected void playSound(int id)
	{
		playSFXByID(id);
	}
	
	/*--------------------
		Abilities
	--------------------*/
		
	/**
	 * Empty function.<br>
	 * You can add NPC abilities here, overriding this function using:<br>
	 * <code>
	 * addAbility(int id, int category, String name, int sfx, int texture, int power, int speed, int space, int seriesSize, int cost)</br>
	 * addAttack(int id, String name, int sfx, int texture, int power, int speed, int space, int seriesSize, int cost)</br>
	 * addAttack(int id, String name, int power, int seriesSize, int cost)</br>
	 * addHeal(int id, String name, int sfx, int texture, int power, int speed, int space, int seriesSize, int cost)</br>
	 * addHeal(int id, String name, int power, int seriesSize, int cost)</br>
	 * </code>
	 */
	protected void buildAbilitiesList()
	{
		//EMPTY
	}
	
	/**
	 * Add new ability
	 * @param id Ability ID
	 * @param category constant integer value ATTACK or HEAL
	 * @param name Ability name
	 * @param sfx sound id or NO_SOUND constant value
	 * @param texture texture number (number in texture file name, between "tex_" prefix and file extension) or NO_TEXTURE constant value
	 * @param power 100% Ability effect value
	 * @param speed ideograms moving down speed
	 * @param space vertical distance between ideograms lines
	 * @param seriesSize quantity of generated ideograms
	 * @param cost AP needed to use Ability
	 */
	protected void addAbility(int id, int category, String name, int sfx, int texture, int power, int speed, int space, int seriesSize, int cost)
	{		
		if(category == ATTACK)
		{
			Logger.log("Creating new Ability Attack ["+id+"]"+name+"(sound="+sfx+", image="+texture+", power="+power+", speed="+speed+", space="+space+", size="+seriesSize+", cost="+cost+")");
			vAbilities.add(new Attack(id, name, getSfx(sfx), tex(texture), power, speed, space, seriesSize, cost));
		}
		else if(category == HEAL)
		{
			Logger.log("Creating new Ability Heal ["+id+"]"+name+"(sound="+sfx+", image="+texture+", power="+power+", speed="+speed+", space="+space+", size="+seriesSize+", cost="+cost+")");
			vAbilities.add(new Heal(id, name, getSfx(sfx), tex(texture), power, speed, space, seriesSize, cost));
		}
	}
	
	/**
	 * Add new Attack Ability
	 * @param id Ability ID
	 * @param name Ability name
	 * @param sfx sound id or NO_SOUND constant value
	 * @param texture texture number (number in texture file name, between "tex_" prefix and file extension) or NO_TEXTURE constant value
	 * @param power 100% Ability effect value
	 * @param speed ideograms moving down speed
	 * @param space vertical distance between ideograms lines
	 * @param seriesSize quantity of generated ideograms
	 * @param cost AP needed to use Ability
	 */
	protected void addAttack(int id, String name, int sfx, int texture, int power, int speed, int space, int seriesSize, int cost)
	{
		Logger.log("Creating new Ability Attack ["+id+"]"+name+"(sound="+sfx+", image="+texture+", power="+power+", speed="+speed+", space="+space+", size="+seriesSize+", cost="+cost+")");
		vAbilities.add(new Attack(id, name, getSfx(sfx), tex(texture), power, speed, space, seriesSize, cost));		
	}
	
	/**
	 * Add new Attack Ability, without image and SFX, with default values: [speed=1 space=200]
	 * @param id Ability ID
	 * @param name Ability name
	 * @param power 100% Ability effect value
	 * @param seriesSize quantity of generated ideograms
	 * @param cost AP needed to use Ability
	 */
	protected void addAttack(int id, String name, int power, int seriesSize, int cost)
	{
		Logger.log("Creating new Ability Attack ["+id+"]"+name+"(power="+power+", size="+seriesSize+", cost="+cost+")");
		vAbilities.add(new Attack(id, name, power, seriesSize, cost));		
	}
	
	/**
	 * Add new Heal Ability
	 * @param id Ability ID
	 * @param name Ability name
	 * @param sfx sound id or NO_SOUND constant value
	 * @param texture texture number (number in texture file name, between "tex_" prefix and file extension) or NO_TEXTURE constant value
	 * @param power 100% Ability effect value
	 * @param speed ideograms moving down speed
	 * @param space vertical distance between ideograms lines
	 * @param seriesSize quantity of generated ideograms
	 * @param cost AP needed to use Ability
	 */
	protected void addHeal(int id, String name, int sfx, int texture, int power, int speed, int space, int seriesSize, int cost)
	{
		Logger.log("Creating new Ability Heal ["+id+"]"+name+"(sound="+sfx+", image="+texture+", power="+power+", speed="+speed+", space="+space+", size="+seriesSize+", cost="+cost+")");
		vAbilities.add(new Heal(id, name, getSfx(sfx), tex(texture), power, speed, space, seriesSize, cost));
	}
	
	/**
	 * Add new Heal Ability, without image and SFX, with default values: [speed=1 space=200]
	 * @param id Ability ID
	 * @param name Ability name
	 * @param power 100% Ability effect value
	 * @param seriesSize quantity of generated ideograms
	 * @param cost AP needed to use Ability
	 */
	protected void addHeal(int id, String name, int power, int seriesSize, int cost)
	{
		Logger.log("Creating new Ability Heal ["+id+"]"+name+"(power="+power+", size="+seriesSize+", cost="+cost+")");
		vAbilities.add(new Heal(id, name, power, seriesSize, cost));
	}
	
	/*--------------------
		NPCs
	--------------------*/	
	
	/**
	 * Empty function.<br>
	 * You can add game NPC here, overriding this function using:<br>
	 * <code>
	 * addNpc(int id, String name, int hp, int portrait, int effigy, int... abilitiesIDs)
	 * </code>
	 * NOTICE: player-controlled character is an object of NPC class too. All NPCs may be assigned to be controlled by player, depending on the game scenario.
	 */
	protected void buildNpcList()
	{
		//EMPTY
	}
	
	/**
	 * Empty function.<br>
	 * You can set NPC Attributes [HP, current status, visuals] here, overriding this function using:<br>
	 * <code>
	 * setHpMax(int npcID, int value)</br>
	 * setStatus(int npcID, int status)</br>
	 * setStatusVisual(int npcID, int status, int effigyTex, int portraitTex)
	 * </code>
	 */
	protected void setNpcAttributes()
	{
		//EMPTY
	}
	
	/**
	 * Set NPC's max hit points value<br>
	 * NOTICE: may be used in scenario/dialogue events
	 * @param npcID NPC ID
	 * @param value new max HP value
	 */
	protected void setHpMax(int npcID, int value)
	{
		getNPCByID(npcID).setHpMax(value);
	}
	
	/**
	 * Change current NPC's status<br>
	 * NOTICE: may be used in scenario/dialogue events
	 * @param npcID NPC ID
	 * @param status accepts constant integer values: {NEUTRAL, SMILE, HAPPY, SAD, CRY, ANGRY, FINE, HURT, AGONY, GLORY, EVIL, ARROGANCE, CORRUPT, CUSTOM_STATUS_1, CUSTOM_STATUS_2, CUSTOM_STATUS_3, CUSTOM_STATUS_4, CUSTOM_STATUS_5}
	 */
	protected void setStatus(int npcID, int status)
	{
		getNPCByID(npcID).setStatus(status);
	}
	
	/**
	 * Set effigy (used only in dialogue mode) and portrait (used only in battle mode) for NPC's status.<br>
	 * NOTICE: Only FINE, HURT and AGONY statuses are used in battle mode.
	 * @param npcID NPC ID
	 * @param status accepts constant integer values: {NEUTRAL, SMILE, HAPPY, SAD, CRY, ANGRY, FINE, HURT, AGONY, GLORY, EVIL, ARROGANCE, CORRUPT, CUSTOM_STATUS_1, CUSTOM_STATUS_2, CUSTOM_STATUS_3, CUSTOM_STATUS_4, CUSTOM_STATUS_5}
	 * @param effigyTex effigy texture number (number in texture file name, between "tex_" prefix and file extension)
	 * @param portraitTex portrait texture number (number in texture file name, between "tex_" prefix and file extension)
	 */
	protected void setStatusVisual(int npcID, int status, int effigyTex, int portraitTex)
	{
		setNpcStatusVisual(npcID, status, effigyTex, portraitTex);
	}
	
	/**
	 * Add new NPC to game characters list.
	 * @param id NPC ID
	 * @param name NPC's name
	 * @param hp NPC's max hit points value
	 * @param portrait default portrait texture number (number in texture file name, between "tex_" prefix and file extension). Assigned to all statuses.
	 * @param effigy default effigy texture number (number in texture file name, between "tex_" prefix and file extension). Assigned to all statuses.
	 * @param abilitiesIDs ID's of abilities, which NPC can use in battle mode
	 */
	protected void addNpc(int id, String name, int hp, int portrait, int effigy, int... abilitiesIDs)
	{
		vNPCs.add(newNPC(id, name, hp, portrait, effigy, addNpcAbilities(abilitiesIDs)));
	}
	
	/*--------------------
		Backgrounds
	--------------------*/
	
	/**
	 * Empty function.<br>
	 * You can add game backgrounds here, overriding this function using:<br>
	 * <code>
	 * addBackground(int id, int texture)
	 * </code>
	 */
	protected void buildBackgroundsList()
	{
		//EMPTY
	}
	
	/**
	 * Add new background to game
	 * @param id background ID
	 * @param texture background texture number (number in texture file name, between "tex_" prefix and file extension).
	 */
	protected void addBackground(int id, int texture)
	{
		vBackgrounds.add(newBackground(id, texture));
	}
	
	/**
	 * Change current background. Used in scenario/dialogue events
	 * @param backgroundID new background ID
	 */
	protected void changeBackground(int backgroundID)
	{
		setBackground(backgroundID);
	}
	
	/*--------------------
		Dialogues
	--------------------*/
	
	/**
	 * Empty function.<br>
	 * You can set dialogue lines background here, overriding this function using:<br>
	 * <code>
	 * setDialoguePanel(int texture)
	 * </code>
	 */
	protected void buildDialoguePanel()
	{
		//EMPTY
	}
	
	/**
	 * Set dialogue lines background
	 * @param texture background texture number (number in texture file name, between "tex_" prefix and file extension).
	 */
	protected void setDialoguePanel(int texture)
	{
		setDialogueBox(texture);
	}
	
	/**
	 * Empty function.<br>
	 * You can create dialogues here, overriding this function using:<br>
	 * <code>
	 * addDialogue(int id, int background, DialogueElement...parts)
	 * </code>
	 */
	protected void buildDialoguesList()
	{
		//EMPTY
	}
	
	/**
	 * Add new dialogue to game
	 * @param id dialogue ID
	 * @param background dialogue background
	 * @param parts DialogueElements, created using function:  <i>part(int sayerID, String content)</i> or  <i>part(int sayerID, String content, Runnable event)</i>.
	 */
	protected void addDialogue(int id, int background, DialogueElement...parts)
	{
		vDialogues.add(newDialogue(id, background, parts));
	}
	
	/**
	 * Single NPC dialogue line with other simultaneously running event.</br>
	 * @param sayerID ID of NPC, which says this dialogue part
	 * @param content dialogue line
	 * @param event Event, running with displaying dialogue line. Event may be empty or may be any function. Event will be running looped until dialogue line changes.
	 * @return new DialogueElement with event. May be use in <i>addDialogue(int id, int background, DialogueElement...parts)</i> function.
	 */
	protected DialogueElement part(int sayerID, String content, Runnable event)
	{
		return newDiaPart(sayerID, content, event);
	}
	
	/**
	 * Single NPC dialogue line without running other events.</br>
	 * @param sayerID ID of NPC, which says this dialogue part.
	 * @param content dialogue line
	 * @return new DialogueElement without event. May be use in <i>addDialogue(int id, int background, DialogueElement...parts)</i> function.
	 */
	protected DialogueElement part(int sayerID, String content)
	{
		return newDiaPart(sayerID, content);
	}
	
	/**
	 * Play dialogue by ID. Used as scenario event
	 * @param dialogueID dialogue ID
	 */
	protected void playDialogue(int dialogueID)
	{
		playDia(dialogueID);
	}
	
	/*--------------------
		Battles
	--------------------*/
	
	/**
	 * Empty function.<br>
	 * You can add battle arenas here, overriding this function using:<br>
	 * <code>
	 * addArena(int id, int bgID, int hudID)
	 * </code>
	 */
	protected void buildArenasList()
	{
		//EMPTY
	}
	
	/**
	 * Add new battle arena to game
	 * @param id arena ID
	 * @param bgID	background id
	 * @param hudID hud id .Hud is especially prepared background, with hollow in center. Through that hollow, player can see falling down ideograms.
	 */
	protected void addArena(int id, int bgID, int hudID)
	{
		addBattlefield(id, bgID, hudID);
	}
	
	/**
	 * Start new battle. Change current mode to battle mode. Used in scenario events.
	 * @param playerID ID of character, controlled by player.
	 * @param enemyID Opponent ID
	 * @param arenaID Battle arena ID
	 * @param jumpVictory Index of scenario part, which starts after winning battle. (From 0 to scenes count-1)
	 * @param jumpLose Index of scenario part, which starts after losing battle. (From 0 to scenes count-1)
	 */
	protected void fight(int playerID, int enemyID, int arenaID, int jumpVictory, int jumpLose)
	{
		startBattle(playerID, enemyID, arenaID, jumpVictory, jumpLose);
	}
		
	/*--------------------
		Static scenes
	--------------------*/	
	
	/**
	 * Display several texts on background in the center of the window. Current text is displayed until pressing enter key. Display several texts on background in the center of the window. Current text is displayed until pressing enter key. Text is displayed letter by letter, expanding from origin.
	 * @param background Background ID
	 * @param texts Texts, which will be displayed in sequence on background.
	 */
	protected void playStaticScene(int background, String... texts)
	{
		drawStaticTextScene(background, CENTER_X, CENTER_Y, false, texts);
	}
	
	/**
	 * Display several texts on background in custom position.</br>
	 * Used as scene event.</br>
	 * Current text is displayed until pressing enter key.</br>
	 * Text is displayed letter by letter, from left to right or expanding from origin.
	 * @param background Background ID
	 * @param textPosX Horizontal position of text
	 * @param textPosY Vertical position of text
	 * @param leftToRight If TRUE, text will be displayed from left to right. Else will expanding from origin.
	 * @param texts Texts, which will be displayed in sequence on background.
	 */
	protected void playStaticScene(int background, int textPosX, int textPosY, boolean leftToRight, String... texts)
	{
		drawStaticTextScene(background, textPosX, textPosY, leftToRight, texts);
	}
	
	/**
	 * Display multiline text on black screen.</br>
	 * Used as scene event.</br>
	 * Is displayed until pressing enter key.
	 * @param size fFnt size
	 * @param color font color (java.awt.Color)
	 * @param texts text lines
	 */
	protected void playTextOnBlack(int size, Color color, String... texts)
	{
		drawBlackScene(size, color, texts);
	}
	
	/**
	 * Display image on black screen.</br>
	 * Used as scene event.</br>
	 * Is displayed until pressing enter key.
	 * @param width Image width
	 * @param height Image height
	 * @param texture Image texture number (number in texture file name, between "tex_" prefix and file extension).
	 */
	protected void playImageOnBlack(int width, int height, int texture)
	{
		drawBlackScene(width, height, texture);
	}
	
	/*--------------------
		Credits
	--------------------*/
	
	/**
	 * Empty function.<br>
	 * You can build game credits here, overriding this function using:<br>
	 * <code>
	 * buildCredits(int background, int speed, int distance, CreditsElement...elements)
	 * </code>
	 */
	protected void buildGameCredits()
	{
		//EMPTY
	}
	
	/**
	 * Display credits. Used as scene event.</br>
	 * Automatically go to next scene, when ends. May be skipped by pressing enter key.</br>
	 * NOTICE: In game may exists only one credits event. If is needed displaying other credits event, it must be built in earlier scene event.
	 */
	protected void playCredits()
	{
		playCreditsScene();
	}
	
	/**
	 * Create new credits section, contains header and text items
	 * @param header Section header
	 * @param texts Section items
	 * @return new CreditsElement object - credits text section
	 */
	protected CreditsElement creditsLine(String header, String...texts)
	{
		return newCreditsElement(header, texts);
	}
	
	/**
	 * Create new credits section, contains header and text items
	 * @param width Image width
	 * @param height Image height
	 * @param texture Image texture number (number in texture file name, between "tex_" prefix and file extension).
	 * @return new CreditsElement object - credits image section
	 */
	protected CreditsElement creditsImage(int width, int height, int texture)
	{
		return newCreditsElement(width, height, texture);
	}
	
	/**
	 * Create new empty section (filler)
	 * @param height section height
	 * @return new CreditsElement object - empty credits section (filler)
	 */
	protected CreditsElement creditsSpace(int height)
	{
		return newCreditsElement(height);
	}
	
	/**
	 * Build new credits with custom background
	 * @param background Background ID
	 * @param speed	Credits speed
	 * @param distance Distance between sections
	 * @param elements CreditsElement objects, created using functions: <i>creditsLine(String header, String...texts)</i>, <i>creditsImage(int width, int height, int texture)</i>, and <i>CreditsElement creditsSpace(int height)</i>
	 */
	protected void buildCredits(int background, int speed, int distance, CreditsElement...elements)
	{
		buildNewCredits(background, speed, distance, elements);
	}
	
	/**
	 * Build new credits with custom background, default speed value = 1 and default distance = 30
	 * @param background Background ID	
	 * @param elements CreditsElement objects, created using functions: <i>creditsLine(String header, String...texts)</i>, <i>creditsImage(int width, int height, int texture)</i>, and <i>CreditsElement creditsSpace(int height)</i>
	 */
	protected void buildCredits(int background, CreditsElement...elements)
	{
		buildNewCredits(background, elements);
	}
	
	/**
	 * Build new credits on black screen
	 * @param speed	Credits speed
	 * @param distance Distance between sections
	 * @param elements CreditsElement objects, created using functions: <i>creditsLine(String header, String...texts)</i>, <i>creditsImage(int width, int height, int texture)</i>, and <i>CreditsElement creditsSpace(int height)</i>
	 */
	protected void buildCredits(int speed, int distance, CreditsElement...elements)
	{
		buildNewCredits(speed, distance, elements);
	}
	
	/**
	 * Build new credits on black screen, with default speed value = 1 and default distance = 30
	 * @param elements CreditsElement objects, created using functions: <i>creditsLine(String header, String...texts)</i>, <i>creditsImage(int width, int height, int texture)</i>, and <i>CreditsElement creditsSpace(int height)</i>
	 */
	protected void buildCredits(CreditsElement... elements)
	{
		buildNewCredits(elements);
	}
	
	
	/*--------------------
		Scenario
	--------------------*/
	
	/**
	 * Empty function.<br>
	 * You can build game scenario here, overriding this function using:<br>
	 * <code>
	 * addEvent(Runnable event)
	 * </code>
	 * NOTE: Scenario events has no identifications number. Access to scene is possible only by index. Indices are added in ascending order, from 0.
	 */
	protected void buildScenario()
	{
		//EMPTY
	}
	
	/**
	 * Add new scene (scenario event)
	 * @param event Scenario event, which may be empty or may be any function. Event will be running looped until scene changes.
	 */
	protected void addEvent(Runnable event)
	{
		vScenario.addEvt(event);
	}
	
	/**
	 * Play scenario from pointed event
	 * @param scene Event index. (From 0 to scenes count-1)
	 */
	protected void jump(int scene)
	{
		vScenario.jump(scene);
	}
}
