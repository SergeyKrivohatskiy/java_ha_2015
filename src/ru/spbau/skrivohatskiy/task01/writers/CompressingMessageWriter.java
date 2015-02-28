package ru.spbau.skrivohatskiy.task01.writers;

import java.io.IOException;

import ru.spbau.skrivohatskiy.task01.Message;

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
     * True it there wasn't any exceptions when writing messages
     */
    private boolean good = true;

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
	try {
	    if (good && firstMsg != null) {
		baseWriter.writeMessage(firstMsg);
	    }
	} finally {
	    baseWriter.close();
	}
    }

    /**
     * Compress two messages into one and write it using other
     * {@link MessageWriter}
     */
    @Override
    public void writeMessage(Message msg) throws IOException {
	if (firstMsg == null) {
	    firstMsg = new Message(msg);
	    return;
	}
	firstMsg.append(msg);
	try {
	    baseWriter.writeMessage(firstMsg);
	} catch (Throwable anyExc) {
	    good = false;
	    throw anyExc;
	}
	firstMsg = null;
    }

}
