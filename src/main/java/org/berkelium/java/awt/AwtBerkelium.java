package org.berkelium.java.awt;

import java.awt.event.KeyEvent;

// taken from Berkelium Window.hpp
public class AwtBerkelium {
	public final static int SHIFT_MOD      = 1 << 0;
	public final static int CONTROL_MOD    = 1 << 1;
	public final static int ALT_MOD        = 1 << 2;
	public final static int META_MOD       = 1 << 3;
	public final static int KEYPAD_KEY     = 1 << 4; // If the key is on the keypad (use instead of keypad-specific keycodes)
	public final static int AUTOREPEAT_KEY = 1 << 5; // If this is not the first KeyPress event for this key
	public final static int SYSTEM_KEY     = 1 << 6; // if the keypress is a system event (WM_SYS* messages in windows)

	public final static int mapKeyModifier(KeyEvent e) {
		int ret = 0;
		if(e.isShiftDown()) ret |= SHIFT_MOD;
		if(e.isControlDown()) ret |= CONTROL_MOD;
		if(e.isAltDown()) ret |= ALT_MOD;
		if(e.isMetaDown()) ret |= META_MOD;
		if(e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) ret |= KEYPAD_KEY;
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
