package org.hrun;

import org.hrun.HrunArgsParse.ArgsParse;
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
        JCommander jCommander = JCommander.newBuilder()
                .addObject(argsParse)
                .build();

        try {
            jCommander.parse(args);
        }catch(Exception e){
            logger.error("不支持的命令行参数，请输入正确的命令！");
            jCommander.usage();
            return;
        }



        System.out.println( "Hello World!" );
    }
}
