package com.api.rest;

import com.github.dzieciou.testing.curl.Platform;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CurlConverter {

    private CurlCommand curlCommand = new CurlCommand();
    String paramUrl = "?";
    String mainUrl;

    public CurlConverter(String host, String path, String method) {
        this.curlCommand.setMethod(method);
        this.mainUrl = host + path;
    }

    public void setHeaders(RestHeaders headers) {
        Map<String, String> head = headers.getHeaders();
        Set<String> keys = head.keySet();
        Iterator var4 = keys.iterator();

        while(var4.hasNext()) {
            String k = (String)var4.next();
            this.curlCommand.removeHeader(k);
            this.curlCommand.add(k, (String)head.get(k));
        }

    }

    public void setParams(RestParams params) {
        if (this.paramUrl.length() > 1) {
            this.paramUrl = "?";
        }

        Map<String, Object> param = params.getParams();
        Object[] keys = param.keySet().toArray();
        int keySize = keys.length;

        for(int i = 0; i < keySize; ++i) {
            if (i < keySize - 1) {
                this.paramUrl = this.paramUrl + keys[i] + "=" + param.get(keys[i]) + "&";
            } else {
                this.paramUrl = this.paramUrl + keys[i] + "=" + param.get(keys[i]);
            }
        }

    }

    public void setBodyCurl(RestBody body) {
        this.curlCommand.removeData();
        this.curlCommand.addData(body.prettyPrint());
    }

    public void setFormPartCurl(String parentKey, MultipartUtility FormPart) {
        this.curlCommand.removeData();
        this.curlCommand.addFormPart(parentKey, FormPart.Print_FormData());
    }

    public String printCurl() {
        if (this.paramUrl.length() > 1) {
            this.curlCommand.setUrl(this.mainUrl + this.paramUrl);
        } else {
            this.curlCommand.setUrl(this.mainUrl);
        }

        return this.curlCommand.asString(Platform.RECOGNIZE_AUTOMATICALLY, true, true);
    }
}
