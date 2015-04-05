/**
 * 
 */
package ru.spbau.skrivohatskiy.task03;

/**
 * Function from A to R
 * 
 * @author Sergey Krivohatskiy
 * @param <A>
 *            argument type
 * @param <R>
 *            return value type
 *
 */
public interface Function<A, R> {

    /**
     * Calculate function from specified argument
     * 
     * @param argument
     *            function argument
     * @return function result
     */
    public R cals(A argument);

    /**
     * @param otherFunction
     *            function that will be applied after this function
     * @return composition of this function and specified function
     * 
     */
    public default <R2> Function<A, R2> then(
	    Function<? super R, R2> otherFunction) {
	Function<A, R> thisFunction = this;

	return new Function<A, R2>() {
	    @Override
	    public R2 cals(A argument) {
		return otherFunction.cals(thisFunction.cals(argument));
	    }
	};
    }
}
