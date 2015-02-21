package ru.spbau.skrivohatskiy.task01;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.spbau.skrivohatskiy.task01.reader.FileMessageReader;
import ru.spbau.skrivohatskiy.task01.reader.IllegalMessageFormatException;
import ru.spbau.skrivohatskiy.task01.writers.CompressingMessageWriter;
import ru.spbau.skrivohatskiy.task01.writers.ConsoleMessageWriter;
import ru.spbau.skrivohatskiy.task01.writers.FileMessageWriter;
import ru.spbau.skrivohatskiy.task01.writers.MessageWriter;

/**
 * 
 * hw1 main class
 * 
 * @see #main(String[])
 * 
 * @author Sergey Krivohatskiy
 * 
 *
 */
public class Main {

    /**
     * Reads messages from input file, compress them and write them into output
     * file or console
     * 
     * @param args
     *            args[0] is an input file name, args[1] is an output file name
     *            if presented
     */
    public static void main(String[] args) {
	if (args.length == 0) {
	    printError("At least one argument required\n"
		    + "Usage: java -jar task1.jar in.file [out.file]");
	    return;
	}
	try (FileMessageReader in = new FileMessageReader(args[0]);
		MessageWriter out = new CompressingMessageWriter(
			getBaseWriter(args))) {
	    while (in.hasNext()) {
		out.writeMessage(in.readMessage());
	    }
	} catch (FileNotFoundException e) {
	    printError("FileNotFoundException: " + e.getMessage());
	} catch (SecurityException e) {
	    printError("SecurityException: " + e.getMessage());
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
