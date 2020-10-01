package org.hrun;

import org.hrun.HrunArgsParse.ArgsParse;
import org.hrun.HrunArgsParse.CommandRun;
import org.hrun.HrunArgsParse.CommandMake;
import org.hrun.HrunArgsParse.CommandScaffold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;


import java.nio.file.Paths;
import java.util.Optional;

/**
 * Hello world!
 *
 */
public class App 
{
    static public String version = "3.1.4";

    static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {
        ArgsParse argsParse = new ArgsParse();
        CommandRun commandRun = new CommandRun();
        CommandMake commandMake = new CommandMake();
        CommandScaffold commandScaffold = new CommandScaffold();

        JCommander jCommander = JCommander.newBuilder()
                .addObject(argsParse)
                .addCommand("run", commandRun)
                .addCommand("make", commandMake)
                .addCommand("scaffold", commandScaffold)
                .build();

        try {
            jCommander.parse(args);
        }catch(Exception e){
            logger.error("不支持的命令行参数，请输入正确的命令！");
            jCommander.usage();
            return;
        }

        if(args.length >= 2 && args[2].equals("run")){


        }

        System.out.println(commandRun.getTestcase_paths());
    }
}
