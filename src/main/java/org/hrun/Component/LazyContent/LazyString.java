package org.hrun.Component.LazyContent;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hrun.Component.Common.Variables;
import org.hrun.Component.ParseableIntf;
import org.hrun.Utils;
import org.hrun.exceptions.HrunExceptionFactory;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hrun.Parse.*;

@Data
@Slf4j
public class LazyString extends LazyContent<String> implements Serializable, ParseableIntf {
    public static Pattern integer_regex_compile = Pattern.compile("(\\d+)");

    public static Pattern double_regex_compile = Pattern.compile("(\\d+)\\.(\\d+)");

    private Class functions_mapping_ptr; //是一个指针地址，指向的是全局的functions_mapping

    private Set check_variables_set; // 用来保存上下文可用的所有变量，在加载lazyString的真正值时用得到

    private Boolean cached; //是否需要保存在缓存当中，这个暂时不实现TODO

    /**
     * _args中存储的可能是可变参数值（String类型），比如遇到变量$ABC时会存储 (String)ABC
     * 也可能是LazyFunction对象，比如遇到
     *      ABC${func2()}DE$cdef
     *      那么args值是[ LazyFunction(${func2()}) , (String)cdef ]
     */
    private List<Object> _args = new ArrayList<Object>();

    private Boolean isLazyString; //是否是需要懒加载的字符串，false代表单纯的字符串，true则需要解析其中的变量或者方法

    private String evalString;//计算后的值转换成String

    /**
     * 用来存放解析后的中间状态，比如 ABC${func2()}DE$cdef，经过处理之后，_string值为 ABC${}DE$cdef,_args是["LazyFunction(${func2($a, $b)})", "c"]
     * 比如字符串是'https://api.ydl.com/api/delivery/$ban',最终整理出来的_string是'https://api.ydl.com/api/delivery/{}',_args 是 [ban]
     * 如果字符串是'$ban',最终整理出来的_string是'{}',_args 是 [ban]
     * 如果字符串是''${setup_hooks1($request)}'',最终整理出来的_string是'{}',_args 是 ['LazyFunction(setup_hooks1(LazyString($request)))]
     */
    private String _string = "";

    private Object evalValue = "";

    public LazyString(String str){
        super(str);
    }

    /**
     * 根据raw_value的内容进行判断是否要进行懒加载处理，同时检查变量是否存在于check_variables_set参数中
     * @param check_variables_set
     */
    public void parse(Set check_variables_set){
        if(is_var_or_func_exist(this.getRaw_value())){
            this.isLazyString = true;
            this.check_variables_set = Utils.deepcopy_obj(new HashSet(check_variables_set));
            this.__parse(this.getRaw_value());
        }else{
            this.isLazyString = false;
            // 已经测试过，此处的替换不需要加\\$
            this.setRaw_value(this.getRaw_value().replace("$$","$"));
        }
    }

    /**
     * 懒加载处理的主要方法，解析原始String中的值，将原始值中包含的方法和参数用{}进行替换，替换后的String变量，放在_string中
     * 比如原始值是 ABC${func2($a, $b)}DE${c}defg，处理后变成
     *                                          _string: "ABC{}DE{}defg"
     *                                          args: ["LazyFunction(${func2($a, $b)})", "c"]
     * @param raw_string 原始值
     */
    public void __parse(String raw_string){
        Integer match_start_position = 0;
        String begin_string;
        match_start_position = raw_string.indexOf("$");
        if(match_start_position != -1){
            begin_string = raw_string.substring(0,match_start_position);
            this._string = begin_string;
        }else{
            this._string = raw_string;
            return ;
        }

        while(match_start_position < raw_string.length()){
            Matcher dollar_match = dolloar_regex_compile.matcher(raw_string);
            if(dollar_match.find() && dollar_match.start() == match_start_position){
                match_start_position = dollar_match.end();
                this._string += "$";
            }

            Matcher func_match = function_regex_compile.matcher(raw_string);
            if(func_match.find(match_start_position) && func_match.start() == match_start_position){
                Map<String,String> function_meta = new HashMap<String,String>();
                function_meta.put("func_name",func_match.group(1));

                function_meta.putAll(parse_function_params(func_match.group(2)));

                // TODO:
                /*LazyFunction lazy_func = new LazyFunction(
                        function_meta,
                        this.check_variables_set
                );
//                this._lazy_func = lazy_func;
                this._args.add(lazy_func);*/
                match_start_position = func_match.end();
                this._string += "{}";
                continue;
            }

            Matcher var_match = variable_regex_compile.matcher(raw_string);
            if(var_match.find(match_start_position) && var_match.start() == match_start_position){
                String var_name;
                if(var_match.group(1) == null || var_match.group(1).equals(""))
                    var_name = var_match.group(2);
                else
                    var_name = var_match.group(1);

                if(!this.check_variables_set.contains(var_name))
                    HrunExceptionFactory.create("E0022");
                this._args.add(var_name);
                match_start_position = var_match.end();
                this._string += "{}";
                continue;
            }

            Integer curr_position = match_start_position;
            String remain_string;

            match_start_position = raw_string.indexOf("$", curr_position+1);
            if(match_start_position == -1){
                remain_string = raw_string.substring(curr_position);
                match_start_position = raw_string.length();
            }else{
                remain_string = raw_string.substring(curr_position,match_start_position);
            }

            this._string += escape_braces(remain_string);
        }
    }

