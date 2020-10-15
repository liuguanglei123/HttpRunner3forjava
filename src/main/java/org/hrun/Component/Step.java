package org.hrun.Component;

public class Step {
    private TStep __step_context;

    public Step(NeedReNameItf step_context){
        __step_context = step_context.perform();
    }

    public TRequest request(){
        return __step_context.getRequest();
    }

    public Object testcase(){
        return __step_context.getTestcasecall();
    }


    def perform(self) -> TStep:
            return self.__step_context









}
