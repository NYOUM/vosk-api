package org.vosk;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Result {
    public String text;

    @Nullable
    public List<WordResult> result;

    public Result(String text, List<WordResult> result) {
        this.text = text;
        this.result = result;
    }

}
