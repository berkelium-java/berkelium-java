package org.berkelium.java.examples.awt;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.berkelium.java.Berkelium;
import org.berkelium.java.Window;

public class AwtExample extends JFrame {
	private static final long serialVersionUID = 8835790859223385092L;
	private int width = 640;
	private int height = 480;
	private BufferedImage img = new BufferedImage (width , height , BufferedImage.TYPE_4BYTE_ABGR);
	private ImageAdapter delegate = new ImageAdapter(img);
	private Berkelium runtime = Berkelium.getInstance();
	private Window win = runtime.createWindow();
	
	public AwtExample() {
		setTitle("AwtExample");
		setMinimumSize(new Dimension(320, 240));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		addMouseListener(new MouseAdapter() {
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
	
	public void run() throws Exception{
		synchronized (runtime) {
			win.resize(width, height);
			//win.navigateTo("http://google.de");
			win.navigateTo("http://www.youtube.com/");
			win.setDelegate(delegate);
		}
	
		while (isVisible()) {
			while(!queue.isEmpty()) {
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
