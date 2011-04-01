package org.berkelium.java.example.helloworld;

import org.berkelium.java.Berkelium;
import org.berkelium.java.Window;
import org.berkelium.java.WindowAdapter;

/**
 * Hello world!
 */
public class HelloWorld {
	public static void main(String[] args) throws InterruptedException {
		System.err.println("creating runtime instance");
		final Berkelium berkelium = Berkelium.createInstance();

		System.err.println("creating window");
		Window win = berkelium.createWindow();

		System.err.println("setting window delegate");
		win.setDelegate(new WindowAdapter() {
			@Override
			public void onLoad(Window win) {
				System.err.println("onLoad");
			}

			@Override
			public void onLoadingStateChanged(Window win, boolean isLoading) {
				System.err.println("onLoadingStateChanged:" + isLoading);
			}

			@Override
			public void onStartLoading(Window win, String newURL) {
				System.err.println("onStartLoading:" + newURL);
			}

			@Override
			public void onAddressBarChanged(Window win, String newURL) {
				System.err.println("onAddressBarChanged:" + newURL);
			}

			@Override
			public void onTitleChanged(Window win, String newURL) {
				System.err.println("onTitleChanged:" + newURL);
			}
		});

		System.err.println("navigateTo");
		win.navigateTo("http://www.google.de/search?q=hello%2C+world");

		System.err.println("update loop");
		for (int i = 0; i < 200; ++i) {
			Berkelium.getInstance().update();
			Thread.sleep(50);
		}
		System.err.println("destroy");
		berkelium.destroy();

		System.err.println("done");
	}
}
