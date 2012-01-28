package org.berkelium.java.examples.browser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;

import org.berkelium.java.api.Window;
import org.berkelium.java.awt.AwtInputAdapter;

public class TabAdapter extends JComponent {
	private static final long serialVersionUID = -6034381086824065656L;
	private Tab tab;
	private AwtInputAdapter input = new AwtInputAdapter() {
		protected Window getWindow() {
			if(tab == null) {
				return null;
			}
			return tab.getWindow();
		}
		protected void execute(Runnable job) {
			if(tab != null) {
				tab.execute(job);
			}
		}

		public void requestFocus() {
			TabAdapter.this.requestFocus();
		}
	};

	{
		setVisible(true);
		setFocusable(true);
		setPreferredSize(new Dimension(640, 480));
		setBackground(Color.blue);
		input.add(this);

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				if (tab == null)
					return;
				tab.execute(new Runnable() {
					public void run() {
						checkSize();
					}
				});
			}
		});
	}

	public void setSize(int width, int height) {
		if (tab != null) {
			tab.resize(width, height);
		}
		super.setSize(width, height);
	}

	public void paint(Graphics g) {
		if (tab != null)
			tab.paint(g, getWidth(), getHeight());
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
