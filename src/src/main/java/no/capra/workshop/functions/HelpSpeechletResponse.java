package no.capra.workshop.functions;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.SpeechletResponse;
import no.capra.workshop.util.CapraWorkshopUtil;

import java.util.Map;
import java.util.function.Function;

public class HelpSpeechletResponse implements Function<java.util.Map<String, Slot>, SpeechletResponse> {
    @Override
    public SpeechletResponse apply(Map<String, Slot> stringSlotMap) {
        return CapraWorkshopUtil.defaultSpeechletResponse();
    }
}
