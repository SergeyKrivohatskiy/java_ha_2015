/**
 * 
 */
package ru.spbau.skrivohatskiy.task02.archiveManager;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class ArchiveReader implements Closeable {

    private final ZipInputStream zipInStream;
    private final DataInputStream in;

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
     * @return TODO
     * @throws IOException
     *             TODO
     */
    public DataPart readNextDataPart() throws IOException {
	// TODO error processing
	String key = in.readUTF();
	int dataLen = in.readInt();
	byte[] data = new byte[dataLen];
	int dataReaded = in.read(data);
	return new DataPart(key, data);
    }

    @Override
    public void close() throws IOException {
	try (ZipInputStream zipInTmp = zipInStream; DataInputStream inTmp = in) {
	    zipInStream.closeEntry();
	}

    }
}
