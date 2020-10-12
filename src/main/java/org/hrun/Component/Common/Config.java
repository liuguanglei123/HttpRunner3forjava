package org.hrun.Component.Common;

import org.hrun.Component.Export;
import org.hrun.Component.TConfig;

import java.util.List;
import java.util.Map;

public class Config {

    private String __name;
    private Variables __variables;
    private String __base_url;
    private Boolean __verify;
    private Export __export;
    private String __path;
    private int __weight;

    public void __init__(String name) {
        this.__name = name;
        this.__variables = new Variables();
        this.__base_url = "";
        this.__verify = false;
        this.__export = new Export();
        this.__weight = 1;
        /* TODO:
            caller_frame = inspect.stack()[1]
            self.__path = caller_frame.filename
         */
    }

    public Config variables(Map variables){
        this.__variables.update(new Variables(variables));
        return this;
    }

    public Config base_url(String base_url){
        this.__base_url = base_url;
        return this;
    }

    public Config verify(Boolean verify){
        this.__verify = verify;
        return this;
    }

    public Config export(List<String> export_var_name){
        this.__export.update(new Export(export_var_name));
        return this;
    }

    public TConfig perform(){
        return new TConfig(
                this.__name,
                this.__base_url,
                this.__verify,
                this.__variables,
                this.__export,
                this.__path,
                this.__weight
        );
    }


}
