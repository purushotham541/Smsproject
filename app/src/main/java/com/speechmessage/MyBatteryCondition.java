package com.speechmessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.support.v7.app.AlertDialog;

/**
 * Created by purushotham on 2/21/2018.
 */

public class MyBatteryCondition extends BroadcastReceiver

{

    //MediaPlayer mediaPlayer;
    @Override
    public void onReceive(Context context, Intent intent)
    {


        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        Intent battery=new Intent();
        battery.setClassName("com.speechmessage","com.speechmessage.MainActivity");
        battery.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        battery.putExtra("battery_level",level);
        context.startActivity(battery);

        /*if(level==62)
        {
            mediaPlayer.start();
            final AlertDialog.Builder builder = new AlertDialog.Builder(mycontext);
            builder.setMessage("           Please unplug cable")
                    .setIcon(R.drawable.battery)

                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mediaPlayer.stop();
                            dialog.dismiss();




                        }
                    });


            AlertDialog alert = builder.create();
            alert.setTitle(String.valueOf(level)+"%"+ "  Battery full");
            alert.show();

        }
        if (level<15)
        {
            mediaPlayer.start();

            //smsManager.sendTextMessage("8125251050",null,"Your battery getting low..!",null,null);
            AlertDialog.Builder builder = new AlertDialog.Builder(mycontext);
            builder.setMessage("           Please plugin cable")
                    .setIcon(R.drawable.battery_low)

                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mediaPlayer.stop();
                            dialog.dismiss();

                            //finish();

                        }
                    });


            AlertDialog alert = builder.create();
            alert.setTitle(String.valueOf(level)+"%"+ "  Battery getting low..");
            alert.show();

        }*/

    }
}



