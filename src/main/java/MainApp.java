
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class MainApp extends Application{
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        // attach path to router
        router.attach("/event", EventResource.class);
        router.attach("/event/{eventID}", EventResource.class);
        router.attach("/date/{type}/{userID}", DateResource.class);
        return router;
    }
}