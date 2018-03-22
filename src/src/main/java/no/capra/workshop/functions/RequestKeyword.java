package no.capra.workshop.functions;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;


public class RequestKeyword implements Function<Map<String, Slot>, SpeechletResponse> {

    private static final Logger log = LoggerFactory.getLogger(RequestKeyword.class);

    @Override
    public SpeechletResponse apply(Map<String, Slot> slots) {

        return Stream.of(slots)
                     .map(map -> map.get("teamName"))
                     .map(Optional::ofNullable)
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .map(Slot::getValue)
                     .peek(slot -> log.info(String.format("The slots is %s ", slot)))
                     .map(this::callRequestKeywordLambdaFunction)
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .map(keyword -> {
                         PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
                         speech.setText(String.format("Your team's keyword is %s", keyword));
                         return speech; })
                     .map(SpeechletResponse::newTellResponse)
                     .findAny()
                     .orElse(defaultSpeechletResponse());
    }

    private Optional<String> callRequestKeywordLambdaFunction(String teamname) {
        return Stream.of(performGet(teamname))
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .map(this::handleResponse)
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .findAny();
    }

    private Optional<String> handleResponse(HttpURLConnection con) {
        try {
            int responseCode = con.getResponseCode();
            log.debug("Response Code : " + responseCode);

            String keyword = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            JSONParser parser = new JSONParser();
            JSONObject event = (JSONObject) parser.parse(in);
            log.debug("event " + event.toString());
            if (event.get("keyword") != null) {
                keyword = (String) event.get("keyword");
            }
            in.close();

            return Optional.ofNullable(keyword);
        }catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<HttpURLConnection> performGet(String teamname) {
        HttpURLConnection con = null;
        try {
            String url = String.format("https://sh8yiobwrk.execute-api.us-east-1.amazonaws.com/Develop/CapraWorkshopRequestKeyword?teamname=%s", teamname);
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.flush();
            wr.close();

            log.debug("\nSending 'GET' request to URL : " + url);
            return Optional.ofNullable(con);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    private SpeechletResponse defaultSpeechletResponse() {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText("I don't understand your request.");

        return SpeechletResponse.newTellResponse(speech);
    }

}
