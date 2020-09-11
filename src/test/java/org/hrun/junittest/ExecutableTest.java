package org.hrun.junittest;

import org.junit.jupiter.api.function.Executable;

public class ExecutableTest implements Executable {
    @Override
    public void execute() throws Throwable {
        System.out.println(a);
    }
    public ExecutableTest(String a){
        this.a = a;
    }

    public String a;
}
