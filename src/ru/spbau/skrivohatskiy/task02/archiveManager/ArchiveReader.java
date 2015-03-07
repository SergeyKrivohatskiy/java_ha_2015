/**
 * 
 */
package ru.spbau.skrivohatskiy.task02.archiveManager;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.zip.ZipInputStream;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class ArchiveReader implements Closeable {

    private final ZipInputStream zipInStream;
    private final DataInputStream in;
    private boolean closed = false;

    /**
     * Reads existing archive created with
     * 
     * @param archiveFileName
     *            archive file name
     * @throws IOException
     *             TODO
     */
    public ArchiveReader(String archiveFileName) throws IOException {
	zipInStream = new ZipInputStream(new BufferedInputStream(
		new FileInputStream(archiveFileName)));
	if (zipInStream.getNextEntry() == null) {
	    // TODO
	}
	in = new DataInputStream(zipInStream);

    }

    /**
     * @throws UTFDataFormatException
     *             if the archive contents path with an invalid modified UTF-8
     *             encoding of a string
     * @throws IOException
     *             if any I/O error occurs
     * @return next data part from archive or null if EOF reached
     * @throws ArchiveProcessingException
     *             if the archive contents invalid data format
     */
    public DataPart readNextDataPart() throws UTFDataFormatException,
	    IOException, ArchiveProcessingException {
	String key = null;
	try {
	    key = in.readUTF();
	    int dataLen = in.readInt();
	    byte[] data = new byte[dataLen];
	    int dataReaded = in.read(data);
	    if (dataReaded < dataLen) {
		throw new ArchiveProcessingException(
			"Failed to read data array. Expected " + dataLen
				+ " bytes, readed " + dataReaded);
	    }
	    return new DataPart(key, data);
	} catch (EOFException e) {
	    if (key == null) { // EOF when reading key == normal EOF
		return null;
	    }
	    // EOF when reading dataLen == invalid format
	    throw new ArchiveProcessingException(
		    "Failed to read data array sie: unexpected EOF", e);
	}
    }

    @Override
    public void close() throws IOException {
	if (closed) {
	    return;
	}
	closed = true;

	try (ZipInputStream zipInTmp = zipInStream; DataInputStream inTmp = in) {
	    zipInStream.closeEntry();
	}

    }
}
