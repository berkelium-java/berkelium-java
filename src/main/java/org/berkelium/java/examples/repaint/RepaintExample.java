package org.berkelium.java.examples.repaint;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.Rect;
import org.berkelium.java.api.Window;
import org.berkelium.java.awt.BufferedImageAdapter;

public class RepaintExample extends JFrame {
	private static final long serialVersionUID = 8835790859223385092L;
	private final Berkelium runtime = Berkelium.getInstance();
	private final Window win = runtime.createWindow();
	private final LinkedList<Rect> rects = new LinkedList<Rect>();

	private final BufferedImageAdapter bia = new BufferedImageAdapter() {
		public void onPaintDone(Window win, final Rect rect) {
			rects.add(rect);
			if(rects.size() > 2) {
				rects.removeFirst();
			}
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					repaint();
				}
			});
		};
	};

	public RepaintExample() {
		setTitle("RepaintExample");
		setSize(new Dimension(1024, 768));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		BufferedImage img = bia.getImage();
		if (img != null) {
			synchronized (bia) {
				g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
			}
		}
		for(Rect rect : rects) {
			g.drawRect(rect.x, rect.y, rect.w, rect.h);
		}
	}

	public void run() throws Exception {
		win.setDelegate(bia);
		bia.resize(800, 600);
		win.resize(800, 600);
		//win.navigateTo("http://jensbeimsurfen.de/clock/");
		win.navigateTo("http://jensbeimsurfen.de/ping-pong/");
	}

	public static void main(String[] args) throws Exception {
		try {
			System.out.println("initializing berkelium-java...");
			Berkelium.createMultiThreadInstance();
			new RepaintExample().run();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
