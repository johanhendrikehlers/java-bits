package com.johanhendrikehlers.queue_number;

/**
 * Issues a globally unique number to visitors in a ascending order starting
 * from 1.
 * 
 * @author Johan Hendrik Ehlers
 * @since 2024-09-04
 */
public interface QueueNumberService {

	public int getNumber();

}