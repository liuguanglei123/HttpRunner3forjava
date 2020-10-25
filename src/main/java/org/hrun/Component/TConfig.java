package org.hrun.Component;

import lombok.Data;
import org.hrun.Component.Common.Variables;
import org.hrun.Component.LazyContent.LazyString;

@Data
public class TConfig {

    private LazyString name;
    private Boolean verify;
    private LazyString base_url;
    private Variables variables;
    /*ODO:
    private Parameters parameters;
    private Hooks setup_hooks;
    private Hooks teardown_hooks;
     */
    private Export export;
    private LazyString path;
    private int weight;

    public TConfig(LazyString name,LazyString base_url,Boolean verify,Variables variables,Export export,LazyString path,
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
