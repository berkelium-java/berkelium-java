package org.berkelium.java;

import java.nio.ByteBuffer;

public interface Buffer {
	public ByteBuffer getByteBuffer();

	public int[] getIntArray();

	public byte[] getByteArray();
}
