package my.java.util.concurrent.locks;

import java.util.Map;

import my.TestUtils;
import my.java.util.concurrent.locks.NamedLockManager.NamedCriticalSectionLock;
import my.java.util.concurrent.locks.NamedLockManager.NamedLock;
import my.java.util.concurrent.locks.NamedLockManager.NamedReadWriteLock.NamedReadLock;
import my.java.util.concurrent.locks.NamedLockManager.NamedReadWriteLock.NamedWriteLock;

import org.testng.Assert;
import org.testng.annotations.Test;

public class NamedLockManagerUnitTests 
{
  @Test
  public void ctor_instanceGetsCreated() 
  {
    new NamedLockManager();
  }
  
  // Critical section lock tests
  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@lockName cannot be null or empty.")
  public void acquireCriticalSectionLock_nullLockName_ThrowsIllegalArgumentException() 
  {
    new NamedLockManager().acquireCriticalSectionLock(null);
  }
  
  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@lockName cannot be null or empty.")
  public void acquireCriticalSectionLock_emptyLockName_ThrowsIllegalArgumentException() 
  {
    new NamedLockManager().acquireCriticalSectionLock("");
  }

  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@lockName cannot be null or empty.")
  public void acquireCriticalSectionLock_lockNameWithAllSpaces_ThrowsIllegalArgumentException() 
  {
    new NamedLockManager().acquireCriticalSectionLock("   ");
  }

  @Test
  public void acquireCriticalSectionLock_validLockName_singleThreaded_LockIsAcquired()
  {
    acquireCriticalSectionLock_validLockName_LockIsAcquired();
  }

  @Test (threadPoolSize = 20, invocationCount = 100, invocationTimeOut = 1000 * 60, timeOut = 1000 * 60 * 5)
  public void acquireCriticalSectionLock_validLockName_multiThreaded_LockIsAcquired()
  {
    acquireCriticalSectionLock_validLockName_LockIsAcquired();
  }

  private void acquireCriticalSectionLock_validLockName_LockIsAcquired()
  {
    final NamedLockManager namedLockManager = new NamedLockManager();

    final Map<String, NamedLock> acquiredLocks = TestUtils.getFieldValue(namedLockManager, "acquiredLocks");
    
    final String lockName = "dummyCriticalSectionLockName";
    
    try(final NamedCriticalSectionLock namedLock = namedLockManager.acquireCriticalSectionLock(lockName))
    {
      Assert.assertEquals(namedLock.getLockName(), lockName); 
      Assert.assertTrue(acquiredLocks.keySet().iterator().next().startsWith(lockName.toLowerCase()), String.format("Lock name '%s' not found in namedLockManager.", lockName));      

      try(final NamedCriticalSectionLock innerNamedLock = namedLockManager.acquireCriticalSectionLock(lockName.toUpperCase()))
      {
        Assert.assertEquals(innerNamedLock.getLockName(), lockName);      
        Assert.assertEquals(innerNamedLock, namedLock);      
        Assert.assertTrue(acquiredLocks.keySet().iterator().next().startsWith(lockName.toLowerCase()), String.format("Lock name '%s' not found in namedLockManager.", lockName));      
      }    
   }    

    Assert.assertTrue(acquiredLocks.isEmpty(), "acquiredLocks collection is not empty.");      
  }

  @Test
  public void acquireCriticalSectionLock_acquireOnTwoThreads_singleThreaded_LockIsAcquired() throws InterruptedException
  {
    acquireCriticalSectionLock_acquireOnTwoThreads_LockIsAcquired();
  }

  @Test (threadPoolSize = 20, invocationCount = 100, invocationTimeOut = 1000 * 60, timeOut = 1000 * 60 * 5)
  public void acquireCriticalSectionLock_acquireOnTwoThreads_multiThreaded_LockIsAcquired() throws InterruptedException
  {
    acquireCriticalSectionLock_acquireOnTwoThreads_LockIsAcquired();
  }

  private void acquireCriticalSectionLock_acquireOnTwoThreads_LockIsAcquired() throws InterruptedException
  {
    final NamedLockManager namedLockManager = new NamedLockManager();
    
    final Map<String, NamedLock> acquiredLocks = TestUtils.getFieldValue(namedLockManager, "acquiredLocks");

    final String lockName = "dummyCriticalSectionLockName";
    
    final Thread thread = new Thread(new Runnable()
                                     {      
                                       @Override
                                       public void run()
                                       {
                                         try(final NamedCriticalSectionLock namedLock = namedLockManager.acquireCriticalSectionLock(lockName.toUpperCase()))
                                         {
                                           Assert.assertEquals(namedLock.getLockName(), lockName);
                                           Assert.assertTrue(acquiredLocks.keySet().iterator().next().startsWith(lockName.toLowerCase()), String.format("Lock name '%s' not found in namedLockManager.", lockName));      
                                         }
                                       }
                                     });
    
    try(final NamedCriticalSectionLock namedLock = namedLockManager.acquireCriticalSectionLock(lockName))
    {
      Assert.assertEquals(namedLock.getLockName(), lockName);
      
      Assert.assertTrue(acquiredLocks.keySet().iterator().next().startsWith(lockName.toLowerCase()), String.format("Lock name '%s' not found in namedLockManager.", lockName));      
      
      TestUtils.validateThreadIsBlocked(thread, "Thread was not blocked for acquiring the lock.", 1000, 100);
    }
    
    thread.join();

    Assert.assertTrue(acquiredLocks.isEmpty(), "acquiredLocks collection is not empty.");      
  }
  
