package com.example.tedy;


import static java.lang.Math.pow;
import static java.lang.Math.sin;

import androidx.activity.result.ActivityResultLauncher;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;

import android.app.ApplicationExitInfo;
import android.content.Context;
import android.content.Intent;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tedy.bean.SearchBlock;
import com.example.tedy.bean.SearchType;
import com.example.tedy.serchMethod.SearchMethod;
import com.example.tedy.serchMethod.SearchMethodForAccessibility;
import com.example.tedy.serchMethod.SearchMethodForUrlScheme;
import com.example.tedy.serchMethod.SerchMethodForBase;
import com.example.tedy.util.FileGet;
import com.example.tedy.util.My_AndroidUtil;

import com.example.tedy.util.Search_RecyclerViewAdapter;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private View contentView;
    private int shortAnimationDuration;
    private boolean flip = true;
//    private boolean flip2 = true;
    private MaterialCardView settingBlock ;
    Search_RecyclerViewAdapter search_recyclerViewAdapter;
    private List<SearchBlock> searchBlocks;




    public void setCurrSearchBlock(SearchBlock currSearchBlock) {
        this.currSearchBlock = currSearchBlock;
    }

    public SearchBlock getCurrSearchBlock() {
        return currSearchBlock;
    }

    private SearchBlock currSearchBlock;
    public ActivityResultLauncher<Intent> Myresult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                Uri dataData = data.getData();

                //设置图片同时返回bitmap
                FileGet.getIb().setImageURI(dataData);
                Drawable drawable = FileGet.getIb().getDrawable();
                Bitmap bitmap = My_AndroidUtil.drawableToBitmap(drawable);
                FileGet.setBm(bitmap);

            }
    }
    );


    private SearchMethod searchMethod;
    private ImageView showPanle;
    public ImageView setting;
    private String fileName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File filesDir = this.getFilesDir();
        Log.d("filesDir", "onCreate: "+filesDir);

        fileName = filesDir + "/data.ser";


        try {

            FileInputStream fileOut1 = new FileInputStream(fileName);
            fileOut1 = fileOut1;
            ObjectInputStream out = new ObjectInputStream(fileOut1);
            searchBlocks = (List<SearchBlock>)out.readObject();
            fileOut1.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            searchBlocks = new ArrayList<>();
            Resources resources = getResources();
            Drawable drawable = resources.getDrawable(R.drawable.home128,null);
            Drawable drawable2 = resources.getDrawable(R.drawable.add,null);
            Bitmap bitmap = My_AndroidUtil.drawableToBitmap(drawable);
            Bitmap bitmap2 = My_AndroidUtil.drawableToBitmap(drawable2);


            searchBlocks.add(new SearchBlock("Base",null,null,My_AndroidUtil.bitmapToByte(bitmap), SearchType.BASE));
            searchBlocks.add(new SearchBlock("Base",null,null,My_AndroidUtil.bitmapToByte(bitmap2), SearchType.ADD));

        }


        setContentView(R.layout.activity_main);
        My_AndroidUtil.init(this, findViewById(R.id.resultList));
        animan_cilck();
        listenInput(findViewById(R.id.search_input));
        setSearchMethod(new SerchMethodForBase());


        RecyclerView v2 = findViewById(R.id.searchMode);
        v2.setLayoutManager(new GridLayoutManager(this,8));




        search_recyclerViewAdapter = new Search_RecyclerViewAdapter(searchBlocks,this,My_AndroidUtil.getDbManger());



        TextInputEditText searchInput = findViewById(R.id.search_input);
        TextInputLayout searchInputL = findViewById(R.id.search_input_l);



        showPanle = findViewById(R.id.showPanle);
        search_recyclerViewAdapter.setSearchButtom(showPanle);
        search_recyclerViewAdapter.setSearchInput(searchInput);
        search_recyclerViewAdapter.setSearchInput_L(searchInputL);
        v2.setAdapter(search_recyclerViewAdapter);


        FileGet.setIb(findViewById(R.id.showPanle));
        FileGet.setMac(this);


//        readSettingInput();

//        findViewById(R.id.close_Bth).setOnClickListener(v->{
//            settingBlock.animate().setDuration(400).scaleX(0).scaleY(0).setInterpolator((Interpolator) x -> {
//                float factor = (float) 0.95;
//                float res = (float) (pow(2, -10 * x) * sin((x - factor / 4) * (2 * 3.14) / factor) + 1);
//                return  res;
//            }).start();
//        });
        setting = findViewById(R.id.settingSet);