    public String escape_braces(String origin_string){
        return origin_string.replace("{", "{{").replace("}", "}}");
    }

    public static Map parse_function_params(String params) {
        Map<String, Object> function_meta = new HashMap<String, Object>() {{
            put("argsList", new ArrayList<String>());
            put("kwargsMap", new HashMap<String, Object>());
        }};

        String params_str = params.trim();
        if (params.equals(""))
            return function_meta;

        String[] args_list = params_str.split(",");
        for (String arg : args_list) {
            arg = arg.trim();
            if (arg.contains("=")) {
                String[] keyvalue = arg.split("=");
                if (keyvalue.length > 2)
                    HrunExceptionFactory.create("E0021");
                ((Map) function_meta.get("kwargsMap")).put(keyvalue[0].trim(), parse_string_value(keyvalue[1].trim()));
            } else {
                ((List) function_meta.get("argsList")).add(arg);
            }
        }
        return function_meta;
    }

    public static Object parse_string_value(String str_value){
        if(integer_regex_compile.matcher(str_value).matches() == true){
            Integer integernum = Integer.valueOf(str_value);
            return integernum;
        }
        else if(double_regex_compile.matcher(str_value).matches() == true){
            Double doublenum = Double.valueOf(str_value);
            return doublenum;
        }
        else if(str_value.equals("true") || str_value.equals("false")){
            Boolean boolvalue = Boolean.valueOf(str_value);
            return boolvalue;
        }else
            return str_value;
    }

    public LazyString to_value(Variables variables_mapping){
        if(this.isLazyString == null || this.isLazyString == false){
            return this;
        }
        // args是一个解析后的实际变量值集合，比如
        // _args : 【 $a,$function($b,$c) 】
        // 其中a值是 "weihao",function执行后结果是 (int)1024,那么
        // args是【 “weihao", (int)1024 】
        List<Object> args = new ArrayList<>();

//        if(this._lazy_func != null){
//            //TODO: function to_value
//
//        }

        /*TODO:
           for(Object arg: this._args){
            if(arg instanceof LazyFunction){
                //TODO: need analyse
                try {
                    args.add((((LazyFunction) arg).to_value(variables_mapping)));
                }catch(Exception e){
                    log.error("执行函数出错，执行的函数是 %s",((LazyFunction) arg).getRaw_str());
                }
            }else{
                Object var_value = get_mapping_variable((String)arg, variables_mapping);
                args.add(var_value);
            }
        }*/
        if(this._string.equals("{}")) {
            this.evalValue = args.get(0);
            this.evalString = String.valueOf(args.get(0));
            return this;
        }
        else{
            for(Object arg: args){
                this._string = this._string.replaceFirst("\\{\\}",String.valueOf(arg));
            }
        }

        this.evalString = this._string;
        this.evalValue = this._string;
        return this;
    }

    // TODO: 检查是否有报错
    //  为什么要检查，因为之前的版本中get_mapping_variable返回的是String对象，现在需要返回实际的变量的类型，对于header中的类型可能都需要是string才行，这里可能会报错
    /*public String get_mapping_variable(String variable_name,Variables variables_mapping){
        if(variables_mapping.getVariable(variable_name) == null){
            log.error(String.format("%s is not found",variable_name));
            HrunExceptionFactory.create("E0022");
        }

        String result = String.valueOf(variables_mapping.getVariable(variable_name).getRaw_value());
        return result;
    }*/

    public Object get_mapping_variable(String variable_name,Variables variables_mapping){
        if(variables_mapping.getVariable(variable_name) == null){
            log.error(String.format("%s is not found",variable_name));
            HrunExceptionFactory.create("E0022");
        }
        Object result = variables_mapping.getVariable(variable_name).getRaw_value();
        return result;
    }

    /**
     * 获得解析后的实际String
     * @return
     */
    @Deprecated
    public String getEvalString(){
        if(this.isLazyString == null || this.isLazyString == false)
            return this.getRaw_value();
        else
            return this.evalString;
    }

    /**
     * 获得解析后的实际值，类型不限于String，使用的时候需要注意类型判断，如果未解析或者解析错误，会返回空
     * @return
     */
    @Override
    public Object getEvalValue(){
        if(this.isLazyString == null || this.isLazyString == false)
            return this.getRaw_value();
        else
            //TODO: 如果evalValue是空怎么办
            return this.evalValue;
    }

    @Deprecated
    public Object getRealValue(){
        if(this.evalValue != null && !this.evalValue.equals(""))
            return this.evalValue;
        else if(this.evalString != null && !this.evalString.equals("")){
            return this.evalString;
        }else{
            return this.getRaw_value();
        }
    }

}
