package com.johanhendrikehlers.queue_number;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is a non blocking implementation of the QueueNumberService
 * interface.
 * 
 * @author Johan Hendrik Ehlers
 * @since 2024-09-04
 */
public class QueueNumberServiceNonBlocking implements QueueNumberService {

	private AtomicInteger count = new AtomicInteger(0);

	@Override
	public int getNumber() {
		return count.incrementAndGet();
	}
}