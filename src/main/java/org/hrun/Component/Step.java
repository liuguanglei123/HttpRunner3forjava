package org.hrun.Component;

public class Step implements PerformableIntf {
    private TStep __step_context;

    public Step(PerformableIntf step_context){
        __step_context = step_context.perform();
    }

    public TRequest request(){
        return __step_context.getRequest();
    }

    public Object testcase(){
        return __step_context.getTestcasecall();
    }

    public TStep perform(){
        return __step_context;
    }








}
