package ru.spbau.skrivohatskiy.task02;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipException;

import ru.spbau.skrivohatskiy.task02.archiveManager.ArchiveProcessingException;
import ru.spbau.skrivohatskiy.task02.archiveManager.ArchiveReader;
import ru.spbau.skrivohatskiy.task02.archiveManager.ArchiveWriter;
import ru.spbau.skrivohatskiy.task02.archiveManager.DataPart;
import ru.spbau.skrivohatskiy.task02.utils.Utils;

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
    private static final PrintStream DEF_OUT = System.out;

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
	try {
	    switch (args[0]) {
	    case COMPRESS:
		List<String> pathsList = Arrays.asList(Arrays.copyOfRange(args,
			2, args.length));
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
	} catch (SecurityException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (ZipException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (ArchiveProcessingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
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
     * @throws IOException
     * 
     */
    private static void compress(String archiveFileName, List<String> pathsList)
	    throws IOException {
	try (ArchiveWriter archive = new ArchiveWriter(archiveFileName)) {
	    for (String path : pathsList) {
		if (isUrl(path)) {
		    processUrl(archive, path);
		} else {
		    processFsPath(archive, path);
		}
	    }
	}
    }

    /**
     * Extracts the content of specified archive file. With relative paths equal
     * to {@link #printTree(String)} output
     * 
     * @param archiveFileName
     *            name of the archive file to extract
     * @throws IOException
     * @throws ArchiveProcessingException
     * 
     */
    private static void decompress(String archiveFileName) throws IOException,
	    ArchiveProcessingException {
	try (ArchiveReader archive = new ArchiveReader(archiveFileName)) {
	    DataPart part;
	    while ((part = archive.readNextDataPart()) != null) {
		Path fsPath;
		if (isUrl(part.key)) {
		    fsPath = urlToPath(part.key);
		} else {
		    fsPath = Paths.get(part.key);
		}
		Files.createDirectories(fsPath.getParent());
		Files.write(fsPath, part.data, StandardOpenOption.CREATE_NEW);
	    }
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
     * @throws ArchiveProcessingException
     * @throws IOException
     */
    private static void printTree(String archiveFileName) throws IOException,
	    ArchiveProcessingException {
	try (ArchiveReader archive = new ArchiveReader(archiveFileName)) {
	    DirectoryTreeBuilder treeBuilder = new DirectoryTreeBuilder();
	    DataPart part;
	    while ((part = archive.readNextDataPart()) != null) {
		if (isUrl(part.key)) {
		    treeBuilder.addPath(urlToPath(part.key));
		} else {
		    treeBuilder.addPath(Paths.get(part.key).normalize());
		}
	    }
	    treeBuilder.print(DEF_OUT);
	}
    }

    private static void printUsage() {
	DEF_OUT.print("Usage: ");
	DEF_OUT.println("java -jar task02.jar $COMMAND$ $archive_name$ [$file|url$..]");
	String usageFormatLine = "Commands: \'%s\' to compress files and web pages "
		+ "to the new archive file, \'%s\' to extract an existing archive, "
		+ "\'%s\' to print files list of an existing archive";
	DEF_OUT.println(String.format(usageFormatLine, COMPRESS, DECOMPRESS,
		PRINT_TREE));
    }

    private static void processFsPath(ArchiveWriter archive, String path)
	    throws IOException {
	processFile(archive, new File(path));
    }

    private static void processFile(ArchiveWriter archive, File file)
	    throws IOException {
	if (!file.exists()) {
	    DEF_OUT.println(String.format(
		    "Skipping file %s. File do not exists", file.getPath()));
	    return;
	} else if (!file.canRead()) {
	    DEF_OUT.println(String.format(
		    "Skipping file %s. File can't be read by the application",
		    file.getPath()));
	    return;
	}
	if (file.isDirectory()) {
	    for (File dirFile : file.listFiles()) {
		processFile(archive, dirFile);
	    }
	    return;
	}

	byte[] dataBytes = null;
	try {
	    dataBytes = Files.readAllBytes(file.toPath());
	} catch (SecurityException e) {
	    DEF_OUT.println(String
		    .format("Skipping file %s. File can't be red because of java security manager. $s",
			    file.getPath(), e.getMessage()));
	} catch (IOException e) {
	    DEF_OUT.println(String.format(
		    "Skipping file %s. Failed to read the file. %s",
		    file.getPath(), e.getMessage()));
	}

	if (dataBytes != null) {
	    archive.writeDataPart(new DataPart(file.getPath(), dataBytes));
	}
    }

    private static void processUrl(ArchiveWriter archive, String path)
	    throws IOException {
	byte[] dataBytes = null;
	try (InputStream is = (new URL(path)).openStream()) {
	    dataBytes = Utils.readAllBytes(is);
	} catch (MalformedURLException e) {
	    DEF_OUT.println(String.format(
		    "Skipping url %s. Failed to parse the url. %s", path,
		    e.getMessage()));
	} catch (IOException e) {
	    DEF_OUT.println(String.format(
		    "Skipping url %s. Failed to read page content. %s", path,
		    e.getMessage()));
	}

	if (dataBytes != null) {
	    archive.writeDataPart(new DataPart(path, dataBytes));
	}
    }

    private static boolean isUrl(String path) {
	return path.startsWith("http://");
    }

    private static Path urlToPath(String urlStr) {
	return Paths.get("http/"
		+ urlStr.substring("http://".length()).replace('/', '_'));
    }
}
