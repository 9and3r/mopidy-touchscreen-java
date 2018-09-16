package com.and3r.mopidytouchscreenjava.mopidy;

import com.google.gson.Gson;

import java.util.HashMap;

public class MopidyRequest {

    private static final Gson gson = new Gson();
    private static int currentID = 0;

    public String jsonrpc = "2.0";
    public int id;
    public String method;
    private HashMap<String, Object> params;

    public MopidyRequest(String method){
        currentID++;
        this.id = currentID;
        this.method = method;
        this.params = new HashMap<>();
    }

    public void addParam(String key, Object value){
        params.put(key, value);
    }

    public String toJSONString(){
        return gson.toJson(this);
    }

}
