package com.indiealexh;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class WhoamiResourceTest {

    @Test
    public void testWhoamiEndpoint() {
        given()
          .when().get("/")
          .then()
             .statusCode(200)
             .body(containsString("Hostname:"))
             .body(containsString("IP:"))
             .body(containsString("RemoteAddr:"))
             .body(containsString("GET / HTTP"));
    }
}