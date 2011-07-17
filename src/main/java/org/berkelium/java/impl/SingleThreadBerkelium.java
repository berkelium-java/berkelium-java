package org.berkelium.java.impl;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.Rect;
import org.berkelium.java.api.Window;
import org.berkelium.java.api.WindowAdapter;

public final class SingleThreadBerkelium extends Berkelium {
	private final static HashMap<Long, Object> map = new HashMap<Long, Object>();
	private final static NativeLibraryLoader loaded = new NativeLibraryLoader();
	private final Thread thread = Thread.currentThread();

	public SingleThreadBerkelium() {
		try {
			_init(loaded.getSystemPath(), loaded.getTempPath());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public final Window createWindow() {
		return new WindowImpl(this);
	}

	private final static native void _init(String path, String berkeliumDir);

	@Override
	public final native void update();

	@Override
	public final native void destroy();

	// FIXME: jni helper
	public final static Rect createRect(int x, int y, int w, int h) {
		return new Rect(x, y, w, h);
	}

	// FIXME: jni helper
	public final static Object createRectArray(int i) {
		return new Rect[i];
	}

	// FIXME: jni helper
	public final static void createRectInArray(Object array, int i, int x,
			int y, int w, int h) {
		Rect rect[] = (Rect[]) array;
		rect[i] = new Rect(x, y, w, h);
	}

	public final static Object get(long handle) {
		Object ret = map.get(handle);
		// System.out.println("Registry.get(" + handle + "): " + ret);
		return ret;
	}

	public final static void add(long handle, Object obj) {
		// System.out.println("Registry.add(" + handle + "):" + obj);
		map.put(handle, obj);
	}

	public final static void remove(long handle) {
		// System.out.println("Registry.remove(" + handle + ")");
		map.remove(handle);
	}

	@Override
	public void execute(Runnable job) {
		job.run();
	}

	@Override
	public void executeAndWait(Runnable job) {
		job.run();
	}

	@Override
	public void sync(Window win) {
		assertNotSameThread();
		win = win.getRealWindow();
		final String prefix = UUID.randomUUID().toString();
		final CyclicBarrier barrier = new CyclicBarrier(2);
		final WindowAdapter delegate = new WindowAdapter() {
			@Override
			public void onConsoleMessage(Window win, String message,
					String sourceId, int lineNo) {
				if (message.equals(prefix)) {
					try {
						barrier.await();
					} catch (InterruptedException e) {
					} catch (BrokenBarrierException e) {
					}
				}
			}
		};
		win.addDelegate(delegate);
		win.getThreadProxyWindow().executeJavascript(
				"console.log('" + prefix + "');");
		try {
			barrier.await();
		} catch (InterruptedException e) {
		} catch (BrokenBarrierException e) {
		}
		win.removeDelegate(delegate);
	}

	@Override
	public void assertNotSameThread() {
		if (Thread.currentThread().equals(thread)) {
			throw new IllegalAccessError(
					"Can not call this function from inside berkelium thread!");
		}
	}
}
