package org.berkelium.java;

public class WindowAdapter implements WindowDelegate {

	@Override
	public void freeLastScriptAlert(String lastValue) {
	}

	@Override
	public void onAddressBarChanged(Window win, String newURL) {
	}

	@Override
	public void onConsoleMessage(Window win, String message, String sourceId, int lineNo) {
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
	public void onCreatedWindow(Window win, Window newWindow, Rect[] initialRect) {
	}

	@Override
	public void onCursorUpdated(Window win, Cursor newCursor) {
	}

	@Override
	public void onExternalHost(Window win, String message, String origin, String target) {
	}

	@Override
	public void onJavascriptCallback(Window win, Object replyMsg, String origin,
			String funcName, Variant[] args) {
	}

	@Override
	public void onLoad(Window win) {
	}

	@Override
	public void onLoadingStateChanged(Window win, boolean isLoading) {
	}

	@Override
	public boolean onNavigationRequested(Window win, String newUrl, String referrer,
			boolean isNewWindow, boolean[] cancelDefaultAction) {
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
	public void onRunFileChooser(Window win, int mode, String title, String defaultFile) {
	}

	@Override
	public void onScriptAlert(Window win, String message, String defaultValue,
			String url, int flags, boolean[] success, String[] value) {
	}

	@Override
	public void onShowContextMenu(Window win, ContextMenuEventArgs args) {
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

	@Override
	public void onWidgetCreated(Window win, Widget newWidget, int zIndex) {
	}

	@Override
	public void onWidgetDestroyed(Window win, Widget wid) {
	}

	@Override
	public void onWidgetMove(Window win, Widget wid, int newX, int newY) {
	}

	@Override
	public void onWidgetPaint(Window win, Widget wid, Buffer sourceBuffer,
			Rect sourceBufferRect, Rect[] copyRects, int dx, int dy, Rect scrollRect) {
	}

	@Override
	public void onWidgetResize(Window win, Widget wid, int newWidth, int newHeight) {
	}

}
