package org.berkelium.java.examples.browser;

import org.berkelium.java.api.Berkelium;

public class BrowserApp {
	private final Berkelium runtime = Berkelium.getInstance();

	public void run() throws Exception {
		SimpleBrowser win = new SimpleBrowser();
		Tab tab = new Tab();
		win.setTab(tab);
		tab.setUrl("http://jensbeimsurfen.de/clock/");

		while (win.isVisible()) {
			win.update();
			synchronized (runtime) {
				runtime.update();
			}
			win.checkRepaint();
			Thread.sleep(10);
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			System.out.println("initializing berkelium-java...");
			Berkelium.createInstance();
			new BrowserApp().run();
			System.out.println("main loop terminated.");
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			System.out.println("destroying berkelium-java...");
			Berkelium.getInstance().destroy();
			System.out.println("berkelium-java destroyed.");
			System.exit(0);
		}
	}
}
