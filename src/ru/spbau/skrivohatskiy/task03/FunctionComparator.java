/**
 * 
 */
package ru.spbau.skrivohatskiy.task03;

import java.util.Comparator;

/**
 * Compares objects of class T by comparing results of specified function
 * 
 * @author Sergey Krivohatskiy
 * @param <T>
 *            Function argument type
 * @param <R>
 *            Function result type
 *
 */
public class FunctionComparator<T, R extends Comparable<? super R>> implements
	Comparator<T> {

    private final Function<T, R> f;

    /**
     * @param f
     *            function used to compare
     */
    public FunctionComparator(Function<T, R> f) {
	this.f = f;
    }

    @Override
    public int compare(T o1, T o2) {
	return f.cals(o1).compareTo(f.cals(o2));
    }

}
