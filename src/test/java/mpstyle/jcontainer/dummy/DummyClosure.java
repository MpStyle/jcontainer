package mpstyle.jcontainer.dummy;

import mpstyle.jcontainer.Closure;

public class DummyClosure implements Closure<ServiceA> {
  private final ServiceC serviceC;

  public DummyClosure(ServiceC serviceC) {
    this.serviceC = serviceC;
  }

  /**
   * Computes a result, or throws an exception if unable to do so.
   *
   * @return computed result
   * @throws Exception if unable to compute a result
   */
  public ServiceA call() {
    return new ServiceB(serviceC);
  }
}
