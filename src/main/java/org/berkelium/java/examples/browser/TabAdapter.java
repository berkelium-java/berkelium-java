package org.berkelium.java.examples.browser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JComponent;

public class TabAdapter extends JComponent {
	private static final long serialVersionUID = -6034381086824065656L;
	private final Queue<Runnable> queue = new ConcurrentLinkedQueue<Runnable>();
	private Tab tab;
	private final MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mouseReleased(MouseEvent e) {
			handleMouseButtonEvent(e, false);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			handleMouseButtonEvent(e, true);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if ((e.getModifiers() & Event.CTRL_MASK) != 0) {
				zoom(e.getWheelRotation() < 0 ? 1 : -1);
				return;
			}
			boolean dir = (e.getModifiers() & Event.SHIFT_MASK) != 0;
			final int x = dir ? e.getWheelRotation() * -33 : 0;
			final int y = dir ? 0 : e.getWheelRotation() * -100;

			queue.add(new Runnable() {
				@Override
				public void run() {
					if (tab != null) {
						tab.getWindow().mouseWheel(x, y);
					}
				}
			});
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			final int x = e.getX();
			final int y = e.getY();

			queue.add(new Runnable() {
				@Override
				public void run() {
					if (tab != null) {
						tab.getWindow().mouseMoved(x, y);
					}
				}
			});
		}
	};

	{
		setVisible(true);
		setFocusable(true);
		setPreferredSize(new Dimension(640, 480));
		setBackground(Color.blue);
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		addMouseWheelListener(mouseAdapter);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				queue.add(new Runnable() {
					@Override
					public void run() {
						checkSize();
					}
				});
			}
		});
	}

	@Override
	public void setSize(int width, int height) {
		tab.resize(width, height);
		super.setSize(width, height);
	}

	private void zoom(final int mode) {
		queue.add(new Runnable() {
			@Override
			public void run() {
				if (tab != null) {
					tab.getWindow().adjustZoom(mode);
				}
			}
		});
	}

	private void handleMouseButtonEvent(MouseEvent e, final boolean down) {
		/*
		final int x = e.getX();
		final int y = e.getY();
		*/
		// java/awt: left=1 middel=2 right=3
		// berkelium: left=0 middel=1 right=2
		final int b = e.getButton() - 1;

		// the event must be handled in the berkelium thread
		queue.add(new Runnable() {
			@Override
			public void run() {
				if (tab != null) {
					tab.getWindow().mouseButton(b, down);
				}
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		if (tab != null)
			tab.paint(g, getWidth(), getHeight());
	}

	public void update() {
		while (!queue.isEmpty()) {
			queue.remove().run();
		}
	}

	public void setTab(Tab tab) {
		this.tab = tab;
		checkSize();
	}

	private void checkSize() {
		if (tab != null)
			tab.resize(getWidth(), getHeight());
	}

	public Tab getTab() {
		return tab;
	}
}
