/**
 * 
 */
package ru.spbau.skrivohatskiy.task03;

/**
 * Function from AxB to R
 * 
 * @author Sergey Krivohatskiy
 *
 * @param <A>
 *            first argument type
 * @param <B>
 *            second argument type
 * @param <R>
 *            result type
 *
 */
public interface Function2<A, B, R> {

    /**
     * Calculate function from specified argument
     * 
     * @param argument1
     *            first function argument
     * @param argument2
     *            second function argument
     * 
     * @return function result
     */
    public R cals(A argument1, B argument2);

    /**
     * @param otherFunction
     *            function that will be applied after this function
     * @return composition of this function and specified function
     * 
     */
    public default <R2> Function2<A, B, R2> then(
	    Function<? super R, R2> otherFunction) {
	Function2<A, B, R> thisFunction = this;

	return new Function2<A, B, R2>() {

	    @Override
	    public R2 cals(A argument1, B argument2) {
		return otherFunction.cals(thisFunction.cals(argument1,
			argument2));
	    }
	};
    }

    /**
     * @param argument1
     *            first function argument placeholder
     * @return function: function(B) = this(placeholder, B)
     */
    public default Function<B, R> bind1(A argument1) {
	Function2<A, B, R> thisFunction = this;

	return new Function<B, R>() {
	    @Override
	    public R cals(B argument2) {
		return thisFunction.cals(argument1, argument2);
	    }
	};
    }

    /**
     * @param argument2
     *            second function argument placeholder
     * @return function: function(A) = this(A, placeholder)
     */
    public default Function<A, R> bind2(B argument2) {
	Function2<A, B, R> thisFunction = this;

	return new Function<A, R>() {
	    @Override
	    public R cals(A argument1) {
		return thisFunction.cals(argument1, argument2);
	    }
	};
    }
}
