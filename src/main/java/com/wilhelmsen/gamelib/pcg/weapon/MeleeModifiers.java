/**
 *
 */
package com.wilhelmsen.gamelib.pcg.weapon;

/**
 * @author Harald Floor Wilhelmsen
 */
public class MeleeModifiers {

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
    public Type t;

    public MeleeModifiers(Type type) {
        this.t = type;
    }

    public enum DmgType {
        BORING,
        POISONOUS,
        INFERNAL,
        DESTRUCTIVE
    }

    public enum prefix {
        DIRTY,
        WEAK,
        AWESOME,
        DEADLY,
        EVISCE
    }

    public enum suffix {
        DOOM,
        EXECUT,
        KINGS,
        DEAD
    }

    public enum Type {PREF, SUFF}
}