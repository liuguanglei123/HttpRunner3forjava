package org.hrun;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.math.BigInteger;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    public String encrypt(Long id) {
        if (id == null) {
            return null;
        } else {
            long number = id;

            for(int i = 0; i < 4; ++i) {
                number ^= 71284230948672L;
                number <<= 3;
            }

            return BigInteger.valueOf(number).toString(35).toUpperCase();
        }
    }

    @Test
    public void testencrypt(){
        System.out.println(encrypt(2331L));
    }
}
