package org.berkelium.example;

import org.berkelium.java.Berkelium;
import org.berkelium.java.Buffer;
import org.berkelium.java.Rect;
import org.berkelium.java.Window;
import org.berkelium.java.WindowAdapter;

class Example1 extends WindowAdapter {
	private final static Berkelium runtime = Berkelium.createInstance();
	private final Window win;

	public Example1() {
		win = runtime.createWindow();
		win.setDelegate(this);
	}

	public void run() throws InterruptedException {
		win.navigateTo("http://google.com");
		for (int i = 0; i < 100; ++i) {
			runtime.update();
			Thread.sleep(100);
		}
	}

	@Override
	public void onAddressBarChanged(Window win, String newURL) {
		System.out.println("onAddressBarChanged:" + newURL);
	}

	@Override
	public void onLoadingStateChanged(Window win, boolean isLoading) {
		System.out.println("onLoadingStateChanged:" + isLoading);
	}

	@Override
	public void onPaint(Window win, Buffer sourceBuffer, Rect sourceBufferRect,
			Rect[] copyRects, int dx, int dy, Rect scrollRect) {
		System.out.println("onPaint");
	}

	public static void main(String[] args) throws Exception {
		new Example1().run();
		// shutdown berkelium
		runtime.destroy();
	}
}