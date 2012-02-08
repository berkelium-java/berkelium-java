package org.berkelium.java.awt;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import org.berkelium.java.api.Window;

public class AwtResizeAdapter {
	public AwtResizeAdapter(final Component comp, final Window win) {
		comp.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				win.resize(comp.getWidth(), comp.getHeight());
			}
		});
	}
}
