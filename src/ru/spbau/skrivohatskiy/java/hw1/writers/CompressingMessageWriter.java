package ru.spbau.skrivohatskiy.java.hw1.writers;

import java.io.IOException;

import ru.spbau.skrivohatskiy.java.hw1.Message;

public class CompressingMessageWriter implements MessageWriter {

    private final MessageWriter baseWriter;
    private Message firstMsg = null;

    public CompressingMessageWriter(MessageWriter baseWriter) {
	this.baseWriter = baseWriter;
    }

    @Override
    public void close() throws IOException {
	if (firstMsg != null) {
	    baseWriter.writeMessage(firstMsg);
	}
	baseWriter.close();
    }

    @Override
    public void writeMessage(Message msg) {
	if (firstMsg == null) {
	    firstMsg = msg;
	    return;
	}
	firstMsg.append(msg);
	baseWriter.writeMessage(firstMsg);
	firstMsg = null;
    }

}
