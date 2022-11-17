import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Date resource class
 */
public class DateResource extends BaseResource{
    /**
     * Process request according to resource.
     * @param json   request body
     * @param method request method
     * @return response string
     */
    public String processRequest(Map json, String method) {
        JSONObject response = new JSONObject();
        // get request resource
        String type = (String) getRequestAttributes().get("type");
        // get and check userID
        try {
            Integer userID = Integer.valueOf((String) getRequestAttributes().get("userID"));
            if (userID < 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            response.put("message", "Invalid User ID");
            response.put("status", 400);
            return response.toString();
        }

        // get date pattern according to requested resource
        String pattern = "";
        switch (type) {
            case "date":
                pattern = "yyyy-MM-dd HH:mm:ss";
                break;
            case "day":
                pattern = "dd";
                break;
            case "month":
                pattern = "MM";
                break;
            case "year":
                pattern = "yyyy";
                break;
        }

        // get and return response
        response.put("result", new SimpleDateFormat(pattern).format(Calendar.getInstance().getTime()));
        return response.toString();
    }
}
