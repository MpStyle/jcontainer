package mpstyle.jcontainer.dummy;

import mpstyle.jcontainer.annotation.Injectable;

@Injectable
public class ServiceG {
  public final ServiceC servicec;

  public ServiceG() {
    throw new RuntimeException("Invalid constructor");
  }

  public ServiceG(ServiceC servicec) {
    this.servicec = servicec;
  }
}
