package com.example.tedy.serchMethod

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.tedy.MyAccessbilityService
import com.example.tedy.util.FileGet
import com.example.tedy.util.My_AndroidUtil

class SearchMethodForAccessibility : SearchMethod {
    override fun search(s: CharSequence?, urlScheme: String?, context: Context?) {
        Toast.makeText(FileGet.getMac(), "触发search方法", Toast.LENGTH_LONG).show();
        Log.d("search", "search: IS SearchMethodForAccessibility")
        MyAccessbilityService.isOpenActive = true
        val currSearchBlock = FileGet.getMac()?.currSearchBlock

//        val jsonStr = """
//                [{"type":"Click","x":26.0,"y":26.0,"id":"inp1","editText":"喻志强"},{"type":"IdNode","x":26.0,"y":26.0,"id":"inp1","editText":"喻志强"}]
//            """.trimIndent()
        var trimIndent = currSearchBlock?.jsonStr.toString().trimIndent().replace("%s",s.toString());

        MyAccessbilityService.setJsonAndPkgName(trimIndent,currSearchBlock?.searchName.toString())
        Log.d("search", "search: IS SearchMethodForAccessibility ${currSearchBlock?.searchName.toString()}")

        Log.d("search", "search: IS SearchMethodForAccessibility${currSearchBlock?.jsonStr.toString()}")

        My_AndroidUtil.doStartApplicationWithPackageName(context, currSearchBlock?.searchName);



    }
}