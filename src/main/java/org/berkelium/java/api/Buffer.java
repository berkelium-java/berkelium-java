package org.berkelium.java.api;

import java.nio.ByteBuffer;

public interface Buffer {
	public ByteBuffer getByteBuffer();

	public int[] getIntArray();

	public byte[] getByteArray();
}
