package org.hrun.Component.LazyContent;

import com.alibaba.fastjson.annotation.JSONField;
import org.hrun.Component.Parseable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class LazyString extends LazyContent<String> implements Serializable, Parseable {
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

    @JSONField(serialize=false)
    private Boolean isLazyString; //是否是需要懒加载的字符串，false代表单纯的字符串，true则需要解析其中的变量或者方法

    @JSONField(serialize=false)
    private String evalString;



    public LazyString(){

    }

    public LazyString(String str){

    }
}
