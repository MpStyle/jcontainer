package mpstyle.jcontainer;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mpstyle.jcontainer.annotation.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Lazy and naive container for the dependency injection.
 */
public class Container {
  private final static Logger LOGGER = LogManager.getRootLogger();
  private final Map<String, Closure> injectableObjects = new TreeMap<String, Closure>();

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
  public <T> void addDefinition(final Class<T> key, final Class<? extends T> clazz) {
    validate(key);

    injectableObjects.put(key.getCanonicalName(), new Closure<T>() {
      public T call() {
        T instance = getInstanceByClass(clazz);

        // Don't move this "for" into {#link #getInstanceByClass} method, because it could permit to use Inject
        // annotation in the Callable
        for (Field field : clazz.getDeclaredFields()) {
          if (field.getAnnotation(Inject.class) != null) {

            field.setAccessible(true);

            try {
              field.set(instance, get(field.getType()));
            } catch (IllegalAccessException e) {
              // It is impossible to set the object market with @Inject
            }
          }
        }

        return instance;
      }
    });
  }

  /**
   * Add an instance of a <i>T</i>.
   *
   * @param key
   * @param obj
   * @param <T>
   */
  public <T> void addInstance(Class<T> key, final T obj) {
    validate(key);

    injectableObjects.put(key.getCanonicalName(), new Closure<T>() {
      public T call() {
        return obj;
      }
    });
  }

  /**
   * Add a {@link Closure} class to the container.
   *
   * @param key
   * @param closure
   * @param <T>
   */
  public <T> void addClosure(final Class<T> key, final Class<? extends Closure<T>> closure) {
    validate(key);

    injectableObjects.put(key.getCanonicalName(), new Closure<T>() {
      public T call() {
        try {
          return getInstanceByClass(closure).call();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  /**
   * Add a {@link Closure} instance to the container.
   *
   * @param key
   * @param closure
   * @param <T>
   */
  public <T> void addClosure(final Class<T> key, final Closure<T> closure) {
    validate(key);

    injectableObjects.put(key.getCanonicalName(), new Closure<T>() {
      public T call() {
        try {
          return closure.call();
        } catch (Exception e) {
          LOGGER.debug(e);
          throw new RuntimeException(e);
        }
      }
    });
  }

  /**
   * Return an instance of the class associated to the <i>$key</i>.
   *
   * @param key The class to instantiate
   * @param <T> The type of class to instantiate
   * @return The instance of the class.
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
      LOGGER.debug(e);
      throw new RuntimeException(e);
    }
  }

  /**
   * Check if exists a way for the {@link Container} to instantiate an object of type <i>T</i>.
   * 
   * @param key The {@link Class} of the object
   * @param <T> The object type
   * @return Returns true if exists a way for the {@link Container} to instantiate an object of type <i>T</i>, otherwise
   *         false.
   */
  public <T> boolean existsKey(Class<T> key) {
    return injectableObjects.containsKey(key.getCanonicalName());
  }

  /**
   * Load a container using an INI file with the definitions.<br />
   * Use {@link IniContainer#from(String)} instead. The method will be removed in 2.0.0 version.
   * 
   * @param filePath The file path of the INI file
   * @return
   */
  @Deprecated
  public static Container fromIni(String filePath) {
    return fromIni(new File(filePath));
  }

  /**
   * Load a container using an INI file with the definitions.<br />
   * Use {@link IniContainer#from(File)} instead. The method will be removed in 2.0.0 version.
   * 
   * @param file
   * @return
   */
  @Deprecated
  public static Container fromIni(File file) {
    return IniContainer.from(file);
  }

  /**
   * Load a container using an YAML file with the definitions.<br />
   * Use {@link YamlContainer#from(String)} instead. The method will be removed in 2.0.0 version.
   *
   * @param filePath The file path of the YAML file
   * @return
   */
  @Deprecated
  public static Container fromYaml(String filePath) {
    return fromYaml(new File(filePath));
  }

  /**
   * Load a container using an YAML file with the definitions.<br />
   * Use {@link YamlContainer#from(File)} instead. The method will be removed in 2.0.0 version.
   * 
   * @param file
   * @return
   */
  @Deprecated
  public static Container fromYaml(File file) {
    return YamlContainer.from(file);
  }

  /**
   * If <i>key</i> is not an instance of {@link Injectable} or have not {@link mpstyle.jcontainer.annotation.Injectable}
   * annotation will be throw a {@link NotInjectableException} exception.
   *
   * @param <T> The type of the class to validate
   * @param key The class to validate
   */
  private <T> void validate(Class<T> key) {
    if (!Injectable.class.isAssignableFrom(key)
        && !key.isAnnotationPresent(mpstyle.jcontainer.annotation.Injectable.class)) {
      throw new NotInjectableException(key);
    }
  }

  /**
   * The magic method: instantiates an object of class <i>clazz</i>.<br>
   * It will try to use each constructor to have the instance. After that, it will set the member marked by
   * {@link Inject} annotation.
   * 
   * @param clazz The class to instantiate
   * @param <T> The type of the class to instantiate
   * @return The instance of class
   */
  private <T> T getInstanceByClass(Class<T> clazz) {
    T instance = null;
    Constructor<T>[] allConstructors = (Constructor<T>[]) clazz.getDeclaredConstructors();

    for (Constructor<T> ctor : allConstructors) {
      try {
        List<Object> parameters = new ArrayList<Object>();
        Class<?>[] pType = ctor.getParameterTypes();

        for (Class aPType : pType) {
          parameters.add(get(aPType));
        }

        instance = ctor.newInstance(parameters.toArray());
      } catch (Exception e) {
        LOGGER.debug(e);
        // Try with another construct. The default one could not throw exception.
      }
    }

    return instance;
  }
}
