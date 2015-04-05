/**
 * 
 */
package ru.spbau.skrivohatskiy.task03;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Function Utils
 * 
 * @author Sergey Krivohatskiy
 *
 */
public class FunctionalUtils {
    /**
     * Returns a list consisting of the results of applying the given function
     * to the elements of this collection
     * 
     * @param collection
     *            input collection
     * @param mapFunction
     *            function to apply to each element
     * @return a list consisting of the results of applying the given function
     */
    public static <A, R> List<R> map(Collection<? extends A> collection,
	    Function<A, R> mapFunction) {
	List<R> result = new ArrayList<R>(collection.size());

	for (A a : collection) {
	    result.add(mapFunction.cals(a));
	}

	return result;
    }

    /**
     * it takes the initial value and the last item of the list and applies the
     * function, then it takes the penultimate item from the end and the result,
     * and so on
     * 
     * @param list
     *            input list
     * @param initValue
     *            initial value
     * @param f
     *            function to apply
     * @return result f value
     */
    public static <A, B> B foldr(List<? extends A> list, B initValue,
	    Function2<A, B, B> f) {
	ListIterator<? extends A> iter = list.listIterator(list.size());
	B result = initValue;

	while (iter.hasPrevious()) {
	    result = f.cals(iter.previous(), result);
	}

	return result;
    }

    /**
     * @param collection
     *            input collection
     * @param predicate
     *            predicate to apply to each element
     * @return a list of an elements from the collection: predicate(element) ==
     *         true
     */
    public static <A> List<A> filter(Collection<? extends A> collection,
	    Predicate<A> predicate) {
	List<A> result = new LinkedList<A>();

	for (A a : collection) {
	    if (predicate.cals(a)) {
		result.add(a);
	    }
	}

	return result;
    }

    /**
     * @param collection
     *            input collection
     * @param predicate
     *            predicate to apply to each element
     * @return a list of the first elements from the collection:
     *         predicate(element) == true
     */
    public static <A> List<A> take(Collection<? extends A> collection,
	    Predicate<A> predicate) {
	List<A> result = new LinkedList<A>();

	for (A a : collection) {
	    if (!predicate.cals(a)) {
		break;
	    }
	    result.add(a);
	}

	return result;
    }

}
