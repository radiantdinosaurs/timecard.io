package main;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by faytx on 8/18/2016.
 * @author faytxzen
 */
public class CalculatorTest {
    @Test
    public void getCurrentTimeStamp() throws Exception {
        assertNotNull("Return value should never be null.", Calculator.getCurrentTimeStamp());
    }

}