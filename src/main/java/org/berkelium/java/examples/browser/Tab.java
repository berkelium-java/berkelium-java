package org.berkelium.java.examples.browser;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.Window;
import org.berkelium.java.api.WindowDelegate;
import org.berkelium.java.awt.BufferedImageAdapter;

public final class Tab {
	private final Window win = Berkelium.getInstance().createWindow();
	private final BufferedImageAdapter bia = new BufferedImageAdapter();
	private String url;

	public Tab() {
		win.addDelegate(bia);
	}

	public Tab(String url) {
		this();
		setUrl(url);
	}

	public void paint(Graphics g, int w, int h) {
		BufferedImage img = bia.getImage();
		if (img != null) {
			// do not allow updates to the image while we draw it
			synchronized (this) {
				g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
			}
		}
	}

	public void resize(int width, int height) {
		win.resize(width, height);
		bia.resize(width, height);
	}

	public Window getWindow() {
		return win;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (!url.contains("://")) {
			if (url.contains(" ")) {
				try {
					url = "http://google.com/search?q="
							+ URLEncoder.encode(url, "UTF-8");
				} catch (UnsupportedEncodingException e) {
				}
			} else {
				url = "http://" + url;
			}
		}
		this.url = url;
		win.navigateTo(url);
	}

	public void addDelegate(WindowDelegate delegate) {
		win.addDelegate(delegate);
	}

	public void removeDelegate(WindowDelegate delegate) {
		win.removeDelegate(delegate);
	}

	public void execute(Runnable job) {
		getWindow().getBerkelium().execute(job);
	}
}