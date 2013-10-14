/********************************************************************
 * File Name:    ReaderWriterLock.java
 *
 * Date Created: Oct 12, 2013
 *
 * @author:      JustTechie (justtechie@gmail.com)
 *
 *******************************************************************/

package my.java.util.concurrent.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import my.java.resourcemanagement.ResourceHolder;
import static my.java.util.Assert.*;

/**
 * This class is a wrapper over {@link ReadWriteLock} enabling the consumer to acquire & release the read\write lock
 * using the try-with-resource statement. 
 * <p>Current, {@link ReadWriteLock} implementation returns {@link Lock} object on calling {@link ReadWriteLock#readLock()} 
 * or {@link ReadWriteLock#writeLock()} after which the {@link Lock#lock()} must be called.
 * {@link ReaderWriterLock} abstracts this by having two methods {@link #acquireReadLock()} and 
 * {@link #acquireWriteLock()} returning {@link AutoCloseableLock} which should be used in
 * try-with-resource statement.
 * <pre>
 * <b>Example:</b>
 *     final ReaderWriterLock<ReentrantReadWriteLock> readerWriterLock = new ReaderWriterLock<ReentrantReadWriteLock>(new ReentrantReadWriteLock());
 *     try(AutoCloseableLock<ReadLock> readLock = readerWriterLock.acquireReadLock()) // Current thread blocks till read lock is acquired.
 *     {
 *       // This block will execute once the read lock is acquired.
 *     }
 *     // Read lock is released once the try-with-resource statement is exited.
 * </pre>
 * 
 * @param <TReaderWriterLock> The type of class implementing {@link ReadWriteLock}.
 */
public class ReaderWriterLock<TReaderWriterLock extends ReadWriteLock> extends ResourceHolder<TReaderWriterLock>
{
  public ReaderWriterLock(final TReaderWriterLock lock)
  {
    super(lock);
    
    notNull("lock", lock);
  }
  
  /**
   * This method acquires the <code>read lock</code>. This method must be called in try-with-resource statement.
   * <b>Example:</b>
   *     final ReaderWriterLock<ReentrantReadWriteLock> readerWriterLock = new ReaderWriterLock<ReentrantReadWriteLock>(new ReentrantReadWriteLock());
   *     try(AutoCloseableLock<ReadLock> readLock = readerWriterLock.acquireReadLock()) // Current thread blocks till read lock is acquired.
   *     {
   *       // This block will execute once the read lock is acquired.
   *     }
   *     // Read lock is released once the try-with-resource statement is exited.
   * </pre>
   * 
   * @return The {@link AutoCloseableLock<TLock>} instance.
   */
  @SuppressWarnings("unchecked")
  public <TLock extends Lock> AutoCloseableLock<TLock> acquireReadLock()
  {
    return new AutoCloseableLock<TLock>((TLock)this.get().readLock());
  }

  /**
   * This method acquires the <code>write lock</code>. This method must be called in try-with-resource statement.
   * <b>Example:</b>
   *     final ReaderWriterLock<ReentrantReadWriteLock> readerWriterLock = new ReaderWriterLock<ReentrantReadWriteLock>(new ReentrantReadWriteLock());
   *     try(AutoCloseableLock<WriteLock> writeLock = readerWriterLock.acquireReadLock()) // Current thread blocks till write lock is acquired.
   *     {
   *       // This block will execute once the write lock is acquired.
   *     }
   *     // Write lock is released once the try-with-resource statement is exited.
   * </pre>
   * 
   * @return The {@link AutoCloseableLock<TLock>} instance.
   */
  @SuppressWarnings("unchecked")
  public <TLock extends Lock> AutoCloseableLock<TLock> acquireWriteLock()
  {
    return new AutoCloseableLock<TLock>((TLock)this.get().writeLock());
  }
}

