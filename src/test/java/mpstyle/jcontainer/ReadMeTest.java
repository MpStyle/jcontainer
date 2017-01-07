package mpstyle.jcontainer;

public class ReadMeTest {
  interface Foo extends Injectable {
  }

  class Dummy implements Injectable {
  }

  class Bar implements Foo {
    private Dummy dummy;

    public Bar(Dummy d) {
      this.dummy = d;
    }
  }

  public void test_01() {
    Container container = new Container();

    // add an instance:
    container.addInstance(Foo.class, new Bar(new Dummy()));

    // or add a definition:
    container.addDefinition(Foo.class, Bar.class);

    // retrieve an object:
    Foo foo = container.get(Foo.class);
  }

  class DummyClosure implements Closure<Foo> {
    private Dummy dummy;

    public DummyClosure(Dummy dummy) {
      this.dummy = dummy;
    }

    public Foo call() {
      return new Bar(dummy);
    }
  }

  public void test_02() {
    UniqueContainer.getInstance().addClosure(Foo.class, DummyClosure.class);

    Foo foo = UniqueContainer.getInstance().get(Foo.class);
  }

  public void test_03() {
    UniqueContainer.getInstance().addInstance(Foo.class, new Bar(new Dummy()));

    // or add a definition:
    UniqueContainer.getInstance().addDefinition(Foo.class, Bar.class);

    // retrieve an object:
    Foo foo = UniqueContainer.getInstance().get(Foo.class);
  }
}
