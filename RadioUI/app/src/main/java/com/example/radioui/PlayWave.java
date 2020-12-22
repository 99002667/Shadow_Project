package com.example.radioui;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class PlayWave {

    private int sampleCount;
    private final AudioTrack audioTrack;
    private static final int SAMPLE_RATE = 192000;
    private static final int AMPLITUDE = 32767;
    private static final int SAMPLE_COUNT_SCALE = 10;
    private static final double TWO_PI = Math.PI * 2;
    private static final double SQUARE_LOWER_BIT = 0.0;



    /** Class Constructor */

    public PlayWave() {
        int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STATIC);
    }



    /** Generate Sine Wave of 20Hz to 20KHz */

    public void setWave(int frequency) {
        sampleCount = (int) ((float) SAMPLE_RATE / SAMPLE_COUNT_SCALE);
        short[] samples = new short[sampleCount];
        double phase = 0;

        for (int i = 0; i < sampleCount; i++) {
            samples[i] = (short) (AMPLITUDE * Math.sin(phase));
            phase += TWO_PI * frequency / SAMPLE_RATE;
        }
        audioTrack.write(samples, 0, sampleCount);
    }



    /** Reloads and Plays the Wave or Starts the Wave generation*/

    public void start() {
        audioTrack.reloadStaticData();
        audioTrack.setLoopPoints(0, sampleCount, -1);
        audioTrack.play();
    }



    /** Stops the Wave Generation */

    public void stop() {

        audioTrack.stop();
    }



    /** Generate Square Wave of 20Hz to 20KHz */

    public void setWaveSquare(int frequency) {

        sampleCount = (int) ((float) SAMPLE_RATE / SAMPLE_COUNT_SCALE);
        short[] samples = new short[sampleCount];
        double phase = 0;
        short sampleCheck;

        for (int i = 0; i < sampleCount; i++) {
            samples[i] = (short) (AMPLITUDE * Math.sin(phase));
            sampleCheck = samples[i];
            if (sampleCheck > SQUARE_LOWER_BIT) {
                samples[i] = AMPLITUDE;
            } else if (sampleCheck < SQUARE_LOWER_BIT) {
                samples[i] = -AMPLITUDE;
            }
            phase += TWO_PI * frequency / SAMPLE_RATE;
        }
        audioTrack.write(samples, 0, sampleCount);
    }



    /**Generate Triangular Wave of 20Hz to 20KHz */

    public void setWaveTriangle(int frequency) {
        sampleCount = (int) ((float) SAMPLE_RATE / frequency);
        short[] samples = new short[sampleCount];
        for (int i = 0; i < sampleCount; i++) {

            if (i < (sampleCount / 4)) {
                samples[i] = (short) ((((float) i * (4.0f * (float) AMPLITUDE)) / (float) sampleCount));
            } else if (i > (3 * (sampleCount / 4))) {
                samples[i] = (short) (((((float) i * (4.0f * (float) AMPLITUDE)) / (float) sampleCount) - 2.0f * AMPLITUDE));
            } else {

                samples[i] = (short) (((float) 2.0f * AMPLITUDE - (((float) i * (4.0f * (float) AMPLITUDE)) / (float) sampleCount)));
            }
        }
        audioTrack.write(samples, 0, sampleCount);
    }


    /**Generate SawTooth Wave of 20Hz to 20KHz */

    public void setWaveSawTooth(int frequency) {
        sampleCount = (int) ((float) SAMPLE_RATE / frequency);
        short[] samples = new short[sampleCount];
        for (int i = 0; i < sampleCount; i++) {
            samples[i] = (short) (((float) (-AMPLITUDE) + (((float) i * (2.0f * (float) AMPLITUDE)) / (float) sampleCount)));
        }
        audioTrack.write(samples, 0, sampleCount);
    }
}
