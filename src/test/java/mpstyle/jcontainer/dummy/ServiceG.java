package mpstyle.jcontainer.dummy;

public class ServiceG {
  public final ServiceC servicec;

  public ServiceG() {
    throw new RuntimeException("Invalid constructor");
  }

  public ServiceG(ServiceC servicec) {
    this.servicec = servicec;
  }
}
