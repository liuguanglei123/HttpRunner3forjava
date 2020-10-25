package org.hrun.Component.Common;

import lombok.Data;
import org.hrun.Component.LazyContent.LazyContent;
import org.hrun.Component.LazyContent.LazyString;
import org.hrun.Component.ParseableIntf;
import org.hrun.Component.VariablesMapping;
import org.hrun.Utils;

import java.io.Serializable;
import java.util.*;

@Data
public class Variables implements Serializable {
    public HashMap<String, LazyContent> content = new HashMap<String,LazyContent>();

    //默认构造函数，用来构造一个空的variables对象
    public Variables(){

    }

    public Variables(Map raw_variables) {
        for (Map.Entry<String, Object> entry : ((HashMap<String, Object>) raw_variables).entrySet()) {
            if (entry.getValue() instanceof String)
                content.put(entry.getKey(), new LazyString(String.valueOf(entry.getValue())));
            else if(entry.getValue() instanceof LazyContent)
                content.put(entry.getKey(),(LazyContent)entry.getValue());
            else
                content.put(entry.getKey(), new LazyContent(entry.getValue()));
        }
    }

    public void setVariables(Map<String,Object> raw_variables){
        for(Map.Entry<String,Object> entry : raw_variables.entrySet()){
            if (entry.getValue() instanceof String)
                content.put(entry.getKey(), new LazyString(String.valueOf(entry.getValue())));
            else
                content.put(entry.getKey(), new LazyContent(entry.getValue()));
        }
    }

    public void setVariables(String key, Object value){
        Map tmpMap = new HashMap<String,Object>();
        tmpMap.put(key,value);
        setVariables(tmpMap);
    }

    public void parse(Set check_variables_set) {
        if(this.content == null || this.content.size() == 0)
            return;

        for(LazyContent value : content.values()){
            if(value instanceof LazyString)
                ((LazyString)value).parse(check_variables_set);
        }
    }

    public ParseableIntf to_value(Variables variables_mapping) {
        //TODO:
        return null;
    }

    /**
     * 在原variables对象上进行扩展
     * @param another_var 另一个Var对象
     */
    public void extend(Variables another_var){
        Optional.ofNullable(another_var).ifPresent(
                a -> this.getContent().putAll(a.getContent())
        );
    }

    public void extend(Map param){
        Optional.ofNullable(param).ifPresent(
                a -> this.getContent().putAll(new Variables(param).getContent())
        );
    }

    /**
     * 合并两个Variables对象然后返回合并后的Variables对象
     * @param var1
     * @param var2
     * var2 的优先级比 var1 高
     */
    public static Variables extend2Variables(Variables var1,Variables var2){
        Variables override_variables_mapping = Utils.deepcopy_dict(Optional.ofNullable(var1).orElse(new Variables()));
        override_variables_mapping.extend(Utils.deepcopy_dict(var2));

        return override_variables_mapping;
    }

    /**
     * 静态方法，判断Var对象中的variables变量是否为空，静态方法的目的是防止参数本身就是空的
     * @param variables
     */
    public static Boolean isNullOrEmpty(Variables variables){
        if(variables == null || variables.getContent().isEmpty())
            return true;
        return false;
    }

    /**
     * 非静态方法，判断Var对象中的variables变量是否为空
     */
    public Boolean isEmpty(){
        if(this.content.isEmpty())
            return true;
        return false;
    }

    public LazyContent getVariable(String name){
        //TODO: 获得variables中的某个value，但是如果value是$var形式，需要再次转化或者什么
        if(this.content.get(name) != null){
            return this.content.get(name);
        }else{
            return null;
        }
    }

    public Integer getSize(){
        return this.content.size();
    }

    public Variables update(Variables variables){
        extend(variables);
        return this;
    }

    public Variables update(Map param){
        extend(param);
        return this;
    }

    public Variables update(VariablesMapping variablesMapping){
        //TODO：
        return this;
    }

    public Set<String> getKeys(){
        return this.content.keySet();
    }

    public LazyContent get(String key){
        return this.content.get(key);
    }



}
