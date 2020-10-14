package org.hrun.Component;

public class RequestWithOptionalArgs {
    private TStep __step_context;

    public RequestWithOptionalArgs(TStep step_context){
        __step_context = step_context;
    }

    public with_params(){
        __step_context.getRequest().GetPparams.update(params)
        return self
    }
}
