package my.javax.naming;

import static org.testng.Assert.*;

import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import my.javax.naming.AutoCloseableContext;

import org.testng.annotations.Test;

public class AutoCloseableContextUnitTests 
{
  @Test (expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "@context cannot be null.")
  public void create_UsingNullLockInstance_Throws()
  {
    new AutoCloseableContext<ContextImpl>(null);
  }
  
  @Test
  public void create_InstanceGetsCreatedAndReturnsTheInputInstance() 
  {
    final ContextImpl context = new ContextImpl(false);
    final AutoCloseableContext<ContextImpl> autoCloseableContext = new AutoCloseableContext<ContextImpl>(context);
    
    assertEquals(autoCloseableContext.get(), context, "Original context and context from AutoCloseableResource are not equal.");
    assertEquals(autoCloseableContext.get().hashCode(), context.hashCode(), "Original context hashcode and hashcode from AutoCloseableResource not equal.");
    assertFalse(context.isClosed(), "Context should not have been closed.");
  }
  
  @Test
  public void close_ContextGetsClosed() throws Exception 
  {
    final ContextImpl context = new ContextImpl(false);
    try(final AutoCloseableContext<ContextImpl> autoCloseableContext = new AutoCloseableContext<ContextImpl>(context))
    {
      assertEquals(autoCloseableContext.get(), context, "Original context and context from AutoCloseableResource are not equal.");
      assertEquals(autoCloseableContext.get().hashCode(), context.hashCode(), "Original context hashcode and hashcode from AutoCloseableResource not equal.");
      assertFalse(context.isClosed(), "Context should not have been closed.");
    }

    assertTrue(context.isClosed(), "Context should be closed.");
  }

  @Test (expectedExceptions = NamingException.class, expectedExceptionsMessageRegExp = "NamingException occurred on close.")
  public void close_ThrowsExceptionOnClose() throws Exception
  {
    final ContextImpl context = new ContextImpl(true);
    try(final AutoCloseableContext<ContextImpl> autoCloseableContext = new AutoCloseableContext<ContextImpl>(context))
    {
      assertEquals(autoCloseableContext.get(), context, "Original context and context from AutoCloseableResource are not equal.");
      assertEquals(autoCloseableContext.get().hashCode(), context.hashCode(), "Original context hashcode and hashcode from AutoCloseableResource not equal.");
      assertFalse(context.isClosed(), "Context should not have been closed.");
    }
  }

  // Private methods
  private static class ContextImpl implements Context
  {
    public ContextImpl(final boolean throwOnClose)
    {
      this.throwOnClose = throwOnClose;
    }

    public boolean isClosed()
    {
      return this.closed;
    }
    
    @Override
    public void close() throws NamingException
    {
      if (this.throwOnClose)       
      {
        throw new NamingException("NamingException occurred on close.");
      }
      
      this.closed = true;
    }

    @Override
    public Object lookup(Name name) throws NamingException
    {
      return null;
    }

    @Override
    public Object lookup(String name) throws NamingException
    {
      return null;
    }

    @Override
    public void bind(Name name, Object obj) throws NamingException
    {
    }

    @Override
    public void bind(String name, Object obj) throws NamingException
    {
    }

    @Override
    public void rebind(Name name, Object obj) throws NamingException
    {
    }

    @Override
    public void rebind(String name, Object obj) throws NamingException
    {
    }

    @Override
    public void unbind(Name name) throws NamingException
    {
    }

    @Override
    public void unbind(String name) throws NamingException
    {
    }

    @Override
    public void rename(Name oldName, Name newName) throws NamingException
    {
    }

    @Override
    public void rename(String oldName, String newName) throws NamingException
    {
    }

    @Override
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException
    {
      return null;
    }

    @Override
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException
    {
      return null;
    }

    @Override
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException
    {
      return null;
    }

    @Override
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException
    {
      return null;
    }

    @Override
    public void destroySubcontext(Name name) throws NamingException
    {
    }

    @Override
    public void destroySubcontext(String name) throws NamingException
    {
    }

    @Override
    public Context createSubcontext(Name name) throws NamingException
    {
      return null;
    }

    @Override
    public Context createSubcontext(String name) throws NamingException
    {
      return null;
    }

    @Override
    public Object lookupLink(Name name) throws NamingException
    {
      return null;
    }

    @Override
    public Object lookupLink(String name) throws NamingException
    {
      return null;
    }

    @Override
    public NameParser getNameParser(Name name) throws NamingException
    {
      return null;
    }

    @Override
    public NameParser getNameParser(String name) throws NamingException
    {
      return null;
    }

    @Override
    public Name composeName(Name name, Name prefix) throws NamingException
    {
      return null;
    }

    @Override
    public String composeName(String name, String prefix) throws NamingException
    {
      return null;
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal) throws NamingException
    {
      return null;
    }

    @Override
    public Object removeFromEnvironment(String propName) throws NamingException
    {
      return null;
    }

    @Override
    public Hashtable<?, ?> getEnvironment() throws NamingException
    {
      return null;
    }

    @Override
    public String getNameInNamespace() throws NamingException
    {
      return null;
    }
    
    // Private members
    private boolean closed;
    private boolean throwOnClose;
  }
}
