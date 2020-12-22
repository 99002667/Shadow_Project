package com.example.ende;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class PlaySine {

    private int sampleCount;
    private final AudioTrack audioTrack;
    private static final int AMPLITUDE = 32767;
    private static final int SAMPLE_RATE = 48000;
    private static final double TWO_PI = Math.PI * 2;
    private static final int SAMPLE_COUNT_SCALE = 100;
    int BUFFER_SIZE = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);  //// Contains Estimated Minimum buffer size required for an AudioTrack

    /** Constructor Class */
    public PlaySine() {
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE, AudioTrack.MODE_STATIC);
    }



    /** Generate Sine Wave from 20Hz to 20KHz */
    public void setWave(int frequency) {
        sampleCount = (int) (((float) SAMPLE_RATE / SAMPLE_COUNT_SCALE));
        short[] samples = new short[sampleCount];
        double phase = 0;

        for (int i = 0; i < sampleCount; i++) {
            samples[i] = (short) (AMPLITUDE * Math.sin(phase));
            phase += TWO_PI * frequency / SAMPLE_RATE;
        }
        audioTrack.write(samples, 0, sampleCount);  //Writes the audio data to the audio sink for playback (streaming mode), or copies audio data for later playback (static buffer mode).
    }



    /** Reloads and Plays the Wave or Starts the Wave generation*/
    public void start() {
        audioTrack.reloadStaticData();
        audioTrack.setLoopPoints(0, sampleCount, -1);
        audioTrack.play();
    }


    /** Stops the Wave Generation */
    public void stop() {
        audioTrack.flush();
        audioTrack.stop();
    }

}
