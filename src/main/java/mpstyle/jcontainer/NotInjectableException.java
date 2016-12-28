package mpstyle.jcontainer;

class NotInjectableException extends RuntimeException {
  public NotInjectableException(Class clazz) {
    super(
        String
            .format(
                "%s class can not be instantiated by the container: does it extend Injectable? is the %s a base class of the required object?",
                clazz.getCanonicalName(), clazz.getCanonicalName()));
  }
}
