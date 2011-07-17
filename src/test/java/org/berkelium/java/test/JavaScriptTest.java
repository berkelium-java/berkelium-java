package org.berkelium.java.test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;

import org.junit.Test;

public class JavaScriptTest extends AbstractBerkeliumTest {
	@Test(timeout=20000)
	public void javaScriptBind0Test() {
		final String id = "testfnk"
				+ UUID.randomUUID().toString().replace('-', '_');
		final AtomicBoolean result = new AtomicBoolean(false);

		Object function = new Object() {
			@SuppressWarnings("unused")
			public void run() {
				result.set(true);
			}
		};

		window.bind(id, function, "run");
		runtime.sync(window);
		window.call(id);
		runtime.sync(window);

		Assert.assertTrue("JavaScript Function not called!", result.get());
	}

	@Test(timeout=20000)
	public void javaScriptBind1Test() {
		final String id = "testfnk"
				+ UUID.randomUUID().toString().replace('-', '_');
		final String testMessage = createTestMessage();
		final AtomicReference<String> result = new AtomicReference<String>();

		Object function = new Object() {
			@SuppressWarnings("unused")
			public void run(String arg) {
				result.set(arg);
			}
		};

		window.bind(id, function, "run", String.class);
		runtime.sync(window);
		window.call(id, testMessage);
		runtime.sync(window);

		Assert.assertEquals("JavaScript Function not called!", testMessage, result.get());
	}
}
