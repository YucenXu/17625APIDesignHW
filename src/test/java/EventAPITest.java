import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EventAPITest {
    static Map<String, Object> payload = new HashMap<>();
    static CloseableHttpClient httpClient;

    @BeforeAll
    static void init() {
        try {
            MainServer.main(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpClient = HttpClientBuilder.create().build();
        payload.put("eventName", "event1");
        payload.put("time", "2022-11-07 08:00:00");
        payload.put("description", "desc");
        payload.put("doctorID", 1);
        payload.put("patientID", 2);
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
    void testGetEvent_success() {
        // add new event
        int newEventID = addEvent_success();
        // get event
        int lastEventID = getLastEventID();
        // check id
        assert newEventID == lastEventID;
        // delete event
        int deletedID = deleteEvent(newEventID);
        // check deleted id
        assert deletedID == newEventID;
    }

    @Test
    void testPostEvent_success() {
        // add new event
        addEvent_success();
        // get previous id
        int lastEventID = getLastEventID();
        // add new event
        int newEventID = addEvent_success();
        // check id
        assert newEventID > lastEventID;
        // delete event
        int deletedID = deleteEvent(newEventID);
        // check deleted id
        assert deletedID == newEventID;
        // delete event
        deleteEvent(lastEventID);
    }

    @Test
    void testPutEvent_success() {
        // add new event
        int newEventID = addEvent_success();
        // update event
        JSONObject putResponse;
        payload.put("eventName", "event1_modified");
        payload.put("eventID", newEventID);
        try {
            HttpPut request = new HttpPut("http://localhost:8080/event");
            StringEntity params = new StringEntity(new JSONObject(payload).toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            putResponse = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return;
        }
        assert (int) putResponse.get("eventID") == newEventID;

        // check update
        JSONObject getResponse;
        try {
            HttpGet request = new HttpGet("http://localhost:8080/event");
            request.addHeader("content-type", "application/json");
            getResponse = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return;
        }

        JSONArray eventArray = (JSONArray) getResponse.get("events");
        String newEventName = eventArray.getJSONObject(eventArray.length() - 1).getString("eventName");
        assert newEventName.equals("event1_modified");

        // delete event
        int deletedID = deleteEvent(newEventID);
        assert deletedID == newEventID;
    }

    @Test
    void testDeleteEvent_success() {
        // add new event
        int newEventID = addEvent_success();
        // delete event
        int deletedID = deleteEvent(newEventID);
        // check deleted id
        assert deletedID == newEventID;
        // get event
        int lastEventID = getLastEventID();
        // check new event deleted
        assert lastEventID != newEventID;
    }

    // Failing Tests
    @Test
    void testGetEvent_fail() {
        // get with invalid id
        JSONObject response;
        try {
            HttpGet request = new HttpGet("http://localhost:8080/event/0");
            request.addHeader("content-type", "application/json");
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return;
        }
        // check message
        assert response.get("message").equals("Invalid Event ID");
    }

    @Test
    void testPostEvent_fail() {
        // add new event
        String message = addEvent_fail();
        // check message
        assert message.equals("Invalid Parameters");
    }

    @Test
    void testPutEvent_fail() {
        // add new event
        int newEventID = addEvent_success();
        // update event
        JSONObject putResponse;
        payload.remove("eventName");
        payload.put("eventID", newEventID);
        try {
            HttpPut request = new HttpPut("http://localhost:8080/event");
            StringEntity params = new StringEntity(new JSONObject(payload).toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            putResponse = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return;
        }
        assert putResponse.get("message").equals("Invalid Parameters");

        // delete event
        int deletedID = deleteEvent(newEventID);
        assert deletedID == newEventID;
        payload.put("eventName", "event1");
    }

    @Test
    void testDeleteEvent_fail() {
        JSONObject response;
        try {
            HttpDelete request = new HttpDelete("http://localhost:8080/event/" + 0);
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return;
        }
        assert response.get("status").equals(400);
        assert response.get("message").equals("Invalid Event ID");
    }


    // Helper methods for testing
    private int getLastEventID() {
        JSONObject response;
        try {
            HttpGet request = new HttpGet("http://localhost:8080/event");
            request.addHeader("content-type", "application/json");
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return -1;
        }
        assert response.get("status").equals(200);
        JSONArray eventArray = (JSONArray) response.get("events");
        if (eventArray.length() > 0) {
            return (int) eventArray.getJSONObject(eventArray.length() - 1).get("eventID");
        }
        return -1;
    }

    private int addEvent_success() {
        JSONObject response;
        try {
            HttpPost request = new HttpPost("http://localhost:8080/event");
            StringEntity params = new StringEntity(new JSONObject(payload).toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return -1;
        }
        assert response.get("status").equals(200);
        return (int) response.get("eventID");
    }

    private String addEvent_fail() {
        JSONObject response;
        payload.remove("eventName");
        try {
            HttpPost request = new HttpPost("http://localhost:8080/event");
            StringEntity params = new StringEntity(new JSONObject(payload).toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return "";
        }
        System.out.println(response);
        assert response.get("status").equals(400);
        payload.put("eventName", "event1");
        return (String) response.get("message");
    }

    private int deleteEvent(int eventID) {
        JSONObject response;
        try {
            HttpDelete request = new HttpDelete("http://localhost:8080/event/" + eventID);
            response = TestUtils.getResponse(httpClient.execute(request));
        } catch (Exception ex) {
            assert false;
            return -1;
        }
        assert response.get("status").equals(200);
        return (int) response.get("eventID");
    }
}
