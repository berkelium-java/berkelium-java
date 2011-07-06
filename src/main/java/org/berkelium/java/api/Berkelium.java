package org.berkelium.java.api;

public abstract class Berkelium {
	protected static Berkelium instance = null;

	public static Berkelium getInstance() {
		if (instance == null) {
			throw new IllegalStateException();
		}
		return instance;
	}
	
	public static Berkelium createInstance() {
		if (instance != null) {
			throw new IllegalStateException();
		}
		try {
			Class<?> c = Berkelium.class.getClassLoader().loadClass(
				"org.berkelium.java.Platform");
			instance = (Berkelium) c.getConstructor().newInstance();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return instance;
	}

	public abstract Window createWindow();

	public abstract void update();

	public abstract void destroy();
}
