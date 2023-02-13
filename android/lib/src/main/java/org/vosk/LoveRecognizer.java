package org.vosk;

import org.json.JSONException;
import org.json.JSONObject;

public class LoveRecognizer {

    private final Recognizer recognizer;

    public LoveRecognizer(Recognizer recognizer) { this.recognizer = recognizer; }

    public boolean acceptWaveForm(short[] audioBuffer, int readerLength) {
        return recognizer.acceptWaveForm(audioBuffer, readerLength);
    }

    public String getResult() {
        // TODO Result deserialization
        return extractResult(recognizer.getResult());
    }

    public PartialResult getPartialResult() {
        return extractPartialResult(recognizer.getPartialResult());
    }

    public String getFinalResult() {
        // TODO Result deserialization
        return extractResult(recognizer.getFinalResult());
    }

    public void setWords(boolean words) {
        recognizer.setWords(words);
    }

    public void setPartialWords(boolean partialWords) {
        recognizer.setPartialWords(partialWords);
    }

    public PartialResult extractPartialResult(String partialResult) {
        try {
            JSONObject partialObject = new JSONObject(partialResult);
            
            PartialResult deserialized = new PartialResult();
            deserialized.partial = partialObject.getString("partial");

            // TODO partial_result list

            return deserialized;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String extractResult(String result) {
        try {
            JSONObject partialObject = new JSONObject(result);
            return partialObject.getString("text");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
