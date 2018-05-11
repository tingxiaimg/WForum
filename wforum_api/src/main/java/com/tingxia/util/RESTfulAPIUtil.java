package com.tingxia.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RESTfulAPIUtil {
    private Map<String,Object> apiData;
    private Map<String,Object> data;

    public RESTfulAPIUtil(){
        apiData = new HashMap<>();
        apiData.put("status",200);
        apiData.put("data",null);
        apiData.put("type","json");
        apiData.put("error","");
        apiData.put("message","ok");
    }

    public void setStatusCode(StatusCode statusCode){
        apiData.put("status",statusCode.getCode());
        apiData.put("error",statusCode.getError());
    }
    public void setMassage(String massage){
        if(!"".equals(massage)){
            apiData.put("message",massage.trim());
        }
    }
    public <T> void addNode(String key,T value){
        if(!"data".equals(key)) {
            apiData.put(key, value);
        }
    }
    public void setData(Map data){
        if(this.data == null ){
            this.data = new HashMap<>();
        }
        this.data.putAll(data);
    }

    public void putData(String key,Object value){
        if(this.data == null){
            this.data = new HashMap<>();
        }
        this.data.put(key,value);
    }

    public Map getAPI(){
        apiData.put("type","json");
        apiData.put("data",this.data);
        return this.apiData;
    }

}
