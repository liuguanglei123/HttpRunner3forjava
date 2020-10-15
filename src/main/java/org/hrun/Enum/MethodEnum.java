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

    public static String getMethod(int index) {
        for (MethodEnum c : MethodEnum.values()) {
            if (c.getIndex() == index) {
                return c.method;
            }
        }
        return null;
    }

    public int getIndex(){
        return this.index;
    }


}
