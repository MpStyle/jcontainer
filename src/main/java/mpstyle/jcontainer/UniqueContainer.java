package mpstyle.jcontainer;

public class UniqueContainer {
  private static Container instance = null;

  public static synchronized Container getInstance() {
    if (instance == null) {
      instance = new Container();
    }

    return instance;
  }

  public static synchronized Container getInstance(boolean autoInstance) {
    if (instance == null) {
      instance = new Container(autoInstance);
    }

    return instance;
  }
}
