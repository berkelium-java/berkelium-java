package org.berkelium.java.api;

public class JavaScriptResizeAdapter extends WindowAdapter {
	@Override
	public void onResizeRequested(Window win, int x, int y, int width, int height) {
		win.resize(width, height);
	}
}
