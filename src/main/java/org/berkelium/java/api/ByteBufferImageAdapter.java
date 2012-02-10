package org.berkelium.java.api;

import java.nio.ByteBuffer;

public class ByteBufferImageAdapter extends WindowAdapter {
	protected ByteBuffer data;
	protected int width;
	protected int height;
	private boolean needs_full_refresh = true;
	private final boolean direct;

	public ByteBufferImageAdapter(boolean direct) {
		this.direct = direct;
	}

	public void onResize(int width, int height) {
		resize(width, height);
	}

	public void resize(int width, int height) {
		if (this.width == width && this.height == height) {
			return;
		}
		this.width = width;
		this.height = height;
		if (direct)
			data = ByteBuffer.allocateDirect(width * height * 4);
		else
			data = ByteBuffer.allocate(width * height * 4);
		needs_full_refresh = true;
	}

	public void dataUpdated(Rect rect) {
	}

	public synchronized void onPaint(Window wini, Buffer bitmap_in,
			Rect bitmap_rect, Rect[] copy_rects, int dx, int dy,
			Rect scroll_rect) {
		// we have no image yet
		if (wini == null)
			return;

		WindowDelegate d = wini.getDelegate();
		if (d == null)
			return;

		try {
			if (needs_full_refresh) {
				// If we've reloaded the page and need a full update, ignore
				// updates
				// until a full one comes in. This handles out of date updates
				// due to
				// delays in event processing.
				if (!handleFullUpdate(bitmap_in, bitmap_rect)) {
					return;
				}
			} else {
				// Now, we first handle scrolling. We need to do this first
				// since it
				// requires shifting existing data, some of which will be
				// overwritten
				// by the regular dirty rect update.
				if (dx != 0 || dy != 0) {
					handleScroll(dx, dy, scroll_rect);
				}
				handleCopyRects(bitmap_in, bitmap_rect, copy_rects);
			}
			dataUpdated(bitmap_rect);
			d.onPaintDone(wini, bitmap_rect);
		} catch (ArrayIndexOutOfBoundsException ex) {
			needs_full_refresh = true;
		}
	}

	/**
	 * 
	 * @param buf
	 * @param rect
	 * @return
	 */
	protected boolean handleFullUpdate(Buffer buf, Rect rect) {
		if (rect.x == 0 && rect.y == 0 && rect.w == width && rect.h == height) {
			data.rewind();
			data.put(buf.getByteArray());
			needs_full_refresh = false;
			return true;
		}
		return false;
	}

	private void handleScroll(int dx, int dy, Rect scroll_rect) {
		// scroll_rect contains the Rect we need to move
		// First we figure out where the data is moved to by translating it
		Rect scrolled_rect = scroll_rect.translate(-dx, -dy);
		// Next we figure out where they intersect, giving the scrolled region
		Rect scrolled_shared_rect = scroll_rect.intersect(scrolled_rect);
		// Only do scrolling if they have non-zero intersection
		if (scrolled_shared_rect.width() > 0
				&& scrolled_shared_rect.height() > 0) {
			Rect shared_rect = scrolled_shared_rect.translate(dx, dy);

			int fx = scrolled_shared_rect.x;
			int fy = scrolled_shared_rect.y;
			int tx = shared_rect.x;
			int ty = shared_rect.y;
			int h = shared_rect.h;
			byte buf[] = new byte[shared_rect.w * 4];

			if (ty < fy) {
				for (int y = 0; y < h; ++y) {
					copyLine(fx, y + fy, tx, y + ty, buf);
				}
			} else {
				for (int y = h - 1; y >= 0; --y) {
					copyLine(fx, y + fy, tx, y + ty, buf);
				}
			}
		}
	}

	private void copyLine(int fx, int fy, int tx, int ty, byte[] buf) {
		setPos(fx, fy);
		data.get(buf);
		setPos(tx, ty);
		data.put(buf);
	}

	private void setPos(int x, int y) {
		data.position((x + (y * width)) * 4);
	}

	private void handleCopyRects(Buffer buffer, Rect r, Rect[] copies) {
		if (copies.length == 1 && copies[0].equals(r)) {
			if (handleFullUpdate(buffer, r)) {
				return;
			}
		}

		byte[] array = buffer.getByteArray();
		for (Rect c : copies) {
			for (int y = 0; y < c.h; y++) {
				setPos(c.x, y + c.y);
				data.put(array, 4 * ((c.x - r.x) + (y + c.y - r.y) * r.w), c.w * 4);
			}
		}
	}
}
