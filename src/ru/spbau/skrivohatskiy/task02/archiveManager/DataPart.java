/**
 * 
 */
package ru.spbau.skrivohatskiy.task02.archiveManager;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class DataPart {
    /**
     * 
     */
    public final String key;
    /**
     * 
     */
    public final byte[] data;

    /**
     * @param key
     *            TODO
     * @param data
     *            TODO
     */
    public DataPart(String key, byte[] data) {
	this.key = key;
	this.data = data;
    }
}
