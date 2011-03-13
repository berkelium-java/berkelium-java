package org.berkelium;

public final class Context {
	private static Context instance = new Context();

	public Context() {
		System.out.println("Context()");
		handle = _init();
	}

	public void destroy() {
		if (handle != 0) {
			_destroy(handle);
			handle = 0;
		}
	}

	private static native long _init();

	private static native void _destroy(long handle);

	protected transient long handle;

	public static Context getDefault() {
		System.out.println("Context getDefault()");
		return instance;
	}
}
