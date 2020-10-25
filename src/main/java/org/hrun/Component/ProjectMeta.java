package org.hrun.Component;

import lombok.Data;

@Data
public class ProjectMeta {

    private String RootDir;
    private Object functions;

    public Boolean isEmpty(){
        //TODO:
        return true;
    }
}
