package no.capra.workshop.util;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class WorkshopHelper {

    public static SpeechletResponse callFunctionForIntent(Map<String, Function<Map<String, Slot>, SpeechletResponse>> intentMap, Intent intent) {
        return Stream.of(intent)
                .map(Optional::ofNullable)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(i -> intentMap.get(i.getName()).apply(i.getSlots()))
                .findAny()
                .orElse(WorkshopHelper.getIDontUnderstandResponse());
    }

    public static SpeechletResponse getIDontUnderstandResponse() {
        return convertTextToSpeechResponse("I don't understand your request");
    }

    public static SpeechletResponse getVerifiedConnectionResponse() {
        return convertTextToSpeechResponse("You reached the lambda function. Now hurry up. The other teams are catching up.");
    }

    public static SpeechletResponse convertTextToSpeechResponse(String text) {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(text);
        return SpeechletResponse.newTellResponse(speech);
    }

}

