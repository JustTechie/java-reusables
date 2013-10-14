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
 * TODO: Add javadocs.
 *
 */
public final class Assert
{
  public static void notNull(final String argumentName, final Object argumentValue)
  {
    if (null == argumentValue)
    {
      throw new IllegalArgumentException(String.format("@%s cannot be null.", argumentName));
    }
  } 
}