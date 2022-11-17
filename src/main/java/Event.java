import org.json.simple.JSONObject;

public class Event {
    static long idCount;
    public long eventID;
    public String eventName;
    public String time;
    public String description;
    public long doctorID;
    public long patientID;

    public Event(String eventName, String time, String description, long doctorID, long patientID) {
        this.eventName = eventName;
        this.time = time;
        this.description = description;
        this.doctorID = doctorID;
        this.patientID = patientID;
        idCount++;
        this.eventID = idCount;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventID", eventID);
        jsonObject.put("eventName", eventName);
        jsonObject.put("time", time);
        jsonObject.put("description", description);
        jsonObject.put("doctorID", doctorID);
        jsonObject.put("patientID", patientID);

        return jsonObject;
    }
}
