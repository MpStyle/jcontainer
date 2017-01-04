package mpstyle.jcontainer;

import static org.junit.Assert.assertTrue;

import java.io.File;

import mpstyle.jcontainer.dummy.ServiceA;
import mpstyle.jcontainer.dummy.ServiceB;

import org.junit.Assert;
import org.junit.Test;

public class YamlContainerTest {
  @Test
  public void test() {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("definitions.yml").getFile());
    Container c = YamlContainer.from(file);

    Assert.assertTrue(c != null);
    Assert.assertTrue(c.existsKey(ServiceA.class));

    ServiceA serviceA = c.get(ServiceA.class);
    assertTrue(serviceA != null);
    assertTrue(serviceA instanceof ServiceB);
  }
}
