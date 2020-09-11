package org.hrun.junittest;

public class NimingleiTest {

    NimingInterfaceTest object1 = new NimingInterfaceTest(){
        @Override
        public void execute() {
            System.out.println("123123123");
        }

    };

    public static void main(String[] args){
        NimingleiTest nimingleiTest = new NimingleiTest();
        nimingleiTest.object1.execute();
    }
}
