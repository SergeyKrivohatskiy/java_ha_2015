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
     *            exception message
     */
    public IllegalMessageFormatException(String message) {
	super(message);
    }

}
