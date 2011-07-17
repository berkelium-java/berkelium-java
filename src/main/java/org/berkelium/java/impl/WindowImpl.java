package org.berkelium.java.impl;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.MultiDelegate;
import org.berkelium.java.api.Window;
import org.berkelium.java.api.WindowDelegate;
import org.berkelium.java.js.Function0;

public class WindowImpl implements Window {
	private final Berkelium berkelium;
	private WindowThreadProxy threadProxy;

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

	public WindowImpl(Berkelium berkelium) {
		this.berkelium = berkelium;
		Context ctx = Context.getDefault();
		context = ctx;
		handle = _init(ctx.handle);
	}

	public Context getContext() {
		return context;
	}

	@Override
	public native void setDelegate(WindowDelegate delegate);

	@Override
	public native int getId();

	@Override
	public native void setTransparent(boolean istrans);

	@Override
	public native void focus();

	@Override
	public native void unfocus();

	@Override
	public native void mouseMoved(int xPos, int yPos);

	@Override
	public native void mouseButton(int buttonID, boolean down);

	@Override
	public native void mouseWheel(int xScroll, int yScroll);

	@Override
	public native void textEvent(String evt);

	@Override
	public native void keyEvent(boolean pressed, int mods, int vk_code,
			int scancode);

	@Override
	public native void resize(int width, int height);

	@Override
	public native void adjustZoom(int mode);

	@Override
	public native void executeJavascript(String javascript);

	@Override
	public native void insertCSS(String css, String elementId);

	@Override
	public native boolean navigateTo(String url);

	@Override
	public native void refresh();

	@Override
	public native void stop();

	@Override
	public native void goBack();

	@Override
	public native void goForward();

	@Override
	public native boolean canGoBack();

	@Override
	public native boolean canGoForward();

	@Override
	public native void cut();

	@Override
	public native void copy();

	@Override
	public native void paste();

	@Override
	public native void undo();

	@Override
	public native void redo();

	@Override
	public native void del();

	@Override
	public native void selectAll();

	@Override
	public native void filesSelected(String files[]);

	@Override
	public native void addEvalOnStartLoading(String script);

	@Override
	public native void clearStartLoading();

	@Override
	public synchronized void destroy() {
		if (multiDelegate != null) {
			multiDelegate.clear();
		}
		if (handle != 0) {
			_destroy(handle);
			handle = 0;
		}
		context = null;
	}

	private native long _init(long contextHandle);

	private native void _destroy(long handle);

	private Context context;
	protected volatile long handle;

	@Override
	public synchronized Window getThreadProxyWindow() {
		if (threadProxy == null) {
			threadProxy = new WindowThreadProxy(this);
		}
		return threadProxy.getProxy();
	}

	@Override
	public Window getRealWindow() {
		return this;
	}

	private MultiDelegate multiDelegate;

	@Override
	public synchronized void addDelegate(WindowDelegate delegate) {
		if (multiDelegate == null) {
			multiDelegate = new MultiDelegate();
			setDelegate(multiDelegate.getProxy());
		}
		multiDelegate.addDelegate(delegate);
	}

	@Override
	public synchronized void removeDelegate(WindowDelegate delegate) {
		if (multiDelegate != null) {
			multiDelegate.removeDelegate(delegate);
		}
	}

	JSImpl jsImpl;

	private synchronized JSImpl getJS() {
		if (jsImpl == null) {
			jsImpl = new JSImpl(this);
		}
		return jsImpl;
	}

	@Override
	public <R> void bind(String name, Function0<R> function) {
		getJS().bind(name, function);
	}

	@Override
	public Berkelium getBerkelium() {
		return berkelium;
	}
}
