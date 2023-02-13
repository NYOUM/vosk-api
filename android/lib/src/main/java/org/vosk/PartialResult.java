package org.vosk;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PartialResult {
    public String partial;

    @Nullable
    public List<WordResult> partialResult;
}
