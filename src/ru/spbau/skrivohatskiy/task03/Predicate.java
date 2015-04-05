/**
 * 
 */
package ru.spbau.skrivohatskiy.task03;

/**
 * Predicate from A Same to Function from A to Boolean
 * 
 * @author Sergey Krivohatskiy
 * 
 * @param <A>
 *            argument type
 *
 */
public interface Predicate<A> extends Function<A, Boolean> {

    /**
     * predicate: predicate(A) == true
     */
    public static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
	@Override
	public Boolean cals(Object argument) {
	    return true;
	}

    };

    /**
     * predicate: predicate(A) == false
     */
    public static final Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
	@Override
	public Boolean cals(Object argument) {
	    return false;
	}

    };

    /**
     * predicate: predicate(A) == true if A != null
     */
    public static final Predicate<Object> NOT_NULL = new Predicate<Object>() {
	@Override
	public Boolean cals(Object argument) {
	    return argument != null;
	}

    };

    /**
     * @return predicate: predicate(A) == !this(A)
     */
    public default Predicate<A> not() {
	Predicate<A> thisPredicate = this;

	return new Predicate<A>() {
	    @Override
	    public Boolean cals(A argument) {
		return !thisPredicate.cals(argument);
	    }
	};
    }

    /**
     * @param other
     *            orher predicate
     * @return predicate: predicate(A) == this(A) || other(A)
     */
    public default Predicate<A> or(Predicate<? super A> other) {
	Predicate<A> thisPredicate = this;

	return new Predicate<A>() {
	    @Override
	    public Boolean cals(A argument) {
		return thisPredicate.cals(argument) || other.cals(argument);
	    }
	};
    }

    /**
     * @param other
     *            orher predicate
     * @return predicate: predicate(A) == this(A) && other(A)
     */
    public default Predicate<A> and(Predicate<? super A> other) {
	Predicate<A> thisPredicate = this;

	return new Predicate<A>() {
	    @Override
	    public Boolean cals(A argument) {
		return thisPredicate.cals(argument) && other.cals(argument);
	    }
	};
    }

    /**
     * @return predicate: predicate(A) == true
     */
    @SuppressWarnings("unchecked")
    static <T> Predicate<T> alwaysTrue() {
	return (Predicate<T>) ALWAYS_TRUE;
    }

    /**
     * @return predicate: predicate(A) == false
     */
    @SuppressWarnings("unchecked")
    static <T> Predicate<T> alwaysFalse() {
	return (Predicate<T>) ALWAYS_FALSE;
    }

    /**
     * @return predicate: predicate(A) == true if A != null
     */
    @SuppressWarnings("unchecked")
    static <T> Predicate<T> notNull() {
	return (Predicate<T>) NOT_NULL;
    }

    /**
     * @param a
     *            object to compare with
     * @return predicate: predicate(b) == true if b == a
     */
    static <T extends Comparable<? super T>> Predicate<T> equals(T a) {
	return new Predicate<T>() {
	    @Override
	    public Boolean cals(T b) {
		return a.compareTo(b) == 0;
	    }
	};
    }

    /**
     * @param a
     *            object to compare with
     * @return predicate: predicate(b) == true if b < a
     */
    static <T extends Comparable<? super T>> Predicate<T> less(T a) {
	return new Predicate<T>() {
	    @Override
	    public Boolean cals(T b) {
		return a.compareTo(b) > 0;
	    }
	};
    }

}
