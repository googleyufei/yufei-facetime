package com.facetime.core.lock;


import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.facetime.core.utils.Invokable;

/**
 * 一个轻量的读写锁绝缘体<P>
 * 用于在一个需要read/write锁的运行上下文中而不需要关心锁。<p>
 * 可以处理需要返回值和不需要返回值两种情形.
 * <li>{@link Runnable} 处理非返回状态
 * <li>{@link Invokable} 处理返回状态
 * 
 * @author dzb2k9
 */
public class RWLockBarrier {
    
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * This is, of course, a bit of a problem. We don't have an avenue for ensuring that this ThreadLocal is destroyed
     * at the end of the request, and that means a thread can hold a reference to the class and the class loader which
     * loaded it. This may cause redeployment problems (leaked classes and class loaders). Apparently JDK 1.6 provides
     * the APIs to validate to see if the current thread has a read lock. So, we tend to remove the TL, rather than set its
     * symbol to false.
     */
    private static class ThreadBoolean extends ThreadLocal<Boolean> {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    }

    private final ThreadBoolean threadHasReadLock = new ThreadBoolean();

    /**
     * Invokes the object after acquiring the read lock (if necessary). If invoked when the read lock has not yet been
     * acquired, then the lock is acquired for the duration of the call. If the lock has already been acquired, then the
     * status of the lock is not changed.
     * <p/>
     * TODO: Check to see if the write lock is acquired and <em>not</em> acquire the read lock in that situation.
     * Currently this code is not re-entrant. If a write lock is already acquired and the thread attempts to get the
     * read lock, then the thread will hang. For the moment, all the uses of ConcurrentBarrier are coded in such a way
     * that reentrant locks are not a problem.
     *
     * @param <T>
     * @param callable
     * @return the result of invoking the Callable
     */
    public <T> T read(Invokable<T> callable) {
        boolean readLockedAtEntry;

        synchronized (threadHasReadLock) {
            readLockedAtEntry = threadHasReadLock.get();
        }

        if (!readLockedAtEntry) {
            lock.readLock().lock();

            synchronized (threadHasReadLock) {
                threadHasReadLock.set(true);
            }
        }

        try {
            return callable.invoke();
        }
        catch (Exception e) {
            return null;
        }
        finally {
            if (!readLockedAtEntry) {
                lock.readLock().unlock();

                synchronized (threadHasReadLock) {
                    threadHasReadLock.remove();
                }
            }
        }
    }

    /**
     * As with {@link #read(Invokable)}, creating an {@link Invokable} wrapper around the runnable object.
     */
    public void read(final Runnable runnable) {
    	Invokable<Void> Callable = new Invokable<Void>() {
            public Void invoke() {
                runnable.run();
                return null;
            }
        };

        read(Callable);
    }

    /**
     * Acquires the exclusive write lock before invoking the Callable. The code will be executed exclusively, no other
     * reader or writer threads will exist (they will be blocked waiting for the lock). If the current thread has a read
     * lock, it is released before attempting to acquire the write lock, and re-acquired after the write lock is
     * released. Note that in that short window, between releasing the read lock and acquiring the write lock, it is
     * entirely possible that some other thread will sneak in and do some work, so the {@link Callable} object should
     * be prepared for cases where the state has changed slightly, despite holding the read lock. This usually manifests
     * as race conditions where either a) some parallel unrelated bit of work has occured or b) duplicate work has
     * occured. The latter is only problematic if the operation is very expensive.
     *
     * @param <T>
     * @param callable
     */
    public <T> T write(Invokable<T> callable) {
        boolean readLockedAtEntry = release_read_lock();

        lock.writeLock().lock();

        try {
            return callable.invoke();
        } finally {
            lock.writeLock().unlock();
            restore_read_lock(readLockedAtEntry);
        }
    }

    private boolean release_read_lock() {
        boolean readLockedAtEntry;

        synchronized (threadHasReadLock) {
            readLockedAtEntry = threadHasReadLock.get();
        }

        if (readLockedAtEntry) {
            lock.readLock().unlock();

            synchronized (threadHasReadLock) {
                threadHasReadLock.set(false);
            }
        }

        return readLockedAtEntry;
    }

    private void restore_read_lock(boolean readLockedAtEntry) {
        if (readLockedAtEntry) {
            lock.readLock().lock();

            synchronized (threadHasReadLock) {
                threadHasReadLock.set(true);
            }
        } else {
            synchronized (threadHasReadLock) {
                threadHasReadLock.remove();
            }
        }
    }

    /**
     * As with {@link #write(Invokable)}, creating an {@link Callable} wrapper around the runnable object.
     */
    public void write(final Runnable runnable) {
        Invokable<Void> invoke = new Invokable<Void>() {
            public Void invoke() {
                runnable.run();
                return null;
            }
        };
        write(invoke);
    }

    /**
     * Try to aquire the exclusive write lock and loop the Runnable. If the write lock is obtained within the specfied
     * timeout, then this method behaves as {@link #write(Runnable)} and will return true. If the write lock is not
     * obtained within the timeout then the runnable is never invoked and the method will return false.
     *
     * @param runnable    Runnable object to execute inside the write lock.
     * @param timeout     Time to wait for write lock.
     * @param timeoutUnit Units of timeout.
     * @return true if lock was obtained & runnabled executed. False otherwise.
     */
    public boolean write(final Runnable runnable, long timeout, TimeUnit timeoutUnit) {
        boolean readLockedAtEntry = release_read_lock();

        boolean obtainedLock = false;

        try {
            try {
                obtainedLock = lock.writeLock().tryLock(timeout, timeoutUnit);

                if (obtainedLock) runnable.run();

            }
            catch (InterruptedException e) {
                obtainedLock = false;
            }
            finally {
                if (obtainedLock) lock.writeLock().unlock();
            }
        }
        finally {
            restore_read_lock(readLockedAtEntry);
        }

        return obtainedLock;
    }

}
