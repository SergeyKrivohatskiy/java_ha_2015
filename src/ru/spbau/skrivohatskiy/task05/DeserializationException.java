/**
 * 
 */
package ru.spbau.skrivohatskiy.task05;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class DeserializationException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     *            the detail message
     * @param cause
     *            the cause
     */
    public DeserializationException(String message, Throwable cause) {
	super(message, cause);
    }

    /**
     * @param message
     *            the detail message
     */
    public DeserializationException(String message) {
	super(message);
    }
}
