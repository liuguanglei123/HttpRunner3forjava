package org.hrun.Component;

import org.hrun.Component.Common.*;

public class RequestWithOptionalArgs implements Performable {
    private TStep __step_context;

    public RequestWithOptionalArgs(TStep step_context){
        __step_context = step_context;
    }

    public RequestWithOptionalArgs with_params(Params params){
        __step_context.getRequest().getParams().update(params);
        return this;
    }

    public RequestWithOptionalArgs with_headers(Headers headers){
        __step_context.getRequest().getHeaders().update(headers);
        return this;
    }

    public RequestWithOptionalArgs with_cookies(Cookies cookies){
        __step_context.getRequest().getCookies().update(cookies);
        return this;
    }

    public RequestWithOptionalArgs with_data(ReqData reqData){
        __step_context.getRequest().getReq_data().update(reqData);
        return this;
    }

    public RequestWithOptionalArgs with_json(ReqJson reqJson){
        __step_context.getRequest().getReq_json().update(reqJson);
        return this;
    }

    public RequestWithOptionalArgs set_timeout(float timeout){
        __step_context.getRequest().setTimeout(timeout);
        return this;
    }

    public RequestWithOptionalArgs set_verify(boolean verify){
        __step_context.getRequest().setVerify(verify);
        return this;
    }

    public RequestWithOptionalArgs set_allow_redirects(boolean allow_redirects){
        __step_context.getRequest().setAllow_redirects(allow_redirects);
        return this;
    }

    /*TODO:
    public RequestWithOptionalArgs upload(boolean allow_redirects){
        __step_context.getRequest().setAllow_redirects(allow_redirects);
        return this;
    }

    def teardown_hook(

    */

    public StepRequestExtraction extract(boolean allow_redirects){
        return new StepRequestExtraction(this.__step_context);
    }

    public StepRequestValidation validate(boolean allow_redirects){
        return new StepRequestValidation(this.__step_context);
    }

    @Override
    public TStep perform(){
        return this.__step_context;
    }
}
