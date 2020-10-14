package org.hrun.Component;

import java.util.Map;

public class RunRequest {
    private TStep __step_context;

    public RunRequest(String name){
        __step_context = new TStep(name);
    }
    public RunRequest wiht_variables(Map variables){
        __step_context.getVariables().update(variables);
        return this;
    }

    public RunRequest setup_hook(String hook,String assign_var_name){
        //TODO：
        return this;
    }

    public RequestWithOptionalArgs get(String url){
        __step_context.request = TRequest(method=MethodEnum.GET, url=url);
        return new RequestWithOptionalArgs(__step_context);
    }





}
