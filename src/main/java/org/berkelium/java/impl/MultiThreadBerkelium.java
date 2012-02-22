package org.berkelium.java.impl;

import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.berkelium.java.api.Berkelium;
import org.berkelium.java.api.Window;

public class MultiThreadBerkelium extends Berkelium {
	private final CyclicBarrier initDoneBarrier = new CyclicBarrier(2);
	private final Thread thread;
	private final Queue<Runnable> queue = new ConcurrentLinkedQueue<Runnable>();
	private RuntimeException initRuntimeException;
	private Berkelium berkelium;
	private final AtomicBoolean isShutdown = new AtomicBoolean(false);

	private final Thread shutdown = new Thread("Berkelium Shutdown Thread") {
		private boolean done = false;

		public synchronized void run() {
			if(done) return;
			done = true;
			try {
				Thread.sleep(1000);
				isShutdown.set(true);
				while (thread.isAlive()) {
					// TODO(drieks)
					// InterruptedException in not correctly handled...
					// see isShutdown quick fix
					thread.interrupt();
					thread.join(100);
				}
			} catch (InterruptedException e) {
			}
		};
	};

	private final Runnable threadRunnable = new Runnable() {
		public void run() {
			try {
				initThread();
			} catch (ExceptionInInitializerError eie) {
				initRuntimeException = new RuntimeException(eie);
				return;
			} catch (RuntimeException re) {
				initRuntimeException = re;
				return;
			} finally {
				try {
					initDoneBarrier.await();
				} catch (InterruptedException e) {
					// TODO
				} catch (Throwable t) {
					Berkelium.handleThrowable(t);
					throw new RuntimeException(t);
				}
			}
			try {
				while (!thread.isInterrupted()) {
					if (!updateThread()) {
						break;
					}
				}
			} catch (Throwable t) {
				Berkelium.handleThrowable(t);
				throw new RuntimeException(t);
			} finally {
				shutdownThread();
			}
		}
	};

	private final Runnable updater = new Runnable() {
		public void run() {
			try {
				berkelium.update();
			} catch (RuntimeException re) {
				handleThrowable(re);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO
			}
		}
	};

	public MultiThreadBerkelium() {
		thread = new Thread(threadRunnable, "Berkelium Thread");
		thread.start();
		try {
			initDoneBarrier.await();
		} catch (InterruptedException e) {
			// TODO
		} catch (Exception e) {
			Berkelium.handleThrowable(e);
		}
		if (initRuntimeException != null)
			throw initRuntimeException;
	}

	private void initThread() {
		berkelium = new SingleThreadBerkelium();
		Runtime.getRuntime().addShutdownHook(shutdown);
	}

	private boolean updateThread() {
		execute(updater);

		while (!queue.isEmpty()) {
			try {
				queue.remove().run();
			} catch (Throwable ignore) {
				Berkelium.handleThrowable(ignore);
			}
		}

		if (isShutdown.get() && queue.isEmpty())
			return false;

		return true;
	}

	private void shutdownThread() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO
		}
		berkelium.destroy();
	}

	public void execute(Runnable command) {
		assertIsRunning();
		queue.add(command);
	}

	public void executeAndWait(final Runnable job) {
		if (Thread.currentThread().equals(thread)) {
			job.run();
			return;
		}

		assertIsRunning();

		final CyclicBarrier barrier = new CyclicBarrier(2);
		final AtomicReference<RuntimeException> ex = new AtomicReference<RuntimeException>();

		execute(new Runnable() {
			public void run() {
				try {
					try {
						job.run();
					} catch (RuntimeException t) {
						ex.set(t);
					}
				} finally {
					try {
						barrier.await();
					} catch (InterruptedException e) {
						Berkelium.handleThrowable(e);
					} catch (BrokenBarrierException e) {
						Berkelium.handleThrowable(e);
					}
				}
			}
		});

		try {
			barrier.await();
		} catch (InterruptedException e) {
			Berkelium.handleThrowable(e);
		} catch (BrokenBarrierException e) {
			Berkelium.handleThrowable(e);
		}
		RuntimeException re = ex.get();
		if (re != null)
			throw re;
	}

	public Window createWindow() {
		final AtomicReference<Window> ret = new AtomicReference<Window>();
		executeAndWait(new Runnable() {
			public void run() {
				ret.set(new WindowImpl(MultiThreadBerkelium.this));
			}
		});
		return ret.get().getThreadProxyWindow();
	}

	public void assertIsRunning() {
		berkelium.assertIsRunning();
	}

	public void assertNotBerkeliumThread() {
		berkelium.assertNotBerkeliumThread();
	}

	public void assertIsBerkeliumThread() {
		berkelium.assertIsBerkeliumThread();
	}

	public void sync(Window win) {
		berkelium.sync(win);
	}

	public void update() {
		// not implemented
		// update is called in berkelium thread
	}

	public void destroy() {
		shutdown.run();
	}
}