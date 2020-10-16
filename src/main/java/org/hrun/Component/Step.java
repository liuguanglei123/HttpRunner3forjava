package org.hrun.Component;

public class Step implements Performable {
    private TStep __step_context;

    public Step(Performable step_context){
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
