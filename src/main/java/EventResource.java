import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Event resource class
 */
public class EventResource extends BaseResource {
    /**
     * Process request according to resource.
     * @param json   request body
     * @param method request method
     * @return response string
     */
    public String processRequest(Map json, String method) throws Exception {
        switch (method) {
            case "get":
                return handleGet();
            case "post":
                return handlePost(json);
            case "put":
                return handlePut(json);
            case "delete":
                return handleDelete();
        }
        return "";
    }

    /**
     * Handle get request.
     * @return response string
     */
    public String handleGet() {
        JSONObject response = new JSONObject();
        Object eventID = getRequestAttributes().get("eventID");
        JSONArray eventArray = new JSONArray();
        if (eventID != null) {
            try {
                Long eventIDLong = Long.valueOf((String) eventID);
                Event event = EventList.eventList.get(eventIDLong);
                if (event == null) {
                    throw new Exception();
                } else {
                    eventArray.put(event.toJson());
                    response.put("status", 200);
                    response.put("events", eventArray);
                    return response.toString();
                }
            } catch (Exception ex) {
                response.put("message", "Invalid Event ID");
                response.put("status", 400);
                return response.toString();
            }
        }

        for (Event event : EventList.eventList.values()) {
            eventArray.put(event.toJson());
        }
        response.put("status", 200);
        response.put("events", eventArray);
        return response.toString();
    }

    /**
     * Handle post request.
     * @param json request body
     * @return response string
     */
    public String handlePost(Map json) {
        Event newEvent;
        JSONObject response = new JSONObject();
        try {
            String newEventName = (String) json.get("eventName");
            String newTime = (String) json.get("time");
            String newDescription = (String) json.get("description");
            Long newDoctorID = (Long) json.get("doctorID");
            Long newPatientID = (Long) json.get("patientID");
            if (newEventName == null || newTime == null || newDescription == null || newDescription == null || newDoctorID == null || newPatientID == null) {
                throw new Exception();
            }
            newEvent = new Event(newEventName, newEventName, newDescription, newDoctorID, newPatientID);
            EventList.eventList.put(newEvent.eventID, newEvent);
            response.put("eventID", newEvent.eventID);
            response.put("status", 200);
        } catch (Exception e) {
            response.put("message", "Invalid Parameters");
            response.put("status", 400);
        }
        return response.toString();
    }

    /**
     * Handle put request.
     * @param json request body
     * @return response string
     */
    public String handlePut(Map json) {
        JSONObject response = new JSONObject();
        try {
            Long eventID = (Long) json.get("eventID");
            String newEventName = (String) json.get("eventName");
            String newTime = (String) json.get("time");
            String newDescription = (String) json.get("description");
            Long newDoctorID = (Long) json.get("doctorID");
            Long newPatientID = (Long) json.get("patientID");
            if (eventID == null || newEventName == null || newTime == null || newDescription == null || newDescription == null || newDoctorID == null || newPatientID == null) {
                throw new Exception();
            }
            Event event = EventList.eventList.get(eventID);
            event.eventName = newEventName;
            event.time = newTime;
            event.description = newDescription;
            event.doctorID = newDoctorID;
            event.patientID = newPatientID;
            EventList.eventList.put(eventID, event);
            response.put("eventID", eventID);
            response.put("status", 200);
        } catch (Exception e) {
            response.put("message", "Invalid Parameters");
            response.put("status", 400);
        }
        return response.toString();
    }

    /**
     * Handle delete request.
     *
     * @return response string
     */
    public String handleDelete() {
        JSONObject response = new JSONObject();
        try {
            Long eventID = Long.valueOf((String) getRequestAttributes().get("eventID"));
            Event event = EventList.eventList.get(eventID);
            response.put("eventID", event.eventID);
            response.put("status", 200);
            EventList.eventList.remove(event.eventID);
        } catch (Exception e) {
            response.put("message", "Invalid Event ID");
            response.put("status", 400);
        }
        return response.toString();
    }
}
