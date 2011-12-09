package org.berkelium.java.api;

public class WindowAdapter implements WindowDelegate {
	public void onPaintDone(Window win, Rect rect) {
	}

	public void freeLastScriptAlert(String lastValue) {
	}

	public void onAddressBarChanged(Window win, String newURL) {
	}

	public void onConsoleMessage(Window win, String message, String sourceId,
			int lineNo) {
	}

	public void onCrashed(Window win) {
	}

	public void onCrashedPlugin(Window win, String pluginName) {
	}

	public void onCrashedWorker(Window win) {
	}

	public void onCreatedWindow(Window win, Window newWindow, Rect initialRect) {
	}

	public void onExternalHost(Window win, String message, String origin,
			String target) {
	}

	public void onJavascriptCallback(Window win, String url, String funcName) {
	}

	public void onLoad(Window win) {
	}

	public void onLoadingStateChanged(Window win, boolean isLoading) {
	}

	public boolean onNavigationRequested(Window win, String newUrl,
			String referrer, boolean isNewWindow, boolean[] cancelDefaultAction) {
		return false;
	}

	public void onPaint(Window win, Buffer sourceBuffer, Rect sourceBufferRect,
			Rect[] copyRects, int dx, int dy, Rect scrollRect) {
	}

	public void onProvisionalLoadError(Window win, String url, int errorCode,
			boolean isMainFrame) {
	}

	public void onResponsive(Window win) {
	}

	public void onRunFileChooser(Window win, int mode, String title,
			String defaultFile) {
	}

	public void onScriptAlert(Window win, String message, String defaultValue,
			String url, int flags, boolean[] success, String[] value) {
	}

	public void onStartLoading(Window win, String newURL) {
	}

	public void onTitleChanged(Window win, String title) {
	}

	public void onTooltipChanged(Window win, String text) {
	}

	public void onUnresponsive(Window win) {
	}
}
