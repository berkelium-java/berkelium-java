package org.berkelium.java.examples.javascript;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.Window;
import org.berkelium.java.api.WindowAdapter;

/**
 * Hello world!
 */
public class JavaScript {
	public static void main(String[] args) throws InterruptedException {
		System.err.println("creating runtime instance");
		final Berkelium berkelium = Berkelium.createSingleThreadInstance();

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
				if (!isLoading) {
					// win.executeJavascript("window.externalHost.postMessage('test', 'urn:uuid:test123');");
					win.executeJavascript("window.Berkelium('asyncCall','javafnk1', [])");
				}
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

			@Override
			public void onScriptAlert(Window win, String message,
					String defaultValue, String url, int flags,
					boolean[] success, String[] value) {
				System.err.println("onScriptAlert:" + message);
			}

			@Override
			public void onConsoleMessage(Window win, String message,
					String sourceId, int lineNo) {
				System.err.printf("onConsoleMessage: '%s' (%s:%d)\n", message,
						sourceId, lineNo);
			}

			@Override
			public void onJavascriptCallback(Window win, String url,
					String funcName) {
				System.err.printf("onJavascriptCallback: '%s' (%s)\n",
						funcName, url);
			}

			@Override
			public void onExternalHost(Window win, String message,
					String origin, String target) {
				System.err.printf("onExternalHost: '%s'/'%s'/'%s'\n", message,
						origin, target);
			}
		});

		update();

		System.err.println("navigateTo");
		win.navigateTo("http://www.google.de/search?q=hello%2C+world");
		update();

		update();
		win.executeJavascript("console.log('hello, world!');");
		update();
		win.executeJavascript("test = function(){console.log('test!');}");
		update();
		win.executeJavascript("test();");
		update();
		win.executeJavascript("alert('alert test!');");

		update();
		/*
		 * TODO win.bind("javafnk1", null);
		 */
		update();
		win.executeJavascript("console.dir(javafnk1);");
		update();
		/*
		 * TODO win.addBindOnStartLoading("javafnk2", null);
		 */
		update();
		win.executeJavascript("javafnk2();");

		update();
		win.navigateTo("http://www.google.de/search?q=100+%E2%82%AC");
		update();
		win.executeJavascript("javafnk1();");
		win.executeJavascript("javafnk2();");
		update();

		System.err.println("destroy");
		berkelium.destroy();

		System.err.println("done");
	}

	private static int count = 0;

	private static void update() throws InterruptedException {
		System.err.printf("\n\n===========================\n%d update loop\n",
				count++);
		for (int i = 0; i < 50; ++i) {
			Berkelium.getInstance().update();
			Thread.sleep(10);
		}
	}
}