//        Log.d("filesDir", "onCreate: "+setting);
//        FileGet.getMac().findViewById(R.id.close_Bth).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("TAG", "onClick: ");
//            }
//        });




}

    public void setPkgname(String pkgname) {
        MyAccessbilityService myAc = FileGet.getMyAc();

        AccessibilityServiceInfo accessibilityServiceInfo = myAc.getServiceInfo();
        accessibilityServiceInfo.packageNames = new String[]{pkgname};
        myAc.setServiceInfo(accessibilityServiceInfo);


        Log.d("1111", "setPkgname: "+myAc.getServiceInfo().packageNames[0]);
    }

    //读取/修改当前的块设置
//    public void WriteSettingInput()
//    {
//        TextInputEditText searchName = findViewById(R.id.SearchName);
//        TextInputEditText input_JsonStr = findViewById(R.id.input_JsonStr);
//        TextInputEditText input_urlScheme = findViewById(R.id.input_urlScheme);
//        TextInputEditText input_type = findViewById(R.id.type);
//
//        searchName.setText(currSearchBlock.getSearchName());
//        input_JsonStr.setText(currSearchBlock.getJsonStr());
//        input_urlScheme.setText(currSearchBlock.getUrlScheme());
//        SearchType oldType = currSearchBlock.getType();
//        if (oldType == SearchType.ACCESSIBILITY){
//            input_type.setText("A");
//        }
//        else if (oldType == SearchType.URL_SCHEME) {
//            input_type.setText("U");
//        }
//
//
//        findViewById(R.id.addSearchBlock).setOnClickListener(v->{
//            SearchType type = null;
//            SearchMethod searchMethod = null;
//
//            if (input_type.getText().toString().equals("U")){
//                type = SearchType.URL_SCHEME;
//                searchMethod = new SearchMethodForUrlScheme();
//            }
//            else if (input_type.getText().toString().equals("A")) {
//                type = SearchType.ACCESSIBILITY;
//                searchMethod = new SearchMethodForAccessibility();
//            }
//            SearchBlock searchBlock = new SearchBlock(searchName.getText().toString(),
//                    input_urlScheme.getText().toString(),
//                    input_JsonStr.getText().toString(),
//                    type
//            );
//            searchBlock.setSearchMethod(searchMethod);
//            if (FileGet.getBm() != null){
//                searchBlock.setApkIcon(My_AndroidUtil.bitmapToByte(FileGet.getBm()));
//
//            }
//            Log.d("FuckBug", "onCreate: "+input_JsonStr.getText().toString());
//
//            search_recyclerViewAdapter.remove(currSearchBlock);
//            search_recyclerViewAdapter.add(searchBlock);
//            scaleCard();
//        });
//        findViewById(R.id.remove_Bth).setOnClickListener(vd->{
//            search_recyclerViewAdapter.remove(currSearchBlock);
//            scaleCard();
//        });
//    }
//
//    public void readSettingInput() {
//        settingBlock = findViewById(R.id.settingBlock);
//
//        TextInputEditText searchName = findViewById(R.id.SearchName);
//        TextInputEditText input_JsonStr = findViewById(R.id.input_JsonStr);
//        TextInputEditText input_urlScheme = findViewById(R.id.input_urlScheme);
//        TextInputEditText input_type = findViewById(R.id.type);
//
//        ImageButton addSearchBlock = findViewById(R.id.addSearchBlock);
//
//        //读取输入框并构建一个SearchBlock 然后添加到search_recyclerViewAdapter里
//        addSearchBlock.setOnClickListener(v->{
//        try {
//            SearchType type = null;
//            SearchMethod searchMethod = null;
//
//            if (input_type.getText().toString().equals("U")){
//                type = SearchType.URL_SCHEME;
//                searchMethod = new SearchMethodForUrlScheme();
//            }
//            else if (input_type.getText().toString().equals("A")) {
//                type = SearchType.ACCESSIBILITY;
//                searchMethod = new SearchMethodForAccessibility();
//            }
//            SearchBlock searchBlock = new SearchBlock(searchName.getText().toString(),
//                    input_urlScheme.getText().toString(),
//                    input_JsonStr.getText().toString(),
//                    type
//            );
//            searchBlock.setSearchMethod(searchMethod);
//            if (FileGet.getBm() != null){
//                searchBlock.setApkIcon(My_AndroidUtil.bitmapToByte(FileGet.getBm()));
//
//            }
//
//            Log.d("FuckBug", "onCreate: "+input_JsonStr.getText().toString());
//            search_recyclerViewAdapter.add(searchBlock);
//            scaleCard();
//            findViewById(R.id.remove_Bth).setOnClickListener(vd->{
////                search_recyclerViewAdapter.remove(currSearchBlock);
//            });
//        }
//        catch (Exception e){
//            Toast.makeText(MainActivity.this, "不能为空或者类型不对", Toast.LENGTH_LONG).show();
//        }
//
//
//
//
//
//
//        });
//        ImageButton setImageButtom = findViewById(R.id.setImageButtom);
//        //设置图片到当前的iamgebutton 传递给fileGet 在resultApi接受 设置给对应的按钮
//        setImageButtom.setOnClickListener(v->{
//            FileGet.setIb(setImageButtom);
//            FileGet.getFile();
//
//
//
//        });
//    }

    private void setSearchMethod(SerchMethodForBase serchMethodForBase) {
        searchMethod = serchMethodForBase;
    }

