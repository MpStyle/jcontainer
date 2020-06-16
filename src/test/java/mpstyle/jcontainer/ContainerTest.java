package mpstyle.jcontainer;

import mpstyle.jcontainer.dummy.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContainerTest {
    @Test
    public void addDefinition_01() {
        Container c = new Container();
        c.addDefinition(ServiceA.class, ServiceB.class);

        ServiceA serviceA = c.get(ServiceA.class);
        assertTrue(serviceA != null);
        assertTrue(serviceA instanceof ServiceB);
    }

    @Test
    public void addInstance() {
        Container c = new Container();
        c.addInstance(ServiceA.class, new ServiceB(new ServiceC()));

        ServiceA serviceA = c.get(ServiceA.class);
        assertTrue(serviceA != null);
        assertTrue(serviceA instanceof ServiceB);
    }

    @Test
    public void addClosure_01() {
        Container c = new Container();
        c.addClosure(ServiceA.class, DummyClosure.class);

        ServiceA serviceA = c.get(ServiceA.class);
        assertTrue(serviceA != null);
        assertTrue(serviceA instanceof ServiceB);
    }

    @Test
    public void addClosure_02() {
        Container c = new Container();
        c.addClosure(ServiceA.class, new DummyClosure(new ServiceC()));

        ServiceA serviceA = c.get(ServiceA.class);
        assertTrue(serviceA != null);
        assertTrue(serviceA instanceof ServiceB);
    }

    @Test
    public void testAnnotation() {
        Container c = new Container();
        ServiceE serviceE = c.get(ServiceE.class);
        assertTrue(serviceE != null);
        assertTrue(serviceE.getServiceF() != null);
        assertEquals("Hello world!", serviceE.getServiceF().getTest());
    }

    @Test
    public void testMultipleConstructors() {
        Container c = new Container();
        ServiceG serviceG = c.get(ServiceG.class);
        assertTrue(serviceG != null);
        assertTrue(serviceG.servicec != null);
    }
}