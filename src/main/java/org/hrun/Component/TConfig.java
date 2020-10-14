package org.hrun.Component;

import lombok.Data;
import org.hrun.Component.Common.Variables;

@Data
public class TConfig {

    private String name;
    private Boolean verify;
    private String base_url;
    private Variables variables;
    /*ODO:
    private Parameters parameters;
    private Hooks setup_hooks;
    private Hooks teardown_hooks;
     */
    private Export export;
    private String path;
    private int weight;

    public TConfig(String name,String base_url,Boolean verify,Variables variables,Export export,String path,
                   int weight){
        this.name = name;
        this.base_url = base_url;
        this.verify = verify;
        this.variables = variables;
        this.export = export;
        this.path = path;
        this.weight = weight;

    }

}
