package com.speechmessage;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener
{

    private static final int PERMISSION = 100;
    ListView messageListView;
    MessagesDB messagesDB;
    List<Message> messagelist;
    MediaPlayer mediaPlayer;
    AlertDialog.Builder builder;


    int positionInt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer=MediaPlayer.create(this,R.raw.music);
        MyBattery myBattery=new MyBattery();

        registerReceiver(myBattery,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));



        messageListView = (ListView) findViewById(R.id.messagelist);

        messagesDB = new MessagesDB(MainActivity.this);


        display();


        messageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                positionInt = position;

                PopupMenu popupMenu = new PopupMenu(MainActivity.this, messageListView);
                popupMenu.getMenuInflater().inflate(R.menu.messageoption, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {

                        int id = item.getItemId();

                        if (id == R.id.action_delete)
                        {
                            messagesDB.deleteMessage(messagelist.get(positionInt).getId());
                            messagelist.clear();

                            display();
                        }
                        return false;
                    }
                });
                popupMenu.show();

                return false;
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]
                            {
                            Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_MMS, Manifest.permission.READ_SMS
                    }, PERMISSION);
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    void display()
    {
        messagelist = messagesDB.getAllmessages();
        if (messagelist.size()>0) {
            MessagesAdater adater = new MessagesAdater();
            messageListView.setAdapter(adater);
        }else{
            Toast.makeText(MainActivity.this,"No messages found",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInit(int status) {

    }

    public class MessagesAdater extends BaseAdapter {
        @Override
        public int getCount()
        {
            return messagelist.size();
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            convertView = getLayoutInflater().inflate(R.layout.message, null);

            TextView messageTextview = (TextView) convertView.findViewById(R.id.messageText);

            messageTextview.setText(messagelist.get(position).getMessage());

            return convertView;
        }
    }

    public class MyBattery extends BroadcastReceiver
    {
        boolean flag=true,flag2=true;

        @Override
        public void onReceive(Context context, Intent intent)
        {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            //xxxxxxxxxxxx
            int status=intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);

            if(level==100)

            {
                if(flag)
                {

                    mediaPlayer.start();
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("           Please unplug cable")
                            .setIcon(R.drawable.battery)

                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mediaPlayer.stop();
                                    flag=false;

                                    dialog.dismiss();



                                }
                            });
                }




                AlertDialog alert = builder.create();
                alert.setTitle(String.valueOf(level)+"%"+ "  Battery full");
                alert.show();


            }
            if (level<69)
            {

                if(status==BatteryManager.BATTERY_STATUS_CHARGING)
                {
                    mediaPlayer.stop();
                }

                else
                {
                    if(flag2)
                    {
                        mediaPlayer.start();

                        //smsManager.sendTextMessage("8125251050",null,"Your battery getting low..!",null,null);
                        builder = new AlertDialog.Builder(MainActivity.this);
                        builder .setMessage("           Please plugin cable")
                                .setIcon(R.drawable.battery_low)

                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mediaPlayer.stop();
                                        flag2=false;


                                        dialog.dismiss();

                                        //finish();

                                    }
                                });


                        AlertDialog alert = builder.create();
                        alert.setTitle(String.valueOf(level)+"%"+ "  Battery getting low..");
                        alert.show();

                    }


                }








                }


            }

        }
    }



