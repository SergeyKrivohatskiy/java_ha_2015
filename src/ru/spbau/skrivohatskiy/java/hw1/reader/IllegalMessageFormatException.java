package ru.spbau.skrivohatskiy.java.hw1.reader;

public class IllegalMessageFormatException extends Exception {

    private static final long serialVersionUID = 1L;

    public IllegalMessageFormatException(String message) {
	super(message);
    }

}
