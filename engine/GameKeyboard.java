package engine;

import org.lwjgl.input.Keyboard;

class GameKeyboard
{
	private static int[] vSupportedLetterKeys=
		{
			Keyboard.KEY_Q,
			Keyboard.KEY_W,
			Keyboard.KEY_E,
			Keyboard.KEY_R,
			Keyboard.KEY_T,
			Keyboard.KEY_Y,
			Keyboard.KEY_U,
			Keyboard.KEY_I,
			Keyboard.KEY_O,
			Keyboard.KEY_P,
			Keyboard.KEY_A,
			Keyboard.KEY_S,
			Keyboard.KEY_D,
			Keyboard.KEY_F,
			Keyboard.KEY_G,
			Keyboard.KEY_H,
			Keyboard.KEY_J,
			Keyboard.KEY_K,
			Keyboard.KEY_L,
			Keyboard.KEY_Z,
			Keyboard.KEY_X,
			Keyboard.KEY_C,
			Keyboard.KEY_V,
			Keyboard.KEY_B,
			Keyboard.KEY_N,
			Keyboard.KEY_M,
		};
		
	private	static int[] vSupportedKeysNumeric=
			{
				Keyboard.KEY_1,
				Keyboard.KEY_2,
				Keyboard.KEY_3,
				Keyboard.KEY_4,
				Keyboard.KEY_5,
				Keyboard.KEY_6,
				Keyboard.KEY_7,
				Keyboard.KEY_8,
				Keyboard.KEY_9,
				Keyboard.KEY_0,
			};
		
	static boolean isLetterKey(int key)
		{		
			for(int counter=0; counter<vSupportedLetterKeys.length; counter++)		
				if(key==vSupportedLetterKeys[counter])
					return true;
			return false;
		}
		
	static boolean isNumericKey(int key)
		{		
			for(int counter=0; counter<vSupportedKeysNumeric.length; counter++)		
				if(key==vSupportedKeysNumeric[counter])
					return true;
			return false;
		}
	
	static boolean isArrowUpKey(int key)
	{		
		if(key==Keyboard.KEY_UP)
			return true;
		return false;
	}
	
	static boolean isArrowDownKey(int key)
	{		
		if(key==Keyboard.KEY_DOWN)
			return true;
		return false;
	}
	
	static boolean isEnterKey(int key)
	{
		if(key==Keyboard.KEY_RETURN)
			return true;
		return false;
	}
	
	static boolean isBackKey(int key)
	{
		if(key==Keyboard.KEY_BACK)
			return true;
		return false;
	}
	
	static boolean isEscapeKey(int key)
	{
		if(key==Keyboard.KEY_ESCAPE)
			return true;
		return false;
	}
}
