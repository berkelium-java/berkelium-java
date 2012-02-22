package org.berkelium.java.test;

import junit.framework.Assert;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.DebugDelegate;
import org.berkelium.java.api.Window;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class AbstractBerkeliumTest {
	protected Window window;
	protected static Berkelium runtime;

	@BeforeClass
	public static void beforeClass() {
		if (runtime == null) {
			runtime = Berkelium.createMultiThreadInstance();
		}
	}

	@Before
	public void createWindow() {
		Assert.assertNull(window);
		window = runtime.createWindow();
		window.addDelegate(new DebugDelegate().getProxy());
		window.resize(640, 480);
	}

	@After
	public void destoryWindow() {
		Assert.assertNotNull(window);
		window.destroy();
		window = null;
	}

	protected String createTestMessage() {
		return String.format("berkelium-java test message %d %f",
				System.currentTimeMillis(), Math.random());
	}
}
