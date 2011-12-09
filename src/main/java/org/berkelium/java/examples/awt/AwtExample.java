package org.berkelium.java.examples.awt;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.Rect;
import org.berkelium.java.api.Window;
import org.berkelium.java.awt.BufferedImageAdapter;

public class AwtExample extends JFrame {
	private static final long serialVersionUID = 8835790859223385092L;
	private final Berkelium runtime = Berkelium.getInstance();
	private final Window win = runtime.createWindow();
	private final BufferedImageAdapter bia = new BufferedImageAdapter() {
		public void onPaintDone(Window win, final Rect rect) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					repaint(rect.left(), rect.top(), rect.right(),
							rect.bottom());
				}
			});
		};
	};
	private final int initialWidth = 640;
	private final int initialHeight = 480;

	public AwtExample() {
		setTitle("AwtExample");
		setSize(new Dimension(initialWidth, initialHeight));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				handleMouseButtonEvent(e, false);
			}

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
		runtime.execute(new Runnable() {
			public void run() {
				win.mouseMoved(x, y);
				win.mouseButton(b, down);
			}
		});
	}

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
	}

	public static void main(String[] args) throws Exception {
		try {
			System.out.println("initializing berkelium-java...");
			Berkelium.createMultiThreadInstance();
			new AwtExample().run();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
