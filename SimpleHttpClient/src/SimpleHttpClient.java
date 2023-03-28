import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleHttpClient {
    public static void main(String[] args) throws IOException {
        BufferedReader conIn = new BufferedReader(new InputStreamReader(System.in));

        // Get user input
        System.out.print("Method: ");
        String method = conIn.readLine();
        System.out.print("Route: ");
        String route = conIn.readLine();
        System.out.print("Data: ");
        String data = conIn.readLine();


        try {
            // Create route using input
            URL url = new URL("http://localhost:8081/api/v1/" + route);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);

            // Send request Data if method is POST
            if(method.equals("POST")) {
                con.setDoOutput(true);

                BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));

                wr.write(data);
                wr.write("\r\n");
                wr.write("\r\n");
                // Make sure to write it
                wr.flush();
                wr.close();
            }

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            // Read server response
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
