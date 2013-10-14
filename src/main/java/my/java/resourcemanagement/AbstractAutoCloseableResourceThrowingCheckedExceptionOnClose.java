/********************************************************************
 * File Name:    AbstractAutoCloseableResourceThrowingCheckedExceptionOnClose.java
 *
 * Date Created: Sep 24, 2013
 *
 * @author:      JustTechie (justtechie@gmail.com)
 *
 *******************************************************************/

package my.java.resourcemanagement;

import java.io.Closeable;

/**
 * Base class for holding a resource and cleaning it up using the try-with-resource statement.
 * For existing classes which do not derive from {@link AutoCloseable} or {@link Closeable}} there
 * can be a class deriving from {@link AbstractAutoCloseableResourceThrowingCheckedExceptionOnClose}
 * and implementing the {@link doClose()} method where the corresponding close() method can be called on 
 * the object instance to do the clean up.
 * 
 * @param <TResource> The type of the resource object.
 * 
 * @param <TExceptionOnClose> The type of the checked exception which can be thrown when object is closed.
 */
public abstract class AbstractAutoCloseableResourceThrowingCheckedExceptionOnClose<TResource, TExceptionOnClose extends Exception> 
                extends ResourceHolder<TResource> 
                implements AutoCloseable
{
  public AbstractAutoCloseableResourceThrowingCheckedExceptionOnClose(final TResource resource)
  {
    super(resource);
  }
  
  @Override
  public void close() throws TExceptionOnClose
  {
    if (null != this.get())
    {
      doClose();
      this.reset(null);
    }
  }
 
  /**
   * This methods needs to be implemented by the derived class having logic to clean up the 
   * resource. Once this method is called then the resource instance is set to null.
   * 
   * @throws TExceptionOnClose The type of exception that can be thrown on resource clean up.
   */
  protected abstract void doClose() throws TExceptionOnClose;  
}

