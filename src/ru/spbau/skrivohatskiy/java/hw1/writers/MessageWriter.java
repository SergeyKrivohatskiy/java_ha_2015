package ru.spbau.skrivohatskiy.java.hw1.writers;

import java.io.Closeable;

import ru.spbau.skrivohatskiy.task1.Message;

/**
 * 
 * @author Sergey Krivohatskiy
 *
 */
public interface MessageWriter extends Closeable {
    /**
     * Writes message to somewhere
     * 
     * @param msg
     *            message to write
     */
    void writeMessage(Message msg);
}
