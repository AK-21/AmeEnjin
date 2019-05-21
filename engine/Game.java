package engine;


import static engine.GameFont.*;
import static engine.GameTexture.*;
import static engine.Ideogram.*;
import static engine.IdeogramsArray.ALL_IDEOGRAMS;

import java.awt.Color;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;


import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;

class Game
{	
	static final int FOCUS_STARTGAME=0;
	static final int FOCUS_EXIT=1;
	static final int FOCUS_BATTLE=2;	
	static final int FOCUS_PLOT=3;
	
	static final int EXIT_MENU=1;
	static final int MULTI_MENU=2;
	static final int SAVE_MENU=3;
	
	static final int WRONG_VALUE = -1;
	
	static final int TEXT_TYPE_RATIO = 2;
	
	static final float FADE_RATIO = 0.02f;
	
	static final int START_DISPLAY_TIME = 100;
	
	Game(String title, boolean category) throws GameException
	{
		vTitle = title;
		initGame(category);			
		loadResources();
	}
	
	Game(String title, String ideogramsTexDirectory, String[] transcriptionArray) throws GameException
	{
		vTitle = title;
		initGame(ideogramsTexDirectory, transcriptionArray);			
		loadResources();
	}
	
	/*-----------------------------------
		NPCs
	-----------------------------------*/
	
	private List<NPC> vNPCs;
	
	protected NPC newNPC(int id, String name, int hp, int portrait, int effigy, List<Ability> abilities)
	{
		return new NPC(id, name, hp, portrait+vTextureNumberOffset, effigy+vTextureNumberOffset, abilities);
	}
	
	protected NPC newNPC(int id, String name, int portrait, int effigy, List<Ability> abilities)
	{
		return new NPC(id, name, portrait+vTextureNumberOffset, effigy+vTextureNumberOffset, abilities);
	}
	
	protected void addNPCs(List<NPC> npcs)
	{
		int length = npcs.size();
		vNPCs = new ArrayList<NPC>(length);
		for(int i=0; i<length; i++)
			vNPCs.add(npcs.get(i));
	}
	
	protected void setStatusVisual(int npcID, int status, int effigyTex, int portraitTex)
	{		
		getNPCByID(npcID).setAppearance(effigyTex+vTextureNumberOffset, portraitTex+vTextureNumberOffset, status);
	}
	
	protected NPC getNPC(int index)
	{
		if(index>=0 && index<vNPCs.size())
			return vNPCs.get(index);
		return null;
	}
	
	protected NPC trygetNPCByID(int id) throws GameException
	{		
		for(int i=0; i<vNPCs.size(); i++)
			if(vNPCs.get(i).getID()==id)
				return vNPCs.get(i);
		throw new GameException("NPC "+id+" not exists");
	}
	
	protected NPC getNPCByID(int id)
	{		
		NPC tmp;
		try
		{
			tmp=trygetNPCByID(id);
		}
		catch(GameException e)
		{
			tmp=null;
			ErrorReporter.display(e);
			close(1);
		}
		return tmp;
	}
	
	protected NPC npc(int id)
	{
		return getNPCByID(id);
	}
	
	/*-----------------------------------
		MUSIC
	-----------------------------------*/
	
	private List<Music> vMusicTracks;
	
	protected void addMusicTracks(List<Music> tracks)
	{
		int length = tracks.size();
		vMusicTracks = new ArrayList<Music>(length);
		for(int i=0; i<length; i++)
			vMusicTracks.add(tracks.get(i));
	}
	
	protected Music getMusic(int index) throws GameException
	{
		if(index>=0 && index<vMusicTracks.size())
			return vMusicTracks.get(index);
		throw new GameException("Cannot find music (index="+index+")");
	}
	
	protected Music getMusicByID(int id) throws GameException
	{
		for(int i=0; i<vMusicTracks.size(); i++)
			if(vMusicTracks.get(i).getID()==id)
				return vMusicTracks.get(i);
		throw new GameException("Cannot find music (id="+id+")");
	}
	
	/*-----------------------------------
		SOUNDS
	-----------------------------------*/
	
	private List<Sound> vSFXs;
	
	protected void addSFX(List<Sound> sounds)
	{
		int length = sounds.size();
		vSFXs = new ArrayList<Sound>(length);
		for(int i=0; i<length; i++)
			vSFXs.add(sounds.get(i));
	}
	
	protected Sound getSound(int index) throws GameException
	{
		if(index>=0 && index<vSFXs.size())
			return vSFXs.get(index);
		throw new GameException("Cannot find sound (index="+index+")");
	}
	
	protected Sound getSoundByID(int id) throws GameException
	{
		for(int i=0; i<vSFXs.size(); i++)
			if(vSFXs.get(i).getID()==id)
				return vSFXs.get(i);
		throw new GameException("Cannot find sound (id="+id+")");
	}
	
	private void resetSounds()
	{
		for(int i=0; i<vSFXs.size(); i++)
			vSFXs.get(i).reset();
	}
	
	void playSound(int id)
	{
		try
		{
			getSoundByID(id).play();
		}
		catch(GameException e)
		{
			ErrorReporter.display(e);
			close(1);
		}		
	}
	/*-----------------------------------
		TITLE
	-----------------------------------*/
	
