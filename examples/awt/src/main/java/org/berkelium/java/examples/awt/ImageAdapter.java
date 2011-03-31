package org.berkelium.java.examples.awt;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import org.apache.log4j.Logger;
import org.berkelium.java.Buffer;
import org.berkelium.java.Rect;
import org.berkelium.java.Window;
import org.berkelium.java.WindowAdapter;

public class ImageAdapter extends WindowAdapter {
	private final static Logger logger = Logger.getLogger(ImageAdapter.class);

	private BufferedImage img;
	private boolean needs_full_refresh = true;
	private final byte[] scroll_buffer = new byte[640*480*4];
	private boolean updated;

	public ImageAdapter(BufferedImage img) {
		this.img = img;
	}

	@Override
	public void onLoadingStateChanged(Window win, boolean isLoading) {
		logger.info("loading state: " + isLoading);
	}
	
	@Override
	public void onAddressBarChanged(Window win, String newURL) {
		logger.info("addr: " + newURL);
	}
	
	@Override
	public void onTitleChanged(Window win, String title) {
		logger.info("title: " + title);
	}
	
	@Override
	public void onPaint(Window wini, Buffer bitmap_in, Rect bitmap_rect, Rect[] copy_rects,
			int dx, int dy, Rect scroll_rect) {
		logger.info("onPaint");
		if (mapOnPaintToTexture( wini,  bitmap_in,  bitmap_rect, copy_rects,
				 dx,  dy, scroll_rect)) {
			updated = true;
		}
	}
	
