package ru.spbau.skrivohatskiy.task1.writers;

import java.io.Closeable;
import java.io.IOException;

import ru.spbau.skrivohatskiy.task1.Message;

/**
 * 
 * @author Sergey Krivohatskiy
 *
 */
public interface MessageWriter extends Closeable {
    /**
     * @param msg
     *            message to write
     * @throws IOException
     *             when an I/O error occurs
     */
    void writeMessage(Message msg) throws IOException;
}
