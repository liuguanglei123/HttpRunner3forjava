package org.hrun;

import com.google.common.base.Strings;
import org.hrun.Component.ProjectMeta;
import org.hrun.exceptions.HrunExceptionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private static Logger logger = LoggerFactory.getLogger(Loader.class);

    public static ProjectMeta project_meta;


    /*load testcases, .env, debugtalk.py functions.
        testcases folder is relative to project_root_directory
        by default, project_meta will be loaded only once, unless set reload to true.

    Args:
        test_path (str): test file/folder path, locate project RootDir from this path.
        reload: reload project meta if set true, default to false

    Returns:
        project loaded api/testcases definitions,
            environments and debugtalk.py functions.

    */
    public static ProjectMeta load_project_meta (String test_path){
        return load_project_meta(test_path,false);
    }

    public static ProjectMeta load_project_meta (String test_path, boolean reload){
        if(project_meta == null && !reload)
            return project_meta;

        project_meta = new ProjectMeta();

        if(Strings.isNullOrEmpty(test_path))
            return project_meta;

        List<String> result = locate_project_root_directory(test_path);
        String debugtalk_path = result.get(0);
        String project_root_directory = result.get(1);

        // TODO:# add project RootDir to sys.path
        // sys.path.insert(0, project_root_directory)

        //TODO:暂不支持加载.env文件

        //TODO:加载debugtalk文件并编译，放在1.1版本去做

        project_meta.setRootDir(project_root_directory);

        return project_meta;
    }

    public static List<String> locate_project_root_directory(String test_path){
        test_path = prepare_path(test_path);

        String debugtalk_path = locate_debugtalk_py(test_path);
        String project_root_directory;

        if(Strings.isNullOrEmpty(debugtalk_path)){
            project_root_directory = new File(debugtalk_path).getParentFile().getAbsolutePath();
        }else{
            project_root_directory = new File("").getAbsolutePath();
        }

        List<String> result = new ArrayList<String>() {{
            add(debugtalk_path);
            add(project_root_directory);
        }};

        return result;
    }

    public static String prepare_path(String path){
        File file = new File(path);
        if(!file.exists()){
            logger.error("path not exist:" + path);
            HrunExceptionFactory.create("E0004");
        }

        return file.getAbsolutePath();
    }

    public static String locate_debugtalk_py(String start_path){
        String debugtalk_path = "";
        try{
            debugtalk_path = locate_file(start_path, "debugtalk.py");
        }catch(Exception e){
        }
        return debugtalk_path;
    }

    public static String locate_file(String start_path, String file_name) {
        File file = new File(start_path);
        String start_dir_path = "";
        if (file.isFile())
            start_dir_path = file.getParentFile().getAbsolutePath();
        else if (file.isDirectory())
            start_dir_path = file.getAbsolutePath();
        else {
            logger.error("invalid path: " + start_path);
            HrunExceptionFactory.create("E0006");
        }

        Path path = Paths.get(start_dir_path).resolve(file_name);
        File resultFile = new File(path.toString());
        if (resultFile.exists() && resultFile.isFile())
            return resultFile.getAbsolutePath();


        /*
            TODO：hrun原版这里有个对根目录的判断，我觉得不需要，但需要测试，测试通过后可以把这个TODO删掉
         */

        // locate recursive upward
        return locate_file(new File(start_dir_path).getParentFile().getAbsolutePath(), file_name);
    }

}
