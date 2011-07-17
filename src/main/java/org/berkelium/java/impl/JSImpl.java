package org.berkelium.java.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.berkelium.java.api.Window;
import org.berkelium.java.api.WindowAdapter;

public class JSImpl {
	private final Map<String, JavaScriptBind> javaScriptBinds = Collections
			.synchronizedMap(new HashMap<String, JavaScriptBind>());
	private final Window window;

	private static class JavaScriptBind {
		final String id = "urn:uuid:" + UUID.randomUUID().toString();
		String name;
		Object target;
		Method method;
	}

	private final WindowAdapter bindAdapter = new WindowAdapter() {
		@Override
		public void onExternalHost(Window win, String message, String origin,
				String target) {
			System.err.printf("test '%s'/'%s' !\n", message, target);
			JavaScriptBind bind = javaScriptBinds.get(target);
			if (bind != null) {
				System.err.printf("JavaScript Function '%s' called!\n",
						bind.name);
				try {
					//TODO(drieks) arguments
					bind.method.setAccessible(true);
					bind.method.invoke(bind.target);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	public JSImpl(Window window) {
		this.window = window;
		window.getThreadProxyWindow().addDelegate(bindAdapter);
	}

	public <R> void bind(String name, Object target, String method, Class<?>... types) {
		JavaScriptBind jsb = new JavaScriptBind();
		try {
			jsb.method = target.getClass().getDeclaredMethod(method, types);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
		jsb.target = target;
		jsb.name = name;
		javaScriptBinds.put(jsb.id, jsb);
		StringBuilder cmd = new StringBuilder();
		cmd.append(name);
		cmd.append("=function(){window.externalHost.postMessage(JSON.stringify(arguments)");
		for(int i = 0; i < types.length; i++) {
			String type = types[i].getCanonicalName();
			if("java.lang.String".equals(type)) {
			} else {
				throw new IllegalArgumentException();
			}
			//cmd.append(String.format("+", i));
		}
		cmd.append(", '");
		cmd.append(jsb.id);
		cmd.append("')};");
		window.getThreadProxyWindow().executeJavascript(cmd.toString());
	}
}
