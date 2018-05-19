package mpstyle.jcontainer.dummy;

import mpstyle.jcontainer.annotation.Inject;

public class ServiceE {
  @Inject
  private ServiceF serviceF;

  public ServiceE() {

  }

  public ServiceF getServiceF() {
    return serviceF;
  }
}
