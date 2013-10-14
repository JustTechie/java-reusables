package my.java.util.concurrent.locks;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class ReaderWriterLockUnitTests 
{
  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@lock cannot be null.")
  public void create_UsingNullLockInstance_Throws()
  {
    new ReaderWriterLock<ReentrantReadWriteLock>(null);
  }

  @Test
  public void readLockAcquiredAndReleased() 
  {
    final ReaderWriterLock<ReentrantReadWriteLock> readerWriterLock = new ReaderWriterLock<ReentrantReadWriteLock>(new ReentrantReadWriteLock());
    
    assertEquals(readerWriterLock.get().getReadHoldCount(), 0, "No read lock should have been acquired.");
    
    try(AutoCloseableLock<ReadLock> readLock = readerWriterLock.acquireReadLock())
    {
      assertEquals(readerWriterLock.get().getReadHoldCount(), 1, "Read lock should have been acquired.");
    }

    assertEquals(readerWriterLock.get().getReadHoldCount(), 0, "No read lock should have been acquired.");
  }

  @Test
  public void writeLockAcquiredAndReleased() 
  {
    final ReaderWriterLock<ReentrantReadWriteLock> readerWriterLock = new ReaderWriterLock<ReentrantReadWriteLock>(new ReentrantReadWriteLock());
    
    assertEquals(readerWriterLock.get().getWriteHoldCount(), 0, "No write lock should have been acquired.");
    
    try(AutoCloseableLock<WriteLock> writeLock = readerWriterLock.acquireWriteLock())
    {
      assertTrue(writeLock.get().isHeldByCurrentThread(), "Write lock should have been acquired.");
      assertEquals(readerWriterLock.get().getWriteHoldCount(), 1, "Write lock should have been acquired.");
    }

    assertEquals(readerWriterLock.get().getWriteHoldCount(), 0, "No write lock should have been acquired.");
  }
}
