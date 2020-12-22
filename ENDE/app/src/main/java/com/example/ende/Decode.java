package com.example.ende;

import com.example.libfreq.Complex;        //import the libfreq.jar file to access the complex java class
import com.example.libfreq.FFT;            //import the libfreq.jar file to access the FFT java class
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Decode extends Fragment {

    private TextView tv1;
    private TextView tv2;
    private Button stopRec;
    private Button startRec;
    private Boolean recording;
    private Boolean decoding;
    private static final int MIN_FREQ = 11800;
    private static final int MAX_FREQ = 12200;
    private static final int SAMPLE_FREQUENCY= 192000;
    private static final int BUFFER_SIZE_IN_BYTES = 8192;

    private static final int[] frequencyArray = new int[]{18000, 18400, 18800, 19200, 19600, 20000, 20400, 20800, 21200, 22000};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.decode_layout, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startRec = (Button) view.findViewById(R.id.startbutton);
        stopRec = (Button) view.findViewById(R.id.stopbutton);
        tv1 = (TextView) view.findViewById(R.id.textView);
        tv2 = (TextView) view.findViewById(R.id.textView2);

        startRec.setEnabled(true);
        stopRec.setEnabled(false);

        decoding = false;
        tv2.setText("Code :");

        startRecButton();
        stopRecButton();
    }



    /** Starts the code detection when start button is pressed */
    private void startRecButton() {

        startRec.setOnClickListener(v -> {

            Thread recordThread = new Thread(() -> {
                recording = true;
                startRecord();
            });
            recordThread.start();
            startRec.setEnabled(false);
            stopRec.setEnabled(true);
        });
    }



  /** Stops the code detection when stop button is pressed */
    private void stopRecButton() {
        stopRec.setOnClickListener(v -> {

            recording = false;
            startRec.setEnabled(true);
            stopRec.setEnabled(false);

        });
    }



    /**   Takes the audio frequency code as input and decodes that input */
    private void startRecord() {

        try {

            int MIN_BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_FREQUENCY, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);  // Contains Estimated Minimum buffer size required for an AudioTrack
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_FREQUENCY, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, MIN_BUFFER_SIZE);
            short[] audioData = new short[MIN_BUFFER_SIZE];
            audioRecord.startRecording();
            StringBuilder decodeResult = new StringBuilder();

            while (recording) {

                audioRecord.read(audioData, 0, MIN_BUFFER_SIZE);
                double freq = findFreq(audioData);      // calls the findFreq function to get the frequency
                tv1.setText(String.valueOf(freq));       // Displays the live frequency

                if (freq > MIN_FREQ && freq < MAX_FREQ) {
                    decoding = true;
                }

                if (decoding) {
                    if (freq > frequencyArray[1] - 100 && freq < frequencyArray[1] + 100) {
                        decodeResult.append("1");
                    } else if (freq > frequencyArray[2] - 100 && freq < frequencyArray[2] + 100) {
                        decodeResult.append("2");
                    } else if (freq > frequencyArray[3] - 100 && freq < frequencyArray[3] + 100) {
                        decodeResult.append("3");
                    } else if (freq > frequencyArray[4] - 100 && freq < frequencyArray[4] + 100) {
                        decodeResult.append("4");
                    } else if (freq > frequencyArray[5] - 100 && freq < frequencyArray[5] + 100) {
                        decodeResult.append("5");
                    } else if (freq > frequencyArray[6] - 100 && freq < frequencyArray[6] + 100) {
                        decodeResult.append("6");
                    } else if (freq > frequencyArray[7] - 100 && freq < frequencyArray[7] + 100) {
                        decodeResult.append("7");
                    } else if (freq > frequencyArray[8] - 100 && freq < frequencyArray[8] + 100) {
                        decodeResult.append("8");
                    } else if (freq > frequencyArray[9] - 100 && freq < frequencyArray[9] + 100) {
                        decodeResult.append("9");
                    } else if (freq > frequencyArray[0] - 100 && freq < frequencyArray[0] + 100) {
                        decodeResult.append("0");
                    } else if (freq > 22800 && freq < 23200) {     //Marker frequency
                        decodeResult.append("M");                  // M is used to represent the marker frequency
                    }
                }
            }

            decodeCode(decodeResult);

            audioRecord.stop();
            decoding = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**   Checks the detected code and display the code  */
    private void decodeCode(StringBuilder decodeResult) {
        if (decodeResult.length() != 0) {
            String a;
            String[] freqDecodeArray = new String[decodeResult.length()];
            StringBuilder decode = new StringBuilder();

            for (int i = 0; i < decodeResult.length(); i++) {
                freqDecodeArray[i] = decodeResult.substring(i, i + 1);
            }

            a = freqDecodeArray[0];
            decode.append(a);

            for (int i = 1; i < freqDecodeArray.length; i++) {

                if (!freqDecodeArray[i].equals(a)) {
                    a = freqDecodeArray[i];
                    if (!freqDecodeArray[i].equals("M")) {
                        decode.append(freqDecodeArray[i]);
                    }
                }
            }

            tv2.setText("Code : " + decode);

        } else {
            tv2.setText("Code Not Detected");
        }
    }


    /**   Finds the frequency using FFT and Complex java class files in libFreq.jar file and returns the frequency  */
    private double findFreq(short[] audioData) {

        Complex[] fftTempArray = new Complex[BUFFER_SIZE_IN_BYTES];
        for (int j = 0, k = 1000; j < BUFFER_SIZE_IN_BYTES; j++, k++) {
            fftTempArray[j] = new Complex(audioData[k], 0);
        }
        Complex[] fftArray = FFT.fft(fftTempArray);

        double[] magnitude = new double[BUFFER_SIZE_IN_BYTES / 2];

         // calculate power spectrum (magnitude) values from fft[]
        for (int k = 0; k < (BUFFER_SIZE_IN_BYTES / 2) - 1; ++k) {
            double real = fftArray[k].re();
            double imaginary = fftArray[k].im();
            magnitude[k] = Math.sqrt(real * real + imaginary * imaginary);
        }

        // find largest peak in power spectrum
        double max_magnitude = magnitude[0];
        int max_index = 0;
        for (int k = 0; k < magnitude.length; ++k) {
            if (magnitude[k] > max_magnitude) {
                max_magnitude = (int) magnitude[k];
                max_index = k;
            }
        }

        //here will get frequency in hz like(17000,18000..etc)
        return (float) SAMPLE_FREQUENCY * (float) max_index / (float) BUFFER_SIZE_IN_BYTES;
    }

}