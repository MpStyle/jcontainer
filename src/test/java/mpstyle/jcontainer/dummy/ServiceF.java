package mpstyle.jcontainer.dummy;

import mpstyle.jcontainer.annotation.Injectable;

@Injectable
public class ServiceF {
  private final String test;

  public ServiceF() {
    test = "Hello world!";
  }

  public String getTest() {
    return test;
  }
}
