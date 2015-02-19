package ru.spbau.skrivohatskiy.java.hw1.writers;

import java.io.IOException;
import java.util.ListIterator;

import ru.spbau.skrivohatskiy.java.hw1.Message;

/**
 * Writes messages to console(System.out)
 * 
 * @author Sergey Krivohatskiy
 *
 */
public class ConsoleMessageWriter implements MessageWriter {
    private int messageIndex = 1;

    @Override
    public void close() throws IOException {
    }

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
