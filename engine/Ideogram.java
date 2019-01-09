package engine;

import static engine.Graphics.DEFAULT_WINDOW_HEIGHT;

class Ideogram extends GameObject
{

	private static final int BORDER_BOT = 0;
	static final int BORDER_TOP=DEFAULT_WINDOW_HEIGHT-BORDER_BOT;	
	
	static final String DIR_IDEOGRAM="ideograms";
	static final String DIR_HIRAGANA="hiragana";
	static final String DIR_KATAKANA="katakana";
	
	static final boolean CATEGORY_HIRAGANA=true;
	static final boolean CATEGORY_KATAKANA=false;
	
	static final String[] ROMAJI=
	{
		"A","I","U","E","O",			//0-4
		"KA","KI","KU","KE","KO",		//5-9
		"SA","SHI","SU","SE","SO",		//10-14
		"TA","CHI","TSU","TE","TO",		//15-19
		"NA","NI","NU","NE","NO",		//20-24
		"HA","HI","FU","HE","HO",		//25-29
		"MA","MI","MU","ME","MO",		//30-34
		"YA","YU","YO",					//35-37
		"RA","RI","RU","RE","RO",		//38-42
		"WA","WO",						//43-44
		"N",							//45
		"GA","GI","GU","GE","GO",		//46-50
		"ZA","JI","ZU","ZE","ZO",		//51-55
		"DA","JI","ZU","DE","DO",		//56-60
		"BA","BI","BU","BE","BO",		//61-65
		"PA","PI","PU","PE","PO"		//66-70
	};
	
	static final String[] HIRAGANA=
	{
		"あ","い","う","え","お",	//0-4
		"か","き","く","け","こ",	//5-9
		"さ","し","す","せ","そ",	//10-14
		"た","ち","つ","て","と",	//15-19
		"な","に","ぬ","ね","の",	//20-24
		"は","ひ","ふ","へ","ほ",	//25-29
		"ま","み","む","め","も",	//30-34
		"や","ゆ","よ",			//35-37
		"ら","り","る","れ","ろ",	//38-42
		"わ","を",				//43-44
		"ん",					//45
		"が","ぎ","ぐ","げ","ご",	//46-50
		"ざ","じ","ず","ぜ","ぞ",	//51-55
		"だ","ぢ","づ","で","ど",	//56-60
		"ば","び","ぶ","べ","ぼ",	//61-65
		"ぱ","ぴ","ぷ","ぺ","ぽ"	//66-70
	};
	
	static final String[] KATAKANA=
	{
		"ア","イ","ウ","エ","オ",	//0-4
		"カ","キ","ク","ケ","コ",	//5-9
		"サ","シ","ス","セ","ソ",	//10-14
		"タ","チ","ツ","テ","ト",	//15-19
		"ナ","ニ","ヌ","ネ","ノ",	//20-24
		"ハ","ヒ","フ","ヘ","ホ",	//25-29
		"マ","ミ","ム","メ","モ",	//30-34
		"ヤ","ユ","ヨ",			//35-37
		"ラ","リ","ル","レ","ロ",	//38-42
		"ワ","ヲ",				//43-44
		"ン",					//45
		"ガ","ギ","グ","ゲ","ゴ",	//46-50
		"ザ","ジ","ズ","ゼ","ゾ",	//51-55
		"ダ","ヂ","ヅ","デ","ド",	//56-60
		"バ","ビ","ブ","ベ","ボ",	//61-65
		"パ","ピ","プ","ペ","ポ"	//66-70
	};
	
	static final byte POS_IDEOGRAM_MAX=4;
	static final byte POS_IDEOGRAM_1=1;
	static final byte POS_IDEOGRAM_2=2;
	static final byte POS_IDEOGRAM_3=3;
	static final byte POS_IDEOGRAM_4=4;
	
	static final int IDEOGRAM_HEIGHT=50; //100;//50;
	static final int IDEOGRAM_WIDTH=100; //250;//100;	
	
