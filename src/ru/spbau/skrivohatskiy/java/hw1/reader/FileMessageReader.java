package ru.spbau.skrivohatskiy.java.hw1.reader;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.spbau.skrivohatskiy.java.hw1.Message;

public class FileMessageReader implements Closeable {
    private final BufferedReader in;
    private Message nextMsg;

    public FileMessageReader(File file) throws IllegalMessageFormatException,
	    IOException {
	in = new BufferedReader(new FileReader(file));
	try {
	    nextMsg = next();
	} catch (Exception e) {
	    close();
	    throw e;
	}
    }

    public FileMessageReader(String fileName)
	    throws IllegalMessageFormatException, IOException {
	this(new File(fileName));
    }

    public Message readMessage() throws IllegalMessageFormatException,
	    IOException {
	Message old = nextMsg;
	nextMsg = next();
	return old;
    }

    @Override
    public void close() throws IOException {
	in.close();
    }

    public boolean hasNext() {
	return nextMsg != null;
    }

    public Message next() throws IllegalMessageFormatException, IOException {
	try {
	    String linesCountStr = in.readLine();
	    if (linesCountStr == null) {
		return null;
	    }
	    int linesCount = Integer.parseInt(linesCountStr);
	    if (linesCount < 0) {
		throw new IllegalMessageFormatException(
			"Message lines count < 0");
	    }

	    List<String> msgLines = new ArrayList<>(linesCount);
	    for (int i = 0; i < linesCount; i++) {
		String line = in.readLine();
		if (line == null) {
		    throw new IllegalMessageFormatException(
			    "Message lines count < available lines in file");
		}
		msgLines.add(line);
	    }

	    return new Message(msgLines);

	} catch (NumberFormatException e) {
	    throw new IllegalMessageFormatException(
		    "NumberFormatException: Expected lines count(Integer type)");
	}
    }
}
