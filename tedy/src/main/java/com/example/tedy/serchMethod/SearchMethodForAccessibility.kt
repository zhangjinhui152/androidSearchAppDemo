package com.example.tedy.serchMethod

import android.content.Context
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.example.tedy.MyAccessbilityService
import com.example.tedy.util.FileGet
import com.example.tedy.util.JsonUitl
import com.example.tedy.util.My_AndroidUtil
import java.util.*

class SearchMethodForAccessibility : SearchMethod {
    override fun search(s: CharSequence?, urlScheme: String?, context: Context?) {
        Toast.makeText(FileGet.getMac(), "触发search方法", Toast.LENGTH_LONG).show();


        MyAccessbilityService.isOpenActive = true
        val currSearchBlock = FileGet.getMac()?.currSearchBlock


        var trimIndent = currSearchBlock?.jsonStr.toString().trimIndent().replace("%s",s.toString())

        FileGet.getMac()?.setPkgname(currSearchBlock?.searchName.toString())


        MyAccessbilityService.pkgName = currSearchBlock?.searchName.toString()
        Log.d("search", "search: IS SearchMethodForAccessibility${currSearchBlock?.jsonStr.toString()}")


        parserJson(jsonStr = trimIndent)
        My_AndroidUtil.doStartApplicationWithPackageName(context, currSearchBlock?.searchName);

    }



    fun parserJson(jsonStr: String){
        Toast.makeText(FileGet.getMac(), "触发parserJson", Toast.LENGTH_LONG).show();
        //转换JSOP成class
        val listForAcom = JsonUitl.convertJsonToClass(jsonStr)
        //遍历判断属于什么类型 就执行对应的指令
        listForAcom?.forEach {
            Log.d("TAG", "parserJson: it${it.type}")

            val task: TimerTask = object : TimerTask() {
                override fun run() {
                    when(it.type){
                        MyAccessbilityService.Companion.Command_Type.Click.toString() -> {
                            Log.d("TAG", "parserJson: it${it.type}")
                            FileGet.getAc()?.onAccessibilityEvent(AccessibilityEvent(AccessibilityEvent.TYPE_ANNOUNCEMENT))
                            FileGet.getAc()?.callAccessibilityServiceCommand { FileGet.getAc()?.JsonPareseClick(it.x,it.y) }
                        }
                        MyAccessbilityService.Companion.Command_Type.IdNode.toString() -> {

                            Log.d("TAG", "parserJson: it${it.type}")
                            FileGet.getAc()?.onAccessibilityEvent(AccessibilityEvent(AccessibilityEvent.TYPE_ANNOUNCEMENT))
                            FileGet.getAc()?.callAccessibilityServiceCommand { FileGet.getAc()?.JsonParseSelectNode(it.id,it.editText) }
                        }
                    }
                }
            }

            val timer = Timer()
            timer.schedule(task, it.delay)
        }

    }
}