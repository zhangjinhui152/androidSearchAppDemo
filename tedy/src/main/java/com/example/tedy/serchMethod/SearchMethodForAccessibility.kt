package com.example.tedy.serchMethod

import android.content.Context
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




        var trimIndent = currSearchBlock?.jsonStr.toString().trimIndent().replace("%s",s.toString())
        FileGet.getMac()?.setPkgname(currSearchBlock?.searchName.toString())
        MyAccessbilityService.setJsonAndPkgName(trimIndent,currSearchBlock?.searchName.toString())





        Log.d("search", "search: IS FileGet.getAc( ${FileGet.getAc()?.serviceInfo?.packageNames?.get(0)}")
        Log.d("search", "search: IS SearchMethodForAccessibility${currSearchBlock?.jsonStr.toString()}")
        Log.d("search", "search: IS SearchMethodForAccessibility${FileGet.getAc()}}")

        My_AndroidUtil.doStartApplicationWithPackageName(context, currSearchBlock?.searchName);



    }
}