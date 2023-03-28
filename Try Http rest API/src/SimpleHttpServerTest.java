import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SimpleHttpServerTest {

    @Test
    void testGetRequest() throws Exception {
        // Start the server on a separate thread
        new Thread(() -> {
            try {
                SimpleHttpServer.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Wait for the server to start
        Thread.sleep(1000);

        // Send a GET request to the server and read the response
        try (Socket socket = new Socket("localhost", 8081);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send the GET request to the server
            out.println("GET /api/v1/21229 HTTP/1.1");
            out.println("Host: localhost:8081");
            out.println("Connection: close");
            out.println();

            // Read the response from the server
            ArrayList<String> lines = SimpleHttpServer.ReadAllLines(in);

            // Verify that the response is valid
            Assertions.assertEquals("HTTP/1.1 200 OK", lines.get(0));
            Assertions.assertEquals("Content-Type: application/json", lines.get(1));
            Assertions.assertTrue(lines.get(2).startsWith("Content-Length: "));
            Assertions.assertEquals("", lines.get(3));

            // TODO: Verify the JSON data in the response
        }

        // Stop the server
        // Note: In a real-world scenario, you might want to use a more graceful shutdown mechanism
        System.exit(0);
    }

    @Test
    void testPostRequest() throws Exception {
        // Start the server on a separate thread
        new Thread(() -> {
            try {
                SimpleHttpServer.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Wait for the server to start
        Thread.sleep(1000);

        // Send a POST request to the server and read the response
        try (Socket socket = new Socket("localhost", 8081);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send the POST request to the server
            String postData = "{\"key\":\"value\"}";
            out.println("POST /api/v1/state HTTP/1.1");
            out.println("Host: localhost:8081");
            out.println("Content-Type: application/json");
            out.println("Content-Length: " + postData.getBytes().length);
            out.println();
            out.println(postData);

            // Read the response from the server
            ArrayList<String> lines = SimpleHttpServer.ReadAllLines(in);

            // Verify that the response is valid
            Assertions.assertEquals("HTTP/1.1 200 OK", lines.get(0));
            Assertions.assertEquals("Content-Type: application/json", lines.get(1));
            Assertions.assertTrue(lines.get(2).startsWith("Content-Length: "));
            Assertions.assertEquals("", lines.get(3));

            // Verify the response data
            Assertions.assertEquals("OK", lines.get(4));
        }

        // Stop the server
        // Note: In a real-world scenario, you might want to use a more graceful shutdown mechanism
        System.exit(0);
    }
}
