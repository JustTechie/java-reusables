package my.java.resourcemanagement;

import my.java.resourcemanagement.ResourceHolder;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ResourceHolderUnitTests 
{
  @Test
  public void create_WithStringInstance_GetsCreatedAndReturnsOriginalInstance() 
  {
    createAndValidate("DummyString");
  }

  @Test
  public void create_WithObjectInstance_GetsCreatedAndReturnsOriginalInstance() 
  {
    createAndValidate(new Object());
  }

  @Test
  public void create_WithNullInstance_GetsCreatedAndReturnsNullInstance() 
  {
    final ResourceHolder<Object> resourceHolder = new ResourceHolder<Object>(null);
    
    Assert.assertNull(resourceHolder.get(), "Resource should be null.");
  }

  @Test
  public void release_CreateWithStringAndRelease_ResourceGetsReleased()
  {
    final String dummyString = "DummyString";
    final ResourceHolder<String> resourceHolder = new ResourceHolder<>(dummyString);
    
    Assert.assertEquals(resourceHolder.release(), dummyString, "Original string value and string value from ResourceHolder not equal.");
    
    Assert.assertNull(resourceHolder.get(), "Resource should be null.");
  }
  
  @Test
  public void reset_CreateWithStringAndResetWithNull_ResourceGetsReset()
  {
    final String dummyString = "DummyString";
    final ResourceHolder<String> resourceHolder = new ResourceHolder<>(dummyString);
    
    Assert.assertEquals(resourceHolder.reset(null), dummyString, "Original string value and string value from ResourceHolder not equal.");
    
    Assert.assertNull(resourceHolder.get(), "Resource should be null.");
  }

  @Test
  public void reset_CreateWithStringAndResetWithOtherString_ResourceGetsReset()
  {
    final String dummyString01 = "DummyString01";
    final ResourceHolder<String> resourceHolder = new ResourceHolder<>(dummyString01);

    validate(dummyString01, resourceHolder);
    
    final String dummyString02 = "DummyString02";
    Assert.assertEquals(resourceHolder.reset(dummyString02), dummyString01, "Original string value and string value from ResourceHolder not equal.");
    
    validate(dummyString02, resourceHolder);
  }

  // Private methods
  private static <T> void createAndValidate(final T resource)
  {
    final ResourceHolder<T> resourceHolder = new ResourceHolder<>(resource);
    
    validate(resource, resourceHolder);
  }

  private static <T> void validate(final T resource, final ResourceHolder<T> resourceHolder)
  {
    Assert.assertEquals(resourceHolder.get(), resource, "Original string value and string value from ResourceHolder not equal.");
    Assert.assertEquals(resourceHolder.get().hashCode(), resource.hashCode(), "Original string value hashcode and string value hashcode from ResourceHolder not equal.");
  }
}