  // Read lock tests
  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@lockName cannot be null or empty.")
  public void acquireReadLock_nullLockName_ThrowsIllegalArgumentException() 
  {
    new NamedLockManager().acquireReadLock(null);
  }
  
  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@lockName cannot be null or empty.")
  public void acquireReadLock_emptyLockName_ThrowsIllegalArgumentException() 
  {
    new NamedLockManager().acquireReadLock("");
  }

  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@lockName cannot be null or empty.")
  public void acquireReadLock_LockNameWithAllSpaces_ThrowsIllegalArgumentException() 
  {
    new NamedLockManager().acquireReadLock("   ");
  }

  @Test
  public void acquireReadLock_validLockName_singleThreaded_LockIsAcquired() 
  {
    acquireReadLock_validLockName_LockIsAcquired();
  }

  @Test (threadPoolSize = 20, invocationCount = 100, invocationTimeOut = 1000 * 60, timeOut = 1000 * 60 * 5)
  public void acquireReadLock_validLockName_multiThreaded_LockIsAcquired() 
  {
    acquireReadLock_validLockName_LockIsAcquired();
  }

  private void acquireReadLock_validLockName_LockIsAcquired()   
  {
    final NamedLockManager namedLockManager = new NamedLockManager();
    
    final Map<String, NamedLock> acquiredLocks = TestUtils.getFieldValue(namedLockManager, "acquiredLocks");

    final String lockName = "dummyReadLockName";
    try(final NamedReadLock namedLock = namedLockManager.acquireReadLock(lockName))
    {      
      Assert.assertEquals(namedLock.getLockName(), lockName);
      Assert.assertTrue(acquiredLocks.keySet().iterator().next().startsWith(lockName.toLowerCase()), String.format("Lock name '%s' not found in namedLockManager.", lockName));      
      
      try(final NamedReadLock innerNamedLock = namedLockManager.acquireReadLock(lockName.toUpperCase()))
      {      
        Assert.assertEquals(innerNamedLock.getLockName(), lockName);
        Assert.assertEquals(innerNamedLock, namedLock);      
        Assert.assertTrue(acquiredLocks.keySet().iterator().next().startsWith(lockName.toLowerCase()), String.format("Lock name '%s' not found in namedLockManager.", lockName));      
      }    
    }    

    Assert.assertTrue(acquiredLocks.isEmpty(), "acquiredLocks collection is not empty.");      
  }

  // Write lock tests
  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@lockName cannot be null or empty.")
  public void acquireWriteLock_nullLockName_ThrowsIllegalArgumentException() 
  {
    new NamedLockManager().acquireWriteLock(null);
  }
  
  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@lockName cannot be null or empty.")
  public void acquireWriteLock_emptyLockName_ThrowsIllegalArgumentException() 
  {
    new NamedLockManager().acquireWriteLock("");
  }

  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@lockName cannot be null or empty.")
  public void acquireWriteLock_LockNameWithAllSpaces_ThrowsIllegalArgumentException() 
  {
    new NamedLockManager().acquireWriteLock("   ");
  }

  @Test
  public void acquireWriteLock_validLockName_singleThreaded_LockIsAcquired() 
  {
    acquireWriteLock_validLockName_LockIsAcquired();
  }

  @Test (threadPoolSize = 20, invocationCount = 100, invocationTimeOut = 1000 * 60, timeOut = 1000 * 60 * 5)
  public void acquireWriteLock_validLockName_multiThreaded_LockIsAcquired() 
  {
    acquireWriteLock_validLockName_LockIsAcquired();
  }

  private void acquireWriteLock_validLockName_LockIsAcquired() 
  {
    final NamedLockManager namedLockManager = new NamedLockManager();
    
    final Map<String, NamedLock> acquiredLocks = TestUtils.getFieldValue(namedLockManager, "acquiredLocks");
    
    final String lockName = "dummyWriteLockName";
    try(final NamedWriteLock namedLock = namedLockManager.acquireWriteLock(lockName))
    {      
      Assert.assertEquals(namedLock.getLockName(), lockName);
      Assert.assertTrue(acquiredLocks.keySet().iterator().next().startsWith(lockName.toLowerCase()), String.format("Lock name '%s' not found in namedLockManager.", lockName));      

      try(final NamedWriteLock innerNamedLock = namedLockManager.acquireWriteLock(lockName.toUpperCase()))
      {      
        Assert.assertEquals(innerNamedLock.getLockName(), lockName);
        Assert.assertEquals(innerNamedLock, namedLock);      
        Assert.assertTrue(acquiredLocks.keySet().iterator().next().startsWith(lockName.toLowerCase()), String.format("Lock name '%s' not found in namedLockManager.", lockName));      
      }    
    }    

    Assert.assertTrue(acquiredLocks.isEmpty(), "acquiredLocks collection is not empty.");      
 }
  
