/**
 * 
 */
package ru.spbau.skrivohatskiy.task02.archiveManager;

/**
 * Data part
 * 
 * @see ArchiveReader
 * @see ArchiveWriter
 * 
 * @author Sergey Krivohatskiy
 *
 */
public class DataPart {
    /**
     * Data part key
     */
    public final String key;
    /**
     * Data part bytes
     */
    public final byte[] data;

    /**
     * @param key
     *            {@link #key}
     * @param data
     *            {@link #data}
     */
    public DataPart(String key, byte[] data) {
	this.key = key;
	this.data = data;
    }
}
