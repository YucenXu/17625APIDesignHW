import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestUtils {
    // https://stackoverflow.com/questions/18073849/get-a-json-object-from-a-http-response
    public static JSONObject getResponse(HttpResponse response) throws IOException {
        return convertStreamToJSON(response.getEntity().getContent());
    }

    // https://stackoverflow.com/questions/18073849/get-a-json-object-from-a-http-response
    public static JSONObject convertStreamToJSON(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject(sb.toString());
    }
}
