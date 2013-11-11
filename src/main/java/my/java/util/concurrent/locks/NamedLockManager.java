/********************************************************************
 * File Name:    NamedLockManager.java
 *
 * Date Created: Oct 15, 2013
 *
 * @author:      JustTechie (justtechie@gmail.com)
 *
 *******************************************************************/

package my.java.util.concurrent.locks;
  
import static my.java.util.Assert.*;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import my.java.util.concurrent.locks.NamedLockManager.NamedReadWriteLock.NamedReadLock;
import my.java.util.concurrent.locks.NamedLockManager.NamedReadWriteLock.NamedWriteLock;

/**
 * This class allows acquiring locks using names. If there is no lock for the given name then a new {@link Lock} instance is created
 * and the lock is acquired. If a lock is already created for a give name then existing {@link Lock} is retrieved and the thread is 
 * blocked till the lock can be acquired.
 * <p>
 * <strong>Note:</strong> Each instance of {@link NamedLockManager} maintains a list of acquired named locks. Application should make sure
 * same instance of {@link NamedLockManager} is used throughout i.e. if dependency injection is used then all consumers should be injected with
 * same instance of {@link NamedLockManager}.
 */
public final class NamedLockManager // TODO: JustTechie - Flexibility to configure factory to generate Lock instances and to acquire the locks !!!
{
  /**
   * This method acquires a {@link Lock} using the given name. If there is no lock for the given name then a new {@link Lock} instance is created
   * and the lock is acquired. If a lock is already created for a give name then existing {@link Lock} is retrieved and the thread is 
   * blocked till the lock can be acquired for this thread.
   * <pre>
   * <strong>Example:</strong>
   *    final NamedLockManager namedLockManager = new NamedLockManager();
   *    try(final NamedCriticalSectionLock namedLock = namedLockManager.acquireCriticalSectionLock("someJobWithID1234")) // This call blocks till lock is acquired.
   *    {
   *          ...
   *    }
   *    // The lock is released once the try-with-resource statement is exited.    
   * </pre> 
   * 
   * @param lockName The name of the lock.
   * 
   * @return The {@link NamedCriticalSectionLock} instance which internally holds the {@link Lock}. The lock is released by calling {@link NamedCriticalSectionLock#close()}.
   */
  public NamedCriticalSectionLock acquireCriticalSectionLock(final String lockName)
  {
    notNullOrEmptyOrWhiteSpace("lockName", lockName);
    
    final LockName criticalSectionLockName = generateLockName(lockName, "criticalSection");
    
    final NamedCriticalSectionLock namedCriticalSectionLock = getNamedLock(criticalSectionLockName, 
                                                              new NamedLockFactory()
                                                              {                          
                                                                @Override
                                                                public NamedLock create(final LockName lockName)
                                                                {
                                                                  return new NamedCriticalSectionLock(lockName, new ReentrantLock()); // TODO: JustTechie - fairness policy ???
                                                                }
                                                              });

    namedCriticalSectionLock.acquireLock(); // block till we acquire the lock.
    
    return namedCriticalSectionLock;
  }

  /**
   * This method acquires {@link Lock} using the given name. If there is no lock for the given name then a new {@link ReadWriteLock} instance is created
   * and the read lock is acquired. If a lock is already created for a give name then existing {@link ReadWriteLock} is retrieved and the thread is 
   * blocked till the read lock can be acquired for this thread.
   * <pre>
   * <strong>Example:</strong>
   *    final NamedLockManager namedLockManager = new NamedLockManager();
   *    try(final NamedReadLock namedLock = namedLockManager.acquireReadLock("someJobWithID1234")) // This call blocks till lock is acquired.
   *    {
   *          ...
   *    }
   *    // The lock is released once the try-with-resource statement is exited.    
   * </pre> 
   * @param lockName The name of the lock.
   * 
   * @return The {@link NamedReadLock} instance which internally holds the {@link ReadWriteLock}. The lock is released by calling {@link NamedReadLock#close()}.
   */
  public NamedReadLock acquireReadLock(final String lockName)
  {
    notNullOrEmptyOrWhiteSpace("lockName", lockName);
    
    final NamedReadWriteLock namedReadWriteLock = getNamedReadWriteLock(lockName);
    
    return namedReadWriteLock.acquireReadLock(); // block till we acquire the lock.
  }
  
