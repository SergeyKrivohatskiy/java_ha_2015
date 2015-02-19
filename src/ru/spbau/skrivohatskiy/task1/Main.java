package ru.spbau.skrivohatskiy.task1;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.spbau.skrivohatskiy.java.hw1.reader.FileMessageReader;
import ru.spbau.skrivohatskiy.java.hw1.reader.IllegalMessageFormatException;
import ru.spbau.skrivohatskiy.java.hw1.writers.CompressingMessageWriter;
import ru.spbau.skrivohatskiy.java.hw1.writers.ConsoleMessageWriter;
import ru.spbau.skrivohatskiy.java.hw1.writers.FileMessageWriter;
import ru.spbau.skrivohatskiy.java.hw1.writers.MessageWriter;

/**
 * 
 * @author Sergey Krivohatskiy
 * 
 *         hw1 main class
 *
 */
public class Main {

    /**
     * Reads messages from input file, compress them and write them into output
     * file or console
     * 
     * @param args
     *            args[0] is an input file name, args[1] is output file name if
     *            presented
     */
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
