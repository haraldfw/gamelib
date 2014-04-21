package com.smokebox.lib.pcg.weapon;

import java.util.Random;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class Ranged {
	
	private static String[] prefix = {
		"Dirty ",
		"Weak ", 
		"Awesome ",
		"Deadly "
	};
	
	public static String getPrefix(Random rand, float grade) {
		if(rand.nextGaussian()*grade > 1) {
			int prefixID = (int) (Math.abs(rand.nextGaussian())*grade/10f);
			
			if(prefixID >= prefix.length) prefixID = prefix.length - 1;
			
			return prefix[prefixID];
		}
		
		return "";
	}
	/*
	 *  Each prefix come with different stat-modifiers. 
	 *  Ex: 
	 *  	Dirty, gives loss of accuracy, but can cause infections(poisin-dam)
	 *  	Deadly, can deal critical strikes
	 *  	Awesome, change to fire two shots at the cost of one
	 *  	Shy, lower knockback. Has a chance not to consume ammo.
	 */
	public static String[] type = {
		"BB-gun",
		"Shooter",
		"Cannon",
		"Pistol",
		"Blunderbuss",
		"SMG",
		"Musket"
		};
	/*
	 * 	The type refers to what kind of weapon it is.
	 * 	Ex:
	 * 		Blunderbuss is a shotgun with many weak pellets.
	 * 		Cannon has a low firerate but huge bullets with massive knockback and damage (piercing?)
	 * 		Pistol, semi-auto medium pellets with decent damage.
	 * 		
	 */
	private static String[] suffix = {
		" of Doom", 
		" of the Zora", 
		" of Destruction"
		};
	
	public static String getSuffix(Random rand, float grade) {
		if(rand.nextGaussian()*grade/5 > 1) {
			return suffix[rand.nextInt(suffix.length)];
		}
		
		return "";
	}
	/*	
	 * The suffix can give huge modifiers to guns, for example fire damage, piercing,
	 * bleed or similar on (maybe) every shot. These modifiers are, however, 
	 * rare(at least the good ones), so getting a good one is considered lucky.
	 */
	
	public static String[] preSuffix = {
		"Major ", 
		"Utter ",
		"Super"
	};
}
