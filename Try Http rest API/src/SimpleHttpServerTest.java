import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleHttpServerTest {

    @Test
    public void testGetRequest() throws IOException {
        // Send GET request to server
        URL url = new URL("http://localhost:8081/api/v1/21229");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Read response from server
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Verify that the response is correct
        String expectedResponse = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        assertEquals(expectedResponse, response.toString());
    }

    @Test
    public void testPostRequest() throws IOException {
        // Send POST request to server
        URL url = new URL("http://localhost:8081/api/v1/state");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        // Set request body
        String requestBody = "{\"key\":\"value\"}";
        con.getOutputStream().write(requestBody.getBytes());

        // Read response from server
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Verify that the response is correct
        String expectedResponse = "OK";
        assertEquals(expectedResponse, response.toString());
    }
}
