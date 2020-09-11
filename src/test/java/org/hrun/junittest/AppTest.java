package org.hrun.junittest;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testA()
    {
        System.out.println("123456");
        assertTrue( true );
    }

    @TestFactory
    Collection<DynamicTest> dynamicTestsFromCollection() {
        return Arrays.asList(



                dynamicTest("1st dynamic test", new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        assertTrue(true);
                    }
                }),




                dynamicTest("1st dynamic test", new ExecutableTest("2")),
                dynamicTest("1st dynamic test", new ExecutableTest("3")),
                dynamicTest("1st dynamic test", new ExecutableTest("4")),
                dynamicTest("1st dynamic test", new ExecutableTest("5")),
                dynamicTest("1st dynamic test", new ExecutableTest("6")),
                dynamicTest("1st dynamic test", new ExecutableTest("7"))
        );
    }

    public Executable testexcutable(){
        System.out.println("123123123");
        return null;
    }
}
