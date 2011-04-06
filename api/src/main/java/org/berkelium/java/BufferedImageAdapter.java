package org.berkelium.java;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class BufferedImageAdapter extends WindowAdapter {
	private BufferedImage img;
	private int[] scrollBuffer;
	private boolean needs_full_refresh = true;
	private boolean updated = false;

	public BufferedImageAdapter() {
	}

	protected int[] getScrollBuffer() {
		if (scrollBuffer == null) {
			scrollBuffer = new int[(img.getWidth() + 1) * (img.getHeight() + 1)];
		}
		return scrollBuffer;
	}

	public void resize(int width, int height) {
		setImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
	}

	public void setImage(BufferedImage img) {
		this.img = img;
		scrollBuffer = null;
	}

	public BufferedImage getImage() {
		return img;
	}

	@Override
	public synchronized void onPaint(Window wini, Buffer bitmap_in, Rect bitmap_rect,
			Rect[] copy_rects, int dx, int dy, Rect scroll_rect) {
		// we have no image yet
		if (img == null)
			return;

		if (needs_full_refresh) {
			// If we've reloaded the page and need a full update, ignore updates
			// until a full one comes in. This handles out of date updates due to
			// delays in event processing.
			if (handleFullUpdate(bitmap_in, bitmap_rect)) {
				updated = true;
				needs_full_refresh = false;
			}
		} else {
			// Now, we first handle scrolling. We need to do this first since it
			// requires shifting existing data, some of which will be overwritten
			// by the regular dirty rect update.
			if (dx != 0 || dy != 0) {
				handleScroll(dx, dy, scroll_rect);
			}
			handleCopyRects(bitmap_in, bitmap_rect, copy_rects);
			updated = true;
		}
	}

	/**
	 * 
	 * @param buf
	 * @param rect
	 * @return
	 */
	protected boolean handleFullUpdate(Buffer buf, Rect rect) {
		final int w = img.getWidth();
		final int h = img.getHeight();

		if (rect.left() == 0 || rect.top() == 0 || rect.right() == w
				|| rect.bottom() == h) {
			final WritableRaster wr = img.getRaster();
			wr.setDataElements(0, 0, w, h, buf.getIntArray());
			return true;
		}
		return false;
	}

	private void handleScroll(int dx, int dy, Rect scroll_rect) {
		WritableRaster wr = img.getRaster();
		int dest_texture_width = img.getWidth();
		int dest_texture_height = img.getHeight();

		// scroll_rect contains the Rect we need to move
		// First we figure out where the data is moved to by translating it
		Rect scrolled_rect = scroll_rect.translate(-dx, -dy);
		// Next we figure out where they intersect, giving the scrolled region
		Rect scrolled_shared_rect = scroll_rect.intersect(scrolled_rect);
		// Only do scrolling if they have non-zero intersection
		if (scrolled_shared_rect.width() <= 0 || scrolled_shared_rect.height() <= 0) {
			return;
		}

		// And the scroll is performed by moving shared_rect by (dx,dy)
		Rect shared_rect = scrolled_shared_rect.translate(dx, dy);

		int wid = scrolled_shared_rect.width();
		int hig = scrolled_shared_rect.height();
		int inc = 1;
		int outputBuffer = 0;
		// source data is offset by 1 line to prevent memcpy aliasing
		// In this case, it can happen if dy==0 and dx!=0.
		int inputBuffer = dest_texture_width;
		int jj = 0;
		if (dy > 0) {
			// Here, we need to shift the buffer around so that we start in
			// the extra row at the end, and then copy in reverse so that we
			// don't clobber source data before copying it.
			outputBuffer = ((scrolled_shared_rect.top() + hig + 1) * dest_texture_width - hig
					* wid);
			inputBuffer = 0;

			inc = -1;
			jj = hig - 1;
		}

		// FIXME: scrolling does not work!
		// wr.getDataElements(0, 0, wid, hig, scroll_buffer);

		// Copy the data out of the texture
		// gl.glGetTexImage(GL_TEXTURE_2D, 0, GL_BGRA, GL_UNSIGNED_BYTE,
		// scroll_buffer, inputBuffer);

		// Annoyingly, OpenGL doesn't provide convenient primitives, so
		// we manually copy out the region to the beginning of the buffer
		/*
		 * for (; jj < hig && jj >= 0; jj += inc) { memcpy( // scroll_buffer, outputBuffer + (jj * wid) *
		 * kBytesPerPixel, // scroll_buffer + (jj*wid * kBytesPerPixel), scroll_buffer, // inputBuffer + ( //
		 * (scrolled_shared_rect.top() + jj) * dest_texture_width // + scrolled_shared_rect.left()// ) * kBytesPerPixel,
		 * // wid * kBytesPerPixel); }
		 */

		// And finally, we push it back into the texture in the right location
		// gl.glTexSubImage2D(GL_TEXTURE_2D, 0, shared_rect.left(), shared_rect
		// .top(), shared_rect.width(), shared_rect.height(), GL_BGRA,
		// GL_UNSIGNED_BYTE, scroll_buffer, outputBuffer);
	}

	private void handleCopyRects(Buffer bitmap_in, Rect bitmap_rect, Rect[] copy_rects) {
		WritableRaster wr = img.getRaster();
		int data[] = bitmap_in.getIntArray();

		for (int i = 0; i < copy_rects.length; i++) {
			int wid = copy_rects[i].width();
			int hig = copy_rects[i].height();
			int top = copy_rects[i].top() - bitmap_rect.top();
			int left = copy_rects[i].left() - bitmap_rect.left();
			int[] scroll_buffer = getScrollBuffer();
			for (int jj = 0; jj < hig; jj++) {
				memcpy(//
					scroll_buffer, //
					jj * wid, //
					data, //
					left + (jj + top) * bitmap_rect.width(), //
					wid//
				);
			}

			// Finally, we perform the main update, just copying the rect that is
			// marked as dirty but not from scrolled data.
			wr.setDataElements(copy_rects[i].left(), copy_rects[i].top(), wid, hig,
				scroll_buffer);
		}
	}

	private void memcpy(int dest[], int destPos, int src[], int srcPos, int length) {
		if (srcPos >= src.length) {
			return;
		}
		if (destPos >= dest.length) {
			return;
		}
		System.arraycopy(src, srcPos, dest, destPos, length);
	}

	public boolean wasUpdated() {
		boolean ret = updated;
		updated = false;
		return ret;
	}
}
