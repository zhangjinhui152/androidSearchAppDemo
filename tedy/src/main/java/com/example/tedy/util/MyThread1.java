package com.example.tedy.util;

import android.content.Context;

public class MyThread1 extends Thread
{
    private DBManger dbManger;
    private Context context;
    public MyThread1(DBManger dbManger, Context context)
    {

       this.dbManger =dbManger;
       this.context = context;


//        My_AndroidUtil.addTheApkMap(context,dbManger);
    }
    public void run()
    {
        //寻找包名并加进数据库
                My_AndroidUtil.addTheApkMap(context,dbManger);
    }

}