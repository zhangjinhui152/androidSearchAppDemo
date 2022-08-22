package com.example.tedy.serchMethod;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class SearchMethodForUrlScheme implements SearchMethod {
    @Override
    public void search(CharSequence s, String urlScheme, Context context) {


        Log.w("search: ", "search: "+s);
        Log.w("search: ", "search: "+urlScheme);


        urlScheme = urlScheme.replace("%s",s.toString());
        Uri data = Uri.parse(urlScheme);
        Intent intent = new Intent(Intent.ACTION_VIEW,data);
        //保证新启动的APP有单独的堆栈，如果希望新启动的APP和原有APP使用同一个堆栈则去掉该项
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "没有匹配的APP，请下载安装",Toast.LENGTH_SHORT).show();
        }
    }
}
