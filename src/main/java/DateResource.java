import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class DateResource extends BaseResource{
    public String processRequest(Map json, String method) {
        JSONObject response = new JSONObject();
        String type = (String) getRequestAttributes().get("type");
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
        response.put("result", new SimpleDateFormat(pattern).format(Calendar.getInstance().getTime()));
        return response.toString();
    }
}
