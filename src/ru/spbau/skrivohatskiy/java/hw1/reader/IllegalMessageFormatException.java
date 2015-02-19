package ru.spbau.skrivohatskiy.java.hw1.reader;

/**
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
