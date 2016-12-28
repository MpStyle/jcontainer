package mpstyle.jcontainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import org.ini4j.Profile;
import org.ini4j.Wini;
import org.yaml.snakeyaml.Yaml;

/**
 * Lazy and naive container for the dependency injection.
 */
public class Container {
  private final Map<String, Callable> injectableObjects = new TreeMap<String, Callable>();

  /**
   * Removes all defined instances and definitions.
   */
  public void clear() {
    injectableObjects.clear();
  }

  /**
   * Add a class definition
   * 
   * @param key
   * @param clazz
   * @param <T>
   */
  public <T> void addDefinition(Class<T> key, final Class<? extends T> clazz) {
    if (!Injectable.class.isAssignableFrom(key)) {
      throw new NotInjectableException(key);
    }

    injectableObjects.put(key.getCanonicalName(), new Callable<T>() {
      public T call() throws Exception {
        return getInstanceByClass(clazz);
      }
    });
  }

  /**
   * Add an instance of a object.
   *
   * @param key
   * @param obj
   * @param <T>
   */
  public <T> void addInstance(Class<T> key, final T obj) {
    if (!(obj instanceof Injectable)) {
      throw new NotInjectableException(key);
    }

    injectableObjects.put(key.getCanonicalName(), new Callable<T>() {
      public T call() throws Exception {
        return obj;
      }
    });
  }

  /**
   * Add a Closure to the container.
   *
   * @param key
   * @param closure
   * @param <T>
   */
  public <T> void addClosure(Class<T> key, final Class<? extends Callable<T>> closure) {
    if (!Injectable.class.isAssignableFrom(key)) {
      throw new NotInjectableException(key);
    }

    injectableObjects.put(key.getCanonicalName(), new Callable<T>() {
      public T call() throws Exception {
        return getInstanceByClosure(closure);
      }
    });
  }

  /**
   * Return an instance of the class associated to the <i>$key</i>.
   *
   * @param key
   * @param <T>
   * @return
   */
  public <T> T get(Class<T> key) {
    if (!existsKey(key)) {
      addDefinition(key, key);
    }

    try {
      T instance = (T) injectableObjects.get(key.getCanonicalName()).call();

      addInstance(key, instance);

      return instance;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   *
   * @param key
   * @param <T>
   * @return
   */
  public <T> boolean existsKey(Class<T> key) {
    return injectableObjects.containsKey(key.getCanonicalName());
  }

  public static Container fromIni(String filename) {
    return fromIni(new File(filename));
  }

  public static Container fromIni(File file) {
    try {
      Wini ini = new Wini(file);
      Container c = new Container();

      for (String sectionName : ini.keySet()) {
        Profile.Section section = ini.get(sectionName);
        for (String optionKey : section.keySet()) {
          Class key = Class.forName(optionKey);
          Class clazz = Class.forName(section.get(optionKey));
          c.addDefinition(key, clazz);
        }
      }

      return c;
    } catch (Exception e) {
      return null;
    }
  }

  public static Container fromYaml(String filename) {
    return fromYaml(new File(filename));
  }

  public static Container fromYaml(File file) {
    try {
      InputStream input = new FileInputStream(file);
      Yaml yaml = new Yaml();
      Map<String, String> map = (Map<String, String>) yaml.load(input);
      Container c = new Container();

      for (Map.Entry<String, String> entry : map.entrySet()) {
        Class key = Class.forName(entry.getKey());
        Class clazz = Class.forName(entry.getValue());
        c.addDefinition(key, clazz);
      }

      return c;
    } catch (Exception e) {
      return null;
    }
  }

  private <T> T getInstanceByClosure(Class<? extends Callable<T>> closure) throws Exception {
    Callable<T> callable = getInstanceByClass(closure);
    return callable.call();
  }

  private <T> T getInstanceByClass(Class<T> clazz) {
    T instance = null;
    Constructor<T>[] allConstructors = (Constructor<T>[]) clazz.getDeclaredConstructors();
    for (Constructor<T> ctor : allConstructors) {
      try {
        List<Object> parameters = new ArrayList<Object>();
        Class<?>[] pType = ctor.getParameterTypes();
        for (Class<?> aPType : pType) {
          parameters.add(get(aPType));
        }

        instance = ctor.newInstance(parameters.toArray());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return instance;
  }
}
