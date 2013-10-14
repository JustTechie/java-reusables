package my.java.util.concurrent.locks;

import java.util.concurrent.locks.ReentrantLock;
import my.java.util.concurrent.locks.AutoCloseableLock;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

public class AutoCloseableLockUnitTests 
{
  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@lock cannot be null.")
  public void create_UsingNullLockInstance_Throws()
  {
    new AutoCloseableLock<ReentrantLock>(null);
  }

  @Test
  public void create_LockGetsAcquiredAndReleasedInTryWithResourceScope()
  {
    final ReentrantLock lock = new ReentrantLock();
    
    assertFalse(lock.isHeldByCurrentThread(), "Lock should not have been held by the current thread.");
    
    try(final AutoCloseableLock<ReentrantLock> autoCloseableLock = new AutoCloseableLock<ReentrantLock>(lock))
    {
      assertTrue(lock.isHeldByCurrentThread(), "Lock should have been held by the current thread.");      
    }

    assertFalse(lock.isHeldByCurrentThread(), "Lock should not have been held by the current thread.");
  }

  @Test
  public void create_LockAlreadyAcquiredAndGetsReleasedInTryWithResourceScope()
  {
    final ReentrantLock lock = new ReentrantLock();
    
    lock.lock();
    assertTrue(lock.isHeldByCurrentThread(), "Lock should have been held by the current thread.");
    
    try(final AutoCloseableLock<ReentrantLock> autoCloseableLock = new AutoCloseableLock<ReentrantLock>(lock, true))
    {
      assertTrue(lock.isHeldByCurrentThread(), "Lock should have been held by the current thread.");      
    }

    assertFalse(lock.isHeldByCurrentThread(), "Lock should not have been held by the current thread.");
  }

  @Test (expectedExceptions = IllegalMonitorStateException.class)
  public void create_LockNotAlreadyAcquiredButPassedAcquiredFlagAsTrueInCtor_ThrowsIllegalMonitorStateException()
  {
    final ReentrantLock lock = new ReentrantLock();
    
    assertFalse(lock.isHeldByCurrentThread(), "Lock should not have been held by the current thread.");
    
    try(final AutoCloseableLock<ReentrantLock> autoCloseableLock = new AutoCloseableLock<ReentrantLock>(lock, true))
    {
      assertFalse(lock.isHeldByCurrentThread(), "Lock should not have been held by the current thread.");      
    }
  }

  @Test
  public void create_LockAlreadyAcquiredButPassedAcquiredFlagAsFalseInCtor_LockIsAcquiredTwiceAndNeedsToBeReleasedAfterTryWithResourceStatement()
  {
    final ReentrantLock lock = new ReentrantLock();

    lock.lock();
    assertTrue(lock.isHeldByCurrentThread(), "Lock should have been held by the current thread.");
    
    try(final AutoCloseableLock<ReentrantLock> autoCloseableLock = new AutoCloseableLock<ReentrantLock>(lock))
    {
      assertTrue(lock.isHeldByCurrentThread(), "Lock should have been held by the current thread.");      
    }
    
    assertTrue(lock.isHeldByCurrentThread(), "Lock should have been held by the current thread.");      

    lock.unlock();
    
    assertFalse(lock.isHeldByCurrentThread(), "Lock should not have been held by the current thread.");      
  }
}
