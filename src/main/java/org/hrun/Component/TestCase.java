package org.hrun.Component;

import lombok.Data;

import java.util.List;

@Data
public class TestCase {
    private TConfig config;
    private List<TStep> teststeps;

    public TestCase(TConfig config,List<TStep> teststeps){
        this.config = config;
        this.teststeps = teststeps;
    }
}
