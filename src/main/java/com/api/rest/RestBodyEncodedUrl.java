package com.api.rest;

import java.util.HashMap;
import java.util.Map;

public class RestBodyEncodedUrl {

    private Map<String, Object> encodedUrlMap = new HashMap();

    public RestBodyEncodedUrl() {
    }

    public void add(String key, Object value) {
        this.encodedUrlMap.put(key, value);
    }

    public Object get(String key) {
        return this.encodedUrlMap.get(key);
    }

    public void remove(String key) {
        this.encodedUrlMap.remove(key);
    }

    public Map<String, Object> getEncodedUrlMap() {
        return this.encodedUrlMap;
    }
}