	static final int DEF_COORD_BEGIN=0;
	static final int DEF_COORD_END=100;
	
	static final int IDEOGRAM_DEF_START_X=297; //0;//297;
	static final int IDEOGRAM_DEF_START_Y=768;
	static final int IDEOGRAM_DIST=10;
	
	private String vTrans;
	
	Ideogram(int id, int texNr, String transcription)
	{
		setID(id);
		setTexNr(texNr);
		setTranscription(transcription);
		setCoords
		(
			DEF_COORD_BEGIN, DEF_COORD_BEGIN,
			DEF_COORD_BEGIN, DEF_COORD_END,
			DEF_COORD_END,DEF_COORD_END,
			DEF_COORD_END,DEF_COORD_BEGIN
		);
	}	
	
	Ideogram(int id, int texNr, String transcription, byte position)
	{		
		setID(id);
		setTexNr(texNr);
		setTranscription(transcription);		
		putOnPosition(position);
	}	
	
	Ideogram(int id, int texNr, String transcription, int lbX, int lbY, int ltX, int ltY, int rtX, int rtY, int rbX, int rbY)
	{
		setID(id);
		setTexNr(texNr);
		setTranscription(transcription);
		setCoords(lbX, lbY, ltX, ltY, rtX, rtY, rbX, rbY);
	}
	
	Ideogram(Ideogram template)
	{
		this(template.getID(), template.getTexNr(), template.getTranscription());
	}
	
	void putOnPosition(byte position)
	{
		int pozX;
		
		switch(position)
		{
			case POS_IDEOGRAM_1: pozX=IDEOGRAM_DEF_START_X; break;
			case POS_IDEOGRAM_2: pozX=IDEOGRAM_DEF_START_X+IDEOGRAM_WIDTH+IDEOGRAM_DIST; break;
			case POS_IDEOGRAM_3: pozX=IDEOGRAM_DEF_START_X+2*IDEOGRAM_WIDTH+2*IDEOGRAM_DIST; break;
			case POS_IDEOGRAM_4: pozX=IDEOGRAM_DEF_START_X+3*IDEOGRAM_WIDTH+3*IDEOGRAM_DIST; break;
			default: pozX=IDEOGRAM_DEF_START_X;
		}
		
		setCoords
		(
			pozX, IDEOGRAM_DEF_START_Y,									//lb
			pozX, IDEOGRAM_DEF_START_Y+IDEOGRAM_HEIGHT,					//lt
			pozX+IDEOGRAM_WIDTH, IDEOGRAM_DEF_START_Y+IDEOGRAM_HEIGHT,	//rt
			pozX+IDEOGRAM_WIDTH, IDEOGRAM_DEF_START_Y					//rb
		);
	}
	
	void setTranscription(String transcription)
	{
		vTrans=transcription;
	}	
	
	void down(int value)
	{
		updatePosY(-value);
	}
	
	String getTranscription()
	{
		return vTrans;
	}
	
	private int partTop()
	{
		int lt=getCoord(COORDS_L, COORDS_T, COORDS_Y);
		int rt=getCoord(COORDS_R, COORDS_T, COORDS_Y);
		
		if(lt==rt)
			return(lt);
		else
			return (lt+rt)/2;			
	}
	
	private int partBot()
	{
		int lb=getCoord(COORDS_L, COORDS_B, COORDS_Y);
		int rb=getCoord(COORDS_R, COORDS_B, COORDS_Y);
		
		if(lb==rb)
			return(lb);
		else
			return (lb+rb)/2;			
	}
	
	boolean outOfLineTop(int borderValue)
	{
		if(partTop()<borderValue)
			return true;
		return false;
	}
	
	boolean outOfLineBot(int borderValue)
	{
		if(partBot()<borderValue)
			return true;
		return false;
	}
	
	boolean isVisible()
	{
		if (outOfLineBot(BORDER_TOP) && !outOfLineTop(BORDER_BOT))
				return true;
		return false;
	}	
}

