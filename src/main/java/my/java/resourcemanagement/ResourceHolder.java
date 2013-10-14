/********************************************************************
 * File Name:    ResourceHolder.java
 *
 * Date Created: Sep 24, 2013
 *
 * @author:      JustTechie (justtechie@gmail.com)
 *
 *******************************************************************/

package my.java.resourcemanagement;

/**
 * Class to hold resource instance of type <code>T</code>.
 * 
 * @param <T> Type of resource object.
 */
public class ResourceHolder<T>
{
  /**
   * Creates instance of {@link ResourceHolder<T>} to hold the input object instance.
   * 
   * @param resource The instance of resource. The instance can be null.
   */
  public ResourceHolder(final T resource)
  {
    this.resource = resource;
  }
  
  /**
   * @return The instance of the resource.
   */
  public T get()
  {
    return this.resource;
  }
  
  /**
   * This method sets the instance of the resource to the new input instance.
   * 
   * @param newResource The new instance to be set.
   * 
   * @return The previous resource instance.
   */
  public T reset(final T newResource)
  {
    final T originalResource = this.resource;
    
    this.resource = newResource;
    
    return originalResource;
  }
  
  /**
   * This method sets the instance of the resource to null.
   * 
   * @return The previous resource instance.
   */
  public T release()
  {
    return reset(null);
  }

  //
  // TODO: JustTechie - Implement equals() & hashcode(). Currently, same can be achieved by calling equals() or hashcode() 
  // on the resource. If needs to be implemented here then following should be considered:
  // - null resource values
  // - equality for strings considering case-insensitivity etc.
  //
  
  // Protected members
  protected T resource;
}

