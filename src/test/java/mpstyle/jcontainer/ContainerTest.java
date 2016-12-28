package mpstyle.jcontainer;

import static org.junit.Assert.assertTrue;

import mpstyle.jcontainer.dummy.Closure;
import mpstyle.jcontainer.dummy.ServiceA;
import mpstyle.jcontainer.dummy.ServiceB;
import mpstyle.jcontainer.dummy.ServiceC;
import mpstyle.jcontainer.dummy.ServiceD;

import org.junit.Test;

public class ContainerTest {
  @Test
  public void addDefinition_01() throws Exception {
    Container c = new Container();
    c.addDefinition(ServiceA.class, ServiceB.class);

    ServiceA serviceA=c.get(ServiceA.class);
    assertTrue(serviceA != null);
    assertTrue( serviceA instanceof ServiceB );
  }

  @Test(expected=NotInjectableException.class)
  public void addDefinition_02() throws Exception {
    Container c = new Container();
    c.addDefinition(ServiceD.class, ServiceD.class);
  }

  @Test
  public void addInstance() throws Exception {
    Container c = new Container();
    c.addInstance(ServiceA.class, new ServiceB(new ServiceC()));

    ServiceA serviceA=c.get(ServiceA.class);
    assertTrue(serviceA != null);
    assertTrue( serviceA instanceof ServiceB );
  }

  @Test
  public void addClosure() throws Exception {
    Container c = new Container();
    c.addClosure(ServiceA.class, Closure.class);

    ServiceA serviceA=c.get(ServiceA.class);
    assertTrue(serviceA != null);
    assertTrue( serviceA instanceof ServiceB );
  }

}