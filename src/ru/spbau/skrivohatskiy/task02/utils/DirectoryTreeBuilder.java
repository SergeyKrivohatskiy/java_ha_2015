/**
 * 
 */
package ru.spbau.skrivohatskiy.task02.utils;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Creates directory tree by paths
 * 
 * @author Sergey Krivohatskiy
 *
 */
public class DirectoryTreeBuilder {

    private static final String DELIMER = "|";
    private final Node root = new Node();
    private final boolean showHidden;

    /**
     * Creates new DirectoryTreeBuilder
     * 
     * @param showHidden
     *            if true hidden directories and files wont be skipped while
     *            printing
     */
    public DirectoryTreeBuilder(boolean showHidden) {
	this.showHidden = showHidden;
    }

    /**
     * Appends specified path to directory tree
     * 
     * @param path
     *            path to append
     */
    public void addPath(Path path) {
	Node node = root;
	for (Path pathPart : path) {
	    node = node.getChild(pathPart.toString());
	}
    }

    private void print(Node node, PrintStream out, String precessor) {
	Map<String, Node> children = node.getChildren();
	if (children.isEmpty()) {
	    return;
	}
	for (Iterator<Entry<String, Node>> it = children.entrySet().iterator(); it
		.hasNext();) {
	    Entry<String, Node> child = it.next();
	    if (!showHidden && child.getKey().startsWith(".")) {
		continue;
	    }
	    out.print(precessor);
	    out.print(DELIMER);
	    out.print('_');
	    out.print(child.getKey());
	    out.println();
	    print(child.getValue(),
		    out,
		    precessor + (it.hasNext() ? DELIMER : " ")
			    + Utils.spaces(child.getKey().length() + 1));
	}
    }

    /**
     * Prints the directory tree in next format
     * 
     * <pre>
     * |_subfolder1
     * |           |_subfolder 1.1
     * |           |              |_a.txt
     * |           |_subfolder 1.2
     * |_http
     *       |_page1
     *       |_page2
     * </pre>
     * 
     * ignoring hidden files and directories
     * 
     * @param out
     *            prints to this output
     */
    public void print(PrintStream out) {
	print(root, out, "");
    }

    private static class Node {
	private final Map<String, Node> children = new TreeMap<>();

	public Node getChild(String name) {
	    if (children.containsKey(name)) {
		return children.get(name);
	    }
	    Node result = new Node();
	    children.put(name, result);
	    return result;
	}

	public Map<String, Node> getChildren() {
	    return children;
	}
    }
}
