package com.example.tedy.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tedy.bean.ApKData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DBManger {

    private Context context;
    private static DBManger instance;
    // 操作表的对象，进行增删改查
    private SQLiteDatabase writableDatabase;

    private DBManger(Context context) {
        this.context = context;
        DBHelper dbHelper = new DBHelper(context, 1);

        writableDatabase = dbHelper.getWritableDatabase();


    }
    public boolean tabbleIsExist(String tableName){
        boolean result = false;
        if(tableName == null){
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"+tableName.trim()+"' ";
            cursor = writableDatabase.rawQuery(sql, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }
    public static DBManger getInstance(Context context) {
        if (instance == null) {
            synchronized (DBManger.class) {
                if (instance == null) {
                    instance = new DBManger(context);
                }
            }
        }
        return instance;
    }

    public void add(String pkgName,String apkName,String firstName) {

        String randomNanoId = NanoIdUtils.randomNanoId();

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO NewTable (id, pakName, name, firstName) VALUES('");
        sql.append(randomNanoId);

        sql.append("','");
        sql.append(pkgName);
        sql.append("','");
        sql.append(apkName);
        sql.append("','");
        sql.append(firstName);
        sql.append("');");

        writableDatabase.execSQL(sql.toString());
    }
    public void add(String pkgName,String apkName,String firstName,byte[] icon) {

        String randomNanoId = NanoIdUtils.randomNanoId();
        ContentValues initValues = new ContentValues();
        initValues.put("id",randomNanoId);
        initValues.put("pakName",pkgName);
        initValues.put("name",apkName);
        initValues.put("firstName",firstName);
        initValues.put("icon",icon);
         writableDatabase.insert("NewTable","id",initValues);


//
//        StringBuilder sql = new StringBuilder();
//        sql.append("INSERT INTO NewTable (id, pakName, name, firstName) VALUES('");
//        sql.append(randomNanoId);
//
//        sql.append("','");
//        sql.append(pkgName);
//        sql.append("','");
//        sql.append(apkName);
//        sql.append("','");
//        sql.append(firstName);
//        sql.append("');");

//        writableDatabase.execSQL(sql.toString());
    }

    public boolean select() {
        Cursor newTable = writableDatabase.query("NewTable", null, null, null, null, null, null);
//        while (newTable.moveToNext()){
//            Log.d("TAG", "select: "+newTable.getString(1));
//        }
       return newTable.getCount()  == 0? true : false;
    }


    public List<ApKData> selectRes(String keyWord) {
        ArrayList<ApKData> res = new ArrayList<>();
        String sql = "select * from NewTable where firstName like ?";
        Cursor cursor = writableDatabase.rawQuery(sql, new String[]{"%"+keyWord+"%"});

        while (cursor.moveToNext()){
            Log.d("TAG", "select: "+cursor.getString(1));
            Log.d("TAG", "select: "+cursor.getString(2));
            Log.d("TAG", "select: "+cursor.getString(3));
            ApKData apKData = new ApKData(cursor.getString(1), cursor.getString(2), cursor.getString(3),null);
            res.add(apKData);
        }
        return res;
    }
    public List<ApKData> selectRes2(String keyWord) {
        ArrayList<ApKData> res = new ArrayList<>();
        String sql = "select * from NewTable where firstName like ?";
        Cursor cursor = writableDatabase.rawQuery(sql, new String[]{"%"+keyWord+""});

        while (cursor.moveToNext()){
            ApKData apKData = new ApKData(cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getBlob(4));
            res.add(apKData);
        }
        Log.d("selectRes2", "selectRes2: "+res.toString());
        return res;
    }
    public List<ApKData> selectResHanzi(String keyWord) {


        ArrayList<ApKData> res = new ArrayList<>();
        String sql = "select * from NewTable where name like ?";
        Cursor cursor = writableDatabase.rawQuery(sql, new String[]{"%"+keyWord+"%"});

        while (cursor.moveToNext()){
            Log.d("TAG", "select: "+cursor.getString(1));
            Log.d("TAG", "select: "+cursor.getString(2));
            Log.d("TAG", "select: "+cursor.getString(3));

            ApKData apKData = new ApKData(cursor.getString(1), cursor.getString(2), cursor.getString(3),null);
            res.add(apKData);
        }
        return res;
    }

    public String selectPkgName(String name) {
        String sql = "select pakName from NewTable where name =  ?";
        Cursor cursor = writableDatabase.rawQuery(sql, new String[]{name});

        String resData  = "";
        while (cursor.moveToNext()){
            Log.d("TAG", "selectPkgName: "+cursor.getString(0));
            resData = cursor.getString(0);
        }
        return resData;
    }

    public void delete() {

    }
}
