package ru.spbau.skrivohatskiy.java.hw1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Sergey Krivohatskiy
 *
 */
public class Message {
    private final List<String> lines;

    /**
     * Creates a message with content
     * 
     * @param lines
     *            message content
     */
    public Message(List<String> lines) {
	this.lines = new ArrayList<>(lines);
    }

    /**
     * Creates an empty message
     */
    public Message() {
	this(Collections.emptyList());
    }

    /**
     * Appends other message content to this message content
     * 
     * @param msg
     *            other message
     */
    public void append(Message msg) {
	lines.addAll(msg.getLines());
    }

    /**
     * @return this message content
     */
    public List<String> getLines() {
	return Collections.unmodifiableList(lines);
    }
}
