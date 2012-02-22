package org.berkelium.java.test;

import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.Assert;

import org.berkelium.java.api.Buffer;
import org.berkelium.java.api.DebugDelegate;
import org.berkelium.java.api.Rect;
import org.berkelium.java.api.Window;
import org.berkelium.java.api.WindowAdapter;
import org.berkelium.java.api.WindowDelegate;
import org.junit.Test;

public class StressTest extends AbstractBerkeliumTest {
	@Test
	public void onPaint() throws InterruptedException {
		for(int i = 0, to = 100; i < to; i++) {
			//System.err.printf("running test %d of %d\n", i + 1, to);
			final AtomicBoolean result = new AtomicBoolean(false);
			
			window.addDelegate(new WindowAdapter() {
				@Override
				public void onPaint(Window win, Buffer sourceBuffer,
						Rect sourceBufferRect, Rect[] copyRects, int dx, int dy,
						Rect scrollRect) {
					Assert.assertEquals(win.getRealWindow(), window.getRealWindow());
					result.set(true);
				}
			});
			
			window.navigateTo("http://google.com/");
			
			runtime.sync(window);
			
			Assert.assertTrue("onPaint not called!", result.get());
			if(i < to - 1) {
				destoryWindow();
				createWindow();
			}
		}
	}

	@Test
	public void create() throws InterruptedException {
		Window[] wins = new Window[100];
		WindowDelegate deligate = new DebugDelegate().getProxy();
		for(int i = 0, to = wins.length; i < to; i++) {
			wins[i] = runtime.createWindow();
			wins[i].addDelegate(deligate);
		}
		for(int i = 0, to = wins.length; i < to; i++) {
			wins[i].resize(640, 480);
		}
		for(int i = 0, to = wins.length; i < to; i++) {
			runtime.sync(wins[i]);
		}
		for(int i = 0, to = wins.length; i < to; i++) {
			wins[i].destroy();
		}
	}
}
