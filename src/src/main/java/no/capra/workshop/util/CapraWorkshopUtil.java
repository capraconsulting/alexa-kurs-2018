package no.capra.workshop.util;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;

public class CapraWorkshopUtil {


    public static SpeechletResponse defaultSpeechletResponse() {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText("You reached the lambda function. Now hurry up. The other teams are catching up.");

        return SpeechletResponse.newTellResponse(speech);
    }
}
