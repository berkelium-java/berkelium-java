package org.berkelium.java.examples.browser;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;

import org.berkelium.java.Window;
import org.berkelium.java.WindowDelegate;

public class MultiDelegate implements InvocationHandler {

	private final List<WindowDelegate> delegates = new LinkedList<WindowDelegate>();
	private final WindowDelegate proxy = (WindowDelegate) Proxy.newProxyInstance(
		getClass().getClassLoader(), new Class<?>[] { WindowDelegate.class }, this);

	public void addDelegate(WindowDelegate delegate) {
		delegates.add(delegate);
	}

	public void removeDelegate(WindowDelegate delegate) {
		delegates.remove(delegate);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object ret = null;
		// System.err.println(method.getName() + args(args));
		for (WindowDelegate obj : delegates) {
			ret = method.invoke(obj, args);
		}
		return ret;
	}

	private static final String args(Object[] args) {
		String str = "";
		for (Object o : args) {
			if (o instanceof Window)
				continue;
			str += " '" + (o != null ? o.toString() : "null") + "' ";
		}
		return str;
	}

	public WindowDelegate getProxy() {
		return proxy;
	}
}