  // Read\Write lock tests
  @Test
  public void acquireReadWriteLock_acquireReadLockAndBlockForWriteLock_singleThreaded_LocksAreAcquired() throws InterruptedException
  {
    acquireReadWriteLock_acquireReadLockAndBlockForWriteLock_LocksAreAcquired();
  }
  
  @Test (threadPoolSize = 20, invocationCount = 100, invocationTimeOut = 1000 * 60, timeOut = 1000 * 60 * 5)
  public void acquireReadWriteLock_acquireReadLockAndBlockForWriteLock_multiThreaded_LocksAreAcquired() throws InterruptedException
  {
    acquireReadWriteLock_acquireReadLockAndBlockForWriteLock_LocksAreAcquired();
  }

  private void acquireReadWriteLock_acquireReadLockAndBlockForWriteLock_LocksAreAcquired() throws InterruptedException  
  {
    final NamedLockManager namedLockManager = new NamedLockManager();
    
    final Map<String, NamedLock> acquiredLocks = TestUtils.getFieldValue(namedLockManager, "acquiredLocks");

    final String lockName = "dummyReadWriteLock";
    
    final Thread thread = new Thread(new Runnable()
                                     {      
                                       @Override
                                       public void run()
                                       {
                                         try(final NamedWriteLock namedLock = namedLockManager.acquireWriteLock(lockName.toUpperCase()))
                                         {
                                           Assert.assertEquals(namedLock.getLockName(), lockName);                                           
                                           Assert.assertTrue(acquiredLocks.keySet().iterator().next().startsWith(lockName.toLowerCase()), String.format("Lock name '%s' not found in namedLockManager.", lockName));      
                                         }
                                       }
                                     });

    try(final NamedReadLock namedLock = namedLockManager.acquireReadLock(lockName))
    {
      Assert.assertEquals(namedLock.getLockName(), lockName);
      
      Assert.assertTrue(acquiredLocks.keySet().iterator().next().startsWith(lockName.toLowerCase()), String.format("Lock name '%s' not found in namedLockManager.", lockName));      

      TestUtils.validateThreadIsBlocked(thread, "Thread was not blocked for write lock.", 1000, 100);
    }
    
    thread.join();

    Assert.assertTrue(acquiredLocks.isEmpty(), "acquiredLocks collection is not empty.");      
  }

  @Test
  public void acquireReadWriteLock_acquireWriteLockAndBlockForReadLock_singleThreaded_LocksAreAcquired() throws InterruptedException
  {
    acquireReadWriteLock_acquireWriteLockAndBlockForReadLock_LocksAreAcquired();
  }

  @Test (threadPoolSize = 20, invocationCount = 100, invocationTimeOut = 1000 * 60, timeOut = 1000 * 60 * 5)
  public void acquireReadWriteLock_acquireWriteLockAndBlockForReadLock_multiThreaded_LocksAreAcquired() throws InterruptedException
  {
    acquireReadWriteLock_acquireWriteLockAndBlockForReadLock_LocksAreAcquired();
  }

  private void acquireReadWriteLock_acquireWriteLockAndBlockForReadLock_LocksAreAcquired() throws InterruptedException
  {
    final NamedLockManager namedLockManager = new NamedLockManager();
    
    final Map<String, NamedLock> acquiredLocks = TestUtils.getFieldValue(namedLockManager, "acquiredLocks");

    final String lockName = "dummyReadWriteLock";
    
    final Thread thread = new Thread(new Runnable()
                                     {      
                                       @Override
                                       public void run()
                                       {
                                         try(final NamedReadLock namedLock = namedLockManager.acquireReadLock(lockName.toUpperCase()))                                         
                                         {
                                           Assert.assertEquals(namedLock.getLockName(), lockName);                                           
                                           Assert.assertTrue(acquiredLocks.keySet().iterator().next().startsWith(lockName.toLowerCase()), String.format("Lock name '%s' not found in namedLockManager.", lockName));      
                                         }
                                       }
                                     });

    try(final NamedWriteLock namedLock = namedLockManager.acquireWriteLock(lockName))
    {
      Assert.assertEquals(namedLock.getLockName(), lockName);
      Assert.assertTrue(acquiredLocks.keySet().iterator().next().startsWith(lockName.toLowerCase()), String.format("Lock name '%s' not found in namedLockManager.", lockName));      
      
      TestUtils.validateThreadIsBlocked(thread, "Thread was not blocked for read lock.", 1000, 100);
    }
    
    thread.join();

    Assert.assertTrue(acquiredLocks.isEmpty(), "acquiredLocks collection is not empty.");      
  }
}
