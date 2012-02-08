package org.berkelium.java.awt;

import java.awt.event.KeyEvent;

import org.berkelium.java.api.KeyModifier;

public class AwtBerkelium {
	public final static int mapKeyModifier(KeyEvent e) {
		int ret = 0;
		if(e.isShiftDown()) ret |= KeyModifier.SHIFT_MOD;
		if(e.isControlDown()) ret |= KeyModifier.CONTROL_MOD;
		if(e.isAltDown()) ret |= KeyModifier.ALT_MOD;
		if(e.isMetaDown()) ret |= KeyModifier.META_MOD;
		if(e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) ret |= KeyModifier.KEYPAD_KEY;
		// TODO repeating
		// if(repeating) ret |= AUTOREPEAT_KEY;
		// TODO system key
		// if(system key) ret |= SYSTEM_KEY;
		return ret;
	}

	// TODO
	public static int mapVkCode(KeyEvent e) {
		return 0;
	}

	// TODO
	public static int mapScancode(KeyEvent e) {
		return 0;
	}
}
