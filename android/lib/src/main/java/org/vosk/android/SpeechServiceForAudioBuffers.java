package org.vosk.android;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.vosk.Recognizer;

import java.lang.ref.WeakReference;

public class SpeechServiceForAudioBuffers {

    private final Recognizer recognizer;

    private RecognizerThread recognizerThread;

    private RecognitionListener recognitionListener;

    private Boolean isRecording;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public SpeechServiceForAudioBuffers(Recognizer recognizer) { this.recognizer = recognizer; }

    /**
     * Starts recognition. Does nothing if recognition is active.
     *
     * @return true if recognition was actually started
     */
    public boolean startListening() {
        if (null != recognizerThread)
            return false;

        recognizerThread = new RecognizerThread(recognitionListener);
        isRecording = true;
//        recognizerThread.start();
        return true;
    }

    public void attachListener(RecognitionListener recognitionListener) {
        this.recognitionListener = recognitionListener;
    }

    public void feedRecognizerBuffer(short[] audioBuffer, int readerLength) {
        recognizerThread.addAudioBuffer(audioBuffer, readerLength);
    }

    private boolean stopRecognizerThread() {
        if (null == recognizerThread)
            return false;

        try {
            recognizerThread.stopRecognition();
//            recognizerThread.interrupt();
//            recognizerThread.join();

            recognizerThread = null;
            return true;
        } catch (Exception e) {
            // Restore the interrupted status.
            Log.e("SpeechServiceFAB", "stopRecognizerThread() -> " + e.getLocalizedMessage());
            recognizerThread = null;
            return false;
        }
    }

    /**
     * Stops recognition. Listener should receive final result if there is
     * any. Does nothing if recognition is not active.
     *
     * @return true if recognition was actually stopped
     */
    public boolean stop() {
        isRecording = false;
        return stopRecognizerThread();
    }

    /**
     * Cancel recognition. Do not post any new events, simply cancel processing.
     * Does nothing if recognition is not active.
     *
     * @return true if recognition was actually stopped
     */
    public boolean cancel() {
        return stopRecognizerThread();
    }

    private final class RecognizerThread {

        WeakReference<RecognitionListener> listener;

        public RecognizerThread(RecognitionListener listener) {
            this.listener = new WeakReference(listener);
        }

        public void addAudioBuffer(short[] audioBuffer, int readerLength) {
            if (recognizer.acceptWaveForm(audioBuffer, readerLength)) {
                final String result = recognizer.getResult();
                Log.v("RecognizerThread", "addAudioBuffer() -> result: " + result);
                mainHandler.post(() -> listener.get().onResult(extractResult(result)));
            } else {
                final String partialResult = recognizer.getPartialResult();
                Log.v("RecognizerThread", "addAudioBuffer() -> partialResult: " + partialResult);
                mainHandler.post(() -> listener.get().onPartialResult(extractPartialResult(partialResult)));
            }
        }

        public void stopRecognition() {
            final String finalResult = recognizer.getFinalResult();
            Log.v("RecognizerThread", "stopRecognition() -> finalResult: " + finalResult);
            mainHandler.post(() -> listener.get().onFinalResult(extractFinalResult(finalResult)));
        }

//        @Override
//        public void run() {
//            while(isRecording) { /* Keeping Thread running while recording */ }
//        }
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
