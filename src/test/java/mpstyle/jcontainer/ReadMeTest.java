package mpstyle.jcontainer;

import java.util.concurrent.Callable;

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

  class Closure implements Callable<Foo> {
    private Dummy dummy;

    public Closure(Dummy dummy) {
      this.dummy = dummy;
    }

    public Foo call() throws Exception {
      return new Bar(dummy);
    }
  }

  public void test_02() {
    UniqueContainer.getInstance().addClosure(Foo.class, Closure.class);

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
