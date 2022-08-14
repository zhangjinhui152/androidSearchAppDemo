package com.example.tedy.util

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.tedy.MainActivity
import com.example.tedy.MyAccessbilityService


@SuppressLint("StaticFieldLeak")
object FileGet : AppCompatActivity() {




    @SuppressLint("StaticFieldLeak")
    @JvmStatic
    private var mainActive : MainActivity? = null

    @SuppressLint("StaticFieldLeak")
    @JvmStatic
    var ib : ImageButton? = null
    @JvmStatic
    var bm : Bitmap? = null

    @JvmStatic
    var myAc : MyAccessbilityService? = null

    fun getAc() : MyAccessbilityService? {
        return myAc
    }
    fun setAc(ac : MyAccessbilityService){
        myAc = ac

    }


    @JvmStatic
    fun getFile( ac : MainActivity){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*" //设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra("NMD","CAO!")
        ac.Myresult.launch(intent)
    }
    @JvmStatic
    fun setMac(mac : MainActivity){
        mainActive = mac
    }
    @JvmStatic
    fun getFile(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*" //设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        mainActive?.Myresult?.launch(intent)
    }

    @JvmStatic
    fun getMac() : MainActivity? {
       return mainActive;
    }



}
