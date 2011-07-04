package org.berkelium.java;

public interface WindowDelegate {
	public void onAddressBarChanged(Window win, String newURL);

	public void onStartLoading(Window win, String newURL);

	public void onLoad(Window win);

	public void onCrashedWorker(Window win);

	public void onCrashedPlugin(Window win, String pluginName);

	public void onProvisionalLoadError(Window win, String url, int errorCode,
			boolean isMainFrame);

	public void onConsoleMessage(Window win, String message, String sourceId, int line_no);

	public void onScriptAlert(Window win, String message, String defaultValue,
			String url, int flags, boolean success[], String value[]);

	public void freeLastScriptAlert(String lastValue);

	public boolean onNavigationRequested(Window win, String newUrl, String referrer,
			boolean isNewWindow, boolean cancelDefaultAction[]);

	public void onLoadingStateChanged(Window win, boolean isLoading);

	public void onTitleChanged(Window win, String title);

	public void onTooltipChanged(Window win, String text);

	public void onCrashed(Window win);

	public void onUnresponsive(Window win);

	public void onResponsive(Window win);

	public void onExternalHost(Window win, String message, String origin, String target);

	public void onCreatedWindow(Window win, Window newWindow, Rect initialRect);

	public void onPaint(Window win, Buffer sourceBuffer, Rect sourceBufferRect,
			Rect copyRects[], int dx, int dy, Rect scrollRect);

	public void onWidgetCreated(Window win, Widget newWidget, int zIndex);

	public void onWidgetDestroyed(Window win, Widget wid);

	public void onWidgetResize(Window win, Widget wid, int newWidth, int newHeight);

	public void onWidgetMove(Window win, Widget wid, int newX, int newY);

	public void onWidgetPaint(Window win, Widget wid, Buffer sourceBuffer,
			Rect sourceBufferRect, Rect copyRects[], int dx, int dy, Rect scrollRect);

	public void onCursorUpdated(Window win, Cursor newCursor);

	public void onShowContextMenu(Window win, ContextMenuEventArgs args);

	public void onJavascriptCallback(Window win, String url, String funcName);

	public void onRunFileChooser(Window win, int mode, String title, String defaultFile);
}
