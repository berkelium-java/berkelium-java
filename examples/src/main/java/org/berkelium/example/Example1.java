package org.berkelium.example;

import org.berkelium.Berkelium;
import org.berkelium.Buffer;
import org.berkelium.Rect;
import org.berkelium.Window;
import org.berkelium.WindowAdapter;

class Example1 extends WindowAdapter {
	private final static Berkelium berkelium = Berkelium.getInstance();
	private final Window win;

	public Example1() {
		win = berkelium.createWindow();
		win.setDelegate(this);
	}

	public void run() throws InterruptedException {
		win.navigateTo("http://google.com");
		for (int i = 0; i < 100; ++i) {
			Berkelium.getInstance().update();
			Thread.sleep(10);
		}
	}

	@Override
	public void onPaint(Window win, Buffer sourceBuffer, Rect sourceBufferRect,
			Rect[] copyRects, int dx, int dy, Rect scrollRect) {
		System.out.println("onPaint");
	}

	public static void main(String[] args) throws Exception {
		new Example1().run();
		// shutdown berkelium
		Berkelium.getInstance().destroy();
	}
}