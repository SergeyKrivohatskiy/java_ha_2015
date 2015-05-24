/**
 * 
 */
package ru.spbau.skrivohatskiy.task05.tests;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.spbau.skrivohatskiy.task05.DeserializationException;
import ru.spbau.skrivohatskiy.task05.DistributedSerializator;

/**
 * @author Sergey Krivohatskiy
 *
 */
@SuppressWarnings("javadoc")
public class SerializatorTests {

    private static final String TEST_INSTANCE_NAME = "test name";
    private static final int TEST_FILE_MAX_INDEX = 10;
    private static final int THREADS_COUNT = 20;
    private static final int ITERATIONS_PER_THREAD = 1000;

    @BeforeClass
    @AfterClass
    public static void removePropFiles() {
	new File(TEST_INSTANCE_NAME + ".properties").delete();
	for (int i = 0; i < TEST_FILE_MAX_INDEX; i++) {
	    new File(TEST_INSTANCE_NAME + i + ".properties").delete();
	}
    }

    @Test
    public void testSimpleSerialization() throws InterruptedException,
	    ExecutionException, DeserializationException {
	DistributedSerializator<SerializableTestClass> serializator = new DistributedSerializator<SerializableTestClass>();
	SerializableTestClass t = new SerializableTestClass(1321,
		"Correct String");

	serialise_and_check(serializator, t);
    }

    @Test
    public void stressTest() throws InterruptedException {
	ExecutorService executor = Executors.newFixedThreadPool(THREADS_COUNT);
	DistributedSerializator<SerializableTestClass> serializator = new DistributedSerializator<SerializableTestClass>();
	AtomicInteger successCounter = new AtomicInteger();
	for (int i = 0; i < THREADS_COUNT; i++) {
	    final String threadIdx = Integer.toString(i);
	    executor.execute(new Runnable() {

		@Override
		public void run() {
		    try {
			for (int i = 0; i < ITERATIONS_PER_THREAD; i++) {
			    int testFileIdx = ThreadLocalRandom.current()
				    .nextInt(TEST_FILE_MAX_INDEX);
			    SerializableTestClass t = new SerializableTestClass(
				    i, threadIdx);

			    Assert.assertTrue(serializator.serialize(t,
				    TEST_INSTANCE_NAME + testFileIdx).get());

			    Assert.assertNotNull(serializator
				    .get(TEST_INSTANCE_NAME + testFileIdx));

			    SerializableTestClass deserialized = serializator
				    .deserialize(
					    TEST_INSTANCE_NAME + testFileIdx)
				    .get();

			    if (deserialized.getStrVal().equals(threadIdx)) {
				Assert.assertEquals(t, deserialized);
			    } else {
				Assert.assertTrue(deserialized.getIntVal() < ITERATIONS_PER_THREAD);
				Assert.assertTrue(Integer.valueOf(deserialized
					.getStrVal()) < THREADS_COUNT);
			    }
			}
		    } catch (InterruptedException | ExecutionException
			    | DeserializationException e) {
			e.printStackTrace();
			Assert.fail("Unexpected exception while running stress test");
		    }
		    successCounter.incrementAndGet();
		}
	    });
	}
	executor.shutdown();
	Assert.assertTrue(executor.awaitTermination(40, TimeUnit.SECONDS));
	Assert.assertEquals(THREADS_COUNT, successCounter.get());
    }

    private void serialise_and_check(
	    DistributedSerializator<SerializableTestClass> serializator,
	    SerializableTestClass t) throws InterruptedException,
	    ExecutionException, DeserializationException {
	Assert.assertTrue(serializator.serialize(t, TEST_INSTANCE_NAME).get());

	SerializableTestClass deserialized = serializator
		.get(TEST_INSTANCE_NAME);

	Assert.assertEquals(t, deserialized);
    }
}
