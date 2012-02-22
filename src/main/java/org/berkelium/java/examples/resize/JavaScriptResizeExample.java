package org.berkelium.java.examples.resize;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.DebugDelegate;
import org.berkelium.java.api.JavaScriptResizeAdapter;
import org.berkelium.java.api.Rect;
import org.berkelium.java.api.Window;
import org.berkelium.java.awt.AwtInputAdapter;
import org.berkelium.java.awt.AwtResizeAdapter;
import org.berkelium.java.awt.AwtUtil;
import org.berkelium.java.awt.BufferedImageAdapter;

public class JavaScriptResizeExample extends JPanel {
	private static final long serialVersionUID = 8835790859223385092L;
	private final Berkelium runtime = Berkelium.getInstance();
	private final Window win = runtime.createWindow();
	private final JFrame frame;
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

	public JavaScriptResizeExample() {
		frame = new JFrame("JavaScriptResizeExample");
		frame.setSize(new Dimension(initialWidth, initialHeight));
		frame.add(this);
		frame.setVisible(true);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
				AwtUtil.destoryLater();
			}
		});

		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				handleMouseButtonEvent(e, false);
			}

			public void mousePressed(MouseEvent e) {
				handleMouseButtonEvent(e, true);
			}
		});
	}

	private void handleMouseButtonEvent(MouseEvent e, boolean down) {
		final BufferedImage bi = bia.getImage();
		if (bia == null)
			return;
		final int x = e.getX() * bi.getWidth() / getWidth();
		final int y = e.getY() * bi.getHeight() / getHeight();

		win.mouseMoved(x, y);
		win.mouseButton(e.getButton(), down);
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
		win.addDelegate(bia);
		win.addDelegate(new JavaScriptResizeAdapter());
		new AwtResizeAdapter(frame, win);
		new AwtInputAdapter(win).add(frame);
		win.addDelegate(new DebugDelegate().getProxy());
		win.resize(initialWidth, initialHeight);
		// TODO add JavaScript resize demo
		win.navigateTo("http://www.youtube.com/");
	}

	public static void main(String[] args) throws Exception {
		try {
			System.out.println("initializing berkelium-java...");
			Berkelium.createMultiThreadInstance();
			new JavaScriptResizeExample().run();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
