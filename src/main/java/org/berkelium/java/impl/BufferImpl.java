package org.berkelium.java.impl;

import java.nio.ByteBuffer;

import org.berkelium.java.api.Buffer;

public final class BufferImpl implements Buffer {
	private int[] intArray = null;
	private byte[] byteArray = null;

	@Override
	public ByteBuffer getByteBuffer() {
		return createByteBuffer(handle, size);
	}

	@Override
	public int[] getIntArray() {
		if (intArray == null) {
			intArray = createIntArray(handle, size);
		}
		return intArray;
	}

	@Override
	public byte[] getByteArray() {
		if (byteArray == null) {
			byteArray = createByteArray(handle, size);
		}
		return byteArray;
	}

	// JNI helper:
	private static native int[] createIntArray(long handle, int size);

	private static native byte[] createByteArray(long handle, int size);

	private native static ByteBuffer createByteBuffer(long handle, int size);

	private BufferImpl() {
	}

	private static Object create(long handle, int size) {
		BufferImpl ret = new BufferImpl();
		ret.handle = handle;
		ret.size = size;
		return ret;
	}

	private long handle;
	private int size;
}
