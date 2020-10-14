package org.hrun;

import com.google.common.base.Strings;
import lombok.Data;
import org.hrun.Component.*;
import org.hrun.Component.Common.Config;
import org.hrun.Component.Common.Variables;
import org.hrun.exceptions.HrunExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hrun.Loader.load_project_meta;

@Data
public class HttpRunner {

    private static Logger logger = LoggerFactory.getLogger(HttpRunner.class);

    private Config config ;
    private List<Step> teststeps;
    private Boolean success = false;
    private TConfig __config;
    private List<TStep> __teststeps;
    private ProjectMeta __project_meta = null;
    private String __case_id;
    private List<String> __export;
    private List<StepData> __step_datas = new ArrayList<>();
    private HttpSession __session;
    private VariablesMapping __session_variables;
    // time
    private long __start_at;
    private long __end_at;
    private long __duration;
    // log
    private String __log_path;


    public void __init_tests__(){
        //TODO:
        this.__config = this.config.perform();
        this.__teststeps = new ArrayList<>();
        for(Step step : this.teststeps){
           this. __teststeps.add(step.perform());
        }
//        for step in this.teststeps:
//            self.__teststeps.append(step.perform())
    }


    public HttpRunner test_start(){
        return test_start(null);
    }


    public HttpRunner test_start(Map param){
        /* 测试用例执行的主要入口，在httprunner原版中，是依赖pytest自动发现机制去调用的，不过不是很理解原版中的param参数是如何传递的
        TODO：了解一下pytest中test方法的参数传递
         */
        this.__init_tests__();

        if(this.__project_meta == null || this.__project_meta.isEmpty()){
            __project_meta = load_project_meta(this.__config.getPath());
        }

        if(Strings.isNullOrEmpty(__case_id))
            this.__case_id = UUID.randomUUID().toString();

        if(Strings.isNullOrEmpty(__log_path))
            this.__log_path = Paths.get(this.__project_meta.getRootDir()).resolve("logs").resolve(this.__case_id + ".run.log").toAbsolutePath().toString();

        // TODO:日志处理，放在1.1实现
        // log_handler = logger.add(self.__log_path, level="DEBUG")

        Variables config_variables = this.__config.getVariables();
        if(param == null || param.isEmpty()){
            config_variables.update(param);
        }
        config_variables.update(this.__session_variables);
        //TODO: 解析config中的name，放在1.2吧
        //TODO:if USE_ALLURE:

        logger.info(
                "TODO：Start to run testcase: {self.__config.name}, TestCase ID: {self.__case_id}"
        );

        try{
            return run_testcase(
                    new TestCase(this.__config, this.__teststeps)
            );
        }finally{
//TODO：            logger.remove(log_handler)
            logger.info("TODO：generate testcase log: {self.__log_path}");
        }
        return null;
    }

    public HttpRunner run_testcase(TestCase testcase){
        this.__config = testcase.getConfig;
        this.__teststeps = testcase.getTestSteps;
        if(this.__project_meta == null || this.__project_meta.isEmpty())
            this.__project_meta = load_project_meta(this.__config.getPath);

        this.__parse_config(this.__config);
        this.__start_at = System.currentTimeMillis();
        this.__step_datas = new ArrayList<StepData>();
        if(this.__session == null){
            this.__session = new HttpSession();
        }
        VariablesMapping extracted_variables = new VariablesMapping();

        for(TStep step : this.__teststeps){
            step.setVariables(merge_variables(step.getVariables, extracted_variables));
            step.setVariables(merge_variables(step.getVariables, this.__config.getVariables()));

            step.getVariables = parse_variables_mapping(step.getVariables, this.__project_meta,getFunctions());

            //TODO: USE_ALLURE:
            Map extract_mapping = this.__run_step(step);

            extracted_variables.update(extract_mapping);
        }

        this.__session_variables.update(extracted_variables);
        this.__duration = System.currentTimeMillis() - this.__start_at;

        return this;
    }

