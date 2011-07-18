package org.berkelium.java.api;

public class WindowAdapter implements WindowDelegate {
	@Override
	public void onPaintDone(Window win, Rect rect) {
	}

	@Override
	public void freeLastScriptAlert(String lastValue) {
	}

	@Override
	public void onAddressBarChanged(Window win, String newURL) {
	}

	@Override
	public void onConsoleMessage(Window win, String message, String sourceId,
			int lineNo) {
	}

	@Override
	public void onCrashed(Window win) {
	}

	@Override
	public void onCrashedPlugin(Window win, String pluginName) {
	}

	@Override
	public void onCrashedWorker(Window win) {
	}

	@Override
	public void onCreatedWindow(Window win, Window newWindow, Rect initialRect) {
	}

	@Override
	public void onExternalHost(Window win, String message, String origin,
			String target) {
	}

	@Override
	public void onJavascriptCallback(Window win, String url, String funcName) {
	}

	@Override
	public void onLoad(Window win) {
	}

	@Override
	public void onLoadingStateChanged(Window win, boolean isLoading) {
	}

	@Override
	public boolean onNavigationRequested(Window win, String newUrl,
			String referrer, boolean isNewWindow, boolean[] cancelDefaultAction) {
		return false;
	}

	@Override
	public void onPaint(Window win, Buffer sourceBuffer, Rect sourceBufferRect,
			Rect[] copyRects, int dx, int dy, Rect scrollRect) {
	}

	@Override
	public void onProvisionalLoadError(Window win, String url, int errorCode,
			boolean isMainFrame) {
	}

	@Override
	public void onResponsive(Window win) {
	}

	@Override
	public void onRunFileChooser(Window win, int mode, String title,
			String defaultFile) {
	}

	@Override
	public void onScriptAlert(Window win, String message, String defaultValue,
			String url, int flags, boolean[] success, String[] value) {
	}

	@Override
	public void onStartLoading(Window win, String newURL) {
	}

	@Override
	public void onTitleChanged(Window win, String title) {
	}

	@Override
	public void onTooltipChanged(Window win, String text) {
	}

	@Override
	public void onUnresponsive(Window win) {
	}
}
