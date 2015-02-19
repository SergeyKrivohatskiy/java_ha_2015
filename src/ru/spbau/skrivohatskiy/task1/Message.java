package ru.spbau.skrivohatskiy.task1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Message consists of message content what is an array of {@link String}'s
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
     * Appends content of other message to content of this message
     * 
     * @param msg
     *            other message
     */
    public void append(Message msg) {
	lines.addAll(msg.getLines());
    }

    /**
     * @return this message content as unmodifiable list
     */
    public List<String> getLines() {
	return Collections.unmodifiableList(lines);
    }
}