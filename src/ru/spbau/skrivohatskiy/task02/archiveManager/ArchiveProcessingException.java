/**
 * 
 */
package ru.spbau.skrivohatskiy.task02.archiveManager;

/**
 * TODO
 * 
 * @author Sergey Krivohatskiy
 *
 */
public class ArchiveProcessingException extends Exception {

    private static final long serialVersionUID = 3685182841591259665L;

    /**
     * @param message
     *            the detail message
     * @param cause
     *            the cause
     */
    public ArchiveProcessingException(String message, Throwable cause) {
	super(message, cause);
    }

    /**
     * @param message
     *            the detail message
     */
    public ArchiveProcessingException(String message) {
	super(message);
    }

}
