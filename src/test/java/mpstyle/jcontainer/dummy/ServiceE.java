package mpstyle.jcontainer.dummy;

import mpstyle.jcontainer.annotation.Inject;
import mpstyle.jcontainer.annotation.Injectable;

@Injectable
public class ServiceE {
  @Inject
  private ServiceF serviceF;

  public ServiceE() {

  }

  public ServiceF getServiceF() {
    return serviceF;
  }
}
