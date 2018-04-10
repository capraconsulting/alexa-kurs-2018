package no.capra.workshop;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import no.capra.workshop.functions.HelpSpeechletResponse;
import no.capra.workshop.functions.ProvideKeyword;
import no.capra.workshop.functions.RequestKeyword;
import no.capra.workshop.util.WorkshopHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.*;

public class CapraWorkshopSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(CapraWorkshopSpeechlet.class);
    private static final Map<String, Function<Map<String, Slot>, SpeechletResponse>> intentMap = new HashMap<>();

    static {
        /**
         * Map over Intents (as you have defined in Alexa skill console)
         * and which Java method should be used
         */
        intentMap.put("getKeyword", new RequestKeyword());
        intentMap.put("AMAZON.HelpIntent", new HelpSpeechletResponse());
    }

    /**
     * This method will be triggered when the Alexa skill
     * triggers the AWS Lambda-function running this code
     */
    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) {
        Intent intent = request.getIntent();
        log.info(format("Intent %s called with slots %s", intent.getName(), intent.getSlots()));

        SpeechletResponse response = WorkshopHelper.callFunctionForIntent(intentMap, intent);
        return response;
    }

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
        // Any initialization logic goes here.
        // Ignore this for now.
    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        // An entrypoint for handling a speech initiated request to starte the skill without providing an Intent.
        // Ignore this for now.
        return null;
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
        // Any cleanup logic goes here.
        // Ignore this for now.
    }
}
