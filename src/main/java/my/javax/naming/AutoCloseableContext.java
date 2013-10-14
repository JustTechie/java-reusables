/********************************************************************
 * File Name:    AutoCloseableContext.java
 *
 * Date Created: Oct 3, 2013
 *
 * @author:      JustTechie (justtechie@gmail.com)
 *
 *******************************************************************/

package my.javax.naming;

import javax.naming.Context;
import javax.naming.NamingException;

import my.java.resourcemanagement.AbstractAutoCloseableResourceThrowingCheckedExceptionOnClose;
import static my.java.util.Assert.*;

/**
 * Wrapper class which holds instance of {@link Context}. It should be used with try-with-resource statement.
 * {@link Context} does not implement {@link AutoCloseable} and hence consumer needs to call {@link Context#close()}
 * explicitly. Using {@link AutoCloseableContext} consumer needs to just pass the original {@link Context} instance
 * in try-with-resource statement and the {@link Context#close()} get called when the scope is exited.
 *  
 * @param <TContext> The type of class implementing {@link Context}.
 */
public class AutoCloseableContext<TContext extends Context> extends AbstractAutoCloseableResourceThrowingCheckedExceptionOnClose<TContext, NamingException>
{
  public AutoCloseableContext(TContext context)
  {
    super(context);
    
    notNull("context", context);
  }

  @Override
  protected void doClose() throws NamingException
  {
    this.get().close();
  }
}

