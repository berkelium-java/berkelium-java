package org.berkelium;

import java.nio.ByteBuffer;

public interface Buffer {
	public ByteBuffer getByteBuffer();

	public int[] getIntArray();

	public byte[] getByteArray();
}
