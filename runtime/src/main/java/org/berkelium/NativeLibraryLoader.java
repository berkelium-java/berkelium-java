package org.berkelium;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NativeLibraryLoader {

	private final static File tempDir;
	static {
		try {
			tempDir = File.createTempFile("temp", Long.toString(System.nanoTime()));

			if (!tempDir.delete()) {
				throw new IOException("Could not delete temp file: "
						+ tempDir.getAbsolutePath());
			}

			if (!tempDir.mkdir()) {
				throw new IOException("Could not create temp directory: "
						+ tempDir.getAbsolutePath());
			}

			// System.err.println("using temp dir: " + tempDir.getCanonicalPath());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public NativeLibraryLoader(String sys) {
		try {
			String base = "org/berkelium/native/" + sys;
			processList(base);

			// we can not alter the PATH, so we need to load the dependencies first
			loadLib(base, "icudt42");
			loadLib(base, "avutil-50");
			loadLib(base, "avcodec-52");
			loadLib(base, "avformat-52");
			loadLib(base, "berkelium");
			loadLib(base, "berkelium-java");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private final void loadLib(String base, String name) throws IOException {
		final String lib = System.mapLibraryName(name);
		System.load(processFile(base + "/" + lib, lib).getCanonicalPath());
	}

	private final InputStream open(String name) throws IOException {
		InputStream ret = getClass().getClassLoader().getResourceAsStream(name);
		if (ret == null)
			throw new FileNotFoundException(name);
		return ret;
	}

	private final byte[] read(String name) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copy(open(name), baos);
		return baos.toByteArray();
	}

	private final void copy(InputStream in, OutputStream out) throws IOException {
		byte buf[] = new byte[4096];

		for (;;) {
			int len = in.read(buf, 0, buf.length);
			if (len <= 0) {
				break;
			}
			out.write(buf, 0, len);
		}
	}

	private final void processList(String base) throws IOException {
		final String data = new String(read(base + "/dependencies.txt"));

		for (String file : data.split(";")) {
			processFile(base + "/" + file, file);
		}
	}

	private File processFile(String from, String to) throws IOException {
		File file = new File(tempDir, to);
		file.getParentFile().mkdirs();
		OutputStream os = new FileOutputStream(file);
		copy(open(from), os);
		os.close();
		return file;
	}

	public String getSystemPath() throws IOException {
		String path = tempDir.getCanonicalPath();
		return System.getenv("PATH") + ";\"" + path + "\"";
	}
}
