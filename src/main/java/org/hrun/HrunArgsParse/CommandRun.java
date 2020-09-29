package org.hrun.HrunArgsParse;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.ArrayList;
import java.util.List;

@Parameters(commandDescription  = "Run TestCase")
public class CommandRun{
//    @Parameter(description = "testcase file path", converter=ArgsParseListConverter.class)

    @Parameter(description = "testcase file path")
    private ArrayList<String> testcase_paths;

    public List<String> getTestcase_paths(){
        return this.testcase_paths;
    }
}