/**
 * 
 */
package ru.spbau.skrivohatskiy.task03.tests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ru.spbau.skrivohatskiy.task03.FunctionalUtils;
import ru.spbau.skrivohatskiy.task03.Predicate;

/**
 * {@link Predicate}
 * 
 * @author Sergey Krivohatskiy
 *
 */
public class PredicateTest {

    /**
     * Tests {@link Predicate} default methods
     */
    @Test
    public void testPredicatesCombinations() {
	Predicate<Object> t = Predicate.alwaysTrue();
	Predicate<Object> f = Predicate.alwaysFalse();
	Predicate<Integer> eqIntMax = Predicate.equals(Integer.MAX_VALUE);
	Predicate<Integer> less5 = Predicate.less(Integer.valueOf(5));
	assertEquals(Boolean.TRUE, f.not().cals(""));
	assertEquals(Boolean.FALSE, t.not().cals(""));
	assertEquals(Boolean.FALSE, f.and(t).cals(""));
	assertEquals(Boolean.FALSE, t.and(f).cals(""));
	assertEquals(Boolean.TRUE, t.and(t).cals(""));
	assertEquals(Boolean.TRUE, f.or(t).cals(""));
	assertEquals(Boolean.FALSE, f.or(f).cals(""));
	assertEquals(Boolean.TRUE, t.or(f).cals(""));
	assertEquals(Boolean.FALSE, eqIntMax.cals(1));
	assertEquals(Boolean.TRUE, eqIntMax.cals(Integer.MAX_VALUE));
	assertEquals(Boolean.TRUE, less5.cals(4));
	assertEquals(Boolean.TRUE, less5.cals(Integer.MIN_VALUE));
	assertEquals(Boolean.FALSE, less5.cals(5));
    }

    /**
     * {@link Predicate#notNull}
     */
    @Test
    public void testNotNull() {
	assertEquals(Boolean.TRUE, Predicate.notNull().cals(""));
	assertEquals(Boolean.FALSE, Predicate.notNull().cals(null));
    }

    /**
     * {@link FunctionalUtils#take} {@link FunctionalUtils#filter}
     */
    @Test
    public void testPredicateUtils() {
	List<Integer> ints = Arrays.asList(1, 4, 6);

	assertEquals(ints, FunctionalUtils.take(ints, Predicate.alwaysTrue()));
	assertEquals(Collections.EMPTY_LIST,
		FunctionalUtils.take(ints, Predicate.alwaysFalse()));
	assertEquals(ints, FunctionalUtils.filter(ints, Predicate.alwaysTrue()));
	assertEquals(Collections.EMPTY_LIST,
		FunctionalUtils.filter(ints, Predicate.alwaysFalse()));
	assertEquals(Arrays.asList(4),
		FunctionalUtils.filter(ints, new Predicate<Integer>() {
		    @Override
		    public Boolean cals(Integer argument) {
			return argument <= 5 && argument != 1;
		    }
		}));
	assertEquals(Arrays.asList(1),
		FunctionalUtils.take(ints, new Predicate<Integer>() {
		    @Override
		    public Boolean cals(Integer argument) {
			return argument < 4 || argument >= 6;
		    }
		}));
    }
}
