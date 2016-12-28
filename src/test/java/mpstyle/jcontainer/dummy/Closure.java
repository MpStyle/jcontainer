package mpstyle.jcontainer.dummy;

import java.util.concurrent.Callable;

public class Closure implements Callable<ServiceA> {
  private final ServiceC serviceC;

  public Closure(ServiceC serviceC) {
    this.serviceC = serviceC;
  }

  /**
   * Computes a result, or throws an exception if unable to do so.
   *
   * @return computed result
   * @throws Exception if unable to compute a result
   */
  public ServiceA call() throws Exception {
    return new ServiceB(serviceC);
  }
}
