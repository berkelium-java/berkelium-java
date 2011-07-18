package org.berkelium.java.api;

public abstract class Berkelium {
	protected static Berkelium instance = null;

	public synchronized static Berkelium getInstance() {
		if (instance == null) {
			throw new IllegalStateException();
		}
		return instance;
	}

	public static Berkelium createSingleThreadInstance() {
		return createBerkeliumInstance("org.berkelium.java.impl.SingleThreadBerkelium");
	}

	public static Berkelium createMultiThreadInstance() {
		return createBerkeliumInstance("org.berkelium.java.impl.MultiThreadBerkelium");
	}

	private synchronized static Berkelium createBerkeliumInstance(String clazz) {
		if (instance != null) {
			throw new IllegalStateException();
		}
		try {
			Class<?> c = Berkelium.class.getClassLoader().loadClass(clazz);
			instance = (Berkelium) c.getConstructor().newInstance();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return instance;
	}

	public abstract Window createWindow();

	public abstract void execute(Runnable job);

	public abstract void executeAndWait(Runnable job);

	public abstract void sync(Window window);

	public abstract void assertIsBerkeliumThread();

	public abstract void assertNotBerkeliumThread();

	public abstract void update();

	public abstract void destroy();
}
