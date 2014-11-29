package com.smokebox.lib.pcg.levelGeneration;

import com.smokebox.lib.utils.Array2;
import com.smokebox.lib.utils.IntVector2;

import java.util.Random;

/**
 * Created by Harald Wilhelmsen on 10/15/2014.
 */
public class TunnelDigger {

	public static int[][] tunnelDiggerWorld(int tiles, long seed) {
		Random rng;
		if(seed == 0) seed = (long) (Math.random()*10000);
		rng = new Random(seed);

		int[][] map = new int[tiles][tiles];
		int tilesDug = 0;

		IntVector2 dir = new IntVector2(1, 0);
		float dirChangeChance = 1f/3f;

		IntVector2 pos = new IntVector2(tiles/2, tiles/2);

		while(tilesDug < tiles) {
			// change direction ??% of the time
			if(rng.nextFloat() <= dirChangeChance) {
				int newDir = rng.nextInt(4);
				int[] corrDirs;
				switch(newDir) {
					case 0:
						dir.set(1, 0);
						break;
					case 1:
						dir.set(0, 1);
						break;
					case 2:
						dir.set(-1, 0);
						break;
					default: // case 3
						dir.set(0, -1);
						break;
				}

				corrDirs = new int[]{-rng.nextInt(3), rng.nextInt(3)};
				if(dir.x != 0) { // direction along x-axis
					for(int i = corrDirs[0]; i <= corrDirs[1]; i++) {
						if(placeTile(map, pos, 0, i)) tilesDug++;
					}
				} else { // direction along y-axis
					for(int i = corrDirs[0]; i <= corrDirs[1]; i++) {
						if(placeTile(map, pos, i, 0)) tilesDug++;
					}
				}
			}
			pos.add(dir);
			System.out.println(tilesDug);
		}

		map = Array2.crop(new int[]{0}, map);
		map = Array2.bufferInt2(map, 1);

		return map;
	}

	private static boolean placeTile(int[][] map, IntVector2 pos, int shiftX, int shiftY) {
		// if digger is outside array, expand array
		if(pos.x + shiftX >= map.length ||
				pos.x + shiftX < 0 ||
				pos.y + shiftY >= map[0].length ||
				pos.y + shiftY < 0) {
			map = Array2.bufferInt2(map, 10);
			pos.add(10, 10);
		}

		if(map[pos.x + shiftX][pos.y + shiftY]++ != 0) {
			return true;
		}
		return false;
	}
}