//    public void scaleCard(){
//
//        if (!flip){
//            settingBlock.animate().setDuration(800).scaleX(1).scaleY(1).setInterpolator((Interpolator) x -> {
//                float factor = (float) 0.4;
//                float v = (float) (pow(2, -10 * x) * sin((x - factor / 4) * (2 * 3.14) / factor) + 1);
//                return  v;
//            }).start();
//        }
//        else{
//            settingBlock.animate().setDuration(800).scaleX(0).scaleY(0).setInterpolator((Interpolator) x -> {
//                float factor = (float) 0.4;
//                float v = (float) (pow(2, -10 * x) * sin((x - factor / 4) * (2 * 3.14) / factor) + 1);
//                return  v;
//            }).start();
//        }
//        flip = !flip;
//
//
//    }

    private void listenInput(TextInputEditText searchInput) {
        Context context = this;
//        searchInput.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
//                    Log.e("MainActivity", "onKey: 按下回车键");
//                    if(currSearchBlock != null && currSearchBlock.getSearchMethod() != null){
//                        currSearchBlock.getSearchMethod().search(searchInput.getText().toString(),currSearchBlock.getUrlScheme(),context);
//                    }
//
//                    return true;
//                }
//                return false;
//            }
//        });
//        searchInput.setOnEditorActionListener(new AppCompatEditText.OnEditorActionListener() {
//            @Override
//
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                /** 关于编辑器动作 输入法动作发送,输入法动作完成, */
//                if (actionId == EditorInfo.IME_ACTION_SEND
//                        || actionId == EditorInfo.IME_ACTION_DONE
//                        || (event != null) && KeyEvent.KEYCODE_ENTER
//                        == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction()) {
//                    Log.w("error", "onEditorAction: 回车键");
//                }
//                //返回false以上的操作会执行两次，因为onEditorAction方法接受了false会表示尚未执行
//                //修改 return true,则回车之响应一次
//                return true ;
//            }
//        });
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchMethod.search(s,"",context);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

        });
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Log.w("TAG", "onEditorAction:回车键 ");
                    if(currSearchBlock != null && currSearchBlock.getSearchMethod() != null){
                        currSearchBlock.getSearchMethod().search(searchInput.getText().toString(),currSearchBlock.getUrlScheme(),context);
                    }
                }
                return false;
            }
        });
    }



    private void animan_cilck() {

        contentView = findViewById(R.id.materialCardView2);
//        loadingView = findViewById(R.id.text2);
        ImageView imageButton = findViewById(R.id.showPanle);
//        int count = 2;

        imageButton.setOnClickListener(v -> {


            foldSearchBlockCard();


        });
    }

    public void foldSearchBlockCard() {
        Log.d("flip", "foldSearchBlockCard: "+flip);
        if (flip) {
            shortAnimationDuration = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);
            crossfade();
            flip = false;
            contentView.setVisibility(View.VISIBLE);
        } else {
            flip = true;
            contentView.setVisibility(View.GONE);

        }
    }
    public void foldSearchBlockCard2() {
        flip = true;
        contentView.setVisibility(View.GONE);
    }


    private void crossfade() {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        contentView.setAlpha(0f);
        contentView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        contentView.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);


    }

    @Override
    protected void onStop() {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(search_recyclerViewAdapter.getData());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(search_recyclerViewAdapter.getData());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        super.onDestroy();
    }
}