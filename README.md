# jContainer

Lazy and naive container for the dependency injection.

## Installation

### Maven
```xml
<repositories>
    ...
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
    ...
</repositories>
...
<dependency>
    <groupId>com.github.MpStyle</groupId>
    <artifactId>jcontainer</artifactId>
    <version>v1.0.0</version>
</dependency>
```

### Gradle
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

...

dependencies {
    compile 'com.github.MpStyle:jcontainer:v1.0.0'
}

```

## Usages

Simple usage of container:

```java

interface Foo extends Injectable {}

class Dummy implements Injectable {}

class Bar implements Foo {
    private Dummy dummy;

    public Bar(Dummy d){ this.dummy = d; }
}

Container container = new Container();

// add an instance:
container.addInstance(Foo.class, new Bar(new Dummy()));

// or add a definition:
container.addDefinition(Foo.class, Bar.class);

// retrieve an object:
Foo foo =  container.get(Foo.class);

// foo is an instance of Bar, and dummy property of Bar is initialized as an instance of Dummy.

```

### Closure
```java
class Closure implements Callable<Foo> {
    private Dummy dummy;

    public Closure(Dummy dummy) {
      this.dummy = dummy;
    }

    public Foo call() throws Exception {
      return new Bar(dummy);
    }
}

UniqueContainer.getInstance().addClosure(Foo.class, Closure.class);

Foo foo = UniqueContainer.getInstance().get(Foo.class);
```

### Singleton instance

Using the wrapper of singleton instance:

```java

interface Foo extends Injectable {}

class Dummy implements Injectable {}

class Bar implements Foo {
    public Dummy dummy;

    public Bar(Dummy d){ this.dummy = d; }
}

// add an instance:
UniqueContainer.getInstance().addInstance( Foo.class, new Bar(new Dummy()) );

// or add a definition:
UniqueContainer.getInstance().addDefinition( Foo.class, Bar.class );

// retrieve an object:
Foo foo =  UniqueContainer.getInstance().get(Foo.class);

// foo is an instance of Bar, and dummy property of Bar is initialized as an instance of Dummy.
```

### From INI file
```java
File file = new File(...); // or String file = "path_to_file";
Container c = Container.fromIni(file);
```

### From Yaml file
```java
File file = new File(...); // or String file = "path_to_file";
Container c = Container.fromYaml(file);
```

## Version

- 1.0.0