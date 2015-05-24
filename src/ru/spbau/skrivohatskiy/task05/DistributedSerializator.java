/**
 * 
 */
package ru.spbau.skrivohatskiy.task05;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Sergey Krivohatskiy
 * @param <T>
 *            class to serialize or deserialize
 *
 */
public class DistributedSerializator<T> {

    private static final int DEF_N_THREADS = 10;
    private static final String SERIALIZATION_COMMENTS = "Created by DistributedSerializator";
    // Method names can't contain '-'. So there will be no conflicts with other
    // properties
    private static final String CLASS_NAME_PROP = "--class--";
    private final Set<String> locks = new HashSet<String>();
    private final Map<String, T> deserializedCache = new ConcurrentHashMap<String, T>();
    private final ExecutorService executor;

    /**
     * Create DistributedSerializator
     */
    public DistributedSerializator() {
	executor = Executors.newFixedThreadPool(DEF_N_THREADS);
    }

    private void getLockForName(String name) throws InterruptedException {
	synchronized (locks) {
	    while (locks.contains(name)) {
		locks.wait();
	    }
	    locks.add(name);
	}
    }

    private void unlockByName(String name) {
	synchronized (locks) {
	    assert locks.contains(name);
	    locks.remove(name);
	    locks.notifyAll();
	}
    }

    /**
     * @param o
     *            object to serialize
     * @param name
     *            object name
     * @return true if object is successfully serialized, false if any
     *         IOException or any exception in getters or other serialization
     *         error occures
     */
    public Future<Boolean> serialize(T o, String name) {
	return executor.submit(this.new SerializeTask(o, name));
    }

    /**
     * @param name
     *            object name
     * @return deserialized object
     * @throws DeserializationException
     *             when deserialization is failed because of any IOException or
     *             any exception in setters or other deserialization error
     *             occures
     * @throws InterruptedException
     *             when current thread was interrupted
     */
    public T get(String name) throws DeserializationException,
	    InterruptedException {
	T result = deserializedCache.get(name);
	if (result != null) {
	    return result;
	}
	getLockForName(name);
	try {
	    // Double checking
	    result = deserializedCache.get(name);
	    if (result != null) {
		return result;
	    }
	    return this.new DeserializeTask(name, false).call();
	} finally {
	    unlockByName(name);
	}
    }

    /**
     * @param name
     *            object name
     * @return deserialized instance
     */
    public Future<T> deserialize(String name) {
	return executor.submit(this.new DeserializeTask(name, true));
    }

    private File instanceNameToFile(String name) {
	return new File(name + ".properties");
    }

    private class SerializeTask implements Callable<Boolean> {
	private final T object;
	private final String name;

	public SerializeTask(T object, String name) {
	    this.object = object;
	    this.name = name;
	}

	@Override
	public Boolean call() {
	    try {
		Class<?> clazz = object.getClass();

		Properties props = new Properties();
		props.setProperty(CLASS_NAME_PROP, clazz.getName());

		for (Entry<String, Method> getter : getGetters(clazz)
			.entrySet()) {
		    String val = getter.getValue().invoke(object).toString();
		    props.setProperty(getter.getKey(), val);
		    if (Thread.currentThread().isInterrupted()) {
			return false;
		    }
		}

		getLockForName(name);
		try {
		    File outFile = instanceNameToFile(name);
		    if (!outFile.createNewFile()) {
			outFile.delete();
			outFile.createNewFile();
		    }
		    try (BufferedOutputStream out = new BufferedOutputStream(
			    new FileOutputStream(outFile))) {
			props.store(out, SERIALIZATION_COMMENTS);
			out.flush();
		    }
		    return true;
		} finally {
		    unlockByName(name);
		}
	    } catch (IOException e) {
		return false;
	    } catch (IllegalAccessException e) {
		return false;
	    } catch (IllegalArgumentException e) {
		return false;
	    } catch (InvocationTargetException e) {
		return false;
	    } catch (InterruptedException e) {
		return false;
	    }
	}
    }

    private class DeserializeTask implements Callable<T> {
	private final String name;
	private final boolean takeLock;

	public DeserializeTask(String name, boolean takeLock) {
	    this.name = name;
	    this.takeLock = takeLock;
	}

