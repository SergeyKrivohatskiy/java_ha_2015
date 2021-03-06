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
	    processException(e);
	} catch (IOException e) {
	    processException(e);
	} catch (IllegalMessageFormatException e) {
	    processException(e);
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

    private static void processException(Throwable exception) {
	if (exception instanceof FileNotFoundException) {
	    printError("Specified file not found or unavailable");
	    printError("Please check programm arguments");
	} else if (exception instanceof IOException) {
	    printError("Failed to read or write file");
	} else if (exception instanceof IllegalMessageFormatException) {
	    printError("Failed to read message from input file");
	    printError("Please check file format");
	} else {
	    throw new IllegalArgumentException("Cant process exception",
		    exception);
	}

	printError("Exception message:");
	printError(exception.getMessage());

	if (exception.getSuppressed().length == 0) {
	    return;
	}

	printError("Supressed exceptions:");

	for (Throwable supressedExc : exception.getSuppressed()) {
	    processException(supressedExc);
	}

    }

    private static void printError(String errorMsg) {
	System.out.println(errorMsg);
    }

}