	private String vTitle;
	
	String getTitle()
	{
		return vTitle;
	}
	
	/*-----------------------------------
				BACKGROUND
	-----------------------------------*/
	
	private int vCurrentBackground=WRONG_VALUE;
	private List<Background> vBackgrounds;
	
	void addBackgroundsList(List<Background> list)
	{
		if(list==null || list.size()<1)
			vBackgrounds = new ArrayList<Background>();
		else
		{
			vBackgrounds = list;
			vCurrentBackground = 0;
		}
	}
	
	Background getCurrentBackground() throws GameException
	{
		if(vCurrentBackground<vBackgrounds.size() && vCurrentBackground>=0)
			return vBackgrounds.get(vCurrentBackground);
		else throw new GameException("Background is not possible to be found!");
	}
	
	protected void setCurrentBackground(int id) throws GameException
	{		
		if(getCurrentBackground().getID()!=id)
		{
			int index = getBGIndexByID(id);
			if(index != WRONG_VALUE)
			{
				vCurrentBackground = index;
				Logger.log("Background changed to "+id);
			}
			else
				throw new GameException("Background with id="+id+" not exists!");
		}
	}
	
	void changeCurrentBackground(int id)
	{
		try
		{
			setCurrentBackground(id);
		}
		catch(GameException e)
		{
			ErrorReporter.display(e);
			close(1);
		}
	}
	
	private int getBGIndexByID(int id)
	{
		for(int i=0; i<vBackgrounds.size(); i++)
			if(vBackgrounds.get(i).getID()==id)
				return i;
		return WRONG_VALUE;
			
	}
	
	/*-----------------------------------
		BATTLE
	-----------------------------------*/

	private List<Battlefield> vBattlefields = new ArrayList<>();
	
	Battlefield getBattlefieldByID(int id) throws GameException
	{
		for(int i=0; i<vBattlefields.size(); i++)
			if(vBattlefields.get(i).getID()==id)
				return vBattlefields.get(i);
		throw new GameException("Cannot find Battlefield with id="+id+"!");
	}
	
	void addBattlefield(int id, int bgID, int hudID)
	{
		vBattlefields.add(generateBattlefield(id, vBackgrounds.get(getBGIndexByID(bgID)), vBackgrounds.get(getBGIndexByID(hudID))));
	}
	
	private Battle vCurrentBattle;
	
	private boolean vBattleGoesOn = false;
	
	protected void startBattle(NPC hero, NPC enemy, Battlefield arena, int scenarioJumpIndexVictory, int scenarioJumpIndexLose)
	{
		if(!vBattleGoesOn)
		{
			Logger.log("Beginning new Battle (["+hero.getID()+"]"+hero.getName()+" vs ["+enemy.getID()+"]"+enemy.getName()+")");
			arena.setPlayers(hero, enemy);			
			vCurrentBattle = new Battle(hero, enemy, arena, vIdeograms, scenarioJumpIndexVictory, scenarioJumpIndexLose);
			resetInputText();
			setMode(FOCUS_BATTLE);
			vCurrentBattle.begin();
			vBattleGoesOn = true;
		}
	}
	
	void startBattle(int heroID, int enemyID, int arenaID, int scenarioJumpIndexVictory, int scenarioJumpIndexLose)
	{		
		try
		{
			startBattle(getNPCByID(heroID), getNPCByID(enemyID), getBattlefieldByID(arenaID), scenarioJumpIndexVictory, scenarioJumpIndexLose);
		}
		catch(GameException e)
		{
			ErrorReporter.display(e);
			close(1);
		}
	}
	
	private void endBattle()
	{
		vCurrentMode = FOCUS_PLOT;
		
		vBattleGoesOn = false;
		vCurrentBattle.unsetDiaTime();
	}
	
	Battlefield generateBattlefield(int id, Background background, Background hud)
	{
		return new Battlefield(1, background, hud, 
				getDefaultFont().getFont());
	}
	
	Background generateBFBackground(int id, int texture)
	{
		return new Background(id, vTextureNumberOffset+texture);
	}
	
	/*-----------------------------------
			DRAW
	-----------------------------------*/	
	
	private float vFadeValue=1f;
	
	void draw(GameObject obj)
	{
		obj.draw(tex -> getTex(tex));
	}	
	
	void drawPortrait(NPC npc)
	{
		npc.drawPortrait(tex -> getTex(tex));
	}
	
	void drawDia(Dialogue dia)
	{
		try
		{
			if(vCurrentMode != FOCUS_BATTLE)
				draw(getCurrentBackground());
			dia.draw(tex -> getTex(tex));
		}
		catch(GameException e)
		{
			ErrorReporter.display(e);
			close(1);
		}
	}
	
	private boolean vFade = false;
	
	private void fadeOut()
	{
		if(vFadeValue<1)
			vFadeValue += FADE_RATIO;
		else
		{
			vFadeOutCompleted = true;
			vFadeOutTime = false;
			vFade=false;
		}
	}
	
	private void fadeIn()
	{
		if(vFadeValue>0)
			vFadeValue -= FADE_RATIO;
		else
		{
			vFadeInTime = false;
			vFadeInCompleted =true;
			vFade=false;
		}
	}
	
