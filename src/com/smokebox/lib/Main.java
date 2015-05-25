package com.smokebox.lib;

import com.smokebox.lib.pcg.weapon.WeaponGenerator;

public class Main {

  /**
   * @param args
   */
  public static void main(String[] args) {
                /*Random rand = new Random(1337);
                Floor f = CellAutomataCaves.generateCaves(75, 50, rand);
		
		RogueBasinDungeon r = new RogueBasinDungeon();
		
		Array2.saveMapAsImage(f.map);
		
		r.createDungeon(50, 50, 20, (long)(Math.random()*100));
		RoomSpreadDungeon.roomSpreadFloor(150, rand);
		*/
    WeaponGenerator w = new WeaponGenerator();
    int grade = 10;
    for (int i = 0; i < 10; i++) {
      System.out.println(w.generateGunName(grade) + ", Lv " + grade);
    }
  }
}