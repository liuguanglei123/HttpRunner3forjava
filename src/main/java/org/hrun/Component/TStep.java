package org.hrun.Component;

import lombok.Data;

import java.util.List;

@Data
public class TStep {
    private String name;
    private Request request;
    private String testcasestr;
    private CallAble testcasecall;
    private VariablesMapping variables;
    private Hooks setup_hooks;
    private Hooks teardown_hooks;
    private VariablesMapping extract;
    private Export export;
    private Validators validators;
    private List<String> validate_script;
}
