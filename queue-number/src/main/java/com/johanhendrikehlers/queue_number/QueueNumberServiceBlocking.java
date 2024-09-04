package com.johanhendrikehlers.queue_number;

/**
 * This class is a blocking implementation of the QueueNumberService interface.
 * 
 * @author Johan Hendrik Ehlers
 * @since 2024-09-04
 */
public class QueueNumberServiceBlocking implements QueueNumberService {

	private int count = 0;

	private Object lock = new Object();

	@Override
	public int getNumber() {
		synchronized (lock) {
			return ++count;
		}
	}
}