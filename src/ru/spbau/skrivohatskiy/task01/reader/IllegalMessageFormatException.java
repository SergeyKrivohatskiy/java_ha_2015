package ru.spbau.skrivohatskiy.task01.reader;

/**
 * Thrown by {@link FileMessageReader} when file has illegal message format
 * 
 * @see FileMessageReader
 * 
 * @author Sergey Krivohatskiy
 *
 */
public class IllegalMessageFormatException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param message
     *            - the detail message (which is saved for later retrieval by
     *            the Throwable.getMessage() method).
     */
    public IllegalMessageFormatException(String message) {
	super(message);
    }

    /**
     * @param message
     *            - the detail message (which is saved for later retrieval by
     *            the Throwable.getMessage() method).
     * @param cause
     *            - the cause (which is saved for later retrieval by the
     *            Throwable.getCause() method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     */
    public IllegalMessageFormatException(String message, Throwable cause) {
	super(message, cause);
    }

}
