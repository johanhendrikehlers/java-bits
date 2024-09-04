package com.johanhendrikehlers.queue_number;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

/**
 * The test class for the {@link QueueNumberService} interface implementations.
 * 
 * @author Johan Hendrik Ehlers
 * @since 2024-09-04
 */
class QueueNumberServiceTest {

	public final static int TOTAL_USERS = 100_000_000;

	@Test
	void testBlocking() {
		System.out.println("Blocking");
		System.out.println("BatchSize, Time");
		int batchSize = 1;
		while (batchSize <= 100_000) {
			QueueNumberService service = new QueueNumberServiceBlocking();
			testForGaps(8, batchSize, service);
			batchSize *= 10;
		}
	}

	@Test
	void testNonBlocking() {
		System.out.println("Non Blocking");
		System.out.println("BatchSize, Time");
		int batchSize = 1;
		while (batchSize <= 100_000) {
			QueueNumberService service = new QueueNumberServiceNonBlocking();
			testForGaps(8, batchSize, service);
			batchSize *= 10;
		}
	}

	protected void testForGaps(int numberOfThreads, int batchSize, QueueNumberService service) {

		// task executor
		ExecutorService executor = Executors.newWorkStealingPool(numberOfThreads);

		// setup our queue of users
		int[] users = new int[TOTAL_USERS];
		Arrays.fill(users, -1);

		// start a timer
		long start = System.currentTimeMillis();

		// divide the users into sub queues
		for (int t = 0; t < TOTAL_USERS; t += batchSize) {
			int startAt = t;
			int endAt = t + batchSize;
			executor.submit(() -> {
				for (int i = startAt; i < endAt; i++) {
					users[i] = service.getNumber();
				}
			});
		}

		// shutdown the executor
		executor.shutdown();

		// wait for all tasks to finish
		while (!executor.isTerminated()) {
			// do nothing
		}

		// stop the timer
		long end = System.currentTimeMillis();

		// check for gaps
		int gaps = 0;
		for (int i = 0; i < users.length; i++) {
			if (users[i] == -1) {
				gaps++;
			}
		}
		assertTrue(gaps == 0, "There are " + gaps + " gaps in the sequence.");

		// print csv result
		System.out.println(batchSize + ", " + (end - start));
	}
}