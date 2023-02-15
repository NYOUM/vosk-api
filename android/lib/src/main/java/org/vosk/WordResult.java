package org.vosk;

public class WordResult {
    public double confidence;
    public double start;
    public double end;
    public String word;

    public WordResult(double confidence, double start, double end, String word) {
        this.confidence = confidence;
        this.start = start;
        this.end = end;
        this.word = word;
    }
}
