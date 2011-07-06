package org.berkelium.java.impl;

import org.berkelium.java.api.Window;
import org.berkelium.java.api.WindowDelegate;

public class WindowImpl implements Window {
	public final static class KeyModifier {
		final static int SHIFT_MOD = 1 << 0;
		final static int CONTROL_MOD = 1 << 1;
		final static int ALT_MOD = 1 << 2;
		final static int META_MOD = 1 << 3;
		// If the key is on the keypad (use instead of keypad-specific keycodes)
		final static int KEYPAD_KEY = 1 << 4;
		// If this is not the first KeyPress event for this key
		final static int AUTOREPEAT_KEY = 1 << 5;
		// if the keypress is a system event (WM_SYS* messages in windows)
		final static int SYSTEM_KEY = 1 << 6;
	}

	public WindowImpl() {
		Context ctx = Context.getDefault();
		context = ctx;
		handle = _init(ctx.handle);
	}

	public Context getContext() {
		return context;
	}

	public native void setDelegate(WindowDelegate delegate);

	public native int getId();

	public native void setTransparent(boolean istrans);

	public native void focus();

	public native void unfocus();

	public native void mouseMoved(int xPos, int yPos);

	public native void mouseButton(int buttonID, boolean down);

	public native void mouseWheel(int xScroll, int yScroll);

	public native void textEvent(String evt);

	public native void keyEvent(boolean pressed, int mods, int vk_code, int scancode);

	public native void resize(int width, int height);

	public native void adjustZoom(int mode);

	public native void executeJavascript(String javascript);

	public native void insertCSS(String css, String elementId);

	public native boolean navigateTo(String url);

	public native void refresh();

	public native void stop();

	public native void goBack();

	public native void goForward();

	public native boolean canGoBack();

	public native boolean canGoForward();

	public native void cut();

	public native void copy();

	public native void paste();

	public native void undo();

	public native void redo();

	public native void del();

	public native void selectAll();

	public native void filesSelected(String files[]);

	public native void addEvalOnStartLoading(String script);

	public native void clearStartLoading();

	public void destroy() {
		if (handle != 0) {
			_destroy(handle);
			handle = 0;
		}
		context = null;
	}

	private native long _init(long contextHandle);

	private native void _destroy(long handle);

	private Context context;
	protected transient long handle;
}
