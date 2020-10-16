package org.hrun.Enum;

import lombok.Data;

public enum MethodEnum {
    GET("GET",1),POST("POST",2);

    private String method;

    private int index;

    MethodEnum(String method, int i) {
        this.method = method;
        this.index = i;
    }

    public static String getMethod(String method) {
        for (MethodEnum c : MethodEnum.values()) {
            if (c.getMethod().equals(method)) {
                return c.method;
            }
        }
        return null;
    }

    public int getIndex(){
        return this.index;
    }

    public String getMethod(){
        return this.method;
    }

    public static MethodEnum getMethodEnum(String method){
        for (MethodEnum c : MethodEnum.values()) {
            if (c.getMethod().equals(method)) {
                return c;
            }
        }
        return null;
    }
}
