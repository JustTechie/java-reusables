/********************************************************************
 * File Name:    AbstractAutoCloseableResource.java
 *
 * Date Created: Oct 3, 2013
 *
 * @author:      JustTechie (justtechie@gmail.com)
 *
 *******************************************************************/

package my.java.resourcemanagement;

import java.io.Closeable;
  
/**
 * Base class for holding a resource and cleaning it up using the try-with-resource statement.
 * For existing classes which do not derive from {@link AutoCloseable} or {@link Closeable}} there
 * can be a class deriving from {@link AbstractAutoCloseableResource} and implementing the 
 * {@link doClose()} method where the corresponding close() method can be called on 
 * the object instance to do the clean up.
 * 
 * @param <TResource> The type of the resource object.
 */
public abstract class AbstractAutoCloseableResource<TResource> extends AbstractAutoCloseableResourceThrowingCheckedExceptionOnClose<TResource, RuntimeException>
{
  public AbstractAutoCloseableResource(final TResource resource)
  {
    super(resource);
  }

  @Override
  public void close()
  {
    super.close();
  }
  
  abstract protected void doClose();
}

