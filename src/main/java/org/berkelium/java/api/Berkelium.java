package org.berkelium.java.api;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;

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

	private final static LinkedList<LogHandler> handlers = new LinkedList<LogHandler>();

	public static void addLogHandler(LogHandler handler) {
		synchronized (handlers) {
			handlers.add(handler);
		}
	}

	public static void removeLogHandler(LogHandler handler) {
		synchronized (handlers) {
			handlers.remove(handler);
		}
	}
	public final static LogHandler berkeliumLogHandler = new LogHandler() {
		public void log(String message) {
			Berkelium.log(message);
		}
	}; 

	public static void log(String format, Object ... args) {
		log(String.format(format, args));
	}

	public static void log(String message) {
		synchronized (handlers) {
			if(handlers.size() == 0) {
				addLogHandler(systemErrLogHandler);
			}
			for(LogHandler handler : handlers) {
				handler.log(message);
			}
		}
	}

	public final static LogHandler systemErrLogHandler = new LogHandler() {
		public void log(String message) {
			System.err.println(message);
		}
	};

	public static void handleThrowable(Throwable t) {
		StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    t.printStackTrace(pw);
		log(t.getMessage() + "\n" + pw.toString());
	}
}
