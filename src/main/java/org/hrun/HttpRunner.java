package org.hrun;

import com.google.common.base.Strings;
import lombok.Data;
import org.hrun.Component.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
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
    private List<StepData> __step_datas;
    private HttpSession __session;
    private VariablesMapping __session_variables;
    // time
    private Float __start_at;
    private Float __end_at;
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

    }

    public void __parse_config(Config config){
        config.getVariables.update(this.__session_variables);
        config.setVariables(Parse.parse_variables_mapping(
                config.getVariables(),this.__project_meta.getFunctions()
            )
        );
        config.setName(Parse.parse_data(config.getName(),config.getVariables(),this.__project_meta.getFunctions()));
        config.setBase_url(Parse.parse_data(config.getBase_url(),config.getVariables(),this.__project_meta.getFunctions()));

        if(this.__project_meta == null || this.__project_meta.isEmpty()){
            this.set__project_meta(load_project_meta(this.__config.getPath()));
        }

        this.__parse_config(this.__config);
        this.__start_at = System.currentTimeMillis();
        this.__step_datas = new ArrayList<StepData>();
        if(this.__session == null){
            this.__session = new HttpSession();
        }
        VariablesMapping extracted_variables = new VariablesMapping();




    }
        # run teststeps
        for step in self.__teststeps:
            # override variables
            # step variables > extracted variables from previous steps
            step.variables = merge_variables(step.variables, extracted_variables)
            # step variables > testcase config variables
            step.variables = merge_variables(step.variables, self.__config.variables)

            # parse variables
            step.variables = parse_variables_mapping(
                step.variables, self.__project_meta.functions
            )

            # run step
            if USE_ALLURE:
                with allure.step(f"step: {step.name}"):
                    extract_mapping = self.__run_step(step)
            else:
                extract_mapping = self.__run_step(step)

            # save extracted variables to session variables
            extracted_variables.update(extract_mapping)

        self.__session_variables.update(extracted_variables)
        self.__duration = time.time() - self.__start_at
        return self


}
