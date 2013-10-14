/********************************************************************
 * File Name:    AutoCloseableLock.java
 *
 * Date Created: Oct 3, 2013
 *
 * @author:      JustTechie (justtechie@gmail.com)
 *
 *******************************************************************/

package my.java.util.concurrent.locks;

import static my.java.util.Assert.*;
import java.util.concurrent.locks.Lock;

import my.java.resourcemanagement.AbstractAutoCloseableResource;

/**
 * Wrapper class which holds instance of {@link Lock}. It should be used with try-with-resource statement.
 * {@link Lock} does not implement {@link AutoCloseable} and hence consumer needs to call {@link Lock#unlock()}
 * explicitly. Using {@link AutoCloseableLock} consumer needs to just pass the original {@link Lock} instance
 * in try-with-resource statement and the {@link Lock#unlock()} get called when the scope is exited.
 * <pre>
 * <b>Example:</b>
 *     final ReentrantLock lock = new ReentrantLock();
 *     try(final AutoCloseableLock<ReentrantLock> autoCloseableLock = new AutoCloseableLock<ReentrantLock>(lock)) // Current thread will block till the lock is acquired.
 *     {
 *       // This block will execute once the lock is acquired.      
 *     }
 *     // Lock is released once the try-with-resource statement is exited.
 * </pre>
 * 
 * @param <TLock> The type of class implementing {@link Lock}
 */
public class AutoCloseableLock<TLock extends Lock> extends AbstractAutoCloseableResource<TLock>
{
  /**
   * Creates instance of {@link AutoCloseableLock} using the specified {@link Lock} instance. In this constructor
   * {@link Lock#lock()} is called and hence the thread will block till the lock is acquired. If the lock is already acquired
   * or the implementation of {@link Lock} supports acquiring lock other than the {@link Lock#lock()} method then in such cases
   * use {@link #AutoCloseableLock(Lock lock, boolean lockAlreadyAcquired)} constructor.
   * 
   * @param lock The instance of {@link Lock} class.
   */
  public AutoCloseableLock(final TLock lock)
  {
    this(lock, false);
  }

  /**
   * Creates instance of {@link AutoCloseableLock} using the specified {@link Lock} instance. In this constructor
   * {@link Lock#lock()} is called and hence the thread will block till the lock is acquired. 
   * 
   * @param lock The instance of {@link Lock} class.
   * 
   * @param lockAlreadyAcquired If the lock is already acquired or the implementation of {@link Lock} supports 
   * acquiring lock other than the {@link Lock#lock()} method then in such cases pass <code>true</code> else
   * pass <code>false</code>.
   * <p>
   * <b>Note:</b> If <code>true</code> is passed for <code>lockAlreadyAcquired</code> but lock is not acquired
   * then {@link Lock#unlock()} may thrown {@link IllegalMonitorStateException}.
   */
  public AutoCloseableLock(final TLock lock, final boolean lockAlreadyAcquired)
  {
    super(lock);
    
    notNull("lock", lock);
    
    if ( ! lockAlreadyAcquired )
    {
      this.get().lock();
    }
  }

  @Override
  protected void doClose()
  {
    this.get().unlock();    
  }  
}

