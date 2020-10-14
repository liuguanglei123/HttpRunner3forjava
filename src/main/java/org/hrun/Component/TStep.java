package org.hrun.Component;

import lombok.Data;
import org.hrun.Component.Common.Variables;

import java.util.List;

@Data
public class TStep {
    private String name;
    private TRequest request;
    //TODO：what?
    private String testcasestr;
    private CallAble testcasecall;
    private Variables variables;
    private Hooks setup_hooks;
    private Hooks teardown_hooks;
    private VariablesMapping extract;
    private Export export;
    private Validators validators;
    private List<String> validate_script;

    public TStep(String name){
        this.name = name;
    }
}
