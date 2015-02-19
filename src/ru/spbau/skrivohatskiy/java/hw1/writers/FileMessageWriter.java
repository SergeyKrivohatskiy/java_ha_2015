package ru.spbau.skrivohatskiy.java.hw1.writers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import ru.spbau.skrivohatskiy.task1.Message;

/**
 * Writes messages to specified file
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
public class FileMessageWriter implements MessageWriter {

    private final PrintStream out;

    /**
     * @param file
     *            output file
     * @throws FileNotFoundException
     *             If the given file object does not denote an existing,
     *             writable regular file and a new regular file of that name
     *             cannot be created, or if some other error occurs while
     *             opening or creating the file
     */
    public FileMessageWriter(File file) throws FileNotFoundException {
	out = new PrintStream(file);
    }

    /**
     * @param fileName
     *            output file name
     * @throws FileNotFoundException
     *             If the given file name does not denote an existing, writable
     *             regular file and a new regular file of that name cannot be
     *             created, or if some other error occurs while opening or
     *             creating the file
     */
    public FileMessageWriter(String fileName) throws FileNotFoundException {
	this(new File(fileName));
    }

    @Override
    public void close() throws IOException {
	out.close();
    }

    /**
     * Writes message to file
     */
    @Override
    public void writeMessage(Message msg) {
	List<String> msgLines = msg.getLines();
	out.println(msgLines.size());

	for (String line : msgLines) {
	    out.println(line);
	}
    }

}
