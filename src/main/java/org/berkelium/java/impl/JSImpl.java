package org.berkelium.java.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.berkelium.java.api.Window;
import org.berkelium.java.api.WindowAdapter;
import org.berkelium.java.js.Function0;

public class JSImpl {
	private final Map<String, JavaScriptBind> javaScriptBinds = Collections
			.synchronizedMap(new HashMap<String, JavaScriptBind>());
	private final Window window;

	private static class JavaScriptBind {
		final String id = "urn:uuid:" + UUID.randomUUID().toString();
		String name;
		Function0<?> function0;
	}

	private final WindowAdapter bindAdapter = new WindowAdapter() {
		@Override
		public void onExternalHost(Window win, String message, String origin,
				String target) {
			JavaScriptBind bind = javaScriptBinds.get(target);
			if (bind != null) {
				System.err.printf("JavaScript Function '%s' called!\n",
						bind.name);
				if (bind.function0 != null) {
					bind.function0.run();
				}
			}
		}
	};

	public JSImpl(Window window) {
		this.window = window;
		window.getThreadProxyWindow().addDelegate(bindAdapter);
	}

	public <R> void bind(String name, Function0<R> function) {
		JavaScriptBind jsb = new JavaScriptBind();
		jsb.function0 = function;
		jsb.name = name;
		javaScriptBinds.put(jsb.id, jsb);
		window.getThreadProxyWindow().executeJavascript(
				name + "=function(){window.externalHost.postMessage('', '"
						+ jsb.id + "')}");
	}
}
