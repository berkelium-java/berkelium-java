package org.berkelium.java.impl;

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
			File temp = new File(System.getProperty("java.io.tmpdir"),
					"berkelium." + System.getProperty("user.name"));

			if (!temp.exists() && !temp.mkdir()) {
				throw new IOException("Could not create temp directory: "
						+ temp.getAbsolutePath());
			}

			for (File path : temp.listFiles()) {
				deleteDirectory(path);
			}

			tempDir = new File(temp, Long.toString(System.nanoTime()));
			tempDir.deleteOnExit();
			temp.deleteOnExit();

			// System.err.println("using temp dir: " +
			// tempDir.getCanonicalPath());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	static public void deleteDirectory(File path) {
		if (path.exists()) {
			for (File entry : path.listFiles()) {
				if (entry.isDirectory()) {
					deleteDirectory(entry);
				} else {
					entry.delete();
				}
			}
			path.delete();
		}
	}

	private final String systemType;

	public NativeLibraryLoader() {
		try {
			systemType = read("org/berkelium/java/native/systemType.txt");
			String base = "org/berkelium/java/native/" + systemType;
			processList(base);

			// we can not alter the PATH, so we need to load the dependencies
			// first
			if (systemType.startsWith("win")) {
				loadLib(base, "icudt46");
				loadLib(base, "avutil-50");
				loadLib(base, "avcodec-52");
				loadLib(base, "avformat-52");
				loadLib(base, "berkelium");
			} else {
				try {
					loadLib(base, "libberkelium");
				} catch(FileNotFoundException ex) {
					try {
						loadLib(base, "libberkelium_d");
					} catch(FileNotFoundException ex2) {
						throw ex;
					}
				}
			}
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

	private final String read(String name) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copy(name, baos);
		return new String(baos.toByteArray());
	}

	private final void copy(String from, OutputStream out) throws IOException {
		byte buf[] = new byte[4096];
		InputStream in = open(from);

		for (;;) {
			int len = in.read(buf, 0, buf.length);
			if (len <= 0) {
				break;
			}
			out.write(buf, 0, len);
		}
		in.close();
	}

	private final void processList(String base) throws IOException {
		final String data = read(base + "/dependencies.txt");

		for (String file : data.split(";")) {
			processFile(base + "/" + file, file);
		}
	}

	private File processFile(String from, String to) throws IOException {
		File file = new File(tempDir, to);
		file.getParentFile().mkdirs();
		OutputStream os = new FileOutputStream(file);
		copy(from, os);
		os.close();
		if ("berkelium".equals(file.getName())) {
			// FIXME: check if we are running with java 1.5:
			if (systemType.startsWith("linux")) {
				Runtime.getRuntime().exec("chmod +x " + file.getAbsolutePath());
			}
			// file.setExecutable(true);
		}
		return file;
	}

	public String getSystemPath() throws IOException {
		String path = tempDir.getCanonicalPath();
		if (path.contains(" ")) {
			path = "\"" + path + "\"";
		}
		return System.getenv("PATH") + File.pathSeparator + path;
	}

	public String getTempPath() throws IOException {
		return tempDir.getCanonicalPath();
	}
}
