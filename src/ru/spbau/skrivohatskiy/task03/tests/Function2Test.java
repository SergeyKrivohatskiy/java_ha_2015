/**
 * 
 */
package ru.spbau.skrivohatskiy.task03.tests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ru.spbau.skrivohatskiy.task03.Function;
import ru.spbau.skrivohatskiy.task03.Function2;
import ru.spbau.skrivohatskiy.task03.FunctionalUtils;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class Function2Test {

    /**
     * Tests {@link FunctionalUtils#foldr}
     */
    @Test
    public void testFoldr() {
	Function2<Integer, Double, Double> summMulPi = new Function2<Integer, Double, Double>() {
	    @Override
	    public Double cals(Integer argument1, Double argument2) {
		return Math.PI * argument1 + argument2;
	    }

	};

	List<Integer> intList = Arrays.asList(1, 5, 7, 9, 18);
	double expected = Math.PI * (1 + 5 + 7 + 9 + 18);
	double actual = FunctionalUtils.foldr(intList, 0d, summMulPi);

	Assert.assertEquals(expected, actual, 1e-10);
    }

    /**
     * Tests {@link Function2} default methods
     */
    @Test
    public void testFunction2() {
	Function2<Byte, Byte, Integer> summ = new Function2<Byte, Byte, Integer>() {
	    @Override
	    public Integer cals(Byte argument1, Byte argument2) {
		return argument1 + argument2;
	    }
	};
	Function<Integer, Double> half = new Function<Integer, Double>() {
	    @Override
	    public Double cals(Integer argument) {
		return argument / 2d;
	    }
	};

	assertEquals(4.5d, summ.then(half).cals((byte) 2, (byte) 7), 1e-15);

	assertEquals(Integer.valueOf(71), summ.bind1((byte) 1).cals((byte) 70));
	assertEquals(Integer.valueOf(71), summ.bind2((byte) 11).cals((byte) 60));
    }
}
