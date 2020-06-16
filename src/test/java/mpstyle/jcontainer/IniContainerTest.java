package mpstyle.jcontainer;


import java.io.File;

import mpstyle.jcontainer.dummy.ServiceA;
import mpstyle.jcontainer.dummy.ServiceB;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IniContainerTest {
  @Test
  public void test() {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("definitions.ini").getFile());
    Container c = IniContainer.from(file);

    assertTrue(c != null);
    assertTrue(c.existsKey(ServiceA.class));

    ServiceA serviceA = c.get(ServiceA.class);
    assertTrue(serviceA != null);
    assertTrue(serviceA instanceof ServiceB);
  }
}
