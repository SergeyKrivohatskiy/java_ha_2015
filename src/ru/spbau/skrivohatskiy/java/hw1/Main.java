package ru.spbau.skrivohatskiy.java.hw1;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.spbau.skrivohatskiy.java.hw1.reader.FileMessageReader;
import ru.spbau.skrivohatskiy.java.hw1.reader.IllegalMessageFormatException;
import ru.spbau.skrivohatskiy.java.hw1.writers.CompressingMessageWriter;
import ru.spbau.skrivohatskiy.java.hw1.writers.ConsoleMessageWriter;
import ru.spbau.skrivohatskiy.java.hw1.writers.FileMessageWriter;
import ru.spbau.skrivohatskiy.java.hw1.writers.MessageWriter;

public class Main {

    public static void main(String[] args) {
	try (FileMessageReader in = new FileMessageReader(args[0]);
		MessageWriter out = new CompressingMessageWriter(
			getBaseWriter(args))) {
	    while (in.hasNext()) {
		out.writeMessage(in.readMessage());
	    }
	} catch (FileNotFoundException e) {
	    printError("FileNotFoundException: " + e.getMessage());
	} catch (IOException e) {
	    printError("IOException: " + e.getMessage());
	} catch (IllegalMessageFormatException e) {
	    printError("IllegalMessageFormatException: " + e.getMessage());
	}
    }

    private static MessageWriter getBaseWriter(String[] args)
	    throws FileNotFoundException {
	if (args.length >= 2) {
	    return new FileMessageWriter(args[1]);
	} else {
	    return new ConsoleMessageWriter();
	}
    }

    private static void printError(String errorMsg) {
	System.out.println(errorMsg);
    }

}
