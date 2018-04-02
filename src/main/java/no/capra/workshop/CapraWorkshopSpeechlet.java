package no.capra.workshop;

import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import no.capra.workshop.functions.HelpSpeechletResponse;
import no.capra.workshop.functions.ProvideKeyword;
import no.capra.workshop.functions.RequestKeyword;
import no.capra.workshop.util.CapraWorkshopUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class CapraWorkshopSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(CapraWorkshopSpeechlet.class);
    private static final Map<String, Function<Map<String, Slot>, SpeechletResponse>> intentMap = new HashMap<>();

    static {
        intentMap.put("getKeyword", new RequestKeyword());
        intentMap.put("provideKeyword", new ProvideKeyword());
        intentMap.put("AMAZON.HelpIntent", new HelpSpeechletResponse());
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

        return Stream.of(request.getIntent())
                .map(Optional::ofNullable)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(intent -> log.info(String.format("Intent %s with slots %s ", intent.getName(), intent.getSlots())))
                .map(intent -> intentMap.get(intent.getName()).apply(intent.getSlots()))
                .findAny()
                .orElse(CapraWorkshopUtil.defaultSpeechletResponse());
    }

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        // an entrypoint for handling a speech initiated request to start the skill withour providing an Intent
        return null;
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
        // any cleanup logic goes here
    }
}
