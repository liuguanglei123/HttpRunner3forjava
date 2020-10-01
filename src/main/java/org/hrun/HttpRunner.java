package org.hrun;

import org.hrun.Component.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRunner {
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


        return null;
    }


//    def test_start(self, param: Dict = None) -> "HttpRunner":
        """main entrance, discovered by pytest"""
        self.__project_meta = self.__project_meta or load_project_meta(
            self.__config.path
        )
        self.__case_id = self.__case_id or str(uuid.uuid4())
        self.__log_path = self.__log_path or os.path.join(
            self.__project_meta.RootDir, "logs", f"{self.__case_id}.run.log"
        )
        log_handler = logger.add(self.__log_path, level="DEBUG")

        # parse config name
        config_variables = self.__config.variables
        if param:
            config_variables.update(param)
        config_variables.update(self.__session_variables)
        self.__config.name = parse_data(
            self.__config.name, config_variables, self.__project_meta.functions
        )

        if USE_ALLURE:
            # update allure report meta
            allure.dynamic.title(self.__config.name)
            allure.dynamic.description(f"TestCase ID: {self.__case_id}")

        logger.info(
            f"Start to run testcase: {self.__config.name}, TestCase ID: {self.__case_id}"
        )

        try:
            return self.run_testcase(
                TestCase(config=self.__config, teststeps=self.__teststeps)
            )
        finally:
            logger.remove(log_handler)
            logger.info(f"generate testcase log: {self.__log_path}")



}
