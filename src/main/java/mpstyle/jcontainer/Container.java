package mpstyle.jcontainer;

import static java.lang.String.format;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import mpstyle.jcontainer.annotation.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Lazy and naive container for the dependency injection.<br />
 * Use the Flyweight design pattern to store a single instance of injectable classes.
 */
public class Container {
    private final static Logger LOGGER = LogManager.getRootLogger();
    private final Map<String, InjectableObjectProps> injectableObjects = new ConcurrentHashMap<String, InjectableObjectProps>();

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
    public <T> Container addDefinition(final Class<T> key, final Class<? extends T> clazz) {
        return this.addDefinition(key, clazz, true);
    }

    public <T> Container addDefinition(final Class<T> key, final Class<? extends T> clazz, boolean isSingleton) {
        injectableObjects.put(
                key.getCanonicalName(),
                new InjectableObjectProps()
                        .setBuilder(new Closure<T>() {
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
                                            LOGGER.debug(e);
                                        }
                                    }
                                }

                                return instance;
                            }
                        })
                        .setSingleton(isSingleton));

        return this;
    }

    /**
     * Add an instance of a <i>T</i>.
     *
     * @param key
     * @param obj
     * @param <T>
     */
    public <T> Container addInstance(Class<T> key, final T obj) {
        InjectableObjectProps props = new InjectableObjectProps()
                .setBuilder(new Closure<T>() {
                    public T call() {
                        return obj;
                    }
                })
                .setSingleton(true);

        injectableObjects.put(key.getCanonicalName(), props);

        return this;
    }

    /**
     * Add a {@link Closure} class to the container.
     *
     * @param key
     * @param closure
     * @param <T>
     */
    public <T> Container addClosure(final Class<T> key, final Class<? extends Closure<T>> closure) {
        return this.addClosure(key, closure, true);
    }

    public <T> Container addClosure(final Class<T> key, final Class<? extends Closure<T>> closure, boolean isSingleton) {
        InjectableObjectProps props = new InjectableObjectProps()
                .setBuilder(new Closure<T>() {
                    public T call() {
                        try {
                            return getInstanceByClass(closure).call();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .setSingleton(isSingleton);

        injectableObjects.put(key.getCanonicalName(), props);

        return this;
    }

    /**
     * Add a {@link Closure} instance to the container.
     *
     * @param key
     * @param closure
     * @param <T>
     */
    public <T> Container addClosure(final Class<T> key, final Closure<T> closure) {
        return addClosure(key, closure, true);
    }

    public <T> Container addClosure(final Class<T> key, final Closure<T> closure, boolean isSingleton) {
        InjectableObjectProps props = new InjectableObjectProps()
                .setBuilder(closure)
                .setSingleton(isSingleton);

        injectableObjects.put(key.getCanonicalName(), props);

        return this;
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
            InjectableObjectProps props = injectableObjects.get(key.getCanonicalName());
            T instance = (T) props.getBuilder().call();

            if (props.isSingleton()) {
                addInstance(key, instance);
            }

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
     * false.
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
     * The magic method: instantiates an object of class <i>clazz</i>.<br>
     * It will try to use each constructor to have the instance. After that, it will set the member marked by
     * {@link Inject} annotation.
     *
     * @param clazz The class to instantiate
     * @param <T>   The type of the class to instantiate
     * @return The instance of class
     */
    private <T> T getInstanceByClass(Class<T> clazz) {
        T instance = null;
        Constructor<T>[] allConstructors = (Constructor<T>[]) clazz.getDeclaredConstructors();
        String canonicalName = clazz.getCanonicalName();

        for (Constructor<T> ctor : allConstructors) {
            try {
                List<Object> parameters = new CopyOnWriteArrayList<Object>();
                Class<?>[] pType = ctor.getParameterTypes();

                for (Class aPType : pType) {
                    parameters.add(get(aPType));
                }

                instance = ctor.newInstance(parameters.toArray());

                break;
            } catch (Exception e) {
                LOGGER.debug(format("Invalid constructor %s", ctor.toString()), e);
            }
        }

        if (instance == null) {
            throw new RuntimeException(format("Error while instantiate type %s", canonicalName));
        }

        return instance;
    }

    private class InjectableObjectProps {
        private Closure builder;
        private boolean isSingleton;

        public Closure getBuilder() {
            return builder;
        }

        public InjectableObjectProps setBuilder(Closure builder) {
            this.builder = builder;
            return this;
        }

        public boolean isSingleton() {
            return isSingleton;
        }

        public InjectableObjectProps setSingleton(boolean singleton) {
            isSingleton = singleton;
            return this;
        }
    }
}
