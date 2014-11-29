package com.smokebox.lib.pcg.roomBased;

import com.smokebox.lib.utils.IntVector2;

/**
 * Created by Harald Wilhelmsen on 11/14/2014.
 */
public final class DoorDef {

	public final RoomDef roomDef;
	public DoorDef end;

	public final int leadsToX;
	public final int leadsToY;

	/**
	 * @param roomDef	The room this door is in
	 */
	public DoorDef(RoomDef roomDef, int leadsToX, int leadsToY) {
		this.roomDef = roomDef;
		this.leadsToX = leadsToX;
		this.leadsToY = leadsToY;
	}
}
