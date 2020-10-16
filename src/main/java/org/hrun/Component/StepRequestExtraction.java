package org.hrun.Component;

import lombok.Data;

@Data
public class StepRequestExtraction implements Performable {

    private TStep __step_context;

    public StepRequestExtraction(TStep step_context){
        this.__step_context = step_context;
    }

    public StepRequestExtraction with_jmespath(boolean allow_redirects){
        //TODO：self.__step_context.extract[var_name] = jmes_path
        return this;
    }

    public StepRequestValidation validate(boolean allow_redirects){
        return new StepRequestValidation(__step_context);
    }

    @Override
    public TStep perform(){
        return __step_context;
    }

}
