package org.berkelium.java.examples.browser;

import org.berkelium.java.api.Berkelium;

public class BrowserApp {
	public static void main(String[] args) {
		try {
			System.out.println("initializing berkelium-java...");

			// multi-threaded api
			Berkelium.createMultiThreadInstance();
			startBrowser();
/*
			// single-threaded api
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					// this dont work: berkelium must be setup before awt
					final Berkelium b = Berkelium.createSingleThreadInstance();
					startBrowser();
					new Runnable() {
						public void run() {
							b.update();
							SwingUtilities.invokeLater(this);
						}
					}.run();
				}
			});
 */
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static void startBrowser() {
		SimpleBrowser win = new SimpleBrowser();
		Tab tab = new Tab();
		win.setTab(tab);
		tab.setUrl("http://jensbeimsurfen.de/clock/");
	}
}
