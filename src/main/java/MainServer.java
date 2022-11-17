import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;

/**
 * Main entrance to the api server.
 */
public class MainServer extends ServerResource {
    public static void main(String[] args) throws Exception {
        Component component = new Component();

        component.getServers().add(Protocol.HTTP, 8080);

        MainApp application = new MainApp();
        component.getDefaultHost().attachDefault(application);

        component.start();
    }
}
