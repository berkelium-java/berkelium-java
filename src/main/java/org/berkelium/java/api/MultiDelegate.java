package org.berkelium.java.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MultiDelegate implements InvocationHandler {

	private final Set<WindowDelegate> delegates = Collections
			.synchronizedSet(new HashSet<WindowDelegate>());
	private final WindowDelegate proxy = (WindowDelegate) Proxy
			.newProxyInstance(getClass().getClassLoader(),
					new Class<?>[] { WindowDelegate.class }, this);

	public void addDelegate(WindowDelegate delegate) {
		delegates.add(delegate);
	}

	public void removeDelegate(WindowDelegate delegate) {
		delegates.remove(delegate);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object ret = null;
		// System.err.println(method.getName() + args(args));
		HashSet<WindowDelegate> ds = new HashSet<WindowDelegate>();
		ds.addAll(delegates);
		for (WindowDelegate obj : ds) {
			ret = method.invoke(obj, args);
		}
		return ret;
	}

	@SuppressWarnings("unused")
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

	public void clear() {
		delegates.clear();
	}
}
