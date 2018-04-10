package no.capra.workshop.functions;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import no.capra.workshop.util.LambdaHelper;
import no.capra.workshop.util.WorkshopHelper;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.String.*;


public class RequestKeyword implements Function<Map<String, Slot>, SpeechletResponse> {

    private static final String REQUEST_KEYWORD_URL = "https://m9ezyn00zd.execute-api.us-east-1.amazonaws.com/Develop/requestKeywordRoomOne";

    /**
     * This method is called when requestKeyword Intent is triggered
     */
    @Override
    public SpeechletResponse apply(Map<String, Slot> slots) {
        return Stream.of(slots)
                .map(map -> map.get("teamName"))
                .map(Optional::ofNullable)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Slot::getValue)
                .map(this::callRequestKeywordLambdaFunction)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(keyword -> WorkshopHelper.convertTextToSpeechResponse(format("Your team's keyword is %s", keyword)))
                .findAny()
                .orElse(WorkshopHelper.getIDontUnderstandResponse());
    }

    /**
     * Method calling the lambda function "requestKeyword" with teamname as a query param
     * Returns the teamname as a string
     */
    private Optional<String> callRequestKeywordLambdaFunction(String teamname) {
        String url = format("%s?teamname=%s", REQUEST_KEYWORD_URL, teamname);
        return LambdaHelper.callRequestKeywordLambdaFunction(url);
    }


}
