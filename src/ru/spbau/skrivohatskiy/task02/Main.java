package ru.spbau.skrivohatskiy.task02;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import ru.spbau.skrivohatskiy.task02.archiveManager.ArchiveReader;
import ru.spbau.skrivohatskiy.task02.archiveManager.ArchiveWriter;
import ru.spbau.skrivohatskiy.task02.archiveManager.DataPart;

/**
 * 
 * hw2 main class
 * 
 * @see #main(String[])
 * 
 * @author Sergey Krivohatskiy
 * 
 *
 */
public class Main {

    private static final String COMPRESS = "compress";
    private static final String DECOMPRESS = "decompress";
    private static final String PRINT_TREE = "list";

    /**
     * Works with archives
     * 
     * @param args
     *            args[0] is one of the next commands: {@value #COMPRESS},
     *            {@value #DECOMPRESS}, {@value #PRINT_TREE} args[1] is an
     *            archive name if args[0] is {@value #COMPRESS} other args will
     *            be interpreted as files or web pages paths
     * 
     * @see #compress(String, List)
     * @see #decompress(String)
     * @see #printTree(String)
     */
    public static void main(String[] args) {
	if (args.length < 2) {
	    printUsage();
	    return;
	}

	switch (args[0]) {
	case COMPRESS:
	    List<String> pathsList = Arrays.asList(Arrays.copyOfRange(args, 2,
		    args.length));
	    compress(args[1], pathsList);
	    break;
	case DECOMPRESS:
	    decompress(args[1]);
	    break;
	case PRINT_TREE:
	    printTree(args[1]);
	    break;
	default:
	    printUsage();
	    break;
	}

    }

    /**
     * Creates archive file with specified name containing all specified files
     * and web pages
     * 
     * @param archiveFileName
     *            name of archive file to be created
     * @param pathsList
     *            files and web pages paths. Relative paths for files and page
     *            paths started with "http://" for web pages
     * 
     */
    private static void compress(String archiveFileName, List<String> pathsList) {
	try (ArchiveWriter archive = new ArchiveWriter(archiveFileName)) {
	    for (String path : pathsList) {
		DataPart dataPart = new DataPart(path, getDataPart(path));
		archive.writeDataPart(dataPart);
	    }
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
    }

    /**
     * Extracts the content of specified archive file. With relative paths equal
     * to {@link #printTree(String)} output
     * 
     * @param archiveFileName
     *            name of the archive file to extract
     * 
     */
    private static void decompress(String archiveFileName) {
	try (ArchiveReader archive = new ArchiveReader(archiveFileName)) {
	    DataPart part = archive.readNextDataPart();
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * Prints the content of specified archive file in next format
     * 
     * <pre>
     * |_subfolder1
     * |	|_subfolder 1.1
     * |	|	|_a.txt
     * |	|_subfolder 1.2
     * |_http
     * |	|_page1
     * |	|_page2
     * </pre>
     * 
     * ignoring hidden files and directories
     * 
     * @param archiveFileName
     *            name of the archive file to use
     */
    private static void printTree(String archiveFileName) {
	// TODO Auto-generated method stub

    }

    private static void printUsage() {
	// TODO
    }

    private static byte[] getDataPart(String path) throws InvalidPathException,
	    NoSuchFileException, IOException {
	if (path.startsWith("http://")) {
	    // TODO
	    return null;
	} else {
	    return Files.readAllBytes(Paths.get(path));
	}
    }
}
