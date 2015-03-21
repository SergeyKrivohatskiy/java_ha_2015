/**
 * 
 */
package ru.spbau.skrivohatskiy.task02.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class Utils {
    /**
     * Reads all bytes from input stream
     * 
     * @param is
     *            input stream
     * @return byte array that contains all bytes from specified input stream
     * @throws IOException
     *             if any IO exceptions occurs
     */
    public static byte[] readAllBytes(InputStream is) throws IOException {
	ByteArrayOutputStream dataBytesOS = new ByteArrayOutputStream();
	byte[] byteChunk = new byte[4096];

	int n;
	while ((n = is.read(byteChunk)) > 0) {
	    dataBytesOS.write(byteChunk, 0, n);
	}
	return dataBytesOS.toByteArray();
    }

    /**
     * @param count
     *            spaces count
     * @return string containing count spaces
     */
    public static String spaces(int count) {
	return String.format("%" + count + "s", "");
    }

}
