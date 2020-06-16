package mpstyle.jcontainer;

import mpstyle.jcontainer.dummy.ServiceA;
import mpstyle.jcontainer.dummy.ServiceB;
import mpstyle.jcontainer.dummy.ServiceC;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NoAutoInstanceContainerTest {
    @Test
    public void addDefinition_01() {
        Container c = new Container(false);

        assertThrows(RuntimeException.class, () -> {
            c.get(ServiceA.class);
        });

        c.addDefinition(ServiceA.class, ServiceB.class);
        c.addDefinition(ServiceC.class);

        ServiceA serviceA = c.get(ServiceA.class);
        assertNotNull(serviceA);
        assertTrue(serviceA instanceof ServiceB);
    }
}
