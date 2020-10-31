package org.hrun;

import org.hrun.Component.Common.Variables;
import org.hrun.Component.FunctionsMapping;
import org.hrun.Component.LazyContent.LazyContent;
import org.hrun.Component.LazyContent.LazyString;
import org.hrun.Component.Parseable;
import org.hrun.Component.TRequest;
import org.hrun.Component.VariablesMapping;
import org.hrun.exceptions.HrunExceptionFactory;

import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {

    //use $$ to escape $ notation
    static public Pattern dolloar_regex_compile = Pattern.compile("\\$\\$");

    //variable notation, e.g. ${var} or $var
    static public Pattern variable_regex_compile = Pattern.compile("\\$\\{(\\w+)\\}|\\$(\\w+)");

    //function notation, e.g. ${func1($var_1, $var_3)}
    static public Pattern function_regex_compile = Pattern.compile("\\$\\{(\\w+)\\(([\\$\\w\\.\\-/\\s=,]*)\\)\\}");


    public static Variables parse_variables_mapping(Variables variables_mapping){
        return parse_variables_mapping(variables_mapping,null);

    }

//    TODO:FunctionsMapping 是什么
//     public static VariablesMapping parse_variables_mapping(Variables variables_mapping, FunctionsMapping functions_mapping){
    public static Variables parse_variables_mapping(Variables variables_mapping, Object functions_mapping){
        Variables parsed_variables = new Variables();

        while(parsed_variables.getSize() != variables_mapping.getSize()){
            for(String var_name : variables_mapping.getKeys()){
                if(parsed_variables.getKeys().contains(var_name))
                    continue;

                LazyContent var_value = variables_mapping.get(var_name);
                Set variables = extract_variables(var_value);

                if (variables.contains(var_name))
                    HrunExceptionFactory.create("E0047");

                //TODO:写个lambda
//                List<String> not_defined_variables = [
//                    v_name for v_name in variables if v_name not in variables_mapping
//                ]


//                if(not_defined_variables != null && not_defined_variables.isEmpty())
//                    HrunExceptionFactory.create("E0047");



                Object parsed_value = null;

                try{
                    //TODO:parsed_value = parse_data(var_value,parsed_variables,functions_mapping);
                    if (var_value instanceof LazyString) {
                        parsed_value = parse_data((LazyString) var_value, parsed_variables,null).getEvalValue();
                    } else if (var_value instanceof LazyContent) {
                        parsed_value = parse_data((LazyContent) var_value, parsed_variables,null);
                    }
                }
                catch(Exception e){
                    continue;
                }

                parsed_variables.setVariables(var_name, parsed_value);
            }
        }

        return parsed_variables;
    }

    public static Set<String> extract_variables(LazyContent content){
        // extract all variables in content recursively.
        // TODO：这里应该支持加载list set map 等类型的数据的，但是现在不支持，需要后续支持
        if(content instanceof LazyString){
            return regex_findall_variables(((LazyString) content).getRaw_value());
        }

        return new HashSet<String>();
    }

    public static Set<String> regex_findall_variables(String content){
        Set<String> result = new HashSet<String>();
        int match_start_position = 0;

        while(match_start_position < content.length()){
            Matcher var_match = variable_regex_compile.matcher(content);
            if(var_match.find(match_start_position)) {
                String var_name;
                if(var_match.group(1) == null || var_match.group(1).equals(""))
                    var_name = var_match.group(2);
                else
                    var_name = var_match.group(1);

                result.add(var_name);
            }

            Integer curr_position = match_start_position;
            match_start_position = content.indexOf("$", curr_position+1);
            if(match_start_position == -1){
                content = content.substring(curr_position);
                match_start_position = content.length();
            }else{
                content = content.substring(curr_position,match_start_position);
            }
        }

        return result;
    }

    public static LazyString parse_data(LazyString content, Variables variables_mapping,Object functions_mapping) {
        //TODO: refactor type check hrun原版注释
        return content.to_value(variables_mapping);
    }

    public static Object parse_data(LazyContent content, Variables variables_mapping,Object functions_mapping) {
        //TODO: refactor type check hrun原版注释
        return content.to_value(variables_mapping);
    }

    public static Parseable parse_data(Parseable raw_data, Variables variables_mapping, Object functions_mapping){
        return raw_data.parse(variables_mapping,null);

    }


}
