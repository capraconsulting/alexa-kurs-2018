package no.capra.workshop.functions;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import no.capra.workshop.util.WorkshopHelper;

import java.util.Map;
import java.util.function.Function;

public class HelpSpeechletResponse implements Function<java.util.Map<String, Slot>, SpeechletResponse> {

    /**
     * This seems like something you can use to verify connection between the Alexa skill and AWS Lambda
     */
    @Override
    public SpeechletResponse apply(Map<String, Slot> stringSlotMap) {
        return WorkshopHelper.getVerifiedConnectionResponse();
    }
}
