package org.hrun.Component.Common;

import lombok.Data;
import org.hrun.Component.LazyContent.LazyContent;
import org.hrun.Component.LazyContent.LazyString;
import org.hrun.Component.Parseable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class Params implements Serializable, Parseable {
    private HashMap<String, LazyContent> params = new HashMap<String,LazyContent>();

    public Params(Map raw_params) {
        for (Map.Entry<String, Object> entry : ((HashMap<String, Object>) raw_params).entrySet()) {
            if (entry.getValue() instanceof String )
                params.put(entry.getKey(), new LazyString(String.valueOf(entry.getValue())));
            else
                params.put(entry.getKey(), new LazyContent(entry.getValue()));
        }
    }

    public void parse(Set check_variables_set) {
        if(this.params == null || this.params.size() == 0)
            return;

        for(LazyContent value : params.values()){
            if(value instanceof LazyString)
                ((LazyString)value).parse(check_variables_set);
        }
    }

    public Parseable to_value(Variables variables_mapping) {
        if(this.params == null || this.params.size() == 0)
            return this;

        for(LazyContent value : params.values()){
            if(value instanceof LazyString)
                ((LazyString)value).to_value(variables_mapping);
        }

        return this;
    }

    public Boolean isEmpty(){
        return (params == null || params.size() == 0);
    }
}