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
import org.berkelium.java.BufferedImageAdapter;
import org.berkelium.java.Rect;
import org.berkelium.java.Window;

public class AwtExample extends JFrame {
	private static final long serialVersionUID = 8835790859223385092L;
	private final Berkelium runtime = Berkelium.getInstance();
	private final Window win = runtime.createWindow();
	private final BufferedImageAdapter bia = new BufferedImageAdapter();
	private final int initialWidth = 640;
	private final int initialHeight = 480;
	private final Queue<Runnable> queue = new ConcurrentLinkedQueue<Runnable>();

	public AwtExample() {
		setTitle("AwtExample");
		setSize(new Dimension(initialWidth, initialHeight));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				handleMouseButtonEvent(e, false);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseButtonEvent(e, true);
			}
		});
	}

	private void handleMouseButtonEvent(MouseEvent e, final boolean down) {
		final BufferedImage bi = bia.getImage();
		if (bia == null)
			return;
		final int x = e.getX() * bi.getWidth() / getWidth();
		final int y = e.getY() * bi.getHeight() / getHeight();
		final int b = e.getButton();

		// the event must be handled in the berkelium thread
		queue.add(new Runnable() {
			@Override
			public void run() {
				win.mouseMoved(x, y);
				win.mouseButton(b, down);
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		BufferedImage img = bia.getImage();
		if (img != null) {
			// do not allow updates to the image while we draw it
			synchronized (bia) {
				g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
			}
		}
	}

	public void run() throws Exception {
		synchronized (runtime) {
			win.setDelegate(bia);
			bia.resize(initialWidth, initialHeight);
			win.resize(initialWidth, initialHeight);
			win.navigateTo("http://www.youtube.com/");
		}

		while (isVisible()) {
			while (!queue.isEmpty()) {
				queue.remove().run();
			}
			synchronized (runtime) {
				runtime.update();
			}
			Rect rect = bia.getUpdatedRect();
			if (!rect.isEmpty()) {
				repaint(rect.left(), rect.top(), rect.right(), rect.bottom());
			}
			Thread.sleep(10);
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			System.out.println("initializing berkelium-java...");
			Berkelium.createInstance();
			System.out.println("running main loop...");
			new AwtExample().run();
			System.out.println("main loop terminated.");
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			System.out.println("destroying berkelium-java...");
			Berkelium.getInstance().destroy();
			System.out.println("berkelium-java destroyed.");
			System.exit(0);
		}
	}
}
