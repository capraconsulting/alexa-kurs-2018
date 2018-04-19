package no.capra.workshop.functions;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import no.capra.workshop.util.LambdaHelper;
import no.capra.workshop.util.WorkshopHelper;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.String.format;

public class ProvideKeyword implements Function<Map<String, Slot>, SpeechletResponse> {

    private static final String WORKSHOP_API_URL = System.getenv("WORKSHOP_API_URL");
    private static final String ROOM_NUMBER = System.getenv("ROOM_NUMBER");

    /**
     * This method is called when provideKeyword Intent is triggered
     */
    @Override
    public SpeechletResponse apply(Map<String, Slot> slots) {

        return Stream.of(slots)
                .map(map -> map.get("teamKeyword"))
                .map(Optional::ofNullable)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Slot::getValue)
                .map(this::callProvideKeywordLambdaFunction)
                .filter(result -> result.equals(true))
                .map(success -> WorkshopHelper.convertTextToSpeechResponse("You made it. Check the color on the light bulb."))
                .findAny()
                .orElse(WorkshopHelper.getIDontUnderstandResponse());
    }

    /**
     * Method calling the lambda function "provideKeyword" with keyword as a query param
     * Returns true if lambda method have been triggered.
     * Returns false if the call fails.
     */
    private Boolean callProvideKeywordLambdaFunction(String keyword) {
        String url = format("provide-keyword-%s?keyword=%s", ROOM_NUMBER, keyword); // This does not seem right
        return LambdaHelper.callProvideKeywordLambdaFunction(url);
    }
}
