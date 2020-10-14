package org.hrun.Component.LazyContent;

import lombok.Data;
import org.hrun.Component.Common.Variables;

import java.io.Serializable;
import java.util.regex.Matcher;

import static org.hrun.Parse.*;

@Data
public class LazyContent<T> implements Cloneable, Serializable {
    //用来记录原始值
    public T raw_value;

    //new一个新的懒加载对象，对于非String类型来说，LazyContent只需要存储原始值就可以了
    public LazyContent(T t){
        this.raw_value = t;
    }

    //默认to_value的场景，一般用于在初始化加载的时候
    public Object to_value(){
        return this.raw_value;
    }

    //用来计算实际懒加载对象所存储的值
    public Object to_value(Variables variables_mapping){
        return this.raw_value;
    }

    //用来判断传入的String是否包含$或者变量 方法名
    public static Boolean is_var_or_func_exist(String content){
        int match_start_position = 0;

        match_start_position = content.indexOf("$", 0);
        if(match_start_position == -1)
            return false;


        while(match_start_position < content.length()){
            Matcher dollar_match = dolloar_regex_compile.matcher(content);
            if(dollar_match.find(match_start_position)){
                match_start_position = dollar_match.end();
                continue;
            }

            Matcher func_match = function_regex_compile.matcher(content);
            if(func_match.find(match_start_position)){
                return true;
            }

            Matcher var_match = variable_regex_compile.matcher(content);
            if(var_match.find(match_start_position)){
                return true;
            }
            return false;
        }

        return false;
    }

    //用来判断是否包含方法
    public static Boolean is_func_exist(String content){
        Matcher var_match = function_regex_compile.matcher(content);
        if(var_match.find()){
            return true;
        }
        return false;
    }

    /**
     * 获得LazyContent对象的原始值 raw_value
     * @return
     */
    public T getRaw_value(){
        return this.raw_value;
    }

    /**
     * 获得LazyContent对象的实际值，如果对象是LazyString类型，返回其实际值realvalue，否则返回raw_value
     * @return
     */
    public Object getEvalValue(){
        if(this instanceof LazyString)
            return ((LazyString)this).getEvalValue();
        else
            return getRaw_value();
    }
}
