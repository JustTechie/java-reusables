/********************************************************************
 * File Name:    TestUtils.java
 *
 * Date Created: Oct 20, 2013
 *
 * @author:      JustTechie (justtechie@gmail.com)
 *
 *******************************************************************/

package my;

import java.lang.Thread.State;
import java.lang.reflect.Field;

import org.testng.Assert;
  
public class TestUtils
{
  public static void validateThreadIsBlocked(final Thread thread, 
                                             final String messageOnFailure,
                                             final int    nIterations,
                                             final long   milliSecondsToWaitPerIteration)
  {
    if ( ! thread.isAlive() )
    {
      thread.start();
    }
    
    for (int nIndex = 0; nIndex < nIterations && thread.getState() != State.WAITING; nIndex++)
    {
      try
      {
        Thread.sleep(milliSecondsToWaitPerIteration);
      }
      catch (InterruptedException e)
      {
        throw new RuntimeException(e);
      }
    }
    
    Assert.assertEquals(thread.getState(), State.WAITING, messageOnFailure);
  }
  
  @SuppressWarnings("unchecked")
  public static <TValue> TValue getFieldValue(final Object classInstance, final String fieldName)
  {
    try
    {
      final Field field = classInstance.getClass().getDeclaredField(fieldName);
      
      if ( ! field.isAccessible() )
      {
        field.setAccessible(true);
      }
      
      return (TValue)field.get(classInstance);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}

