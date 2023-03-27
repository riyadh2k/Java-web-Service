import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SimpleHttpServer {

    public static void main(String[] args) throws IOException {

        // Create a new server socket that listens on port 8081
        ServerSocket serverSocket = new ServerSocket(8081);
        ArrayList<String> state = new ArrayList<>();

        while (true) {
            // Loop forever, accepting new client connections
            Socket clientSocket = serverSocket.accept();

            // Print a message to indicate that a new client connection has been accepted
            System.out.println("Accepted connection from " + clientSocket.getInetAddress());

            // Create input and output streams for the client socket
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            ArrayList<String> lines = ReadAllLines(in);

            // If the method is GET and the path is /api/v1/21229, return the JSON data
            if (lines.size() > 1 && lines.get(0).startsWith("GET") && lines.get(0).contains("/api/v1/21229")) {
                // Return some data from file

                // Define the JSON data to return
                //String data = "{\"key\":\"value\"}";
                String data = new String(Files.readAllBytes(Paths.get("/Users/shabenur/Desktop/Java Web Service/Try Http RestAPI/Try Http rest API/src/data.json")));
                System.out.println(data);
                //Set HTTP response headers
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: application/json");
                out.println("Content-Length: " + data.getBytes().length);
                out.println();

                // Send the JSON data to the client
                out.println(data);
                // If the method is GET and the path is /api/v1/21229, return the JSON data
            } else if (lines.size() > 1 && lines.get(0).startsWith("GET") && lines.get(0).contains("/api/v1/state")) {
                // Return the state


                // Define the JSON data to return
                String data = new String(state.get(0));
                System.out.println(data);
                //Set HTTP response headers
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: application/json");
                out.println("Content-Length: " + data.getBytes().length);
                out.println();

                // Send the JSON data to the client
                out.println(data);
                // If the method is GET and the path is /api/v1/21229, return the JSON data
            } else if (lines.size() > 1 && lines.get(0).startsWith("POST") && lines.get(0).contains("/api/v1/state")) {
                // Set state from request body

                state = ReadAllLines(in);
                storeData(state.get(0));


                // Return "OK" to client
                String data = new String("OK");
                //System.out.println(data);
                //Set HTTP response headers
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: application/json");
                out.println("Content-Length: " + data.getBytes().length);
                out.println();

                // Send the JSON data to the client
                out.println(data);
            } else {
                // If the method or path is not recognized, return a 404 error
                out.println("HTTP/1.1 404 Not Found");
            }
            // Close the client socket
            clientSocket.close();
            //storeData();
        }

    }

    // Read everything until an empty line
    public static ArrayList<String> ReadAllLines(BufferedReader in) throws IOException {
        ArrayList<String> ret = new ArrayList<String>();

        while(true){
            String line = in.readLine();

            // Stream is closed
            if(line == null)
                return ret;

            // End of http message
            if(line.isEmpty())
                return ret;

            else ret.add(line);
        }
    }

    public static void storeData(String data){
        String jsonFilePath = "/Users/shabenur/Desktop/Java Web Service/Try Http RestAPI/Try Http rest API/src/data.json";

        // Parse the existing JSON data
        JSONObject jsonData = null;
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            jsonData = (JSONObject) JSONValue.parse(jsonContent);
        } catch (IOException e) {
            // Handle file read error
            e.printStackTrace();
        }

        // Assume we have received a POST request with JSON data
        JSONObject postData = new JSONObject();
        postData.put("data", data);

        // Merge the POST data into the existing JSON data
        if (jsonData == null) {
            jsonData = new JSONObject();
        }
        jsonData.putAll(postData);

        // Write the updated JSON data back to the file
        try (FileWriter file = new FileWriter(jsonFilePath)) {
            file.write(jsonData.toJSONString());
            file.flush();
        } catch (IOException e) {
            // Handle file write error
            e.printStackTrace();
        }
    }


}
