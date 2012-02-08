package org.berkelium.java.api;

//taken from Berkelium Window.hpp
public final class KeyModifier {
	public final static int SHIFT_MOD = 1 << 0;
	public final static int CONTROL_MOD = 1 << 1;
	public final static int ALT_MOD = 1 << 2;
	public final static int META_MOD = 1 << 3;
	// If the key is on the keypad (use instead of keypad-specific keycodes)
	public final static int KEYPAD_KEY = 1 << 4;
	// If this is not the first KeyPress event for this key
	public final static int AUTOREPEAT_KEY = 1 << 5;
	// if the keypress is a system event (WM_SYS* messages in windows)
	public final static int SYSTEM_KEY = 1 << 6;
}