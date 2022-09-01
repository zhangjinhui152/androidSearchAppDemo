package com.example.tedy.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.pm.PackageInfoCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tedy.R;
import com.example.tedy.bean.ApKData;
import com.example.tedy.serchMethod.SearchMethod;
import com.example.tedy.serchMethod.SerchMethodForBase;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class My_AndroidUtil {

    private static List<PackageInfo> apps;
    private static List<String> thirdAPP;


    private static RecyclerView resultList;
    private static My_RecyclerViewAdapter my_recyclerViewAdapter;

    public static DBManger getDbManger() {
        return dbManger;
    }

    private static DBManger dbManger;
    private static SearchMethod searchMethod;



    public static void setSearchMethod(SearchMethod sm) {
        searchMethod = sm;
    }





    public static  void init(Context context,RecyclerView rel){
        //数据库方法类
        dbManger = DBManger.getInstance(context);






        //这个是显示结果的布局
        resultList = rel;

        //结果布局的适配器
        my_recyclerViewAdapter = new My_RecyclerViewAdapter(new ArrayList<>(), context, dbManger);


        //不消说 这是倒序
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setStackFromEnd(true);
        resultList.setLayoutManager(linearLayoutManager);
//
        ItemAnimation itemAnimator = new ItemAnimation();
        resultList.setItemAnimator(itemAnimator);



        resultList.setAdapter(my_recyclerViewAdapter);
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback();
        callback.setmAdapter(my_recyclerViewAdapter);




//        resultList.setItemAnimator(new SlideInLeftAnimator());

        //这是拖动的适配器 没什么用
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(resultList);
        Thread thread = new MyThread1(dbManger,context);
        thread.start();
        setSearchMethod(new SerchMethodForBase());
    }
    public static void addTheApkMap(Context context, DBManger dbManger) {
        try {

            List<String> thirdAppList = My_AndroidUtil.getThirdAppList(context);
            //判空 如果没有数据则搜索包并加载数据库里
            boolean flag = dbManger.select();

            Log.d("My_AndroidUtil..flag())", "flag--->"+flag);
            if(!flag){

                return;
            }
            for (String appPkgName : thirdAppList) {
                Context c = context.createPackageContext(appPkgName, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);

                String appName = My_AndroidUtil.getAppName(c);
                appName = My_AndroidUtil.format(appName);



                Bitmap bitmap = My_AndroidUtil.getBitmap(c);

                byte[] bitmapByte = My_AndroidUtil.bitmapToByte(bitmap);


                String firstName = My_AndroidUtil.pingyingFirst(appName);

                dbManger.add(appPkgName,appName,firstName,bitmapByte);
//                Log.d("My_AndroidUtil..appName())", appName);
//                Log.d("My_AndroidUtil..flag())", "flag--->"+flag);
                Log.d("My_AndroidUtil..firstName())", firstName);
//                Log.d("My_AndroidUtil..appPkgName())", bitmapByte.toString());


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //找包名
    public static List<String> getThirdAppList(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager .getInstalledPackages(0);
        // 判断是否系统应用：
        //List<PackageInfo> apps = new ArrayList<PackageInfo>();
        thirdAPP = new ArrayList<>();
        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo pak = (PackageInfo)packageInfoList.get(i);
            //判断是否为系统预装的应用
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // 第三方应用
                // apps.add(pak);
                thirdAPP.add( pak.packageName);
            } else
            {
                //系统应用
            }
        }
        return thirdAPP ;

    }

    public static List<PackageInfo> getApps() {
        if (apps != null) {
            return apps;
        } else
            return null;
    }

    public static List<String> getThirdAPP() {
        if (apps != null) {
            return thirdAPP;
        } else
            return null;
    }





    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(

                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;

            if(labelRes <= 0){
                return "";
            }
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized Long getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return PackageInfoCompat.getLongVersionCode(packageInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取图标 bitmap
     *
     * @param context
     */
    public static synchronized Bitmap getBitmap(Context context) {
//        PackageManager packageManager = null;
//        ApplicationInfo applicationInfo = null;
//        try {
//
//            packageManager = context.getApplicationContext()
//                    .getPackageManager();
//            if (packageManager == null){
//                return null;
//            }
//            applicationInfo = packageManager.getApplicationInfo(
//                    context.getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            applicationInfo = null;
//        }
//
//
//        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
//        Bitmap bitmap = My_AndroidUtil.drawableToBitmap(d);
//        return bitmap;
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
            if (d == null){
                return null;
            }
            Bitmap bitmap = My_AndroidUtil.drawableToBitmap(d);
            if (bitmap == null){
                return null;
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

//    public static Drawable getIcon(Context context){
//
//        PackageManager manager = context.getPackageManager();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        Drawable icon = null;
//        List<ResolveInfo> resolveInfos = manager.queryIntentActivities(intent, 0);
//        for (ResolveInfo info : resolveInfos) {
//            ActivityInfo ai = info.activityInfo;
//            icon = ai.loadIcon(manager);
//        }
//    return icon;
//    }


    public static Bitmap drawableToBitmap(Drawable drawable) {

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getAlpha() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }
    public static synchronized Drawable getDrawable(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable

        return d;
    }

    public static String pingyingFirst(String hanyu) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        StringBuilder firstPinyin = new StringBuilder();

        char[] hanyuArr = hanyu.trim().toCharArray();
        try {
            for (int i = 0, len = hanyuArr.length; i < len; i++) {
                if (Character.toString(hanyuArr[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] pys = PinyinHelper.toHanyuPinyinStringArray(hanyuArr[i], format);

                    for (String s : pys){
                        firstPinyin.append(s);
                    }
                } else {
                    firstPinyin.append(hanyuArr[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        Log.d("TAG", "onCreate: " + firstPinyin.toString());
        return firstPinyin.toString();
    }





    public static  byte[] bitmapToByte(Bitmap bitmap){

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
    public static Bitmap byteToBitmap(byte[] temp){
        if(temp != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        }else{
            return null;
        }
    }

    public static void launchApp(Context context, String packageName) {
        try {
            Intent intent = new Intent();
            //通过包名启动
            PackageManager packageManager = context.getPackageManager();
            intent = packageManager.getLaunchIntentForPackage(packageName);
            if (null != intent) {
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void doStartApplicationWithPackageName(Context context,String packagename) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            //BDToast.showToast(getText(R.string.app_not_found).toString());
            Toast.makeText(context.getApplicationContext(), "app_not_found", Toast.LENGTH_SHORT).show();
            return;
        } else{
//            Toast.makeText(context.getApplicationContext(), "open successfully", Toast.LENGTH_SHORT).show();
        }


        Intent resolveIntent = context.getPackageManager().getLaunchIntentForPackage(packagename);// 这里的packname就是从上面得到的目标apk的包名
        context.startActivity(resolveIntent);// 启动目标应用
    }
    public static boolean checkHanzi(String countname)
    {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(countname);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static void filterInput(CharSequence s,DBManger dbManger,My_RecyclerViewAdapter my_recyclerViewAdapter) {
        
        String pkg_key = s.toString();
        StringBuilder pkg_keys = new StringBuilder();

        int length = pkg_key.length();
        for (int i = 0; i< length; i++){
            char c = pkg_key.charAt(i);
            pkg_keys.append(pkg_key.charAt(i));
            pkg_keys.append('%');
        }
        pkg_key = pkg_keys.toString();


        if (!pkg_key.isEmpty()){
            Log.d("TAG", "onTextChanged: "+pkg_key);
            if (My_AndroidUtil.checkHanzi(pkg_key)){
                List<ApKData> strings = dbManger.selectResHanzi(pkg_key);
                my_recyclerViewAdapter.removeAll();
                my_recyclerViewAdapter.cloneList(strings);
//                my_recyclerViewAdapter.notifyDataSetChanged();
            }
            else{
                List<ApKData> strings = dbManger.selectRes2(pkg_key);
                my_recyclerViewAdapter.removeAll();
                my_recyclerViewAdapter.cloneList(strings);
//                my_recyclerViewAdapter.notifyDataSetChanged();
            }

        }
        else{
            my_recyclerViewAdapter.removeAll();
//            my_recyclerViewAdapter.notifyDataSetChanged();
        }
    }
    public static void filterInput(CharSequence s) {

        String pkg_key = s.toString();
        StringBuilder pkg_keys = new StringBuilder();

        int length = pkg_key.length();
        for (int i = 0; i< length; i++){
            char c = pkg_key.charAt(i);
            pkg_keys.append(pkg_key.charAt(i));
            pkg_keys.append('%');
        }
        pkg_key = pkg_keys.toString();


        if (!pkg_key.isEmpty()){
            Log.d("TAG", "onTextChanged: "+pkg_key);
            if (My_AndroidUtil.checkHanzi(pkg_key)){
                List<ApKData> strings = dbManger.selectResHanzi(pkg_key);
                my_recyclerViewAdapter.removeAll();
                my_recyclerViewAdapter.cloneList(strings);
//                my_recyclerViewAdapter.notifyDataSetChanged();
            }
            else{
                List<ApKData> strings = dbManger.selectRes2(pkg_key);
                my_recyclerViewAdapter.removeAll();
                my_recyclerViewAdapter.cloneList(strings);
//                my_recyclerViewAdapter.notifyDataSetChanged();
            }

        }
        else{
            my_recyclerViewAdapter.removeAll();
//            my_recyclerViewAdapter.notifyDataSetChanged();
        }
    }
    public static String format(String s){
        String str=s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
        return str;
    }



    //打开外部app，新窗口打开
//    private void doStartApplicationWithPackageName(String packagename) {
//        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
//        PackageInfo packageinfo = null;
//        try {
//            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (packageinfo == null) {
//            //BDToast.showToast(getText(R.string.app_not_found).toString());
//            Toast.makeText(getApplicationContext(), "app_not_found", Toast.LENGTH_SHORT).show();
//            return;
//        } else
//            Toast.makeText(getApplicationContext(), "open successfully", Toast.LENGTH_SHORT).show();
//
//        Intent resolveIntent = getPackageManager().getLaunchIntentForPackage(packagename);// 这里的packname就是从上面得到的目标apk的包名
//        startActivity(resolveIntent);// 启动目标应用
//    }
}