    public void __parse_config(Config config) {
        config.getVariables.update(this.__session_variables);
        config.setVariables(Parse.parse_variables_mapping(
                config.getVariables(), this.__project_meta.getFunctions()
                )
        );
        config.setName(Parse.parse_data(config.getName(), config.getVariables(), this.__project_meta.getFunctions()));
        config.setBase_url(Parse.parse_data(config.getBase_url(), config.getVariables(), this.__project_meta.getFunctions()));

        if (this.__project_meta == null || this.__project_meta.isEmpty()) {
            this.set__project_meta(load_project_meta(this.__config.getPath()));
        }
    }

    public Map __run_step(TStep step){
        logger.info("run step begin: {step.name} >>>>>>");
        StepData step_data = null;

        if(step.getRequest() != null){
            step_data = this.__run_step_request(step);
        }else if(step.getTestcase != null){
            step_data = this.__run_step_testcase(step);
        }else{
            HrunExceptionFactory.create("E0001");
        }

        this.__step_datas.add(step_data);
        logger.info("run step end: {step.name} <<<<<<\n");
        return step_data.getExport_vars();
    }

    public StepData __run_step_request(TStep step){
        StepData step_data = new StepData(step.getName());
        prepare_upload_step(step,this.__project_meta.getFuntions());
        request_dict = step.getRequest().getDict();
        request_dict.remove("upload");
        parsed_request_dict = parse_data(request_dict,step.getVariables(),this.__project_meta.getFunctions());

        parsed_request_dict["headers"].setdefault(
                "HRUN-Request-ID",
                "HRUN-{self.__case_id}-{str(int(time.time() * 1000))[-6:]}"
                );
        step.getVariables().put("request", parsed_request_dict);
        if(step.hasSetup_hooks()){
            this.__call_hooks(step.getSetup_hooks(), step.getVariables(), "setup request");
        }
        // prepare arguments
        method = parsed_request_dict.pop("method")
        url_path = parsed_request_dict.pop("url")
        url = build_url(self.__config.base_url, url_path)
        parsed_request_dict["verify"] = self.__config.verify
        parsed_request_dict["json"] = parsed_request_dict.pop("req_json", {})
        # request
        resp = self.__session.request(method, url, **parsed_request_dict)
        resp_obj = ResponseObject(resp)
        step.variables["response"] = resp_obj

        # teardown hooks
        if step.teardown_hooks:
            self.__call_hooks(step.teardown_hooks, step.variables, "teardown request")

        def log_req_resp_details():
            err_msg = "\n{} DETAILED REQUEST & RESPONSE {}\n".format("*" * 32, "*" * 32)

            # log request
            err_msg += "====== request details ======\n"
            err_msg += f"url: {url}\n"
            err_msg += f"method: {method}\n"
            headers = parsed_request_dict.pop("headers", {})
            err_msg += f"headers: {headers}\n"
            for k, v in parsed_request_dict.items():
                v = utils.omit_long_data(v)
                err_msg += f"{k}: {repr(v)}\n"

            err_msg += "\n"

            # log response
            err_msg += "====== response details ======\n"
            err_msg += f"status_code: {resp.status_code}\n"
            err_msg += f"headers: {resp.headers}\n"
            err_msg += f"body: {repr(resp.text)}\n"
            logger.error(err_msg)

        # extract
        extractors = step.extract
        extract_mapping = resp_obj.extract(extractors)
        step_data.export_vars = extract_mapping

        variables_mapping = step.variables
        variables_mapping.update(extract_mapping)

        # validate
        validators = step.validators
        session_success = False
        try:
            resp_obj.validate(
                validators, variables_mapping, self.__project_meta.functions
            )
            session_success = True
        except ValidationFailure:
            session_success = False
            log_req_resp_details()
            # log testcase duration before raise ValidationFailure
            self.__duration = time.time() - self.__start_at
            raise
        finally:
            self.success = session_success
            step_data.success = session_success

            if hasattr(self.__session, "data"):
                # httprunner.client.HttpSession, not locust.clients.HttpSession
                # save request & response meta data
                self.__session.data.success = session_success
                self.__session.data.validators = resp_obj.validation_results

                # save step data
                step_data.data = self.__session.data

        return step_data

    }
}
