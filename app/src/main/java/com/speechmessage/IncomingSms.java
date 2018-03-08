package com.speechmessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by purushotham on 2/21/2018.
 */
public class IncomingSms extends BroadcastReceiver {

    Context mcontext;

    ArrayList<String> numbersArray = new ArrayList<>();
    MessagesDB call;
    Cursor cursor;



    static int messageCount=0;

    public void onReceive(Context context, Intent intent) {
        mcontext = context;

        call = new MessagesDB(mcontext);

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        messageCount=1;

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    System.out.println("Whole Message: "+currentMessage);
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);


                    AudioManager audio_mngr = (AudioManager) mcontext.getSystemService(Context.AUDIO_SERVICE);

                    audio_mngr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                    call.insertMessage(message);

                //    Toast.makeText(mcontext, message, Toast.LENGTH_LONG).show();

                    SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(mcontext);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("message",senderNum);
                    editor.commit();

                 //   textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);


                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }

}