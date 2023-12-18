import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;

//import com.google.gson.Gson;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

public class APITesting_WCTax {
    private RequestSpecification mRequestSpec;

    @Before
    public void setup() {
        Map<String, String> pathParams = new HashMap<String, String>();
        pathParams.put("json", "true");
        pathParams.put("locale", "en_US");
        pathParams.put("status", "any");

        RequestSpecBuilder requestBuilder = new RequestSpecBuilder().
            setBaseUri("https://my.sitebay.org/f/api/v1").
            setBasePath("rest/v1.1/jetpack-blogs/173063404/rest-api/").
            addQueryParams(pathParams).
            setAuth(oauth2(System.getenv("API_TEST_OAUTH_KEY")));
        this.mRequestSpec = requestBuilder.build();    
    }

    @Test
    public void canGetTaxClasses() {
        given().
            spec(this.mRequestSpec).
            queryParam("path", "/wc/v3/taxes/classes").
        when().
            get().
        then().
            statusCode(200).
            body("data", hasSize(3),
                "data.slug", hasItems("standard", "reduced-rate", "zero-rate")
            );
    }
}
