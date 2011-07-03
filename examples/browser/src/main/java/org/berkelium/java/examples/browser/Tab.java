package org.berkelium.java.examples.browser;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.berkelium.java.Berkelium;
import org.berkelium.java.BufferedImageAdapter;
import org.berkelium.java.Rect;
import org.berkelium.java.Window;
import org.berkelium.java.WindowDelegate;

public final class Tab {
	private final Window win = Berkelium.getInstance().createWindow();
	private final BufferedImageAdapter bia = new BufferedImageAdapter();
	private final MultiDelegate multi = new MultiDelegate();
	private String url;

	public Tab() {
		multi.addDelegate(bia);
		win.setDelegate(multi.getProxy());
	}

	public Tab(String url) {
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
					url = "http://google.com/search?q=" + URLEncoder.encode(url, "UTF-8");
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
		multi.addDelegate(delegate);
	}

	public void removeDelegate(WindowDelegate delegate) {
		multi.removeDelegate(delegate);
	}

	public Rect getUpdatedRect() {
		return bia.getUpdatedRect();
	}
}