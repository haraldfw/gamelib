/**
 * 
 */
package com.smokebox.lib.pcg.weapon;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class MeleeModifiers {
	
	public static enum DmgType{
		BORING,
		POISONOUS,
		INFERNAL,
		DESTRUCTIVE
	}
	
	public static enum prefix{
		DIRTY,
		WEAK,
		AWESOME,
		DEADLY,
		EVISCE
	}
	
	public static enum suffix{
		DOOM,
		EXECUT,
		KINGS,
		DEAD
	}

	public static String[] prefix = {
		"Dirty ",
		"Weak ",
		"Awesome ",
		"Deadly ",
		"Eviscerating "
	};
	
	public static float[] damageMod = {
		
	};
	
	public static String[] suffix = {
		" of Doom",
		" of Execution",
		" of the Kings",
		" of the Dead"
	};
	
	public static enum Type{PREF, SUFF};
	public Type t;
	
	public MeleeModifiers(Type type) {
		this. t = type;
	}
}