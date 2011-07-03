package org.berkelium.java.examples.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.berkelium.java.Rect;
import org.berkelium.java.Window;
import org.berkelium.java.WindowAdapter;
import org.berkelium.java.WindowDelegate;

public class SimpleBrowser extends JFrame {
	private static final long serialVersionUID = 8835790859223385092L;
	private final Toolbar toolbar = new Toolbar();
	private final TabAdapter adapter = new TabAdapter();
	private final WindowDelegate delegate = new WindowAdapter() {
		@Override
		public void onTitleChanged(Window win, String title) {
			setTitle(title);
		}
	};

	public SimpleBrowser() {
		setTitle("AwtExample");
		setSize(new Dimension(640, 480));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		add(toolbar, BorderLayout.PAGE_START);
		add(adapter, BorderLayout.CENTER);
		setBackground(Color.green);
		setVisible(true);
	}

	public void setTab(Tab tab) {
		Tab old = adapter.getTab();
		if (old != null) {
			old.removeDelegate(delegate);
		}
		toolbar.setTab(tab);
		adapter.setTab(tab);
		tab.addDelegate(delegate);
	}

	public void update() {
		adapter.update();
	}

	public void checkRepaint() {
		Tab tab = adapter.getTab();
		if(tab == null) return;
		Rect rect = tab.getUpdatedRect();
		if (!rect.isEmpty()) {
			repaint(rect.left(), rect.top(), rect.right(), rect.bottom());
		}
	}
}
