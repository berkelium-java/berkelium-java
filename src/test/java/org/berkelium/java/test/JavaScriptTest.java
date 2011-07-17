package org.berkelium.java.test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.Assert;

import org.berkelium.java.api.Window;
import org.berkelium.java.api.WindowAdapter;
import org.berkelium.java.js.Function0;
import org.junit.Test;

public class JavaScriptTest extends AbstractBerkeliumTest {
	@Test
	public void executeJavascript() {
		final String testMessage = createTestMessage();
		final AtomicBoolean result = new AtomicBoolean(false);

		window.addDelegate(new WindowAdapter() {
			@Override
			public void onConsoleMessage(Window win, String message,
					String sourceId, int lineNo) {
				Assert.assertEquals(win.getRealWindow(), window.getRealWindow());
				if (testMessage.equals(message)) {
					result.set(true);
				}
			}
		});

		window.executeJavascript("console.log('" + testMessage + "');");

		runtime.sync(window);

		Assert.assertTrue("onConsoleMessage not called!", result.get());
	}

	@Test
	public void executeJavascript1000() {
		for (int i = 0; i < 1000; i++) {
			// executeJavascript();
		}
	}

	@Test
	public void externalHost() {
		final String id = "urn:uuid:" + UUID.randomUUID().toString();
		final String testMessage = createTestMessage();
		final AtomicBoolean result = new AtomicBoolean(false);

		window.addDelegate(new WindowAdapter() {
			@Override
			public void onExternalHost(Window win, String message,
					String origin, String target) {
				if (id.equals(target)) {
					result.set(true);
				}
			}
		});

		window.executeJavascript("window.externalHost.postMessage('"
				+ testMessage + "', '" + id + "');");

		runtime.sync(window);

		Assert.assertTrue("onExternalHost not called!", result.get());
	}

	@Test
	public void javaScriptBindTest() {
		final String id = "testfnk"
				+ UUID.randomUUID().toString().replace('-', '_');
		final AtomicBoolean result = new AtomicBoolean(false);

		Function0<Void> function = new Function0<Void>() {
			@Override
			public Void run() {
				result.set(true);
				return null;
			}
		};

		window.bind(id, function);
		runtime.sync(window);
		window.executeJavascript(id + "();");
		runtime.sync(window);

		Assert.assertTrue("JavaScript Function not called!", result.get());
	}
}
