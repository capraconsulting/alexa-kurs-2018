package no.capra.workshop.functions;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class ProvideKeyword implements Function<Map<String, Slot>, SpeechletResponse> {

    private static final Logger log = LoggerFactory.getLogger(ProvideKeyword.class);

    @Override
    public SpeechletResponse apply(Map<String, Slot> slots) {

        return Stream.of(slots)
                     .map(map -> map.get("teamKeyword"))
                     .map(Optional::ofNullable)
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .map(Slot::getValue)
                     .peek(slot -> log.info(String.format("The slots is %s ", slot)))
                     .map(this::callProvideKeywordLambdaFunction)
                     .filter(result -> result.equals(true))
                     .map(success -> {
                         PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
                         speech.setText("You made it. Check the color on the light bulb.");
                         return speech; })
                     .map(SpeechletResponse::newTellResponse)
                     .findAny()
                     .orElse(defaultSpeechletResponse());
    }

    private SpeechletResponse defaultSpeechletResponse() {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText("I don't understand your request.");

        return SpeechletResponse.newTellResponse(speech);
    }

    private Boolean callProvideKeywordLambdaFunction(String keyword) {
        try {
            String url = String.format("https://sh8yiobwrk.execute-api.us-east-1.amazonaws.com/Develop/CapraWorkshopProvideKeyword?keyword=%s", keyword);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.flush();
            wr.close();

            log.debug("\nSending 'GET' request to URL : " + url);

            int responseCode = con.getResponseCode();
            log.debug("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //print result
            log.debug(response.toString());

            in.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
