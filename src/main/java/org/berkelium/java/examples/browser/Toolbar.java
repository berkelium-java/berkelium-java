package org.berkelium.java.examples.browser;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.berkelium.java.api.Window;
import org.berkelium.java.api.WindowAdapter;

public class Toolbar extends JPanel implements ActionListener {
	private static final long serialVersionUID = -3682975961726249069L;
	private final JButton prev = new JButton("<");
	private final JButton next = new JButton(">");
	private final JButton reload = new JButton("R");
	private final JButton stop = new JButton("X");
	private final JTextField url = new JTextField("");
	// private final JButton go = new JButton("go");
	private Tab tab;
	private final WindowAdapter adapter = new WindowAdapter() {
		@Override
		public void onAddressBarChanged(Window win, String newURL) {
			url.setText(newURL);
			updateNavButtons();
		}

		@Override
		public void onLoadingStateChanged(Window win, boolean isLoading) {
			reload.setVisible(!isLoading);
			stop.setVisible(isLoading);
			updateNavButtons();
		}

		@Override
		public boolean onNavigationRequested(Window win, String newUrl, String referrer,
				boolean isNewWindow, boolean[] cancelDefaultAction) {
			updateNavButtons();
			return true;
		}

		@Override
		public void onCreatedWindow(Window win, Window newWindow,
				org.berkelium.java.api.Rect initialRect) {
			// TODO: the new tab is empty, is the url correct?
			simpleBrowser.setTab(new Tab(newWindow));
		}

	};
	private final SimpleBrowser simpleBrowser;

	public Toolbar(SimpleBrowser simpleBrowser) {
		this.simpleBrowser = simpleBrowser;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 1, 1);
		c.gridx = 0;
		c.gridy = 0;
		add(prev, c);
		c.gridx = 1;
		add(next, c);
		c.gridx = 2;
		add(reload, c);
		add(stop, c);
		c.gridx = 3;
		c.weightx = 1;
		add(url, c);
		// url.setMinimumSize(new Dimension(200, 20));
		url.addActionListener(this);
		prev.addActionListener(this);
		next.addActionListener(this);
		reload.addActionListener(this);
	}

	public void setTab(Tab tab) {
		if (this.tab != null) {
			this.tab.removeDelegate(adapter);
		}
		this.tab = tab;
		url.setText(tab.getUrl());
		tab.addDelegate(adapter);
	}

	private void updateNavButtons() {
		if (tab == null)
			return;
		Window win = tab.getWindow();
		if (win == null)
			return;
		prev.setEnabled(win.canGoBack());
		next.setEnabled(win.canGoForward());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (tab == null)
			return;
		Window win = tab.getWindow();
		if (win == null)
			return;
		if (e.getSource() == url) {
			tab.setUrl(url.getText());
		} else if (e.getSource() == next) {
			win.goForward();
		} else if (e.getSource() == prev) {
			win.goBack();
		} else if (e.getSource() == reload) {
			win.refresh();
		}
	}
}
