import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.*;
import org.restlet.ext.json.JsonRepresentation;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for resources.
 */
public abstract class BaseResource extends ServerResource {
    String jsonString = "";

    /**
     * Handle get request.
     *
     * @return response
     */
    @SuppressWarnings("rawtypes")
    @Get
    public Representation doGet() {
        jsonString = "";
        try {
            jsonString = processRequest(null, "get");
        } catch (Exception e) {
            jsonString = getErrorResponse();
        }
        return new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
    }

    /**
     * Handle post request.
     *
     * @return response
     */
    @Post("json")
    public Representation doPost(Representation entity) {
        Map json;
        jsonString = "";
        try {
            // get request body
            JsonRepresentation represent = new JsonRepresentation(entity);
            JSONObject jsonobject = represent.getJsonObject();
            JSONParser parser = new JSONParser();
            String jsonText = jsonobject.toString();
            // put request body into map
            json = (Map) parser.parse(jsonText);
            // handle request
            jsonString = processRequest(json, "post");
        } catch (Exception e) {
            jsonString = getErrorResponse();
        }
        return new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
    }

    /**
     * Handle put request.
     *
     * @return response
     */
    @Put("json")
    public Representation doPut(Representation entity) {
        Map json = null;
        jsonString = "";
        try {
            // get request body
            JsonRepresentation represent = new JsonRepresentation(entity);
            JSONObject jsonobject = represent.getJsonObject();
            JSONParser parser = new JSONParser();
            String jsonText = jsonobject.toString();
            // put request body into map
            json = (Map) parser.parse(jsonText);
            // handle request
            jsonString = processRequest(json, "put");
        } catch (Exception e) {
            jsonString = getErrorResponse();
        }
        return new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
    }

    /**
     * Handle delete request.
     *
     * @return response
     */
    @Delete("json")
    public Representation doDelete(Representation entity) {
        try {
            jsonString = processRequest(null, "delete");
        } catch (Exception e) {
            jsonString = getErrorResponse();

        }
        return new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
    }

    /**
     * Abstract method to process request.
     *
     * @param json   request body
     * @param method request method
     * @return response string
     * @throws Exception
     */
    public abstract String processRequest(Map json, String method) throws Exception;

    /**
     * Get Internal Server Error response.
     *
     * @return response string
     */
    private String getErrorResponse() {
        JSONObject responseJson = new JSONObject();
        responseJson.put("status", 500);
        responseJson.put("message", "Internal Server Error");
        return responseJson.toString();
    }
}
