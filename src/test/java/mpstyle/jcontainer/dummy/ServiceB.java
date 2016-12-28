package mpstyle.jcontainer.dummy;

public class ServiceB implements ServiceA {
  private final ServiceC serviceC;

  public ServiceB(ServiceC serviceC) {
    this.serviceC = serviceC;
  }

  public ServiceC getServiceC() {
    return serviceC;
  }
}
