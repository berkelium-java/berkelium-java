package org.berkelium.java.impl;

import java.util.HashMap;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.Rect;
import org.berkelium.java.api.Window;

public final class Platform extends Berkelium {
	private final static HashMap<Long, Object> map = new HashMap<Long, Object>();
	private final static NativeLibraryLoader loaded = new NativeLibraryLoader();

	static {
		try {
			_init(loaded.getSystemPath(), loaded.getTempPath());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public Platform() {
	}

	@Override
	public final Window createWindow() {
		return new WindowImpl();
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
	public final static void createRectInArray(Object array, int i, int x, int y, int w,
			int h) {
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
}
