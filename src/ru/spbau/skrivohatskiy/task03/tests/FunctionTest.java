/**
 * 
 */
package ru.spbau.skrivohatskiy.task03.tests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ru.spbau.skrivohatskiy.task03.Function;
import ru.spbau.skrivohatskiy.task03.FunctionComparator;
import ru.spbau.skrivohatskiy.task03.FunctionalUtils;

/**
 * {@link Function}
 * 
 * @author Sergey Krivohatskiy
 *
 */
public class FunctionTest {

    /**
     * Tests {@link Function#then}
     */
    @Test
    public void testThen() {
	Function<Integer, Integer> mul2 = new Function<Integer, Integer>() {
	    @Override
	    public Integer cals(Integer argument) {
		return argument * 2;
	    }

	};
	assertEquals("mul2(mul2(1)) == 4", Integer.valueOf(4), mul2.then(mul2)
		.cals(1));
    }

    /**
     * Tests {@link FunctionalUtils#map}
     */
    @Test
    public void testMap() {
	List<Integer> intList = Arrays.asList(1, 5, 7, 9, 18);
	Function<Integer, Integer> suc = new Function<Integer, Integer>() {
	    @Override
	    public Integer cals(Integer argument) {
		return argument + 1;
	    }

	};
	assertEquals("map i -> suc i", Arrays.asList(2, 6, 8, 10, 19),
		FunctionalUtils.map(intList, suc));
    }

    /**
     * Tests {@link FunctionComparator}
     */
    @Test
    public void testComparator() {
	Function<Integer, Boolean> even = new Function<Integer, Boolean>() {
	    @Override
	    public Boolean cals(Integer argument) {
		return argument % 2 == 0;
	    }

	};
	FunctionComparator<Integer, Boolean> compEven = new FunctionComparator<>(
		even);

	assertEquals("even(4) == even(8)", 0, compEven.compare(4, 8));
    }
}
