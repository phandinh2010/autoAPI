package vinid.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Main {
    public static void main(String[] args) {
        init();
        getListStudent();
    }

    public static void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/student";
        RestAssured.port = 8080;
    }

    public static void getListStudent() {
        Response res = given().when().get("/list");
        res.prettyPrint();
        res.then().statusCode(200);
    }
}
