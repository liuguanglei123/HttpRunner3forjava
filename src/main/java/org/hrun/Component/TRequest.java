package org.hrun.Component;

import lombok.Data;
import org.hrun.Component.Common.*;
import org.hrun.Enum.MethodEnum;
import org.hrun.Parse;

@Data
public class TRequest implements Parseable {
    private MethodEnum method;
    private String url;
    private Params params;
    private Headers headers;
    private ReqJson req_json;
    private ReqData req_data;
    private Cookies cookies;
    private Float timeout;
    private boolean allow_redirects;
    private boolean verify;
    private Object upload; //TODO：上传文件

    public TRequest(String method,String url){
        this.method = MethodEnum.getMethodEnum(method);
        this.url = url;
    }

    public TRequest(){

    }


    @Override
    public Parseable parse(Variables variables_mapping, FunctionsMapping functions_mapping) {
        return new TRequest();
    }
}
