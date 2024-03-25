package vinid.api.rest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GraphqlBody {

    private JsonElement jsonElement = new JsonObject();

    public GraphqlBody(String body) {
        this.init(body);
    }

    private void init(String body) {
        this.jsonElement.getAsJsonObject().addProperty("query", body);
    }

    public String getBody() {
        return this.jsonElement.getAsJsonObject().toString();
    }
}
