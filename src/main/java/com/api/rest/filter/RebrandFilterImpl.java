package com.api.rest.filter;

import com.api.utils.Log;
import io.restassured.http.Header;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RebrandFilterImpl extends RequestFilter{

    private static final Log log = new Log(RebrandFilterImpl.class);
    private String result = "";
    private static final String VINID = "vinid";

    public RebrandFilterImpl() {
    }

    public boolean condition() {
        return !this.result.isEmpty();
    }

    private boolean verifyResponseHeader() {
        List<Header> headers = this.response.getHeaders().asList();
        Iterator var2 = headers.iterator();

        Header h;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            h = (Header)var2.next();
        } while(!h.getName().contains("vinid") && !h.getValue().toLowerCase().contains("vinid"));

        this.result = this.result + "RESPONSE HEADER, ";
        return false;
    }

    private boolean verifyResponseBody() {
        String responseBody = this.response.getBody().asString();
        if (responseBody.toLowerCase().contains("vinid")) {
            this.result = this.result + "RESPONSE BODY, ";
            return false;
        } else {
            return true;
        }
    }

    private boolean verifyFormParam() {
        Map<String, String> formParams = this.requestSpec.getFormParams();
        Set<String> keys = formParams.keySet();
        Iterator var3 = keys.iterator();

        String k;
        do {
            if (!var3.hasNext()) {
                return true;
            }

            k = (String)var3.next();
        } while(!k.toLowerCase().contains("vinid") && !((String)formParams.get(k)).toLowerCase().contains("vinid"));

        this.result = this.result + "FORM PARAMS, ";
        return false;
    }

    private boolean verifyHeader() {
        List<Header> headers = this.requestSpec.getHeaders().asList();
        Iterator var2 = headers.iterator();

        Header h;
        do {
            if (!var2.hasNext()) {
                return true;
            }

            h = (Header)var2.next();
        } while(!h.getName().toLowerCase().contains("vinid") && !h.getValue().toLowerCase().contains("vinid"));

        this.result = this.result + "REQUEST HEADER, ";
        return false;
    }

    private boolean verifyParameter() {
        Map<String, String> requestParams = this.requestSpec.getRequestParams();
        Set<String> keys = requestParams.keySet();
        Iterator var3 = keys.iterator();

        String k;
        do {
            if (!var3.hasNext()) {
                return true;
            }

            k = (String)var3.next();
        } while(!k.toLowerCase().contains("vinid") && !((String)requestParams.get(k)).toLowerCase().contains("vinid"));

        this.result = this.result + "PARAMETERS, ";
        return false;
    }

    private boolean verifyBaseUri() {
        String baseUri = this.requestSpec.getBaseUri();
        if (baseUri.toLowerCase().contains("vinid")) {
            this.result = this.result + "REQUEST URI, ";
            return false;
        } else {
            return true;
        }
    }

    public void action() {
        log.error("Rebrand 'vinid' was failed in: " + this.result);
    }

}
