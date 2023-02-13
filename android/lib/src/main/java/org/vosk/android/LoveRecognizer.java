package org.vosk.android;

import org.json.JSONException;
import org.json.JSONObject;
import org.vosk.Recognizer;

public class LoveRecognizer {

    private final Recognizer recognizer;

    public LoveRecognizer(Recognizer recognizer) { this.recognizer = recognizer; }

    public boolean acceptWaveForm(byte[] audioBuffer, int readerLength) {
        return recognizer.acceptWaveForm(audioBuffer, readerLength);
    }

    public String getResult() {
        return recognizer.getResult();
    }

    public String getPartialResult() {
        return recognizer.getPartialResult();
    }

    public String getFinalResult() {
        return recognizer.getFinalResult();
    }

    public void setWords(boolean words) {
        recognizer.setWords(words);
    }

    public void setPartialWords(boolean partialWords) {
        recognizer.setWords(partialWords);
    }

    public String extractPartialResult(String partialResult) {
        try {
            JSONObject partialObject = new JSONObject(partialResult);
            return partialObject.getString("partial");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String extractFinalResult(String finalResult) {
        try {
            JSONObject partialObject = new JSONObject(finalResult);
            return partialObject.getString("text");
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
