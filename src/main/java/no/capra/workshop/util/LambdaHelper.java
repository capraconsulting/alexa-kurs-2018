package no.capra.workshop.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Stream;

public class LambdaHelper {

    public static Boolean callProvideKeywordLambdaFunction(String url) {
        try {
            HttpURLConnection con = getHttpUrlConnection(url);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Optional<String> callRequestKeywordLambdaFunction(String url) {
        return Stream.of(performGet(url))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(LambdaHelper::handleResponse)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }

    private static Optional<String> handleResponse(HttpURLConnection con) {
        try {
            String keyword = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JSONParser parser = new JSONParser();
            JSONObject event = (JSONObject) parser.parse(in);
            if (event.get("keyword") != null) {
                keyword = (String) event.get("keyword");
            }
            in.close();

            return Optional.ofNullable(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static HttpURLConnection getHttpUrlConnection(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        return con;
    }

    private static Optional<HttpURLConnection> performGet(String url) {
        try {
            HttpURLConnection con = getHttpUrlConnection(url);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.flush();
            wr.close();

            return Optional.ofNullable(con);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();

        }
    }
}
