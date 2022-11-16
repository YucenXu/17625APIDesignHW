import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;

public class MainServer extends ServerResource {
    public static void main(String[] args) throws Exception {
//        Event newEvent = new Event("event with xxx", "2022-11-07 08:00:00", "desc", 1, 2);
//        EventList.eventList.put(newEvent.eventID, newEvent);
        Component component = new Component();

        component.getServers().add(Protocol.HTTP, 8080);

        MainApp application = new MainApp();
        component.getDefaultHost().attachDefault(application);

        component.start();
    }
}
