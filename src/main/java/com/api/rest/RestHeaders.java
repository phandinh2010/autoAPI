package com.api.rest;

import java.util.HashMap;
import java.util.Map;

public class RestHeaders {

    private Map<String, String> headers = new HashMap();

    public RestHeaders() {
    }

    /** @deprecated */
    @Deprecated
    public RestHeaders addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public RestHeaders add(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    /** @deprecated */
    @Deprecated
    public RestHeaders removeHeader(String key) {
        this.headers.remove(key);
        return this;
    }

    public RestHeaders remove(String key) {
        this.headers.remove(key);
        return this;
    }

    /** @deprecated */
    @Deprecated
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public Map<String, String> getAll() {
        return this.headers;
    }

    /** @deprecated */
    @Deprecated
    public void addHeader(Map<String, String> map) {
        this.headers.putAll(map);
    }

    public void add(Map<String, String> map) {
        this.headers.putAll(map);
    }
}
