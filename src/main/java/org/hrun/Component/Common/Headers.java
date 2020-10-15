package org.hrun.Component.Common;

import lombok.Data;
import org.hrun.Component.LazyContent.LazyContent;
import org.hrun.Component.LazyContent.LazyString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Headers implements Serializable {
    private HashMap<String, LazyContent> content = new HashMap<String,LazyContent>();

    public Headers(Map raw_headers) {
        for (Map.Entry<String, Object> entry : ((HashMap<String, Object>) raw_headers).entrySet()) {
            if (entry.getValue() instanceof String )
                content.put(entry.getKey(), new LazyString(String.valueOf(entry.getValue())));
            else
                content.put(entry.getKey(), new LazyContent(entry.getValue()));
        }
    }

    public void parse(Set check_variables_set) {
        if(this.content == null || this.content.size() == 0)
            return;

        for(LazyContent value : content.values()){
            if(value instanceof LazyString)
                ((LazyString)value).parse(check_variables_set);
        }
    }

    public Headers to_value(Variables variables_mapping) {
        if(this.content == null || this.content.size() == 0)
            return this;

        for(LazyContent value : content.values()){
            if(value instanceof LazyString)
                ((LazyString)value).to_value(variables_mapping);
        }
        return this;
    }

    public Boolean isEmpty(){
        return (content == null || content.size() == 0);
    }

    public Map<String,String> toMap(){
        Map<String,String> headerMap = this.content.entrySet().stream().collect(
                Collectors.toMap(
                        entry -> entry.getKey(), entry -> String.valueOf(Optional.ofNullable(entry.getValue().getEvalValue()).orElse(""))
                )
        );

        return headerMap;
    }

    public void update(Headers headers){
        //TODO:
    }
}
