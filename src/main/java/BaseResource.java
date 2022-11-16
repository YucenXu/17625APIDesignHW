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

public abstract class BaseResource extends ServerResource {
    String jsonString="";


    @SuppressWarnings("rawtypes")
    @Get
    public Representation doGet(){
        Map json =null;
        jsonString = "";
        try {
            jsonString = processRequest(json,"get");
        } catch (Exception e) {
            jsonString = getErrorResponse();
        }
        return new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
    }

    @Post("json")
    public Representation doPost(Representation entity){
        Map json;
        jsonString = "";
        try {
            JsonRepresentation represent = new JsonRepresentation(entity);
            JSONObject jsonobject = represent.getJsonObject();
            JSONParser parser = new JSONParser();
            String jsonText = jsonobject.toString();
            json = (Map) parser.parse(jsonText);
            jsonString = processRequest(json,"post");
        } catch (Exception e) {
            jsonString = getErrorResponse();
        }
        return new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
    }

    @Put("json")
    public Representation doPut(Representation entity){
        Map json = null;
        jsonString = "";
        try {
            JsonRepresentation represent = new JsonRepresentation(entity);
            JSONObject jsonobject = represent.getJsonObject();
            JSONParser parser = new JSONParser();
            String jsonText = jsonobject.toString();
            json = (Map) parser.parse(jsonText);
            jsonString = processRequest(json,"put");
        } catch (Exception e) {
            jsonString = getErrorResponse();
        }
        return new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
    }

    @Delete("json")
    public Representation doDelete(Representation entity){
        try {
            jsonString = processRequest(null,"delete");
        } catch (Exception e) {
            jsonString = getErrorResponse();

        }
        return new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
    }

    public abstract String processRequest(Map json,String method) throws Exception;

//    public static Map<String, String> getMapFromParam(Form form) {
//        Map<String, String> map = new HashMap<String, String>();
//        for (Parameter parameter : form) {
//            map.put(parameter.getName(), parameter.getValue());
//        }
//        return map;
//    }

    private String getErrorResponse() {
        JSONObject responseJson = new JSONObject();
        responseJson.put("status", 500);
        responseJson.put("message", "Internal Server Error");
        return responseJson.toString();
    }
}
