package org.hrun.Component;

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
    setup_hooks: Hooks = []
    teardown_hooks: Hooks = []
            # used to extract request's response field
    extract: VariablesMapping = {}
    # used to export session variables from referenced testcase
    export: Export = []
    validators: Validators = Field([], alias="validate")
    validate_script: List[Text] = []
}
