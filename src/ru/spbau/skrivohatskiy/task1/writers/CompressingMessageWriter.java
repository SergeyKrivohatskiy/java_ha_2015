package ru.spbau.skrivohatskiy.task1.writers;

import java.io.IOException;

import ru.spbau.skrivohatskiy.task1.Message;

/**
 * Compress two messages into one and write it using other {@link MessageWriter}
 * If there is uncompressed message when closing uncompressed message will be
 * written as is
 * 
 * @author Sergey Krivohatskiy
 *
 */
public class CompressingMessageWriter implements MessageWriter {

    private final MessageWriter baseWriter;
    private Message firstMsg = null;

    /**
     * @param baseWriter
     *            writer that'll be used to write compressed messages
     */
    public CompressingMessageWriter(MessageWriter baseWriter) {
	this.baseWriter = baseWriter;
    }

    /**
     * If there is uncompressed message when closing uncompressed message will
     * be written as is
     */
    @Override
    public void close() throws IOException {
	if (firstMsg != null) {
	    baseWriter.writeMessage(firstMsg);
	}
	baseWriter.close();
    }

    /**
     * Compress two messages into one and write it using other
     * {@link MessageWriter}
     */
    @Override
    public void writeMessage(Message msg) throws IOException {
	if (firstMsg == null) {
	    firstMsg = msg;
	    return;
	}
	firstMsg.append(msg);
	baseWriter.writeMessage(firstMsg);
	firstMsg = null;
    }

}
