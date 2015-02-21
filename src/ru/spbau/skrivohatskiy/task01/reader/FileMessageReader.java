package ru.spbau.skrivohatskiy.task01.reader;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.spbau.skrivohatskiy.task01.Message;

/**
 * Reads messages one by one from specified file
 * 
 * Message format:
 * 
 * <pre>
 * n
 * MessageContent line 1
 * MessageContent line 2
 * ...
 * MessageContent line n
 * </pre>
 * 
 * where n is a message content size in lines
 * 
 * @author Sergey Krivohatskiy
 *
 */
public class FileMessageReader implements Closeable {
    private final BufferedReader in;

    /**
     * @param file
     *            input file
     * @throws FileNotFoundException
     *             if the file does not exist, is a directory rather than a
     *             regular file, or for some other reason cannot be opened for
     *             reading.
     */
    public FileMessageReader(File file) throws FileNotFoundException {
	in = new BufferedReader(new FileReader(file));
    }

    /**
     * @param fileName
     *            input file name
     * @throws FileNotFoundException
     *             if the file does not exist, is a directory rather than a
     *             regular file, or for some other reason cannot be opened for
     *             reading.
     */
    public FileMessageReader(String fileName) throws FileNotFoundException {
	this(new File(fileName));
    }

    /**
     * @return red message
     * @throws IllegalMessageFormatException
     *             if next message can't be red
     * @throws IOException
     *             if IOException occurred while reading next message
     */
    public Message readMessage() throws IllegalMessageFormatException,
	    IOException {
	try {
	    String linesCountStr = in.readLine();
	    if (linesCountStr == null) {
		throw new IllegalStateException(
			"Reading when reader hasn't message available");
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
			    "Message lines count > available lines in file");
		}
		msgLines.add(line);
	    }

	    return new Message(msgLines);

	} catch (NumberFormatException e) {
	    throw new IllegalMessageFormatException(
		    "Expected lines count(Integer type)");
	}
    }

    @Override
    public void close() throws IOException {
	in.close();
    }

    /**
     * @return true if there is a message available to read
     * @throws IOException
     *             if IOException occurred while checking File for data
     */
    public boolean hasNext() throws IOException {
	return in.ready();
    }
}
