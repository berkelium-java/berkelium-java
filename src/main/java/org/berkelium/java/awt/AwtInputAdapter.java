package org.berkelium.java.awt;

import java.awt.Component;
import java.awt.Event;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.berkelium.java.api.Window;

public class AwtInputAdapter extends MouseAdapter implements MouseMotionListener, MouseWheelListener, KeyListener, FocusListener {
	private final Window window;

	public AwtInputAdapter() {
		this(null);		
	}

	public AwtInputAdapter(Window window) {
		this.window = window;
	}

	protected Window getWindow() {
		return window;
	}

	protected void execute(Runnable job) {
		job.run();
	}

	public void add(Component comp) {
		comp.addMouseListener(this);
		comp.addMouseMotionListener(this);
		comp.addMouseWheelListener(this);
		comp.addKeyListener(this);
		comp.addFocusListener(this);
	}

	public void mouseReleased(MouseEvent e) {
		mouseButton(e, false);
	}

	public void mousePressed(MouseEvent e) {
		mouseButton(e, true);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if ((e.getModifiers() & Event.CTRL_MASK) != 0) {
			zoom(e.getWheelRotation() < 0 ? 1 : -1);
			return;
		}
		boolean dir = (e.getModifiers() & Event.SHIFT_MASK) != 0;
		final int x = dir ? e.getWheelRotation() * -33 : 0;
		final int y = dir ? 0 : e.getWheelRotation() * -100;
		mouseWheel(x, y);
	}

	public void mouseButton(final MouseEvent e, final boolean down) {
		if(down) {
			requestFocus();
		}
		execute(new Runnable() {
			public void run() {
				Window win = getWindow();
				if(win != null) {
					// java/awt: left=1 middel=2 right=3
					// berkelium: left=0 middel=1 right=2
					//TODO check
					win.mouseButton(e.getButton() - 1, down);
				}
			}
		});
	}

	public void mouseWheel(final int x, final int y) {
		execute(new Runnable() {
			public void run() {
				Window win = getWindow();
				if(win != null) {
					win.mouseWheel(x, y);
				}
			}
		});
	}
	
	public void mouseMoved(final MouseEvent e) {
		execute(new Runnable() {
			public void run() {
				Window win = getWindow();
				if(win != null) {
					win.mouseMoved(e.getX(), e.getY());
				}
			}
		});
	}
	
	public void mouseDragged(final MouseEvent e) {
		execute(new Runnable() {
			public void run() {
				Window win = getWindow();
				if(win != null) {
					win.mouseMoved(e.getX(), e.getY());
				}
			}
		});
	}

	public void zoom(final int mode) {
		execute(new Runnable() {
			public void run() {
				Window win = getWindow();
				if(win != null) {
					win.adjustZoom(mode);
				}
			}
		});
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		keyEvent(true, e);
	}

	public void keyReleased(KeyEvent e) {
		keyEvent(false, e);
	}

	private void keyEvent(final boolean pressed, final KeyEvent e) {
		execute(new Runnable() {
			public void run() {
				Window win = getWindow();
				if(win != null) {
					win.keyEvent(pressed, AwtBerkelium.mapKeyModifier(e), AwtBerkelium.mapVkCode(e), AwtBerkelium.mapScancode(e));
				}
			}
		});
	}

	public void requestFocus() {
	}

	public void focusGained(FocusEvent e) {
		execute(new Runnable() {
			public void run() {
				Window win = getWindow();
				if(win != null) {
					win.focus();
				}
			}
		});
	}

	public void focusLost(FocusEvent e) {
		execute(new Runnable() {
			public void run() {
				Window win = getWindow();
				if(win != null) {
					win.unfocus();
				}
			}
		});
	}
};

