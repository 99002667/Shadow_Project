package com.example.radioui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private ToggleButton onOff;
    private TextView tv1, tv2, tv3;
    private EditText et1;
    private AudioManager audioManager;
    private SeekBar volumeSeekBar;
    private SeekBar freqSeekBar;
    private double frequency = 20;
    private final PlayWave wave = new PlayWave();
    public static final int MIN_FREQUENCY = 20;
    public static final int MAX_FREQUENCY = 20000;
    public static final double VOLUME_MUL_VALUE= 6.67;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        onOff = (ToggleButton) findViewById(R.id.toggleButton);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.volume);
        tv3 = (TextView) findViewById(R.id.textView3);
        et1 = (EditText) findViewById(R.id.ed);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeSeekBar = (SeekBar) findViewById(R.id.seekBar2);
        freqSeekBar = (SeekBar) findViewById(R.id.seekBar);

        tv1.setText("Enter a frequency in range of 20Hz-20Khz");
        tv2.setText(String.valueOf(volumeSeekBar.getProgress()));
        tv3.setText(String.valueOf(freqSeekBar.getProgress()));

        volumeSeekBarInit();
        freqSeekBarInit();

       /**When Toggle Button is pressed */

        onOff.setOnCheckedChangeListener((buttonView, isChecked) -> {

            int id = radioGroup.getCheckedRadioButtonId();         // Gives Radio Button id in the Radio Group

            if (isChecked) {
                switch (id) {
                    case 2131362101:           // Square Wave Radio button id in Radio Group
                        onOffSquare();
                        break;
                    case 2131361806:           // Triangular Wave Radio button id in Radio Group
                        onOffTriangle();
                        break;
                    case 2131362060:           // SawTooth Wave Radio button id in Radio Group
                        onOffSawTooth();
                        break;
                    default:                  // Default Sine Wave Radio button id is considered
                        onOffSin();
                        break;
                }
            } else {
                wave.stop();
              }
        });
    }



    /** Calls the SetWave function in the PlayWave class if start button is checked */

    private void onOffSin() {

        checkFrequencyRange();


        if (frequency >= MIN_FREQUENCY && frequency <= MAX_FREQUENCY) {
            wave.setWave((int) frequency);                           // Calls the setWave function in the PlayWave class
            boolean on = onOff.isChecked();
            if (on) {
                wave.start();
            } else {
                wave.stop();
              }
        }
    }



    /** Calls the setWaveSquare function in the PlayWave class if start button is checked */

    private void onOffSquare() {

        checkFrequencyRange();

        if (frequency >= MIN_FREQUENCY && frequency <= MAX_FREQUENCY) {
            wave.setWaveSquare((int) frequency);                 // Calls the setWaveSquare function in the PlayWave class
            boolean on = onOff.isChecked();
            if (on) {
                wave.start();
            } else {
                wave.stop();
              }
        }
    }



    /** Calls the setWaveTriangle function in the PlayWave class if start button is checked */

    private void onOffTriangle() {

        checkFrequencyRange();

        if (frequency >= MIN_FREQUENCY && frequency <= MAX_FREQUENCY) {
            wave.setWaveTriangle((int) frequency);            // Calls the setWaveTriangle function in the PlayWave class
            boolean on = onOff.isChecked();
            if (on) {
                wave.start();
            } else {
                wave.stop();
            }
        }
    }



    /** Calls the setWaveSawTooth function in the PlayWave class if start button is checked */

    private void onOffSawTooth() {

        checkFrequencyRange();

        if (frequency >= MIN_FREQUENCY && frequency <= MAX_FREQUENCY) {
            wave.setWaveSawTooth((int) frequency);            // Calls the setWaveSawTooth function in the PlayWave class
            boolean on = onOff.isChecked();
            if (on) {
                wave.start();
            } else {
                wave.stop();
            }
        }
    }



    /** Volume Adjustment through Volume Seek Bar  */

    private void volumeSeekBarInit() {
        try {
            volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        } catch (Exception e) {
            e.printStackTrace();
        }
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
                tv2.setText(String.valueOf((int) (progress * VOLUME_MUL_VALUE)));             // Updating the Volume text box with progressed value
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }



    /** Frequency Adjustment through Frequency Seek Bar  */

    private void freqSeekBarInit() {


        freqSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                wave.stop();
                tv3.setText(String.valueOf(progress));                        // Updating the progressed frequency value to the text view Box
                et1.setText(String.valueOf(progress));                        // Updating the progressed frequency value to the edit text Box
                frequency = Double.parseDouble(tv3.getText().toString());
                wave.setWave((int) frequency);
                wave.start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int freq = freqSeekBar.getProgress();
                wave.setWave((int) freq);
                wave.start();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wave.stop();
            }
        });
    }




    /** Checking the given frequency Range*/

    private void checkFrequencyRange() {
        try {
            frequency = Double.parseDouble(et1.getText().toString());
            if (frequency >= MIN_FREQUENCY && frequency <= MAX_FREQUENCY) {

                tv3.setText(String.valueOf(frequency));
            } else {
                tv1.setText("Enter a frequency in range of 20Hz-20Khz");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

