package com.teccart.pchar.spaceadventures;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by pchar on 2018-03-18.
 */

public class mySQLiteDBAdapter{
    private Context context;
    private String DATABASE_NAME="Astres";
    private MyDBHelper dbHelper;
    private int DATABASE_VERSION=1;
    private SQLiteDatabase mySQLiteDatabase;

    public mySQLiteDBAdapter(Context context)
    {
        this.context=context;
        dbHelper=new MyDBHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void open()
    {
        try {
            this.mySQLiteDatabase = dbHelper.getWritableDatabase();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public void insertAstre(String nom, int taille, boolean status, String url)
    {
        ContentValues cv = new ContentValues();
        cv.put("nom",nom);
        cv.put("taille",taille);
        cv.put("status",status);
        cv.put("url",url);
        this.mySQLiteDatabase.insert("Astres",null,cv);
    }

    public void deleteAstres()
    {
        this.mySQLiteDatabase.delete("Astres",null,null);
    }


    public ArrayList<Astre> selectAllAstres()
    {
        ArrayList<Astre> DemoList = new ArrayList<Astre>();
        boolean demo;

        String query="select * from Astres;";

        Cursor cursor2 =mySQLiteDatabase.rawQuery(query,null);

        if(cursor2!=null && cursor2.moveToFirst())
        {
            do {
                // Convert the value in 3rd column to Boolean.
                demo=(cursor2.getInt(3)==1);
                // Add every Astre into ArrayList from the SQliteDB.
                DemoList.add(new Astre(cursor2.getInt(0),cursor2.getString(1),
                        cursor2.getInt(2),demo,cursor2.getString(4)));
            }while(cursor2.moveToNext());
        }
        return DemoList;
    }

    private class MyDBHelper extends SQLiteOpenHelper
    {
        public MyDBHelper(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context,dbname,factory,version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query="Create table Astres(id integer primary key autoincrement, nom text,taille integer," +
                    "status boolean,url text);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
            String query="Drop table IF EXISTS Astres;";
            db.execSQL(query);
            onCreate(db);
        }


    }
}
