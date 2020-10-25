package org.hrun.Component;

public class StepRequestValidation implements PerformableIntf {
    private TStep __step_context;

    public StepRequestValidation(TStep step_context){
        this.__step_context = step_context;
    }

    public StepRequestValidation assert_equal(String jmes_path,Object expected_value,String message) {
        //TODO:
//        self.__step_context.validators.append(
//                {"equal": [jmes_path, expected_value, message]}
//          }
        return this;
    }


    @Override
    public TStep perform() {
        return __step_context;
    }
}
