package ru.spbau.skrivohatskiy.task01.writers;

import java.io.IOException;
import java.util.ListIterator;

import ru.spbau.skrivohatskiy.task01.Message;

/**
 * Writes messages to console(System.out)
 * 
 * Message format:
 * 
 * <pre>
 * Message n
 * n.1 MessageContent line 1
 * n.1 MessageContent line 2
 * ...
 * n.m MessageContent line m
 * </pre>
 * 
 * where n is a message number and m is a message content size in lines
 * 
 * @author Sergey Krivohatskiy
 *
 */
public class ConsoleMessageWriter implements MessageWriter {
    private int messageIndex = 1;

    @Override
    public void close() throws IOException {
    }

    /**
     * Writes message to console
     */
    @Override
    public void writeMessage(Message msg) {
	System.out.println("Message " + messageIndex);

	ListIterator<String> iter = msg.getLines().listIterator();
	while (iter.hasNext()) {
	    System.out.println(messageIndex + "." + (iter.nextIndex() + 1)
		    + ". " + iter.next());
	}

	messageIndex += 1;
    }

}
