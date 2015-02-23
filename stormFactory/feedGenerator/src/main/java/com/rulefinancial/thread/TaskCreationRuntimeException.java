package com.rulefinancial.thread;

/**
 * Exception that extends RuntimeException thrown when there are problems during task creating
 * 
 * @author Piotr Gabara
 *
 */
public class TaskCreationRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 2800650270451759356L;
	
	/**
	 * Simple constructor of class that extends a super class constructor
	 * @param throwable
	 */
	public TaskCreationRuntimeException(Throwable throwable) {
		super(throwable);
	}

}
