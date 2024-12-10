package rice.rbridge.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.*;
import java.util.Map;

public class API {
    static String MOJANG_UUID = "https://api.mojang.com/users/profiles/minecraft/";
    public static class ParameterStringBuilder {
        public static String getParamsString(Map<String, String> params)
                throws UnsupportedEncodingException{
            StringBuilder result = new StringBuilder("?");

            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            }

            String resultString = result.toString();
            return resultString.length() > 0
                    ? resultString.substring(0, resultString.length() - 1)
                    : resultString;
        }
    }

    private JsonObject toJson(StringBuilder jsonStr) {
        Gson g = new Gson();
        return g.fromJson(jsonStr.toString(), JsonObject.class);
    }

    private StringBuilder readInput(HttpURLConnection connection) throws IOException {
        String read = null;
        InputStreamReader isrObj = new InputStreamReader(connection.getInputStream());
        BufferedReader bf = new BufferedReader(isrObj);
        // to store the response from the servers
        StringBuilder responseStr = new StringBuilder();
        while ((read = bf .readLine()) != null) {
            responseStr.append(read);
        }
        bf.close();
        return responseStr;
    }
    public String getUUID(String name) throws IOException {
        URL urlForGetReq = new URL(MOJANG_UUID + name);
        HttpURLConnection connection = (HttpURLConnection) urlForGetReq.openConnection();
        connection.setRequestMethod("GET");

        int codeResponse = connection.getResponseCode();

        if (codeResponse != HttpURLConnection.HTTP_OK) {
            return null;
        }


        StringBuilder responseStr = readInput(connection);

        connection.disconnect();

        Gson g = new Gson();
        JsonObject jsonObject = g.fromJson(String.valueOf(responseStr), JsonObject.class);
        return jsonObject.get("id").toString().replace("\"","");
    }
    public String simpleGet(String url) throws IOException {
        try {
            URL urlForGetReq = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlForGetReq.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            int codeResponse = connection.getResponseCode();

            if (codeResponse != HttpURLConnection.HTTP_OK) {
                return null;
            }
            StringBuilder responseStr = readInput(connection);
            connection.disconnect();
            return String.valueOf(responseStr);
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }
    public String simplePost(String url, String jsonPayload) {
        try {
            HttpURLConnection connection = getHttpURLConnection(url, jsonPayload);

            // Get the response code
            int codeResponse = connection.getResponseCode();

            // Check if the response is HTTP_OK (200)
            if (codeResponse != HttpURLConnection.HTTP_OK) {
                System.out.println("Something went wrong! " + codeResponse + " " + connection.getResponseMessage());
                return null;
            }

            // Read the response
            StringBuilder responseStr = readInput(connection);
            connection.disconnect();
            return responseStr.toString();

        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    private static HttpURLConnection getHttpURLConnection(String url, String jsonPayload) throws IOException {
        URL urlForPostReq = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlForPostReq.openConnection();

        // Set the request method to POST
        connection.setRequestMethod("POST");

        // Set content type and other properties
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(5000);  // 5 seconds timeout
        connection.setReadTimeout(5000);     // 5 seconds timeout

        // Enable input/output streams
        connection.setDoOutput(true);

        // Write the JSON payload to the connection's output stream
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        return connection;
    }


}
