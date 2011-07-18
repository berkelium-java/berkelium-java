package org.berkelium.java.examples.browser;

import org.berkelium.java.api.Berkelium;

public class BrowserApp {
	public static void main(String[] args) {
		try {
			System.out.println("initializing berkelium-java...");
			Berkelium.createMultiThreadInstance();
			SimpleBrowser win = new SimpleBrowser();
			Tab tab = new Tab();
			win.setTab(tab);
			tab.setUrl("http://jensbeimsurfen.de/clock/");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
