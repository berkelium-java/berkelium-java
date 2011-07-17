package org.berkelium.java.test;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.DebugDelegate;
import org.berkelium.java.api.LogHandler;
import org.berkelium.java.api.Window;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractBerkeliumTest {
	protected Window window;
	protected final static Berkelium runtime = Berkelium
			.createMultiThreadInstance();

	private final LogHandler debug = new LogHandler() {
		@Override
		public void log(String message) {
			System.err.println(message);
		}
	};

	@Before
	public void createWindow() {
		window = runtime.createWindow().getThreadProxyWindow();
		window.addDelegate(new DebugDelegate(debug).getProxy());
	}

	@After
	public void destoryWindow() {
		window.destroy();
		window = null;
	}

	protected String createTestMessage() {
		return String.format("berkelium-java test message %d %f",
				System.currentTimeMillis(), Math.random());
	}
}