  /**
   * This method acquires {@link Lock} using the given name. If there is no lock for the given name then a new {@link ReadWriteLock} instance is created
   * and the write lock is acquired. If a lock is already created for a give name then existing {@link ReadWriteLock} is retrieved and the thread is 
   * blocked till the write lock can be acquired for this thread.
   * <pre>
   * <strong>Example:</strong>
   *    final NamedLockManager namedLockManager = new NamedLockManager();
   *    try(final NamedWriteLock namedLock = namedLockManager.acquireWriteLock("someJobWithID1234")) // This call blocks till lock is acquired.
   *    {
   *          ...
   *    }
   *    // The lock is released once the try-with-resource statement is exited.    
   * </pre> 
   * @param lockName The name of the lock.
   * 
   * @return The {@link NamedWriteLock} instance which internally holds the {@link ReadWriteLock}. The lock is released by calling {@link NamedWriteLock#close()}.
   */
  public NamedWriteLock acquireWriteLock(final String lockName)
  {
    notNullOrEmptyOrWhiteSpace("lockName", lockName);
    
    final NamedReadWriteLock namedReadWriteLock = getNamedReadWriteLock(lockName);
    
    return namedReadWriteLock.acquireWriteLock(); // block till we acquire the lock.
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Private methods
  private NamedReadWriteLock getNamedReadWriteLock(final String lockName)
  {
    final LockName readerWriterLockName = generateLockName(lockName, "readerWriter");

    return getNamedLock(readerWriterLockName, 
                        new NamedLockFactory()
                        {                                                                      
                          @Override
                          public NamedLock create(final LockName lockName)
                          {
                            return new NamedReadWriteLock(lockName, new ReentrantReadWriteLock()); // TODO: JustTechie - fairness policy ???
                          }
                        });
  }

  @SuppressWarnings("unchecked")
  private <TNamedLock> TNamedLock getNamedLock(final LockName lockName, final NamedLockFactory factory)
  {
    NamedLock namedLock = null;
    synchronized (this.syncObject)
    {
      namedLock = this.acquiredLocks.get(lockName.fullName);
      if (null == namedLock)
      {
        namedLock = factory.create(lockName);
        
        this.acquiredLocks.put(lockName.fullName, namedLock);
      }
      else
      {
        namedLock.incrementReference(); // If we define namedLock as TNamedLock instead of NamedLock then incrementReference() is not visible !!!
      }
    }    

    return (TNamedLock)namedLock;
  }
  
  private LockName generateLockName(final String lockName, final String suffix)
  {
    return new LockName(lockName, String.format("%s_%s_lock", lockName.toLowerCase(), suffix)); // Assumption: Normally lock names will be internal to code and will not have localization need i.e. no need to use locale. 
  }
  
  // Private members
  private Object syncObject = new Object();
  private HashMap<String, NamedLock> acquiredLocks = new HashMap<>(); // Using HashMap directly since we are converting the input lock name to lower case i.e. no need for case-insensitive keys.
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////
  // Inner classes
  /**
   * Base class for <code>NamedLocks</code>.
   */
  public abstract class NamedLock implements AutoCloseable
  {
    private NamedLock(final LockName lockName)
    {
      this.lockName = lockName;
    }

    /**
     * @return The name of the lock.
     */
    public String getLockName()
    {
      return this.lockName.simpleName;
    }
    
    abstract void acquireLock(); // Consumers cannot directly acquire lock but NamedLockManager acquires the lock while consumer releases the lock by calling close();
    
    /**
     * This method closes the acquired lock.
     */
    @Override
    public void close()
    {
      decrementReference();
    }

    protected void incrementReference()
    {
      synchronized (NamedLockManager.this.syncObject)
      {
        this.nReferences.getAndIncrement();
      }
    }
    
    protected void decrementReference()
    {
      synchronized (NamedLockManager.this.syncObject)
      {
        if (0 == this.nReferences.decrementAndGet())
        {
          NamedLockManager.this.acquiredLocks.remove(this.lockName.fullName);
        }
      }
    }

    // Protected members
    private final LockName   lockName;
    private final AtomicLong nReferences = new AtomicLong(1);    
  }

  /**
   * This class holds the {@link Lock} instance.
   */
  public final class NamedCriticalSectionLock extends NamedLock
  {
    private NamedCriticalSectionLock(final LockName lockName, final Lock lock)
    {
      super(lockName);
    
      this.lock = lock;
    }

    @Override
    public void close()
    {
      this.lock.unlock(); // Release this lock before acquiring any lock in super class to avoid any deadlock situation. 
      
      super.close();
    }
    
    @Override
    void acquireLock()
    {
      this.lock.lock();
    }
    
    // Private members
    private final Lock lock;
  }
  
  /**
   * This class is used to hold the ReadWriteLock instance and corresponding reference counting.
   */
  public final class NamedReadWriteLock extends NamedLock
  {
    private NamedReadWriteLock(final LockName lockName, final ReadWriteLock readWritelock)
    {
      super(lockName);
      
      this.rawReadWritelock = readWritelock;
      this.namedReadLock    = new NamedReadLock();
      this.namedWriteLock   = new NamedWriteLock();
    }

    /**
     * @return The {@link NamedReadLock} instance.
     */
    public NamedReadLock acquireReadLock()
    {
      this.namedReadLock.acquireLock();
      
      return this.namedReadLock;
    }
    
    /**
     * @return The {@link NamedWriteLock} instance.
     */
    public NamedWriteLock acquireWriteLock()
    {
      this.namedWriteLock.acquireLock();
      
      return this.namedWriteLock;
    }
    
    @Override
    void acquireLock()
    {
      // Do nothing as we are not acquiring this lock but the read or write locks.
      // TODO: JustTechie - throw exception
    }

    // Private members
    private final ReadWriteLock  rawReadWritelock;    
    private final NamedReadLock  namedReadLock;
    private final NamedWriteLock namedWriteLock;
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Inner classes of NamedReadWriteLock

    /**
     * This class holds the read lock which should be released by calling {@link NamedReadLock#close()}.
     */
    public final class NamedReadLock extends NamedReadWriteLockDecorator
    {
      private NamedReadLock()
      {
        super(NamedReadWriteLock.this.rawReadWritelock.readLock());
      }
    }
    
    /**
     * This class holds the write lock which should be released by calling {@link NamedWriteLock#close()}.
     */
    public final class NamedWriteLock extends NamedReadWriteLockDecorator
    {
      private NamedWriteLock()
      {
        super(NamedReadWriteLock.this.rawReadWritelock.writeLock());
      }
    }

    private class NamedReadWriteLockDecorator extends NamedLockDecorator
    {
      private NamedReadWriteLockDecorator(final Lock rawReadOrWriteLock)
      {
        super(NamedReadWriteLock.this);
        
        this.readOrWriteLock = rawReadOrWriteLock;
      }
      
      @Override
      void acquireLock()
      {
        this.readOrWriteLock.lock();
      }
      
      @Override
      public void close()
      {
        this.readOrWriteLock.unlock(); // Release this lock before acquiring any lock in super class to avoid any deadlock situation.
        
        super.close();
      }

      // Private members
      private final Lock readOrWriteLock;
    }

    private class NamedLockDecorator extends NamedLock
    {
      private NamedLockDecorator(final NamedLock namedLock)
      {
        super(namedLock.lockName);
        
        this.innerNamedLock = namedLock;
      }
      
      @Override
      public String getLockName()
      {
        return this.innerNamedLock.getLockName();
      }
      
      @Override
      public void close()
      {
        this.innerNamedLock.close();
      }
      
      @Override
      void acquireLock()
      {
        this.innerNamedLock.acquireLock();
      }
      
      @Override
      protected void incrementReference()
      {
        this.innerNamedLock.incrementReference();
      }
      
      @Override
      protected void decrementReference()
      {
        this.innerNamedLock.decrementReference();
      }
      
      // Private members
      private final NamedLock innerNamedLock;
    }  
  }
  
  // Private interfaces\classes of NamedLockManager
  private interface NamedLockFactory
  {
    NamedLock create(LockName lockName);
  }
  
  private class LockName
  {
    private LockName(final String simpleName, final String fullName)
    {
      this.simpleName = simpleName;
      this.fullName   = fullName;
    }

    private final String simpleName;
    private final String fullName;
  }  
}