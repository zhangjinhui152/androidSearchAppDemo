package com.example.tedy.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

        public static final String db_name = "test.db";

        public DBHelper(Context context, int version) {
            super(context, db_name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {



            db.execSQL("CREATE TABLE NewTable (\n" +
                    "\tid TEXT(50) NOT NULL,\n" +
                    "\tpakName TEXT(50) NOT NULL,\n" +
                    "\tname TEXT(20) NOT NULL,\n" +
                    "\tfirstName INTEGER NOT NULL,\n" +
                    "\ticon BLOB NOT NULL\n" +
                    ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

