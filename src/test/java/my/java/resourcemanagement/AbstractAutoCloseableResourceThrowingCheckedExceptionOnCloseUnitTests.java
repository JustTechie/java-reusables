package my.java.resourcemanagement;

import static org.testng.Assert.*;
import my.java.resourcemanagement.AbstractAutoCloseableResourceThrowingCheckedExceptionOnClose;

import org.testng.annotations.Test;

public class AbstractAutoCloseableResourceThrowingCheckedExceptionOnCloseUnitTests 
{
  @Test
  public void create_InstanceGetsCreatedAndReturnsTheString() 
  {
    final String stringValue = "DummyString";
    final AutoCloseableImpl autoCloseableImpl = new AutoCloseableImpl(stringValue, false);
    
    assertEquals(autoCloseableImpl.get(), stringValue, "Original string value and string value from AutoCloseableResource not equal.");
    assertEquals(autoCloseableImpl.get().hashCode(), stringValue.hashCode(), "Original string value hashcode and string value hashcode from AutoCloseableResource not equal.");
    assertFalse(autoCloseableImpl.isClosed(), "Resource should not have been closed.");
  }
  
  @Test
  public void close_ResourceGetsClosed() throws Exception 
  {
    final AutoCloseableImpl autoCloseableImplOriginalInstance = new AutoCloseableImpl("DummyString", false);
    try(final AutoCloseableImpl autoCloseableReference = autoCloseableImplOriginalInstance)
    {
      assertFalse(autoCloseableReference.isClosed(), "Resource should not have been closed.");
      assertNotNull(autoCloseableReference.get(), "Resource should not be null.");
    }

    assertTrue(autoCloseableImplOriginalInstance.isClosed(), "Resource should be closed.");
    assertNull(autoCloseableImplOriginalInstance.get(), "Resource should be null.");
  }

  @Test (expectedExceptions = Exception.class, expectedExceptionsMessageRegExp = "Exception occurred on close.")
  public void close_ThrowsExceptionOnClose() throws Exception 
  {
    final AutoCloseableImpl autoCloseableImplOriginalInstance = new AutoCloseableImpl("DummyString", true);
    try(final AutoCloseableImpl autoCloseableImpl = autoCloseableImplOriginalInstance)
    {
      assertFalse(autoCloseableImpl.isClosed(), "Resource should not have been closed.");
      assertNotNull(autoCloseableImpl.get(), "Should should not be null.");
    }
  }

  // Private methods
  private static class AutoCloseableImpl extends AbstractAutoCloseableResourceThrowingCheckedExceptionOnClose<String, Exception>
  {
    public AutoCloseableImpl(final String resource, final boolean throwOnClose)
    {
      super(resource);
      
      this.throwOnClose = throwOnClose;
    }

    public boolean isClosed()
    {
      return this.closed;
    }
    
    @Override
    protected void doClose() throws Exception
    {
      if (this.throwOnClose)
      {
        throw new Exception("Exception occurred on close.");
      }
      
      this.closed = true;
    }
    
    // Private members
    private boolean closed;
    private boolean throwOnClose;
  }
}
