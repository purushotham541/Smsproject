package com.speechmessage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by purushotham on 2/21/2018.
 */
public class MessagesDB extends SQLiteOpenHelper
{

    public static String DB_NAME = "SPEECH";
    public static String TABLE_NAME = "messages";
    public static int VERSION = 1;
    public static String KEY_ID = "id";
    public static String KEY_MESSAGE = "message";

    SQLiteDatabase sdb;

    public MessagesDB(Context context)
    {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL("create table messages(id integer primary key autoincrement,message text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS messages");
        onCreate(db);
    }

    public long insertMessage(String message) {

        sdb=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(KEY_MESSAGE, message);

        return sdb.insert(TABLE_NAME, null, contentValues);

    }
    public void deleteMessage(int id)
    {
        sdb=this.getWritableDatabase();

        System.out.println(""+id);

        sdb.execSQL("DELETE FROM messages WHERE id="+id);

    }

    public  List<Message> getAllmessages()
    {
        List<Message> messageList=new  ArrayList<Message>();

        String selectQuery = "SELECT * FROM " +TABLE_NAME;

        sdb=this.getReadableDatabase();
        Cursor cursor=sdb.rawQuery(selectQuery,null);

        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();

            do {
                Message message = new Message();

                message.setId(Integer.parseInt(cursor.getString(0)));
                message.setMessage(cursor.getString(1));

                messageList.add(message);

            }while (cursor.moveToNext());
        }

        return messageList;
    }
}
