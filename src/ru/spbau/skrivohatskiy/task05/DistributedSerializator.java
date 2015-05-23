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
    private final Set<String> locks = new HashSet<String>();
    private final Map<String, T> deserializedCache = new ConcurrentHashMap<String, T>();
    private final ExecutorService executor;
    private final Class<?> tClazz;
    private final Map<String, Method> getters = new HashMap<String, Method>();
    private final Map<String, Method> setters = new HashMap<String, Method>();

    /**
     * @param tClazz
     *            T.class that used for building T objects while deserialization
     *            and getting information about objects properties
     */
    public DistributedSerializator(Class<T> tClazz) {
	executor = Executors.newFixedThreadPool(DEF_N_THREADS);
	this.tClazz = tClazz;
	for (Method mtd : tClazz.getMethods()) {
	    if (!mtd.getName().startsWith("get")
		    || mtd.getParameterCount() != 0) {
		continue;
	    }
	    Class<?> returnType = mtd.getReturnType();
	    if (!(returnType.equals(String.class)
		    || returnType.equals(Integer.class)
		    || returnType.equals(Double.class) || returnType
			.equals(Character.class))) {
		continue;
	    }
	    getters.put(mtd.getName().substring(3), mtd);
	}
	for (Method mtd : tClazz.getMethods()) {
	    if (!mtd.getName().startsWith("set")
		    || mtd.getParameterCount() != 1) {
		continue;
	    }
	    String name = mtd.getName().substring(3);
	    Method getter = getters.get(name);
	    // Deserialize only properties serialized with
	    // DistributedSerializator
	    if (getter == null) {
		continue;
	    }
	    if (!getter.getReturnType().equals(mtd.getParameterTypes()[0])) {
		continue;
	    }
	    setters.put(name, mtd);
	}
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
	getLockForName(name);
	try {
	    T result = deserializedCache.get(name);
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
		if (!object.getClass().equals(tClazz)) {
		    return false;
		}

		Properties props = new Properties();

		for (Entry<String, Method> getter : getters.entrySet()) {
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
	private T createObject() throws DeserializationException {
	    try {
		return (T) tClazz.newInstance();
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

		T result = createObject();
		for (Entry<String, Method> setter : setters.entrySet()) {
		    String val = props.getProperty(setter.getKey());
		    Object valToSet = stringToInstance(val, setter.getValue()
			    .getParameterTypes()[0]);
		    setter.getValue().invoke(result, valToSet);
		    if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		    }
		}
		deserializedCache.put(name, result);
		return result;
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

	private Object stringToInstance(String val, Class<?> clazz)
		throws DeserializationException {
	    try {
		if (clazz.equals(String.class)) {
		    return val;
		}
		if (clazz.equals(Integer.class)) {
		    return Integer.valueOf(val);
		}
		if (clazz.equals(Double.class)) {
		    return Double.valueOf(val);
		}
		if (clazz.equals(Character.class)) {
		    if (val.length() != 1) {
			throw new DeserializationException(
				"Invalid data for Character class. "
					+ "Expected a string of length 1");
		    }
		    return Character.valueOf(val.charAt(0));
		}
	    } catch (NumberFormatException e) {
		throw new DeserializationException(
			"Failed to read serialized propertie from " + val
				+ " for " + clazz.getName(), e);
	    }
	    // Should not be. Unprocessed class
	    throw new RuntimeException("Invalid class");
	}
    }
}