	private boolean mapOnPaintToTexture(Window wini, Buffer bitmap_in, Rect bitmap_rect, Rect[] copy_rects,
			int dx, int dy, Rect scroll_rect) {
		
		WritableRaster wr = img.getRaster();
		int dest_texture_width = img.getWidth();
		int dest_texture_height = img.getHeight();
		// img.setData(sourceBufferRect.w, sourceBufferRect.h, data);
		// glBindTexture(GL_TEXTURE_2D, dest_texture);

		final int kBytesPerPixel = 4;
		// If we've reloaded the page and need a full update, ignore updates
		// until a full one comes in. This handles out of date updates due to
		// delays in event processing.
		if (needs_full_refresh) {
			if (bitmap_rect.left() != 0 || bitmap_rect.top() != 0
					|| bitmap_rect.right() != dest_texture_width
					|| bitmap_rect.bottom() != dest_texture_height) {
				return false;
			}

			wr.setDataElements(0,0, dest_texture_width, dest_texture_height, bitmap_in.getByteArray());
			/*gl.glTexImage2D(GL_TEXTURE_2D, 0, kBytesPerPixel, dest_texture_width,
				dest_texture_height, 0, GL_BGRA, GL_UNSIGNED_BYTE, bitmap_in
						.getIntArray());*/
			needs_full_refresh = false;
			logger.debug("mapOnPaintToTexture: full update");
			return true;
		}
		logger.debug("");
		logger.debug("mapOnPaintToTexture rect:" + bitmap_rect);

		// Now, we first handle scrolling. We need to do this first since it
		// requires shifting existing data, some of which will be overwritten
		// by the regular dirty rect update.
		if (dx != 0 || dy != 0) {
			// scroll_rect contains the Rect we need to move
			// First we figure out where the the data is moved to by translating it
			Rect scrolled_rect = scroll_rect.translate(-dx, -dy);
			// Next we figure out where they intersect, giving the scrolled region
			Rect scrolled_shared_rect = scroll_rect.intersect(scrolled_rect);
			// Only do scrolling if they have non-zero intersection
			if (scrolled_shared_rect.width() > 0 && scrolled_shared_rect.height() > 0) {
				// And the scroll is performed by moving shared_rect by (dx,dy)
				Rect shared_rect = scrolled_shared_rect.translate(dx, dy);

				int wid = scrolled_shared_rect.width();
				int hig = scrolled_shared_rect.height();
				if (logger.isDebugEnabled()) {
					logger
							.debug("Scroll rect: w=" + wid + ", h=" + hig + ", ("
									+ scrolled_shared_rect.left() + ","
									+ scrolled_shared_rect.top() + ") by (" + dx + ","
									+ dy + ")");
				}
				int inc = 1;
				int outputBuffer = 0;
				// source data is offset by 1 line to prevent memcpy aliasing
				// In this case, it can happen if dy==0 and dx!=0.
				int inputBuffer = dest_texture_width * 1 * kBytesPerPixel;
				int jj = 0;
				if (dy > 0) {
					// Here, we need to shift the buffer around so that we start in
					// the extra row at the end, and then copy in reverse so that we
					// don't clobber source data before copying it.
					outputBuffer = ((scrolled_shared_rect.top() + hig + 1)
							* dest_texture_width - hig * wid)
							* kBytesPerPixel;
					inputBuffer = 0;

					inc = -1;
					jj = hig - 1;
				}

				// FIXME: scrolling does not work!
				
				// Copy the data out of the texture
				//gl.glGetTexImage(GL_TEXTURE_2D, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				//	scroll_buffer, inputBuffer);

				// Annoyingly, OpenGL doesn't provide convenient primitives, so
				// we manually copy out the region to the beginning of the buffer
				for (; jj < hig && jj >= 0; jj += inc) {
					memcpy( //
						scroll_buffer, outputBuffer + (jj * wid) * kBytesPerPixel,
						// scroll_buffer + (jj*wid * kBytesPerPixel),
						scroll_buffer, //
						inputBuffer + ( //
								(scrolled_shared_rect.top() + jj) * dest_texture_width //
								+ scrolled_shared_rect.left()//
								) * kBytesPerPixel, //
						wid * kBytesPerPixel);
				}

				// And finally, we push it back into the texture in the right location
//				gl.glTexSubImage2D(GL_TEXTURE_2D, 0, shared_rect.left(), shared_rect
//						.top(), shared_rect.width(), shared_rect.height(), GL_BGRA,
//					GL_UNSIGNED_BYTE, scroll_buffer, outputBuffer);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug(wini + " Bitmap rect: w=" + bitmap_rect.width() + ", h="
					+ bitmap_rect.height() + ", (" + bitmap_rect.top() + ","
					+ bitmap_rect.left() + ") tex size " + dest_texture_width + "x"
					+ dest_texture_height);
		}
		if (copy_rects.length > 0) {
			logger.info("copy_rects: " + copy_rects.length);
		}
		for (int i = 0; i < copy_rects.length; i++) {
			int wid = copy_rects[i].width();
			int hig = copy_rects[i].height();
			int top = copy_rects[i].top() - bitmap_rect.top();
			int left = copy_rects[i].left() - bitmap_rect.left();
			if (logger.isDebugEnabled()) {
				logger.debug(wini + " Copy rect: w=" + wid + ", h=" + hig + ", (" + top
						+ "," + left + ")");
			}
			for (int jj = 0; jj < hig; jj++) {
				memcpy(//
					scroll_buffer, //
					jj * wid * kBytesPerPixel, //
					bitmap_in.getByteArray(), //
					(left + (jj + top) * bitmap_rect.width()) * kBytesPerPixel, //
					wid * kBytesPerPixel//
				);
			}

			// Finally, we perform the main update, just copying the rect that is
			// marked as dirty but not from scrolled data.
			wr.setDataElements(copy_rects[i].left(), copy_rects[i].top(), wid, hig, scroll_buffer);
//			gl.glTexSubImage2D(GL_TEXTURE_2D, 0, copy_rects[i].left(), copy_rects[i]
//					.top(), wid, hig, GL_BGRA, GL_UNSIGNED_BYTE, scroll_buffer, 0);
		}

//		gl.glBindTexture(GL_TEXTURE_2D, 0);
		return true;
	}

	private void memcpy(byte dest[], int destPos, byte src[], int srcPos, int length) {
		if (srcPos >= src.length) {
			logger.error("out of bounds memcpy: src " + srcPos + " >= " + src.length);
			return;
		}
		if (destPos >= dest.length) {
			logger.error("out of bounds memcpy: dest " + destPos + " >= " + dest.length);
			return;
		}
		System.arraycopy(src, srcPos, dest, destPos, length);
	}

	public boolean isUpdated() {
		boolean ret = updated;
		updated = false;
		return ret;
	}
}