	@SuppressWarnings("unchecked")
	private T createObject(Class<?> clazz) throws DeserializationException {
	    try {
		return (T) clazz.newInstance();
	    } catch (InstantiationException | IllegalAccessException e) {
		throw new DeserializationException(
			"Can't create instance from the class object", e);
	    }
	}

	@Override
	public T call() throws DeserializationException, InterruptedException {
	    if (takeLock) {
		getLockForName(name);
	    }
	    try (BufferedInputStream in = new BufferedInputStream(
		    new FileInputStream(instanceNameToFile(name)))) {
		Properties props = new Properties();
		props.load(in);
		Class<?> clazz = Class.forName(props
			.getProperty(CLASS_NAME_PROP));

		T result = createObject(clazz);
		Map<String, Method> setters = getSetters(clazz);
		for (String propertyKey : props.stringPropertyNames()) {
		    Method setter = setters.get(propertyKey);
		    if (setter == null) {
			continue;
		    }
		    String val = props.getProperty(propertyKey);
		    setter.invoke(result,
			    stringToClazz(val, setter.getParameterTypes()[0]));
		    if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		    }
		}
		deserializedCache.put(name, result);
		return result;
	    } catch (LinkageError | ClassNotFoundException e) {
		throw new DeserializationException("Class can't be loaded", e);
	    } catch (FileNotFoundException e) {
		throw new DeserializationException("Serialized data not found",
			e);
	    } catch (IOException e) {
		throw new DeserializationException(
			"Can't read serialized data", e);
	    } catch (IllegalAccessException e) {
		throw new DeserializationException(
			"Can't access setter method", e);
	    } catch (IllegalArgumentException e) {
		// Can't be. See stringToInstance
		throw new RuntimeException();
	    } catch (InvocationTargetException e) {
		throw new DeserializationException(
			"Exception happends while invocing setter", e);
	    } finally {
		if (takeLock) {
		    unlockByName(name);
		}
	    }
	}

	private Object stringToClazz(String val, Class<?> class1)
		throws DeserializationException {
	    if (class1.equals(String.class)) {
		return val;
	    }
	    if (class1.equals(char.class)) {
		if (val.length() != 1) {
		    throw new DeserializationException(
			    "Expected string of length 1. Got " + val);
		}
		return val.charAt(0);
	    }
	    try {
		return Array.get(Array.newInstance(class1, 1), 0).getClass()
			.getConstructor(String.class).newInstance(val);
	    } catch (InvocationTargetException e) {
		throw new DeserializationException(
			"Exception happends while parsing property", e);
	    } catch (SecurityException e) {
		throw new DeserializationException("Can't serialize property",
			e);
	    } catch (ArrayIndexOutOfBoundsException
		    | NegativeArraySizeException | NoSuchMethodException
		    | InstantiationException | IllegalAccessException
		    | IllegalArgumentException e) {
		// Can't be
		throw new RuntimeException(e);
	    }
	}
    }

    private final static Map<String, Method> getGetters(Class<?> clazz) {
	Map<String, Method> getters = new HashMap<String, Method>();
	for (Method mtd : clazz.getMethods()) {
	    if (!mtd.getName().startsWith("get")
		    || mtd.getParameterCount() != 0) {
		continue;
	    }
	    Class<?> returnType = mtd.getReturnType();
	    if (!isPrimitiveOrStringClass(returnType)) {
		continue;
	    }
	    getters.put(mtd.getName().substring(3), mtd);
	}
	return getters;
    }

    private final static Map<String, Method> getSetters(Class<?> clazz) {
	Map<String, Method> setters = new HashMap<String, Method>();
	for (Method mtd : clazz.getMethods()) {
	    if (!mtd.getName().startsWith("set")
		    || mtd.getParameterCount() != 1
		    || !isPrimitiveOrStringClass(mtd.getParameterTypes()[0])) {
		continue;
	    }
	    String name = mtd.getName().substring(3);
	    setters.put(name, mtd);
	}
	return setters;
    }

    private static boolean isPrimitiveOrStringClass(Class<?> clazz) {
	return clazz.equals(String.class) || clazz.equals(long.class)
		|| clazz.equals(int.class) || clazz.equals(short.class)
		|| clazz.equals(byte.class) || clazz.equals(char.class)
		|| clazz.equals(boolean.class) || clazz.equals(float.class)
		|| clazz.equals(double.class);
    }
}
