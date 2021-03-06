package com.example.contentsharing;

import com.example.libfreq.Complex;               // importing jar file complex class
import com.example.libfreq.FFT;                  // importing jar file FFT class
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ContentDecoder extends Fragment {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private ToggleButton tb1;
    private Boolean recording;
    private Button saveContact;
    private Boolean decodingName;
    private Boolean decodingNumber;
    private static final int SAMPLE_FREQUENCY= 192000;
    private static final int BUFFER_SIZE_IN_BYTES = 8192;
    private static final int MIN_START_NAM_FREQ = 18750;
    private static final int MAX_START_NAM_FREQ = 18850;
    private static final int MAX_START_NUM_FREQ = 18950;
    
    private static final int[] frequencyArray = new int[]{18000, 18100, 18200, 19000, 19100, 19200, 19300, 19400, 19500, 19600, 19700, 19800, 19900, 20000, 20200, 20400, 20600, 20800, 21000, 21100, 21200, 21300, 21400, 21500, 21600, 21900, 22000, 22100, 22200, 22300, 22400, 22500, 22600, 22700, 18400, 18500, 18600};
                                                       //   A      B      C      D      E     F      G      H      I      J       K      L      M      N      O      P      Q       R     S      T       U      V      W     X      Y     Z      Space

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_decoder, container, false);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saveContact = (Button) view.findViewById(R.id.button);
        tv1 = (TextView) view.findViewById(R.id.textView);
        tv2 = (TextView) view.findViewById(R.id.textView2);
        tv3 = (TextView) view.findViewById(R.id.textView3);
        tb1 = (ToggleButton) view.findViewById(R.id.toggleButton2);

        decodingName = false;
        decodingNumber = false;

        startButton();
        saveContactButton();
    }


    /**   Direct to the Contacts application in phone if Name and Number are valid when Save Button is pressed */

    private void saveContactButton() {
        saveContact.setOnClickListener(v -> {

            if (!(tv2.getText().toString().equals("Name Not Detected") || tv3.getText().toString().equals("Number Not Detected"))) {

                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, tv2.getText().toString());
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, tv3.getText().toString());

                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Invalid Contact", Toast.LENGTH_SHORT).show();
            }

        });
    }


    /**   Initializes the startRecord function when startButton is pressed */

    private void startButton() {

        tb1.setOnClickListener(v -> {
            boolean press = tb1.isChecked();
            if (press) {

                Thread recordThread = new Thread(() -> {
                    recording = true;
                    startRecord();
                });
                recordThread.start();

            } else {
                recording = false;
            }
        });
    }


    /**   Takes the audio frequency as input and decodes that input */

    private void startRecord() {

        try {
            int MIN_BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_FREQUENCY, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);  // Contains Estimated Minimum buffer size required for an AudioTrack
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_FREQUENCY, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, MIN_BUFFER_SIZE);
            short[] audioData = new short[MIN_BUFFER_SIZE];
            audioRecord.startRecording();
            StringBuilder decodeResultName = new StringBuilder();
            StringBuilder decodeResultNumber = new StringBuilder();

            while (recording) {

                audioRecord.read(audioData, 0, MIN_BUFFER_SIZE);
                double freq = findFreq(audioData);      // calls the findFreq function to get the frequency
                tv1.setText(String.valueOf(freq));      // Displays the live frequency

                if (freq > MIN_START_NAM_FREQ && freq < MAX_START_NAM_FREQ) {
                    decodingName = true;                                // If Name start frequency is detected
                    decodingNumber = false;
                }

                if (freq > MAX_START_NAM_FREQ && freq < MAX_START_NUM_FREQ) {
                    decodingNumber = true;                              // If Number start frequency is detected
                    decodingName = false;
                }

                if (decodingName) {

                    if (freq > frequencyArray[1] - 50 && freq < frequencyArray[1] + 50) {
                        decodeResultName.append("B");
                    } else if (freq > frequencyArray[2] - 50 && freq < frequencyArray[2] + 50) {
                        decodeResultName.append("C");
                    } else if (freq > frequencyArray[3] - 50 && freq < frequencyArray[3] + 50) {
                        decodeResultName.append("D");
                    } else if (freq > frequencyArray[4] - 50 && freq < frequencyArray[4] + 50) {
                        decodeResultName.append("E");
                    } else if (freq > frequencyArray[5] - 50 && freq < frequencyArray[5] + 50) {
                        decodeResultName.append("F");
                    } else if (freq > frequencyArray[6] - 50 && freq < frequencyArray[6] + 50) {
                        decodeResultName.append("G");
                    } else if (freq > frequencyArray[7] - 50 && freq < frequencyArray[7] + 50) {
                        decodeResultName.append("H");
                    } else if (freq > frequencyArray[8] - 50 && freq < frequencyArray[8] + 50) {
                        decodeResultName.append("I");
                    } else if (freq > frequencyArray[9] - 50 && freq < frequencyArray[9] + 50) {
                        decodeResultName.append("J");
                    } else if (freq > frequencyArray[0] - 50 && freq < frequencyArray[0] + 50) {
                        decodeResultName.append("A");
                    } else if (freq > frequencyArray[10] - 50 && freq < frequencyArray[10] + 50) {
                        decodeResultName.append("K");
                    } else if (freq > frequencyArray[11] - 50 && freq < frequencyArray[11] + 50) {
                        decodeResultName.append("L");
                    } else if (freq > frequencyArray[12] - 50 && freq < frequencyArray[12] + 50) {
                        decodeResultName.append("M");
                    } else if (freq > frequencyArray[13] - 50 && freq < frequencyArray[13] + 50) {
                        decodeResultName.append("N");
                    } else if (freq > frequencyArray[14] - 100 && freq < frequencyArray[14] + 100) {
                        decodeResultName.append("O");
                    } else if (freq > frequencyArray[15] - 100 && freq < frequencyArray[15] + 100) {
                        decodeResultName.append("P");
                    } else if (freq > frequencyArray[16] - 100 && freq < frequencyArray[16] + 100) {
                        decodeResultName.append("Q");
                    } else if (freq > frequencyArray[17] - 100 && freq < frequencyArray[17] + 100) {
                        decodeResultName.append("R");
                    } else if (freq > frequencyArray[18] - 50 && freq < frequencyArray[18] + 50) {
                        decodeResultName.append("S");
                    } else if (freq > frequencyArray[19] - 50 && freq < frequencyArray[19] + 50) {
                        decodeResultName.append("T");
                    } else if (freq > frequencyArray[20] - 50 && freq < frequencyArray[20] + 50) {
                        decodeResultName.append("U");
                    } else if (freq > frequencyArray[21] - 50 && freq < frequencyArray[21] + 50) {
                        decodeResultName.append("V");
                    } else if (freq > frequencyArray[22] - 50 && freq < frequencyArray[22] + 50) {
                        decodeResultName.append("W");
                    } else if (freq > frequencyArray[23] - 50 && freq < frequencyArray[23] + 50) {
                        decodeResultName.append("X");
                    } else if (freq > frequencyArray[24] - 50 && freq < frequencyArray[24] + 50) {
                        decodeResultName.append("Y");
                    } else if (freq > frequencyArray[25] - 50 && freq < frequencyArray[25] + 50) {
                        decodeResultName.append("Z");
                    } else if (freq > frequencyArray[26] - 50 && freq < frequencyArray[26] + 50) {
                        decodeResultName.append(" ");
                    } else if (freq > 22800 && freq < 23200) {        //Marker frequency
                        decodeResultName.append("*");                 // * is used to represent the marker frequency
                    }
                }


                if (decodingNumber) {
                    if (freq > frequencyArray[1] - 50 && freq < frequencyArray[1] + 50) {
                        decodeResultNumber.append("1");
                    } else if (freq > frequencyArray[2] - 50 && freq < frequencyArray[2] + 50) {
                        decodeResultNumber.append("2");
                    } else if (freq > frequencyArray[3] - 50 && freq < frequencyArray[3] + 50) {
                        decodeResultNumber.append("3");
                    } else if (freq > frequencyArray[4] - 50 && freq < frequencyArray[4] + 50) {
                        decodeResultNumber.append("4");
                    } else if (freq > frequencyArray[5] - 50 && freq < frequencyArray[5] + 50) {
                        decodeResultNumber.append("5");
                    } else if (freq > frequencyArray[6] - 50 && freq < frequencyArray[6] + 50) {
                        decodeResultNumber.append("6");
                    } else if (freq > frequencyArray[7] - 50 && freq < frequencyArray[7] + 50) {
                        decodeResultNumber.append("7");
                    } else if (freq > frequencyArray[8] - 50 && freq < frequencyArray[8] + 50) {
                        decodeResultNumber.append("8");
                    } else if (freq > frequencyArray[9] - 50 && freq < frequencyArray[9] + 50) {
                        decodeResultNumber.append("9");
                    } else if (freq > frequencyArray[0] - 50 && freq < frequencyArray[0] + 50) {
                        decodeResultNumber.append("0");
                    } else if (freq > 22900 && freq < 23100) {         // Marker frequency
                        decodeResultNumber.append("*");                // * is used to represent the marker frequency
                    } else if (freq > 23100 && freq < 23300) {
                        recording = false;
                        tb1.toggle();
                    }
                }
            }

            decodeCodeName(decodeResultName);
            decodeCodeNumber(decodeResultNumber);

            audioRecord.stop();

            decodingName = false;
            decodingNumber = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**   Checks Number is detected or not  */

    private void decodeCodeNumber(StringBuilder decodeResultNumber) {
        if (decodeResultNumber.length() != 0) {
            String a;
            String[] freqDecodeArray = new String[decodeResultNumber.length()];
            StringBuilder decode = new StringBuilder();

            for (int i = 0; i < decodeResultNumber.length(); i++) {
                freqDecodeArray[i] = decodeResultNumber.substring(i, i + 1);
            }

            a = freqDecodeArray[0];
            decode.append(a);

            for (int i = 1; i < freqDecodeArray.length; i++) {

                if (!freqDecodeArray[i].equals(a)) {
                    a = freqDecodeArray[i];
                    if (!freqDecodeArray[i].equals("*")) {
                        decode.append(freqDecodeArray[i]);
                    }
                }
            }

            tv3.setText(" " + decode);

        } else {
            tv3.setText("Number Not Detected");
        }
    }


    /**   Checks Name is detected or not  */

    private void decodeCodeName(StringBuilder decodeResult) {
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

                    if (!freqDecodeArray[i].equals("*")) {
                        if (i > 3 && (freqDecodeArray[i - 3].equals(" ") || freqDecodeArray[i - 2].equals(" "))) {     // Checking the space before the character
                            decode.append(freqDecodeArray[i].toUpperCase());         //If space is present before the character then converting that character to capital letter
                        } else {
                            decode.append(freqDecodeArray[i].toLowerCase());
                        }
                    }
                }
            }

            tv2.setText(" " + decode);

        } else {
            tv2.setText("Name Not Detected");
        }
    }


    /**   Finds the frequency using libFreq.jar file and returns the frequency  */

    private double findFreq(short[] audioData) {

        Complex[] fftTempArray = new Complex[BUFFER_SIZE_IN_BYTES];
        for (int j = 0, k = 1000; j < BUFFER_SIZE_IN_BYTES; j++, k++) {
            fftTempArray[j] = new Complex(audioData[k], 0);
        }
        Complex[] fftArray = FFT.fft(fftTempArray);

        double[] magnitude = new double[BUFFER_SIZE_IN_BYTES / 2];

         //  calculate power spectrum (magnitude) values from fft[]
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

        // here will get frequency in hz like(17000,18000..etc)
        return (float) SAMPLE_FREQUENCY * (float) max_index / (float) BUFFER_SIZE_IN_BYTES;
    }
}
