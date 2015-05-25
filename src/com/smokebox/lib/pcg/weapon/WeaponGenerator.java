/**
 *
 */
package com.smokebox.lib.pcg.weapon;

import java.io.File;
import java.util.Random;

/**
 * @author Harald Floor Wilhelmsen
 */
public class WeaponGenerator {

	/*
         * Every gun has three modifiers as to what kind of weapon it is.
	 * Prefix, suffix and type. The type is the main modifier to tell what kind of weapon it
	  * is.
	 * The prefix and suffix both have several modifiers to what kind of damage the gun deals, 
	 * how many bullets it fires and so on... The type defines what kind of gun it is.
	 * 
	 * There are three checks with random numbers to tell what modifiers you get.
	 * And every modifier has a number to represent it's "goodness". The higher
	 * the goodness, the rarer it is. However, several things can impact these
	 * random number checks, for example, the chance to get good weapons is
	 * increased when drops from a boss is calculated, or when the
	 * difficulty-level increases. This is most likely to be represented in a
	 * "grade-number", the higher the grade-numeral, the better the weapon, usually.
	 * 
	 * The generator is setup to give a prefix, type and suffix to every weapon,
	 * but there is a chance that prefix or suffix does not have to spawn.
	 * 
	 * The type refers to what the weapon will look like, as in the base parts. 
	 * But the prefix and suffix is very likely to change them. 
	 * 	Ex:
	 * 		Dirty will make the barrel and/or stock look rusty.
	 * 
	 * There might also be a way to make descriptions for the weapons. As in a Dirty
	 * weapons, the description might be: "Can cause infections". This to give a player
	 * a hint as to what the weapon does. This description can pop up on the screen when
	 * the player picks up the weapon. "Not safe for children". "May or may not burn 
	 * your house down".
	 * 
	 * The prefixes and suffixes might also get increased effects, depending on what grade
	 * the gun has. As in how late or in what situation the gun was found. So a rusty gun
	 * found late in the game is better than a rusty gun found early.
	 * 
	 * -- Generation --
	 * Firstly the type will be selected to choose what kind of weapon. This can be set
	 * manually(so that a pirate-boss can drop a cannon and so on), but if it is not, it
	 * will be randomly generated. Then the Prefix and suffix is selected, these *can* 
	 * also be set manually(maybe not). The prefix and suffix both have several
	 * sprites attached to them(as in stocks and barrels and so on, in every weapon-
	 * variant, the weapons will most likely be low-res, so making them will be quick. 
	 * These variations are also most likely to only be in color, which make them even
	 * easier to produce), so that almost gun is unique. You can get a gun with a rusty
	 * stock and a barrel that is on fire that shoots fire-bullets.
	 */

  /*
   * This class has methods to generate guns, and will return instances of the classes
   * Melee or Ranged (maybe).
   */
  Random rand;

  public WeaponGenerator() {
    rand = new Random();
  }

  public File generateMeleeImage() {
    File path = new File("");

    // Generate image from the stats given as parameters

    return path;
  }

  public String generateGunName(float grade) {
    return Ranged.getPrefix(rand, grade) + Ranged.type[rand.nextInt(Ranged.type.length)] + Ranged
        .getSuffix(rand, grade);
  }

  public String generateMeleeName(float grade) {
    return Ranged.getPrefix(rand, grade) + Ranged.type[rand.nextInt(Ranged.type.length)] + Ranged
        .getSuffix(rand, grade);
  }
}
