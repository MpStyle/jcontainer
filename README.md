# jContainer

Lazy and naive container for the dependency injection.
Use the flyweight design pattern to store a single instance of injectable classes.

[![Build Status](https://travis-ci.org/MpStyle/jcontainer.svg?branch=master)](https://travis-ci.org/MpStyle/jcontainer) [![](https://jitpack.io/v/MpStyle/jcontainer.svg)](https://jitpack.io/#MpStyle/jcontainer)

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
    <version>v3.0.2</version>
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
    compile 'com.github.MpStyle:jcontainer:v2.0.0'
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

## Version

### 2.0.0:
- Change signatures of addClosure methods.

### 1.1.0:
- Created the new loader classes IniContainer and YamlContainer, to load a Container from INI and YAML files.
- Deprecated Container#fromIni(String), Container#fromIni(File), Container#fromYaml(String) and Container#fromYaml(File) methods (they will be removed from 2.0.0 version).
- Improved JavaDocs
- Improved performance and security
- Added support to inject using annotations.

### 1.0.0
