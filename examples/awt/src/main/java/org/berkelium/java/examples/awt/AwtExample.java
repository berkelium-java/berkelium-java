package org.berkelium.java.examples.awt;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;

import org.berkelium.java.Berkelium;
import org.berkelium.java.Window;

public class AwtExample extends JFrame {
	private static final long serialVersionUID = 8835790859223385092L;
	private final int width = 640;
	private final int height = 480;
	private final BufferedImage img = new BufferedImage(width, height,
			BufferedImage.TYPE_INT_ARGB);
	private final ImageAdapter delegate = new ImageAdapter(img);
	private final Berkelium runtime = Berkelium.getInstance();
	private final Window win = runtime.createWindow();

	public AwtExample() {
		setTitle("AwtExample");
		setMinimumSize(new Dimension(width - 1, height - 1));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				final int x = e.getX() * width / getWidth();
				final int y = e.getY() * height / getHeight();
				final int b = e.getButton();
				invoke(new Runnable() {
					public void run() {
						win.mouseMoved(x, y);
						win.mouseButton(b, false);
					}
				});
			}

			@Override
			public void mousePressed(MouseEvent e) {
				final int x = e.getX() * width / getWidth();
				final int y = e.getY() * height / getHeight();
				final int b = e.getButton();
				invoke(new Runnable() {
					public void run() {
						win.mouseMoved(x, y);
						win.mouseButton(b, true);
					}
				});
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		if (img != null)
			g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}

	Queue<Runnable> queue = new ConcurrentLinkedQueue<Runnable>();

	private void invoke(Runnable runnable) {
		queue.add(runnable);
	}

	public void run() throws Exception {
		synchronized (runtime) {
			win.resize(width, height);
			// win.navigateTo("http://google.de");
			win.navigateTo("http://www.youtube.com/");
			win.setDelegate(delegate);
		}

		while (isVisible()) {
			while (!queue.isEmpty()) {
				queue.remove().run();
			}
			synchronized (runtime) {
				runtime.update();
			}
			if (delegate.isUpdated()) {
				repaint();
			}
			Thread.sleep(10);
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			Berkelium.createInstance();
			new AwtExample().run();
		} finally {
			Berkelium.getInstance().destroy();
			System.exit(0);
		}
	}
}
