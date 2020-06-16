# jContainer

Lazy and naive container for the dependency injection for Android and Java 8 and above.
Use the flyweight design pattern to store a single instance of injectable classes.

**Last version:** 3.1.1

**License:** MIT

**Continuous Integration:** [![Build Status](https://travis-ci.org/MpStyle/jcontainer.svg?branch=master)](https://travis-ci.org/MpStyle/jcontainer) [![](https://jitpack.io/v/MpStyle/jcontainer.svg)](https://jitpack.io/#MpStyle/jcontainer)

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
    <version>v3.1.0</version>
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
    compile 'com.github.MpStyle:jcontainer:v3.1.0'
}

```

## Usages

Simple usage of container:

```java

interface Foo {}

class Dummy {}

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

### Container with auto-instance

Using:

```java
Container container = new Container(true);
```

The container can not instantiate an object if there is not a definition for it. 

### Annotation

```java

public class ServiceF {
  private final String test;

  public ServiceF() {
    test = "Hello world!";
  }

  public String getTest() {
    return test;
  }
}

public class ServiceE {
  @Inject
  private ServiceF serviceF;

  public ServiceE() {

  }

  public ServiceF getServiceF() {
    return serviceF;
  }
}

Container c = new Container();
ServiceE serviceE = c.get(ServiceE.class);

```

### Closure

It is possible to add a Callable which wraps the logic of instantiation of an object:

```java
class DummyClosure implements Closure<Foo> {
    private Dummy dummy;

    public DummyClosure(Dummy dummy) {
      this.dummy = dummy;
    }

    public Foo call() {
      return new Bar(dummy);
    }
}

UniqueContainer.getInstance().addClosure(Foo.class, DummyClosure.class);

Foo foo = UniqueContainer.getInstance().get(Foo.class);
```

### Singleton instance

Using the wrapper of singleton instance:

```java

interface Foo {}

class Dummy {}

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
Container c = IniContainer.from(file);
```

### From Yaml file
```java
File file = new File(...); // or String file = "path_to_file";
Container c = YamlContainer.from(file);
```

## Release a new version

Steps:
* Change version in pom.xml file
* Change version in README.md file
* Check the build result on [Travis CI](https://travis-ci.org/github/MpStyle/jcontainer)
* Draft a new release in GitHub
* Check the build on [JitPack](https://jitpack.io/#MpStyle/jcontainer)