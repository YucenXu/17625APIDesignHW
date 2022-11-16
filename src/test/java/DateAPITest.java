import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateAPITest {
    static CloseableHttpClient httpClient;
    static int validUserID;
    static int invalidUserID;

    @BeforeAll
    static void init() {
        try {
            MainServer.main(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpClient = HttpClientBuilder.create().build();
        validUserID = 1;
        invalidUserID = -1;
    }

    @AfterAll
    static void cleanUp() {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Successful Tests
    @Test
    void testDate_success() {
        JSONObject response;
        try {
            HttpGet request = new HttpGet("http://localhost:8080/date/date/" + validUserID);
            request.addHeader("content-type", "application/json");
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return;
        }
        String pattern = "yyyy-MM-dd HH:mm:ss";
        assert response.get("result").equals(new SimpleDateFormat(pattern).format(Calendar.getInstance().getTime()));
    }

    @Test
    void testDay_success() {
        JSONObject response;
        try {
            HttpGet request = new HttpGet("http://localhost:8080/date/day/" + validUserID);
            request.addHeader("content-type", "application/json");
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return;
        }
        String pattern = "dd";
        assert response.get("result").equals(new SimpleDateFormat(pattern).format(Calendar.getInstance().getTime()));
    }

    @Test
    void testMonth_success() {
        JSONObject response;
        try {
            HttpGet request = new HttpGet("http://localhost:8080/date/month/" + validUserID);
            request.addHeader("content-type", "application/json");
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return;
        }
        String pattern = "MM";
        assert response.get("result").equals(new SimpleDateFormat(pattern).format(Calendar.getInstance().getTime()));
    }

    @Test
    void testYear_success() {
        JSONObject response;
        try {
            HttpGet request = new HttpGet("http://localhost:8080/date/year/" + validUserID);
            request.addHeader("content-type", "application/json");
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return;
        }
        String pattern = "yyyy";
        assert response.get("result").equals(new SimpleDateFormat(pattern).format(Calendar.getInstance().getTime()));
    }

    // Failing Tests
    @Test
    void testDate_fail() {
        JSONObject response;
        try {
            HttpGet request = new HttpGet("http://localhost:8080/date/date/" + invalidUserID);
            request.addHeader("content-type", "application/json");
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return;
        }
        assert response.get("status").equals(400);
        assert response.get("message").equals("Invalid User ID");
    }

    @Test
    void testDay_fail() {
        JSONObject response;
        try {
            HttpGet request = new HttpGet("http://localhost:8080/date/day/" + invalidUserID);
            request.addHeader("content-type", "application/json");
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return;
        }
        assert response.get("status").equals(400);
        assert response.get("message").equals("Invalid User ID");
    }

    @Test
    void testMonth_fail() {
        JSONObject response;
        try {
            HttpGet request = new HttpGet("http://localhost:8080/date/month/" + invalidUserID);
            request.addHeader("content-type", "application/json");
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return;
        }
        assert response.get("status").equals(400);
        assert response.get("message").equals("Invalid User ID");
    }

    @Test
    void testYear_fail() {
        JSONObject response;
        try {
            HttpGet request = new HttpGet("http://localhost:8080/date/year/" + invalidUserID);
            request.addHeader("content-type", "application/json");
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return;
        }
        assert response.get("status").equals(400);
        assert response.get("message").equals("Invalid User ID");
    }
}
