package org.berkelium.java.api;

import org.berkelium.java.js.Function0;

public interface Window {
	Window getThreadProxyWindow();

	Window getRealWindow();

	void setDelegate(WindowDelegate delegate);

	void addDelegate(WindowDelegate delegate);

	void removeDelegate(WindowDelegate delegate);

	int getId();

	void setTransparent(boolean istrans);

	void focus();

	void unfocus();

	void mouseMoved(int xPos, int yPos);

	void mouseButton(int buttonID, boolean down);

	void mouseWheel(int xScroll, int yScroll);

	void textEvent(String evt);

	void keyEvent(boolean pressed, int mods, int vk_code, int scancode);

	void resize(int width, int height);

	void adjustZoom(int mode);

	void executeJavascript(String javascript);

	void insertCSS(String css, String elementId);

	boolean navigateTo(String url);

	void refresh();

	void stop();

	void goBack();

	void goForward();

	boolean canGoBack();

	boolean canGoForward();

	void cut();

	void copy();

	void paste();

	void undo();

	void redo();

	void del();

	void selectAll();

	void filesSelected(String files[]);

	void addEvalOnStartLoading(String script);

	void clearStartLoading();

	void destroy();

	Berkelium getBerkelium();

	<R> void bind(String name, Function0<R> function);
}
