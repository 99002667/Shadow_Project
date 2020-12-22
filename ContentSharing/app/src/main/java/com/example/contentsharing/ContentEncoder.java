package com.example.contentsharing;

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

public class ContentEncoder extends Fragment {

    private String code;
    private EditText et1;
    private EditText et2;
    private TextView tv2;
    private String number;
    private ToggleButton tb1;
    private SeekBar volumeSeekBar;
    private AudioManager audioManager;
    private int[] freqArray = new int[11];
    private final PlaySine wave = new PlaySine();
    public static final int NUMBER_LENGTH = 10;
    public static final int MIN_NAME_LENGTH = 0;
    public static final int MARKER_FREQ = 23000;
    public static final int MAX_NAME_LENGTH = 50;
    public static final double VOLUME_MUL_VALUE= 6.67;
    public static final int TRANSFER_COMPLETED = 23200;
    public static final int START_NUM_TRANSFER = 18900;
    public static final int START_STOP_NAM_TRANSFER = 18800;
    private static final int[] frequencyArray = new int[]{18000, 18100, 18200, 19000, 19100, 19200, 19300, 19400, 19500, 19600, 19700, 19800, 19900, 20000, 20200, 20400, 20600, 20800, 21000, 21100, 21200, 21300, 21400, 21500, 21600, 21900, 22000, 22100, 22200, 22300, 22400, 22500, 22600, 22700, 18400, 18500, 18600};
                                                       //  A       B      C     D       E     F       G      H      I     J       K      L     M      N       O      P      Q     R       S       T     U      V      W      X      Y      Z

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_encoder, container, false);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et1 = (EditText) view.findViewById(R.id.editText);
        et2 = (EditText) view.findViewById(R.id.editText2);
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


    /**  Validation of Name and Phone Number given by user */

    private void encodeInit() {

        tb1.setOnClickListener(v -> {

            code = et1.getText().toString().toUpperCase();
            number = et2.getText().toString();

            if (code.length() > MAX_NAME_LENGTH || code.length() == MIN_NAME_LENGTH) {
                et1.setText("Unknown Name");
            }
            if (number.length() != NUMBER_LENGTH) {
                et2.setText("Enter a valid Phone Number");
            }

            Thread encodeThread = new Thread(() -> {
                sendName(code);
                sendNumber(number);
            });

            encodeThread.start();
        });
    }



    /**  Sending the Name through audio frequencies using marker digits  */

    private void sendName(String code) {

        if (number.length() == NUMBER_LENGTH && code.length() > MIN_NAME_LENGTH && code.length() <= MAX_NAME_LENGTH) {
            boolean press = tb1.isChecked();
            if (press) {

                startStopCode();

                for (int i = 0; i < code.length(); i++) {
                    char aChar = code.charAt(i);
                    if (aChar != ' ') {
                        wave.setWave(frequencyArray[(int) aChar - 65]);
                    } else {
                        wave.setWave(frequencyArray[26]);
                    }
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

            } else {
                wave.stop();

            }
        }
    }


    /**  Sending the Phone Number through audio frequencies using marker digits  */

    private void sendNumber(String number) {

        if (number.length() == NUMBER_LENGTH && code.length() > MIN_NAME_LENGTH && code.length() <= MAX_NAME_LENGTH) {
            boolean press = tb1.isChecked();
            if (press) {

                startStopCodeNumber();

                for (int i = 0, j = 1; i < number.length(); i++, j++) {
                    freqArray[i] = Integer.parseInt(number.substring(i, j));

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

                StopCode();
                tb1.toggle();

            } else {
                wave.stop();

            }
        }

    }


    /**  Sets the stopping audio frequency of 23200Hz to indicate that Phone Number transfer is completed  */

    private void StopCode() {
        wave.setWave(TRANSFER_COMPLETED);
        wave.start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wave.stop();
    }


    /**  Sets the starting audio frequency of 18900Hz to transfer the Phone number  */

    private void startStopCodeNumber() {
        wave.setWave(START_NUM_TRANSFER);
        wave.start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wave.stop();
    }


    /**  23KHz audio frequency is generated when markerCode() is called  */

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


    /**  Sets the starting and ending audio frequency of 18800Hz for Name transfer */

    private void startStopCode() {
        wave.setWave(START_STOP_NAM_TRANSFER);
        wave.start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wave.stop();
    }

}
