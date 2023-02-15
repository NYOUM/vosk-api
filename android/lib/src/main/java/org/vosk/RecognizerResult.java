package org.vosk;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RecognizerResult {
    public String text;

    @Nullable
    public List<WordResult> result;

    public RecognizerResult(String text, List<WordResult> result) {
        this.text = text;
        this.result = result;
    }

}
