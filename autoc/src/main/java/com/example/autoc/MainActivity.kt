package com.example.autoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log


class MainActivity : AppCompatActivity() {


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //例如你传入了一个JSON
        val jsonStr = """
                [{"type":"Click","x":26.0,"y":26.0,"id":"inp1","editText":"喻志强"},{"type":"IdNode","x":26.0,"y":26.0,"id":"inp1","editText":"喻志强"}]
            """.trimIndent()
        //设置json！

        MyAccessbilityService.setJsonAndPkgName(jsonStr, "com.example.autoc")


//       判断是否是启无障碍
//        Log.d("TAG", "onCreate: ${MyAccessbilityService.isServiceCreated}")
//        if (!MyAccessbilityService.isServiceCreated){
//            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//            startActivity(intent)
//        }



    }


    override fun onDestroy() {
//        MyAccessbilityService.isServiceCreated = false
        super.onDestroy()
    }
}