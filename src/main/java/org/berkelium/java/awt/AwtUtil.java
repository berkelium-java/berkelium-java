package org.berkelium.java.awt;

import javax.swing.SwingUtilities;

import org.berkelium.java.api.Berkelium;

public class AwtUtil {

	public static void destoryLater() { 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Berkelium.getInstance().destroy();
			}
		});
	}
}