	private boolean vChangeSceneTime = false;
	private boolean vSceneChangingProcessInProgress = false;
	
	private void sceneFadeOut()
	{
			if(!vChangeSceneTime)
			{
				fadeOut();
				if(!vFadeOutTime)
					vChangeSceneTime=true;
			}		
	}
	
	private void sceneFadeIn()
	{
		if(!vChangeSceneTime)
		{
			fadeIn();
			if(vFadeInCompleted)
			{
				vSceneChangingProcessInProgress = false;
				vLocked = false;				
			}
		}		
	}
	
	private void changeScene()
	{		
		if(vChangeSceneTime)
		{
			if(vBattleGoesOn)
				endBattle();
			if(vNextSceneToPlay==WRONG_VALUE)
			{
				if(Logger.isActive())
					Logger.log("Going to scene "+vScenario.getNext());
				vScenario.next();
			}
			else
			{
				Logger.log("Jumping to scene "+vNextSceneToPlay);
				vScenario.jump(vNextSceneToPlay);
			}
			vStaticSceneEvt = false;
			vStaticSceneEvtOnce = false;
			vBlackScreenInit = false;
			vChangeSceneTime=false;
			vCreditsRunning = false;
			vCreditsReady=false;
			vFadeInTime = true;
			resetSounds();
			
		}
		else if(vFadeOutTime && !vFadeOutCompleted)
		{
			vLocked = true;
			sceneFadeOut();
		}
		else if(vFadeInTime && !vFadeInCompleted)
		{			
			vLocked = true;
			sceneFadeIn();
		}
	}
	
	private boolean vFadeOutTime = false;
	private boolean vFadeOutCompleted = false;
	
	private boolean vFadeInTime = true;
	private boolean vFadeInCompleted = false;
	
	
	private int vNextSceneToPlay = WRONG_VALUE;
	
	void scenarioNext()
	{
		scenarioJump(WRONG_VALUE);
	}
	
	void scenarioJump(int value)
	{
		vNextSceneToPlay = value;
		if(!vSceneChangingProcessInProgress)
		{			
			vFadeInCompleted = false;
			vFadeOutCompleted = false;
			vFadeOutTime = true;
			vSceneChangingProcessInProgress = true;
			vTyping = false;
		}
	}

	private boolean vScenarioFlagUp = false;
	
	private void funcScenarioFlagUp()
	{
		if(vScenarioFlagUp)
		{
			vScenario.flagUp();
			vScenarioFlagUp = false;
		}
	}
	
	void scenarioFlagUp()
	{
		vScenarioFlagUp = true;
	}
	
	/*-----------------------------------
		IDEOGRAMS
	-----------------------------------*/
	
	private int vAllIdeograms;
	
	private String[] vIdeogramsTranscriptionArray;
	
	private void createIdeogramsArray()
	{
		vIdeograms = new IdeogramsArray(vAllIdeograms);
		
		int counter = 0;
		
		while(counter<vAllIdeograms)
		{
			vIdeograms.add(new Ideogram(counter,counter,
					vIdeogramsTranscriptionArray[counter]));
			counter++;
		}		
	}
	
	private void setCategory(boolean category)
	{		
		if(category==CATEGORY_HIRAGANA)
			vIdeogramCatDir=DIR_HIRAGANA;
		else
			vIdeogramCatDir=DIR_KATAKANA;
		vIdeogramsTranscriptionArray = ROMAJI;
	}
	
	/*-----------------------------------
				TEXTURES
	-----------------------------------*/
	
	private TexArray vTextures;	
	
	private int vTextureNumberOffset = vAllIdeograms-1;
	
	private int vTexturesLimit;
	
	int getTextureNumberOffset()
	{
		return vTextureNumberOffset;
	}
	
	private void createTexArray()
	{
		try
		{
			countTextures();
		}
		catch (GameException e)
		{
			ErrorReporter.display(e);
			close(1);
		}
		vTextures = new TexArray(vTexturesLimit);
		addIdeogramsTextures();
	}
	
	private void addIdeogramsTextures()
	{
		String path = DIR_IDEOGRAM+File.separator+vIdeogramCatDir;
		int counter = 0;
		while(counter<vAllIdeograms)
		{
			vTextures.add(new GameTexture(counter,path+File.separator+(counter)+TEX_FILE_EXTENSION));
			counter++;
		}
	}
	
	private GameTexture getTex(int id)
	{
		GameTexture tmp;
		try
		{
			return vTextures.getItemByID(id);
		}
		catch(GameException e)
		{
			tmp=null;
			ErrorReporter.display(e);
			close(1);
		}
		return tmp;
	}		
	
	private void loadTextures()
	{		
		int i = 0;
		
		while(i+vAllIdeograms<vTexturesLimit)
		{
			vTextures.add(new GameTexture(vAllIdeograms+i,"tex_"+(i+1)+TEX_FILE_EXTENSION));
			i++;
		}		
	}
	
