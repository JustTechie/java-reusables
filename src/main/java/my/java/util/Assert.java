/********************************************************************
 * File Name:    Assert.java
 *
 * Date Created: Oct 14, 2013
 *
 * @author:      JustTechie (justtechie@gmail.com)
 *
 *******************************************************************/

package my.java.util;
  
/**
 * Class containing simple assert methods.
 */
public final class Assert
{
  /**
   * This method checks if the specified argument value is null or not. If the value is
   * null then {@link IllegalArgumentException} is thrown with the message <strong><i>@paramName cannot be null.</i></strong>
   * 
   * @param argumentName The argument name to be used in message.
   * 
   * @param argumentValue The argument value to verify.
   * 
   * @throws IllegalArgumentException This exception is thrown if the specified <code>argumentValue</code> is null.
   */
  public static void notNull(final String argumentName, final Object argumentValue)
  {
    if (null == argumentValue)
    {
      throw new IllegalArgumentException(String.format("@%s cannot be null.", argumentName));
    }
  }
  
  
  /**
   * This method checks if the specified string value is null or empty or contains only white spaces. If the value is
   * null or empty or only white spaces then {@link IllegalArgumentException} is thrown with the message <strong><i>@paramName cannot be null or empty.</i></strong>
   * 
   * @param argumentName
   * @param argumentValue
   */
  public static void notNullOrEmptyOrWhiteSpace(final String argumentName, final String argumentValue) 
  {
    if (null == argumentValue || argumentValue.isEmpty() || argumentValue.trim().isEmpty())
    {
      throw new IllegalArgumentException(String.format("@%s cannot be null or empty.", argumentName));
    }    
  }
}