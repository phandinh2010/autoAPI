package com.api.rest;

import java.util.HashMap;
import java.util.Map;

public class RestParams {

    private Map<String, Object> params = new HashMap();

    public RestParams() {
    }

    public void addParam(String key, Object value) {
        this.params.put(key, value);
    }

    public void remove(String key) {
        this.params.remove(key);
    }

    public void removeAll() {
        this.params.clear();
    }

    public Map<String, Object> getParams() {
        return this.params;
    }
}
