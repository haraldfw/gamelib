package com.smokebox.lib.pcg.weapon;

import com.smokebox.lib.utils.geom.Triangle;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class Melee {

	public float damage;
	
	public Triangle hitbox;
	
	public MeleeModifiers prefixMod;
	public MeleeModifiers suffixMod;
	
	public Melee(float rng, float swngWdth, float swngSpd, float dmg) {
		hitbox = new Triangle(0, 0, -swngWdth/2, 20, swngWdth/2, 20, 0, rng, 0);
	}
	
	public Triangle swing(float angle, float posx, float posy) {
		// Return hitbox after direction is set and hitbox is changed accordingly.
		hitbox.setOriginKeepRelativeDistance(posx, posy);
		hitbox.setRotation(angle);
		return hitbox;
	}
	
	
}
