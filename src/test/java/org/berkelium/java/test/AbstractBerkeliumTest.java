package org.berkelium.java.test;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.DebugDelegate;
import org.berkelium.java.api.Window;
import org.berkelium.java.js.Function1;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractBerkeliumTest {
	protected Window window;
	protected final static Berkelium runtime = Berkelium
			.createMultiThreadInstance();

	private final Function1<Void, String> debug = new Function1<Void, String>() {
		@Override
		public Void run(String arg) {
			System.err.println(arg);
			return null;
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
