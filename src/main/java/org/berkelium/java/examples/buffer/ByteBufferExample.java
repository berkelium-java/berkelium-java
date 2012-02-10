package org.berkelium.java.examples.buffer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.ByteBufferImageAdapter;
import org.berkelium.java.api.Rect;
import org.berkelium.java.api.Window;
import org.berkelium.java.awt.AwtInputAdapter;
import org.berkelium.java.awt.AwtResizeAdapter;

public class ByteBufferExample extends JPanel {
	private static final long serialVersionUID = 8835790859223385092L;
	private final Berkelium runtime = Berkelium.getInstance();
	private final Window win = runtime.createWindow();
	{
		new AwtResizeAdapter(this, win);
	}

	private class TestByteBufferImageAdapter extends ByteBufferImageAdapter {
		private BufferedImage img;

		public TestByteBufferImageAdapter() {
			super(false);
		}
		
		public BufferedImage getImage() {
			return img;
		}

		public void dataUpdated(Rect rect) {
			img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			img.getRaster().setDataElements(0, 0, width, height, data.array());
		}

		public void onPaintDone(Window win, final Rect rect) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					repaint(rect.left(), rect.top(), rect.right(),
							rect.bottom());
				}
			});
		};
	};
	private final TestByteBufferImageAdapter bia = new TestByteBufferImageAdapter();


	private final int initialWidth = 640;
	private final int initialHeight = 480;

	public ByteBufferExample() {
		JFrame frame = new JFrame();
		frame.setTitle("ByteBufferExample");
		frame.setSize(new Dimension(initialWidth, initialHeight));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(this);
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
			win.addDelegate(bia);
			new AwtInputAdapter(win).add(this);
			win.resize(initialWidth, initialHeight);

			win.setHtml(
				"<html><head><script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js'>/**/</script><script type='text/javascript'>" +
				"$(function() {" +
				"var x = true;" +
				"function test() {" +
				"x = !x;" +
				"$('<span style=\"background:' + (x ? 'red' : 'green') + ';position:fixed;top:' + (Math.random() * window.innerHeight) + 'px;left:' + (Math.random() * window.innerWidth) + 'px;width:20px; height:20px\"></span>').appendTo('body');" +
				"window.setTimeout(test, 100);" +
				"}" +
				"test();" +
				"});" +
				"</script></head><body></body></html>");
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			System.out.println("initializing berkelium-java...");
			Berkelium.createMultiThreadInstance();
			new ByteBufferExample().run();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
