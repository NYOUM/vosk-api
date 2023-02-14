package org.vosk;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoveRecognizer {

    private final Recognizer recognizer;

    public LoveRecognizer(Recognizer recognizer) { this.recognizer = recognizer; }

    public boolean acceptWaveForm(short[] audioBuffer, int readerLength) {
        return recognizer.acceptWaveForm(audioBuffer, readerLength);
    }

    public Result getResult() {
        return extractResult(recognizer.getResult());
    }

    public PartialResult getPartialResult() {
        return extractPartialResult(recognizer.getPartialResult());
    }

    public Result getFinalResult() {
        return extractResult(recognizer.getFinalResult());
    }

    public void setWords(boolean words) {
        recognizer.setWords(words);
    }

    public void setPartialWords(boolean partialWords) {
        recognizer.setPartialWords(partialWords);
    }

    private PartialResult extractPartialResult(String partialResult) {
        try {
            JSONObject partialObject = new JSONObject(partialResult);
            
            PartialResult deserialized = new PartialResult(
                    partialObject.getString("partial"),
                    deserializeWords(partialObject, "partial_result")
            );

            return deserialized;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Result extractResult(String result) {
        try {
            JSONObject resultJSON = new JSONObject(result);

            Result deserialized = new Result(
                    resultJSON.getString("text"),
                    deserializeWords(resultJSON, "result")
            );

            return deserialized;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    private List<WordResult> deserializeWords(JSONObject json, String fieldName) throws JSONException {

        // no need to include Gson/Jackson for something trivial
        JSONArray wordsJSON = json.optJSONArray(fieldName);
        if (wordsJSON == null) {
            return null;
        }

        List<WordResult> words = new ArrayList<>(wordsJSON.length());
        for (int i = 0; i < wordsJSON.length(); i++) {
            JSONObject wordJSON = wordsJSON.getJSONObject(i);
            WordResult word = new WordResult(
                    wordJSON.getDouble ("conf"),
                    wordJSON.getDouble("start"),
                    wordJSON.getDouble("end"),
                    wordJSON.getString("word")
            );
            words.add(word);
        }

        return words;
    }
}
