package mpstyle.jcontainer;

import java.io.File;

import mpstyle.jcontainer.dummy.ServiceA;
import mpstyle.jcontainer.dummy.ServiceB;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class YamlContainerTest {
    @Test
    public void test() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("definitions.yml").getFile());
        Container c = YamlContainer.from(file);

        assertNotNull(c);
        assertTrue(c.existsKey(ServiceA.class));

        ServiceA serviceA = c.get(ServiceA.class);
        assertNotNull(serviceA);
        assertTrue(serviceA instanceof ServiceB);
    }
}
