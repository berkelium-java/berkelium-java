package org.berkelium.java.api;

public interface WindowDelegate {
	public void onAddressBarChanged(Window win, String newURL);

	public void onStartLoading(Window win, String newURL);

	public void onLoad(Window win);

	public void onCrashedWorker(Window win);

	public void onCrashedPlugin(Window win, String pluginName);

	public void onProvisionalLoadError(Window win, String url, int errorCode,
			boolean isMainFrame);

	public void onConsoleMessage(Window win, String message, String sourceId,
			int line_no);

	public void onScriptAlert(Window win, String message, String defaultValue,
			String url, int flags, boolean success[], String value[]);

	public void freeLastScriptAlert(String lastValue);

	public boolean onNavigationRequested(Window win, String newUrl,
			String referrer, boolean isNewWindow, boolean cancelDefaultAction[]);

	public void onLoadingStateChanged(Window win, boolean isLoading);

	public void onTitleChanged(Window win, String title);

	public void onTooltipChanged(Window win, String text);

	public void onCrashed(Window win);

	public void onUnresponsive(Window win);

	public void onResponsive(Window win);

	public void onExternalHost(Window win, String message, String origin,
			String target);

	public void onCreatedWindow(Window win, Window newWindow, Rect initialRect);

	public void onPaint(Window win, Buffer sourceBuffer, Rect sourceBufferRect,
			Rect copyRects[], int dx, int dy, Rect scrollRect);

	public void onJavascriptCallback(Window win, String url, String funcName);

	public void onRunFileChooser(Window win, int mode, String title,
			String defaultFile);
}
