package my.java.util;

import org.testng.annotations.Test;
import static my.java.util.Assert.*;

public class AssertUnitTests 
{
  @Test
  public void ctor_ObjectIsCreated() 
  {
    // This test is just for having 100% code coverage !!!
    new Assert();
  }
  
  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@dummyArgumentName cannot be null.")
  public void notNull_NullInstanceIsPassed_ThrowsIllegalArgumentException()
  {
    notNull("dummyArgumentName", null);
  }

  @Test 
  public void notNull_NonNullInstanceIsPassed_NoExceptionIsThrown()
  {
    notNull("dummyArgumentName", "dummyValue");
  }

  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@dummyArgumentName cannot be null or empty.")
  public void notNullOrEmptyOrWhiteSpace_NullInstanceIsPassed_ThrowsIllegalArgumentException()
  {
    notNullOrEmptyOrWhiteSpace("dummyArgumentName", null);
  }

  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@dummyArgumentName cannot be null or empty.")
  public void notNullOrEmptyOrWhiteSpace_EmptyStringIsPassed_ThrowsIllegalArgumentException()
  {
    notNullOrEmptyOrWhiteSpace("dummyArgumentName", "");
  }

  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@dummyArgumentName cannot be null or empty.")
  public void notNullOrEmptyOrWhiteSpace_WhiteSpaceStringIsPassed_ThrowsIllegalArgumentException()
  {
    notNullOrEmptyOrWhiteSpace("dummyArgumentName", "   ");
  }

  @Test
  public void notNullOrEmptyOrWhiteSpace_ValidStringIsPassed_NoExceptionIsThrown()
  {
    notNullOrEmptyOrWhiteSpace("dummyArgumentName", "dummyValue");
  }
}