	private void countTextures() throws GameException
	{
		FilenameFilter filter = new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				if(name.endsWith(TEX_FILE_EXTENSION)&&name.startsWith("tex_"))
				{
					return true;
				} 
				else
				{
					return false;
				}
			}
		};		
				
		String[] list= new File(DIR_TEX).list(filter);
		
		if(list==null)
			throw new GameException("Building texture list is not possible!");
		
		vTexturesLimit = vAllIdeograms + list.length;
	}
	
	/*-----------------------------------
				PAUSE
	-----------------------------------*/
	
	
	void pause()
	{
		vGameLoop.pause();
	}
	
	void openExitMenu()
	{
		vGameLoop.pause();
		ExitDialog.invoke(()->exitAction(),()->backToGame());
	}
	
	void openMultiMenu()
	{
		vGameLoop.pause();
		MultiDialog.invoke(()->saveGameMulti(),()->exitActionMulti(),()->backToGameMulti());
	}
	
	void exitActionMulti()
	{		
		MultiDialog.hide();
		vLastMenu=EXIT_MENU;
		ExitDialog.invoke(()->exitAction(),()->backToGame());
	}
	
	void backToGameMulti()
	{
		MultiDialog.hide();		
		vGameLoop.resume();	
		setMode(vLastFocus);
	}
	
	void saveGameMulti()
	{
		MultiDialog.hide();
		vLastMenu=SAVE_MENU;
		SaveDialog.invoke(vScenario.getCurrent(), ()->saveGame(), ()->saveCancel());
	}
	
	void saveCancel()
	{
		SaveDialog.hide();
		vLastMenu=MULTI_MENU;
		MultiDialog.invoke(()->saveGameMulti(),()->exitActionMulti(),()->backToGameMulti());
	}
	
	void saveGame()
	{
		SaveDialog.hide();
		vLastMenu=MULTI_MENU;
		MultiDialog.invoke(()->saveGameMulti(),()->exitActionMulti(),()->backToGameMulti());
	}
	
	void backToGameSave()
	{
		SaveDialog.hide();
		vGameLoop.resume();
		setMode(vLastFocus);
	}
	
	void backToGame()
	{
		ExitDialog.hide();
		vGameLoop.resume();
		setMode(vLastFocus);
	}
	
	String vIdeogramCatDir;	
	IdeogramsArray vIdeograms;
	
	/*-----------------------------------
		KEYBOARD
	-----------------------------------*/
	
	
	/*-----------------------------------
		KEYBOARD
	-----------------------------------*/
	
	private boolean vLocked = false;
	
	void keyboardFunctionBattle(int key)
	{		
		if(vCurrentBattle.isAttackInProgress() && !vCurrentBattle.isPointsDisplayTime())
		{
			if(GameKeyboard.isLetterKey(key))
			{
				addToInputText(Keyboard.getKeyName(key));
			}
			else if(GameKeyboard.isEnterKey(key))
			{
				if(Logger.isActive())
				{
					boolean status=vCurrentBattle.verify();
					if(status)
						Logger.log("Correct answer: \""+vCurrentBattle.getInputText()+"\"");
					else
						Logger.log("Incrrect answer: \""+vCurrentBattle.getInputText()+"\"");
				}
				else
					vCurrentBattle.verify();
				vCurrentBattle.seriesTrigger();
				resetInputText();
			}
			else if(GameKeyboard.isBackKey(key))
			{
				removeLastInputChar();							
			}
		}
		else
		{
			if(vCurrentBattle.isPlayerTurn() && !vCurrentBattle.isKeyboardLocked())
			{
				if(GameKeyboard.isEnterKey(key))
				{
					vCurrentBattle.choseSelectedAbility();					
					vCurrentBattle.lockKeyboard();
				}
				else if(GameKeyboard.isArrowUpKey(key))
				{
					vCurrentBattle.prevAbility();
				}
				else if(GameKeyboard.isArrowDownKey(key))
				{
					vCurrentBattle.nextAbility();
				}
			}
		}
		if(GameKeyboard.isEscapeKey(key))
		{			
			setMode(FOCUS_EXIT);
			vLastFocus = FOCUS_BATTLE;
			vLastMenu=EXIT_MENU;
			openExitMenu();
		}
	}
	
	void keyboardFunctionExit(int key)
	{
		if(GameKeyboard.isEscapeKey(key))
		{
			if(vLastMenu==EXIT_MENU)			
				backToGame();
			else if(vLastMenu==MULTI_MENU)
				backToGameMulti();
			else if(vLastMenu==SAVE_MENU)
				backToGameSave();
			else
				exitGame();
		}
	}
	
	private int vLastMenu=-1;

	
	void keyboardFunctionPlot(int key)
	{
		if(!vFade && !vTyping && !vLocked /*&& !vCreditsRunning*/)
		{
			if(GameKeyboard.isEnterKey(key))
			{
				if(vStaticSceneEvt)
				{					
					vTyping = true;
					resetTyping();
					vCurrentSceneText++;
				}
				else if(vBlackScreen)
				{
					scenarioNext();
					vBlackScreen = false;
				}
				else if(vCreditsRunning)
				{
					scenarioNext();
				}
				else
				{
					scenarioFlagUp();
				}
			}
			else if(GameKeyboard.isEscapeKey(key))
			{
							
				setMode(FOCUS_EXIT);
				vLastMenu=MULTI_MENU;
				vLastFocus = FOCUS_PLOT;
				openMultiMenu();
			}
		}	
		else if(!vFade && vTyping && !vLocked)
			completeString();
	}
	
	void keyboardFunctionDefault(int key)
	{
		if(GameKeyboard.isEscapeKey(key))
		{
			exitGame();									
		}
	}
	
	private int vLastFocus;
	
	void checkKeyboardEvent()
	{
		while (Keyboard.next())
	    {
			if (Keyboard.getEventKeyState())
			{
				if(vStartActionCompleted)
				{
					int pressedKey = Keyboard.getEventKey();				
					
					switch(vCurrentMode)
					{					
						case FOCUS_BATTLE:
						{
							keyboardFunctionBattle(pressedKey);
							break;
						}
						case FOCUS_EXIT:
						{
							keyboardFunctionExit(pressedKey);
							break;
						}
						case FOCUS_PLOT:
						{
							keyboardFunctionPlot(pressedKey);
							break;
						}
						default:
						{
							keyboardFunctionDefault(pressedKey);						
						}
					}
				}
			}
	    }
	}	
	
	private void exitAction()
	{
		ExitDialog.hide();
		vExit = true;
		vGameLoop.resume();
	}
	
	private boolean vExit = false;
	
	protected void exitGame()
	{
		ExitDialog.hide();
		Graphics.destroy();
		AL.destroy();
		System.exit(0);
	}
	
	
	/*-----------------------------------
			INPUT TEXT
	-----------------------------------*/
	
	
	
	private void resetInputText()
	{
		vCurrentBattle.resetInputText();;
	}
	
	private void addToInputText(String text)
	{
		vCurrentBattle.addToInputText(text);
	}
	
	private void removeLastInputChar()
	{
		vCurrentBattle.removeLastInputChar();
	}
	
	
	
	/*-----------------------------------
		GAME MODE
	-----------------------------------*/
	
	private int vCurrentMode;
	protected void setMode(int mode)
	{
		vCurrentMode = mode;
	}
		
		
	/*-----------------------------------
		INIT
	-----------------------------------*/	
	
	
	private void initGame(boolean category) throws GameException
	{
		vAllIdeograms = ALL_IDEOGRAMS;
		vTextureNumberOffset = vAllIdeograms-1;
		setCategory(category);
		createTexArray();
		createIdeogramsArray();
		Battle.setTextureGetter(tex -> getTex(tex));		
		initLoop();
		vFadeOutTime=true;
	}
	
	private void initGame(String ideogramsTexDirectory, String[] transcriptionArray) throws GameException
	{
		vAllIdeograms = transcriptionArray.length;
		vTextureNumberOffset = vAllIdeograms-1;
		vIdeogramsTranscriptionArray = transcriptionArray;
		vIdeogramCatDir = ideogramsTexDirectory;		
		createTexArray();
		createIdeogramsArray();
		Battle.setTextureGetter(tex -> getTex(tex));		
		initLoop();
		vFadeOutTime=true;
	}
	
	/*-----------------------------------
		RESOURCES
	-----------------------------------*/	
	
	private Archive vResources;
	
	private void loadResources()
	{
		vResources = new Archive();
		loadFonts();
		
		Dialogue.setDiaFnt(vDialogueFont);
		loadTextures();
	}
	
	/*-----------------------------------
		FONTS
	-----------------------------------*/	
		
	
	private GameFont vSceneFont;
	private GameFont vDialogueFont;
	private GameFont vFontCreditsHeader;
	private GameFont vFontCredits;
	
	private void write(GameFont font, int posX, int posY, String text)
	{
		Graphics.writeText(text, font, posX, posY);
	}

	
	private GameFont vDefaultFont;
	protected GameFont getDefaultFont()
	{
		return vDefaultFont;
	}
	
	private void loadFonts()
	{
		vResources.getContent(DIR_FONT,ARCHIVE_FNT);
		
		initInfoFonts();
		
		vDefaultFont = new GameFont(GameFont.getFontName(), 40, vResources);
		
		vSceneFont = new GameFont(vDefaultFont.getFont(), 40, DEFAULT_COLOR);
		vBSFnt = vSceneFont;
		vDialogueFont = new GameFont(getDefaultFont().getFont(), 30);
		
		
		vFontCreditsHeader = new GameFont(getDefaultFont().getFont(), 40);
		vFontCredits = new GameFont(getDefaultFont().getFont(), 30);

	}
	
	void setCreditsFont(Color color, int sizeHeader, int sizeText)
	{
		vFontCreditsHeader = new GameFont(vFontCreditsHeader.getFont(), sizeHeader, color);
		vFontCredits = new GameFont(vFontCredits.getFont(), sizeText, color);
	}
	
	void setCreditsFont(Color color)
	{
		vFontCreditsHeader = new GameFont(vFontCreditsHeader.getFont(), vFontCreditsHeader.getFont().getSize(), color);
		vFontCredits = new GameFont(vFontCredits.getFont(), vFontCredits.getFont().getSize(), color);
	}
	
	void setSceneFont(Color color, int size)
	{
		vSceneFont = new GameFont(vSceneFont.getFont(), size, color);
	}
	
	void setSceneFont(Color color)
	{
		vSceneFont = new GameFont(vSceneFont.getFont(), 26, color);
	}
	
	void setBattleFont(Color color)
	{
		Battle.setFontColor(color);
		Battle.setBattleFont(getDefaultFont());
		ArenaBox.setFontColor(color);
		ArenaBox.setFonts(getDefaultFont());
		
	}
	
	void setDisabledFont(Color color)
	{
		ArenaBox.setDisabledFontColor(color);
	}
	
	void setDialogueFont(Color color)
	{
		vDialogueFont = new GameFont(vDialogueFont.getFont(), 30, color);
		Dialogue.setDiaFnt(vDialogueFont);
	}
	
	/*-----------------------------------
		SCENARIO
	-----------------------------------*/
	
	private Scenario vScenario;
	
	protected void setScenario(Scenario newScenario)
	{
		vScenario = newScenario;
	}	
	
	Dialogue build(int id, int background, DialogueElement...elements)
	{		
		int length = elements.length;
		Dialogue dia = new Dialogue(id, background);		
		for(int i=0; i<length; i++)
		{
			int current = i;
			dia.addLine(elements[current].getLine());			
			dia.addEvt(()->
			{
				drawDia(dia);
				if(current==0)
				{
					diaBegin();
					try		
					{
						setCurrentBackground(background);
					}
					catch(GameException e)
					{
						ErrorReporter.display(e);
						close(1);
					}
					
				}
				elements[current].getEvt().run();
			});
			if(i==length-1)				
				dia.addEvt(()->{diaEnd(dia);});
			else
				dia.addEvt(()->{dia.nextLineEvt(); resetSounds(); drawDia(dia);});
		}
		return dia;
		
	}
	
	private void diaEnd(Dialogue dia)
	{		
		if(vCurrentMode != FOCUS_BATTLE && !isBattleDiaTime())
		{
			scenarioNext();
			drawDia(dia);
		}
		else
			dia.reset();
	}
	
	private void diaBegin()
	{
		if(vBattleGoesOn)
			vCurrentBattle.setDiaTime();
	}
	
	private boolean isBattleDiaTime()
	{
		if(vCurrentBattle!=null)
			if(vCurrentBattle.isDiaTime())
				return true;
		return false;
	}
	
	protected DialogueElement element(NPC sayer, String content, Runnable evt)
	{
		return new DialogueElement(sayer, content, evt);
	}
	
	protected DialogueElement element(NPC sayer, String content)
	{
		return new DialogueElement(sayer, content);
	}
	
	void setDiaBox(int texture)
	{
		Dialogue.generateDiaBox(vTextureNumberOffset+texture);
	}
			
	
	/*-----------------------------------
		LOOP
	-----------------------------------*/	
	
	private void initLoop()
	{
		vGameLoop = new	Loop(
				() -> loopBeginPermanentFunc(),
				() -> loopUserEventFunc(),
				() -> loopGameEventFunc(),
				() -> loopEndPermanentFunc()
		);
	}
	
	private void loopBeginPermanentFunc()
	{
		//EMPTY
	}
	
	private void loopEndPermanentFunc()
	{
		if(vSceneChangingProcessInProgress)
			changeScene();
		if(!vSceneChangingProcessInProgress)
			funcScenarioFlagUp();
		
		Graphics.drawBlackScreen(vFadeValue);		
		
		if(ErrorReporter.error())
			close(1);  
	}	
		
	private void loopGameEventFunc()
	{
		if(!vStartActionCompleted)
		{
			startGame();
		}
		if(vCurrentMode == FOCUS_BATTLE)
			try
			{
				vCurrentBattle.run();
				if(vCurrentBattle.getResult()!=Battle.BATTLE_NOT_RESOLVED)
				{
					int jumpVal = vCurrentBattle.getJumpIndex();
					
					if(jumpVal == Battle.BATTLE_JUMP_DEFAULT)
						scenarioNext();
					else
						scenarioJump(jumpVal);;
				}
			}
			catch (GameException e)
			{
				ErrorReporter.display(e);
				close(1);
			}
			
		if(vCurrentMode==FOCUS_PLOT)
		{
			try
			{
				vScenario.play();			
			}
			catch (GameException e)
			{
				ErrorReporter.display(e);
				close(1);
			}
		}
	}
	
	private void loopUserEventFunc()
	{
		checkKeyboardEvent();
		if(vExit)
			exitGame();
	}
	
	protected void runLoop()
	{
		vGameLoop.run();
	}
	
	private Loop vGameLoop;
	
	void close()
	{
		Graphics.destroy();
		AL.destroy();
		System.exit(0);
	}
	
	void close(int status)
	{
		Graphics.destroy();
		AL.destroy();
		System.exit(status);
	}	
	
	/*-----------------------------------
		LOAD
	-----------------------------------*/
	
	private boolean vLoad=false;
	private int vLoaded=Scenario.EVT_START;
	
	private int getLoaded()
	{
		if(!vLoad)
			return Scenario.EVT_START;
		return vLoaded;
	}
	
	void load(int value)
	{
		if(value<=0)
			vLoad=false;
		else
		{
			vLoad=true;
			vLoaded=value;
		}
	}
	
	/*-----------------------------------
		START GAME
	-----------------------------------*/
	
	private int vStartDisplayCounter=0;
	
	private boolean vStartDisplay=true;
	private boolean vStartActionCompleted=false;
	private boolean vStartFadeInCompleted=false;
	private boolean vStartFadeOutCompleted=false;
	private boolean vStartTimeOut=false;	
	
	private void startgameFadeIn()
	{		
		fadeIn();
		if(!vFadeInTime)
		{
			vStartFadeInCompleted=true;
			vCreditsRunning=false;
		}
	}
	
	private void startgameFadeOut()
	{		
		fadeOut();
		if(!vFadeOutTime)
		{
			vStartFadeOutCompleted=true;
			vStartDisplay=false;
			vScenario.jump(getLoaded());
			vCurrentMode = FOCUS_PLOT;
			vFadeInTime=true;
		}
	}
	
	private void plotFadeIn()
	{		
		fadeIn();
		if(!vFadeInTime)
		{
			vStartActionCompleted=true;
		}
	}
	
	private void startGame()
	{
		if(!vStartFadeInCompleted)
		{
			startgameFadeIn();
		}
		else if(!vStartTimeOut)
		{
			if(vStartDisplayCounter==START_DISPLAY_TIME)
			{
				vStartTimeOut=true;
			}
			else
			{
				vStartDisplayCounter++;
			}
		}
		else if(vStartTimeOut && !vStartFadeOutCompleted)
		{
			startgameFadeOut();
		}
		else
		{
			plotFadeIn();
		}
		if(vStartDisplay)
		{
			drawInfo();
		}
	}
	
	private GameFont vNameFnt;
	private GameFont vInfoFnt;
	private GameFont vNameRomajiFnt;
	
	private void initInfoFonts()
	{
		Color jpc = new Color(106,9,9);
		Color rc= new Color(154,154,154);
		
		vInfoFnt = new GameFont(GameFont.FONT_JP, 35, vResources,  " BEDOPRWY", rc);
		vNameFnt = new GameFont(GameFont.FONT_JP, 170, vResources,  "雨エンジ", jpc);
		vNameRomajiFnt = new GameFont(GameFont.FONT_PL, 30, vResources, " .012AEeijmnv", rc);
	}
	
	private void drawInfo()
	{
		int xpos=125;
		int posy=Graphics.DEFAULT_WINDOW_HEIGHT/2-90;
		
		Graphics.writeTextLR("POWERED BY", vInfoFnt, xpos+4, posy-30);
		Graphics.writeTextLR("AmeEnjin v0.2.1", vNameRomajiFnt, xpos+170, posy+15);
		Graphics.writeTextLR("雨エンジン", vNameFnt, xpos, posy);
	}	
	
	/*-----------------------------------
		TEXT ON BACKGROUND
	-----------------------------------*/	
	
	private boolean vStaticSceneEvt = false;
	
	
	private boolean vTyping = false;
	private boolean fullStringReady = false;
	
	private String vTempString = "";
	private String vFullString = "";
	private int vTempStringLength = 0;
	
	private boolean timeToAddLetter=false;
	
	private void completeString()
	{
		vTempString = vFullString;
		vTempStringLength = vTmpLength;
	}
	
	private int vTmpLength = 0;
	
	private void type(String text, int textPosX, int textPosY, boolean lr)
	{		
		
		if(!fullStringReady)
		{
			vFullString = text;
			fullStringReady = true;
			vTmpLength = text.length();			
		}
		if(timeToAddLetter)
		{
			if(vTempStringLength>=vTmpLength)
				vTyping=false;
			else if(vTempStringLength<=0)
				vTempString="";
			else
				vTempString = vFullString.substring(0, vTempStringLength);
			timeToAddLetter = false;
		}
		if(lr)
			Graphics.writeTextLR(vTempString, vSceneFont, textPosX, textPosY);
		else
			write(vSceneFont, textPosX, textPosY, vTempString);
	}
	
	private void resetTyping()
	{
		vTempString = "";
		vTempStringLength = 0;
		vFullString = "";
		fullStringReady = false;
	}
	
	private int vAddLetterCounter=0;
	private void addLetter()
	{
		if(vAddLetterCounter == TEXT_TYPE_RATIO)
		{
			vTempStringLength++;
			vAddLetterCounter=0;
			timeToAddLetter = true;
		}
		else
		{
			if(!vFade)
				vAddLetterCounter++;
		}
	}
	
	private int vCurrentSceneText = 0;
	
	private boolean vStaticSceneEvtOnce = false;
	
	void drawStaticTextScene(int background, int textPosX, int textPosY, boolean lr, String... texts) throws GameException
	{
		if(texts.length<1)
			throw new GameException("Cannot find text!");
		if(!vStaticSceneEvt)
		{
			if(!vStaticSceneEvtOnce)
			{				
				Logger.log("Displaying text on background");
				vTyping = true;
				resetTyping();
				vCurrentSceneText = 0;
				vStaticSceneEvt = true;
				vStaticSceneEvtOnce = true;
			}				
		}
		if(vCurrentSceneText >= texts.length)
		{
			draw(getCurrentBackground());
			scenarioNext();
		}
		else
		{
			if(getCurrentBackground().getID() != background)
				setCurrentBackground(background);
			draw(getCurrentBackground());
			
			if(vTyping)
			{
				addLetter();
				type(texts[vCurrentSceneText], textPosX, textPosY, lr);
			}
			else
			{
				if(lr)
					Graphics.writeTextLR(texts[vCurrentSceneText], vSceneFont, textPosX, textPosY);
				else
					write(vSceneFont, textPosX, textPosY, texts[vCurrentSceneText]);
			}
		}
	}
	
	/*-----------------------------------
		BLACK SCREEN SCENES
	-----------------------------------*/	
		
	private boolean vBlackScreen = false;
	private boolean vBlackScreenInit = false;
	
	private GameFont vBSFnt;
	
	void drawBlackScene(int size, Color color, String... text)
	{
		if(!vBlackScreenInit)
		{
			Logger.log("Displaying text on black");
			vBlackScreen = true;
			vBlackScreenInit = true;
			vBSFnt = new GameFont(this.getDefaultFont().getFont(), size, color);
		}
		Graphics.drawBlackScreen();
		int dist = vBSFnt.getFont().getSize();
		
		int pos = (Graphics.DEFAULT_WINDOW_HEIGHT/2)-(dist/2);
		
		pos -= ((text.length-1)*dist)/2;
		pos-=((text.length-1)*dist)/4;
		
		for(int i=0; i<text.length; i++)
		{
			write(vBSFnt, Graphics.DEFAULT_WINDOW_WIDTH/2, pos, text[i]);
			pos += dist;
			pos+=dist/2;
			
		}				
	}
	
	Box image(int width, int height, int posX, int posY, int texture)
	{		
		int l = posX-width/2;
		int r = posX+width/2;
		int b = posY-height/2;
		int t = posY+height/2;
		
		Box box = new Box(l, r, b, t);
		
		box.setTexNr(texture+vTextureNumberOffset);
		
		return box;
		
		
	}
	
	
	Box vImage = null;
	
	void drawBlackScene(int width, int height, int texture)
	{		
		if(!vBlackScreenInit)
		{
			Logger.log("Displaying image on black");
			vBlackScreen = true;
			vImage = image(width, height, Graphics.DEFAULT_WINDOW_WIDTH/2, Graphics.DEFAULT_WINDOW_HEIGHT/2, texture);
			vBlackScreenInit = true;
		}
		Graphics.drawBlackScreen();
		this.draw(vImage);
	}
	
	private boolean vCreditsRunning = false;
	private Credits vCredits = null;
	
	/*-----------------------------------
		CREDITS
	-----------------------------------*/
	
	void buildCredits(int background, int speed, int distance, CreditsElement...elements)
	{		
		vCredits = new Credits(background);
		vCredits.setSpeed(speed);
		vCredits.setMargin(distance);
		if(vCredits.hasBackground())
		{
			try
			{				
				setCurrentBackground(vCredits.getBackground());
			}
			catch (GameException e)
			{				
				ErrorReporter.display(e);
				close(1);
			}
		}
		
		for(int i=0; i<elements.length; i++)
		{
			vCredits.add(elements[i]);
		}
	}
	
	void buildCredits(int speed, int distance, CreditsElement...elements)
	{		
		vCredits = new Credits();
		vCredits.setSpeed(speed);
		vCredits.setMargin(distance);
		if(vCredits.hasBackground())
		{
			try
			{				
				setCurrentBackground(vCredits.getBackground());
			}
			catch (GameException e)
			{				
				ErrorReporter.display(e);
				close(1);
			}
		}
		vCreditsRunning = true;
		for(int i=0; i<elements.length; i++)
		{
			vCredits.add(elements[i]);
		}
	}
	
	private boolean vCreditsReady=false;
	void playCredits()
	{		
		try
		{
			tryPlayCredits();
		}
		catch(GameException e)
		{
			ErrorReporter.display(e);
			close(1);
		}
	}
	
	void tryPlayCredits() throws GameException
	{		
		if(vCredits==null)
			throw new GameException("Cannot find credits to display!");
		else
		{
			if(!vCreditsReady)
			{
				Logger.log("Displaying credits");
				vCredits.gotoOrigin();
				vCreditsReady=true;
			}
			vCreditsRunning = true;
			drawCredits();
			vCredits.up(vCredits.getSpeed());
			if(vCredits.isCompleted())
			{
				scenarioNext();							
			}
		}
	}
	
	private void drawCredits()
	{
		if(vCredits.hasBackground())
		{
			try
			{				
				draw(getCurrentBackground());
			}
			catch (GameException e)
			{				
				ErrorReporter.display(e);
				close(1);
			}
		}
		else
			Graphics.drawBlackScreen();
		
		vCredits.draw(tex -> getTex(tex));
		
	}
	
	CreditsElement createCreditsElement(String header, String...texts)
	{
		return new CreditsElement
		(
			vFontCreditsHeader, 
			header, 
			vFontCredits,
			texts
		);
	}
	
	CreditsElement createCreditsElement(int width, int height, int texture)
	{
		return new CreditsElement(width, height, texture+vTextureNumberOffset);
	}
	
	CreditsElement createCreditsElement(int height)
	{
		return new CreditsElement(height);
	}
}
