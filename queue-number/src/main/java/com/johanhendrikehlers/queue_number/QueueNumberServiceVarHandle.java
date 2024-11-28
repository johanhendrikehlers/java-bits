package com.johanhendrikehlers.queue_number;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * This class is a blocking implementation of the QueueNumberService interface
 * but rather than using the synchronized keyword I lets use the VarHandle API
 * introduced in Java 9.
 * 
 * @author Johan Hendrik Ehlers
 * @since 2024-09-04
 */
public class QueueNumberServiceVarHandle implements QueueNumberService {

	@SuppressWarnings("unused")
	private volatile int counter = 1;
	private static final VarHandle COUNTER_HANDLE;

	static {
		try {
			COUNTER_HANDLE = MethodHandles.lookup().findVarHandle(QueueNumberServiceVarHandle.class, "counter",
					int.class);
		} catch (ReflectiveOperationException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	@Override
	public int getNumber() {
		return (int) COUNTER_HANDLE.getAndAdd(this, 1);
	}
}