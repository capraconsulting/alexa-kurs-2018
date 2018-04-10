package no.capra.workshop;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

public class CapraWorkshopRequestHandler extends SpeechletRequestStreamHandler {

    private static final Logger log = LoggerFactory.getLogger(CapraWorkshopRequestHandler.class);

    /**
     * This does not seem interesting.
     * I suggest looking at CapraWorkshopSpeechlet.
     */
    public CapraWorkshopRequestHandler() {
        super(new CapraWorkshopSpeechlet(), new HashSet<>());
    }
}
