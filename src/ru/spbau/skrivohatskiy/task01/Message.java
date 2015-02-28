package ru.spbau.skrivohatskiy.task01;

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
     * Creates copy of other message
     * 
     * @param msg
     *            message to copy
     */
    public Message(Message msg) {
	this.lines = new ArrayList<>(msg.lines);
    }

    /**
     * Appends content of other message to content of this message
     * 
     * @param msg
     *            other message
     */
    public void append(Message msg) {
	lines.addAll(msg.lines);
    }

    /**
     * @return this message content as unmodifiable list
     */
    public List<String> getLines() {
	return Collections.unmodifiableList(lines);
    }
}
