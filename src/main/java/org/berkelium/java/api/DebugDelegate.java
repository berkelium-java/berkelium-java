package org.berkelium.java.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.berkelium.java.js.Function1;

public class DebugDelegate implements InvocationHandler {
	private final WindowAdapter adapter = new WindowAdapter();
	private final Function1<Void, String> handler;
	private final WindowDelegate proxy = (WindowDelegate) Proxy
			.newProxyInstance(getClass().getClassLoader(),
					new Class<?>[] { WindowDelegate.class }, this);

	public DebugDelegate(Function1<Void, String> handler) {
		this.handler = handler;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		StringBuffer sb = new StringBuffer();
		sb.append(method.getReturnType().getName());
		sb.append(' ');
		sb.append(method.getName());
		sb.append('(');
		for (int i = 0, to = args == null ? 0 : args.length; i < to; i++) {
			if (i != 0) {
				sb.append(", ");
			}
			if (args[i] == null) {
				sb.append("null");
			} else if (args[i] instanceof String) {
				sb.append('"');
				sb.append(args[i]);
				sb.append('"');
			} else {
				sb.append(args[i].toString());
			}
		}
		sb.append(')');
		handler.run(sb.toString());
		return method.invoke(adapter, args);
	}

	public WindowDelegate getProxy() {
		return proxy;
	}
}
