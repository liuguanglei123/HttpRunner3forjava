package org.hrun.Component;

import java.util.List;

public class StepData {
    private boolean success;
    private String name;
    private List<SessionData> dataList;
    private SessionData data;
    private VariablesMapping export_vars;

    public StepData(String name){
        this.name = name;
    }
}
