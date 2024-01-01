package vinid.api.rest;


import java.io.File;
import java.util.Iterator;
import java.util.List;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.JsonConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import vinid.api.rest.auth.RestAuth;
import vinid.api.rest.filter.RebrandFilterImpl;
import vinid.api.rest.filter.RequestFilter;
import vinid.api.utils.Log;


public class RestRequest {

    private Log requestLog = new Log(RestRequest.class);
    private String url;
    private String path;
    private RestHeaders header;
    private RestBody body;
    private GraphqlBody graphqlBody;
    private MultipartUtility FormPart;
    private RestMethod method;
    private RestParams params;
    private CurlConverter curlConverter;
    private RequestSpecification requestSpec;
    private RequestSpecBuilder requestSpecBuilder;
    private RestAssuredConfig config;
    private RequestFilter filter;
    private int connectionTimeout;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;

    public RestRequest(String url, String path, RestMethod method) {
        this.url = url;
        this.path = path;
        this.method = method;
        this.init(30000);
    }

    public RestRequest(String url, String path, RestMethod method, int connectionTimeout) {
        this.url = url;
        this.path = path;
        this.method = method;
        this.init(connectionTimeout);
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setFilter(RequestFilter filter) {
        this.filter = filter;
    }

    private void init(int connectionTimeout) {
        this.header = new RestHeaders();
        this.body = new RestBody();
        this.params = new RestParams();
        this.requestSpecBuilder = new RequestSpecBuilder();
        this.requestSpecBuilder.setBaseUri(this.url);
        this.curlConverter = new CurlConverter(this.url, this.path, this.method.toString());
        this.config = RestAssured.config().redirect(RedirectConfig.redirectConfig().followRedirects(true)).jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)).httpClient(HttpClientConfig.httpClientConfig().setParam("http.connection.timeout", connectionTimeout).setParam("http.socket.timeout", connectionTimeout));
        this.useDefaultFilter();
    }

    public void useDefaultFilter() {
        this.setFilter(new RebrandFilterImpl());
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RestHeaders getHeader() {
        return this.header;
    }

    public void setHeader(RestHeaders header) {
        this.header = header;
        this.requestSpecBuilder.addHeaders(this.header.getAll());
        this.curlConverter.setHeaders(this.header);
    }

    public RestBody getBody() {
        return this.body;
    }

    public void setBody(RestBody body) {
        this.body = body;
        String b = this.body.print();
        this.requestSpecBuilder.setBody(b).setContentType(ContentType.JSON);
        this.curlConverter.setBodyCurl(this.body);
    }

    public void setGraphqlBody(GraphqlBody graphqlBody) {
        this.graphqlBody = graphqlBody;
        this.requestSpecBuilder.setBody(this.graphqlBody.getBody());
    }

    public void setFormPart(String parentkey, MultipartUtility FormPart) {
        this.FormPart = FormPart;
        this.requestSpecBuilder.addFormParam(this.FormPart.Print_FormData().toString(), new Object[0]);
        this.curlConverter.setFormPartCurl(parentkey, FormPart);
    }

    public void setBodyEncodedUrl(RestBodyEncodedUrl bodyEncodedUrl) {
        this.requestSpecBuilder.addFormParams(bodyEncodedUrl.getEncodedUrlMap());
    }

    public void setMultiPart(BodyMultiPart multiPart) {
        List<MultiPartInfo> multiPartInfos = multiPart.getMultiPartInfos();
        Iterator var3 = multiPartInfos.iterator();

        while(var3.hasNext()) {
            MultiPartInfo multiPartInfo = (MultiPartInfo)var3.next();
            String key = multiPartInfo.getKey();
            String value = multiPartInfo.getValue();
            MultiPartType type = multiPartInfo.getType();
            if (type == MultiPartType.FILE) {
                this.requestSpecBuilder.addMultiPart(key, new File(value));
            } else if (type == MultiPartType.TEXT) {
                this.requestSpecBuilder.addMultiPart(key, value);
            }
        }

    }

    public RestParams getParams() {
        return this.params;
    }

    public void setParams(RestParams params) {
        this.params = params;
        if (this.method == RestMethod.GET) {
            this.requestSpecBuilder.addParams(this.params.getParams());
        } else {
            this.requestSpecBuilder.addQueryParams(this.params.getParams());
        }

        this.curlConverter.setParams(params);
    }

    public void setAuth(RestAuth authScheme) {
        this.requestSpecBuilder.setAuth(authScheme.getAuth());
    }

    public RestResponse send() {
        this.requestSpec = this.requestSpecBuilder.build();
        this.requestLog.info("Started %s", this.curlConverter.printCurl());
        switch (this.method) {
            case POST:
                return new RestResponse((Response)RestAssured.given().filter(this.filter).config(this.config).spec(this.requestSpec).when().post(this.path, new Object[0]));
            case PUT:
                return new RestResponse((Response)RestAssured.given().filter(this.filter).config(this.config).spec(this.requestSpec).when().put(this.path, new Object[0]));
            case PATCH:
                return new RestResponse((Response)RestAssured.given().filter(this.filter).config(this.config).spec(this.requestSpec).when().patch(this.path, new Object[0]));
            case DELETE:
                return new RestResponse((Response)RestAssured.given().filter(this.filter).config(this.config).spec(this.requestSpec).when().delete(this.path, new Object[0]));
            case GET:
            default:
                return new RestResponse((Response)RestAssured.given().filter(this.filter).config(this.config).spec(this.requestSpec).when().get(this.path, new Object[0]));
        }
    }

    public RestResponse sendWithLog() {
        this.requestSpec = this.requestSpecBuilder.build();
        switch (this.method) {
            case POST:
                return new RestResponse((Response)((RequestSpecification) RestAssured.given().config(this.config).spec(this.requestSpec).log().all()).when().post(this.path, new Object[0]));
            case PUT:
                return new RestResponse((Response)((RequestSpecification)RestAssured.given().config(this.config).spec(this.requestSpec).log().all()).when().put(this.path, new Object[0]));
            case PATCH:
                return new RestResponse((Response)((RequestSpecification)RestAssured.given().config(this.config).spec(this.requestSpec).log().all()).when().patch(this.path, new Object[0]));
            case DELETE:
                return new RestResponse((Response)((RequestSpecification)RestAssured.given().config(this.config).spec(this.requestSpec).log().all()).when().delete(this.path, new Object[0]));
            case GET:
            default:
                return new RestResponse((Response)((RequestSpecification)RestAssured.given().config(this.config).spec(this.requestSpec).log().all()).when().get(this.path, new Object[0]));
        }
    }

    public String toString() {
        return this.curlConverter.printCurl();
    }

    public void setCookie(Cookies cookie) {
        this.requestSpec.cookies(cookie);
    }
}
