package ru.spbau.skrivohatskiy.java.hw1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message {
    private final List<String> lines;

    public Message(List<String> lines) {
	this.lines = new ArrayList<>(lines);
    }

    public Message() {
	this(Collections.emptyList());
    }

    public void append(Message msg) {
	lines.addAll(msg.getLines());
    }

    public List<String> getLines() {
	return Collections.unmodifiableList(lines);
    }
}
