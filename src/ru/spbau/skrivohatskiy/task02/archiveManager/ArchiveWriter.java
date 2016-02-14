/**
 * 
 */
package ru.spbau.skrivohatskiy.task02.archiveManager;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Creates new archive file and writes {@link DataPart}'s to it
 * 
 * @author Sergey Krivohatskiy
 *
 */
public class ArchiveWriter implements Closeable {
    private static final String ZIP_ENTRY_NAME = "Created by ArchiveWriter 0.2b";
    private final ZipOutputStream zipOutStream;
    private final DataOutputStream out;
    private boolean closed = false;

    /**
     * Creates new archive
     * 
     * @param archiveFileName
     *            archive file name
     * @throws FileNotFoundException
     *             if the file exists but is a directory rather than a regular
     *             file, does not exist but cannot be created, or cannot be
     *             opened for any other reason
     * @throws IOException
     *             if the archive writer fails to begin writing a new ZIP entry
     */
    public ArchiveWriter(String archiveFileName) throws IOException {
	zipOutStream = new ZipOutputStream(new BufferedOutputStream(
		new FileOutputStream(archiveFileName)));
	try {
	    zipOutStream.putNextEntry(new ZipEntry(ZIP_ENTRY_NAME));
	} catch (IOException e) {
	    try (ZipOutputStream tmp = zipOutStream) {
		throw e;
	    }
	}
	out = new DataOutputStream(zipOutStream);
    }

    /**
     * Saves data part using this binary format:
     * 
     * <pre>
     * data part 1 key(string in modified UTF-8 encoding {@link DataOutputStream#writeUTF(String)})
     * data part 1 size(4 byte signed integer)
     * data part 1(data part bytes)
     * data part 2 key
     * data part 2 size
     * data part 2
     * </pre>
     * 
     * @param dataPart
     *            data part
     * @throws IOException
     *             if an I/O error occurs
     * 
     */
    public void writeDataPart(DataPart dataPart) throws IOException {
	out.writeUTF(dataPart.key);
	out.writeInt(dataPart.data.length);
	out.write(dataPart.data);
    }

    @Override
    public void close() throws IOException {
	if (closed) {
	    return;
	}
	closed = true;

	// DataOutputStream closes the underlying stream(zipOutStream)
	try (DataOutputStream tmpOut = out) {
	    zipOutStream.closeEntry();
	}
    }
}
