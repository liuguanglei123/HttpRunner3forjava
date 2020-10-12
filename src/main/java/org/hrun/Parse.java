package org.hrun;

import org.hrun.Component.FunctionsMapping;
import org.hrun.Component.VariablesMapping;
import org.hrun.exceptions.HrunExceptionFactory;

import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Parse {

    //use $$ to escape $ notation
    static public Pattern dolloar_regex_compile = Pattern.compile("\\$\\$");

    //variable notation, e.g. ${var} or $var
    static public Pattern variable_regex_compile = Pattern.compile("\\$\\{(\\w+)\\}|\\$(\\w+)");

    //function notation, e.g. ${func1($var_1, $var_3)}
    static public Pattern function_regex_compile = Pattern.compile("\\$\\{(\\w+)\\(([\\$\\w\\.\\-/\\s=,]*)\\)\\}");


    public VariablesMapping parse_variables_mapping(VariablesMapping variables_mapping){
        return parse_variables_mapping(variables_mapping,null);

    }

    public VariablesMapping parse_variables_mapping(VariablesMapping variables_mapping, FunctionsMapping functions_mapping){
        VariablesMapping parsed_variables = new HashMap<Object,Object>();

        while(parsed_variables.size() != variables_mapping.size()){
            for(String var_name : variables_mapping.keySet()){
                if(parsed_variables.keySet().contains(var_name))
                    continue;

                Object var_value = variables_mapping.get(var_name);
                Set variables = extract_variables(var_value);

                if (variables.contains(var_name))
                    HrunExceptionFactory.create("E0047");

                //TODO:写个lambda
                List<String> not_defined_variables = [
                    v_name for v_name in variables if v_name not in variables_mapping
                ]
                if(not_defined_variables != null && not_defined_variables.isEmpty())
                    HrunExceptionFactory.create("E0047");

                try{
                    parsed_value = parse_data(var_value,parsed_variables,functions_mapping);
                }
                catch(Exception e){
                    continue;
                }

                parsed_variables.put(var_name,parsed_value);

            }
        }

        return parsed_variables;
    }

    public Set<String> extract_variables(){
        return new HashSet<String>();
    }
}
