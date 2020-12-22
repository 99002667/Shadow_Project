package com.example.ende;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Encode extends Fragment {

    TextView tv1;
    private String code;
    private TextView tv2;
    private EditText et1;
    private ToggleButton tb1;
    private SeekBar volumeSeekBar;
    int[] freqArray = new int[11];
    private AudioManager audioManager;
    public static final int MIN_LENGTH = 0;
    public static final int MAX_LENGTH = 10;
    public static final int MARKER_FREQ = 23000;
    private final PlaySine wave = new PlaySine();
    public static final double VOLUME_MUL_VALUE= 6.67;
    public static final int START_STOP_FREQ = 12000;

    private static final int[] frequencyArray = new int[]{18000, 18400, 18800, 19200, 19600, 20000, 20400, 20800, 21200, 22000};



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.encode_layout, container, false);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et1 = (EditText) view.findViewById(R.id.editTextNumber);
        tv1 = (TextView) view.findViewById(R.id.textView);
        tb1 = (ToggleButton) view.findViewById(R.id.toggleButton);
        tv2 = (TextView) view.findViewById(R.id.volume);
        volumeSeekBar = (SeekBar) view.findViewById(R.id.seekBar2);
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        tv2.setText(String.valueOf(volumeSeekBar.getProgress()));

        encodeInit();
        volumeSeekBarInit();
    }



    /**  Volume Adjustment using SeekBar */
    private void volumeSeekBarInit() {
        try {
            volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekBar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));
        } catch (Exception e) {
            e.printStackTrace();
        }
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
                tv2.setText(String.valueOf((int) (progress * VOLUME_MUL_VALUE)));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }



    /**  Validation of Code that given by user and transferring the code */
    private void encodeInit() {

        tb1.setOnClickListener(v -> {

            code = et1.getText().toString();

            if (code.length() > MAX_LENGTH || code.length() == MIN_LENGTH) {
                et1.setText("Enter a valid Code");
            }

            Thread encodeThread = new Thread(() -> {

                if (code.length() > MIN_LENGTH && code.length() <= MAX_LENGTH) {
                    boolean press = tb1.isChecked();
                    if (press) {

                        startStopCode();

                        for (int i = 0, j = 1; i < code.length(); i++, j++) {

                            freqArray[i] = Integer.parseInt(code.substring(i, j));
                            wave.setWave(frequencyArray[freqArray[i]]);
                            wave.start();

                            try {
                                Thread.sleep(120);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            wave.stop();
                            markerCode();
                        }

                        startStopCode();
                        tb1.toggle();

                    } else {
                        wave.stop();

                    }
                }
            });

            encodeThread.start();
        });
    }



    /**  23KHz audio frequency is generated  for 120ms when markerCode() is called  */
    private void markerCode() {
        wave.setWave(MARKER_FREQ);
        wave.start();
        try {
            Thread.sleep(120);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wave.stop();
    }



    /**  Sets the starting and ending audio frequency of code to 12KHz for 300ms */
    private void startStopCode() {
        wave.setWave(START_STOP_FREQ);
        wave.start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wave.stop();
    }

}
