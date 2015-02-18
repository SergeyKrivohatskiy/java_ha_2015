package ru.spbau.skrivohatskiy.java.hw1.writers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import ru.spbau.skrivohatskiy.java.hw1.Message;

public class FileMessageWriter implements MessageWriter {

    private final PrintStream out;

    public FileMessageWriter(File file) throws FileNotFoundException {
	out = new PrintStream(file);
    }

    public FileMessageWriter(String fileName) throws FileNotFoundException {
	this(new File(fileName));
    }

    @Override
    public void close() throws IOException {
	out.close();
    }

    @Override
    public void writeMessage(Message msg) {
	List<String> msgLines = msg.getLines();
	out.println(msgLines.size());

	for (String line : msgLines) {
	    out.println(line);
	}
    }

}
