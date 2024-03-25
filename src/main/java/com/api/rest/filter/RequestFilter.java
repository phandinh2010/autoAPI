package vinid.api.rest.filter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;


public abstract class RequestFilter implements Filter {

    protected Response response;
    protected FilterableRequestSpecification requestSpec;
    protected FilterableResponseSpecification responseSpec;
    protected FilterContext ctx;

    public RequestFilter() {
    }

    public abstract boolean condition();

    public abstract void action();

    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        this.requestSpec = requestSpec;
        this.responseSpec = responseSpec;
        this.ctx = ctx;
        this.response = ctx.next(requestSpec, responseSpec);
        if (this.condition()) {
            this.action();
        }

        return this.response;
    }

}

