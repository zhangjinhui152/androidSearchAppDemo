package com.example.tedy

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.example.tedy.util.FileGet
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*


class MyAccessbilityService : AccessibilityService() {


    companion object {


        @JvmStatic
        var isServiceCreated = false

        @JvmStatic
        var isOpenActive = false



        var pkgName : String= "com.example.autoc"
        var jsonStr  =""
        enum class Command_Type(val type : String) {
            Click("Click"),
            IdNode("IdNode"),
        }
        private var moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())//使用kotlin反射处理，要加上这个
            .build()
        private val parameterizedType =
            Types.newParameterizedType(List::class.java, AutoCommand::class.java)
        private val jsonAdapter = moshi.adapter<List<AutoCommand>>(parameterizedType)

        fun convertClassToJson(autoCommands : List<AutoCommand>): String {

            val toJson = jsonAdapter.toJson(autoCommands)
            println("toJson = ${toJson}")
            return toJson

        }

        @JvmStatic
        fun setJsonAndPkgName(jsonStr: String,pkgName: String){
            this.jsonStr = jsonStr
            this.pkgName = pkgName

        }



        fun convertJsonToClass(jsonStr :String ) : List<AutoCommand>?{

            val fromJson = jsonAdapter.fromJson(jsonStr)
            println("fromJson = ${fromJson}")
//        fromJson?.forEach {
//            println("it.age = ${it.type}")
//        }
            return  fromJson
        }



    }

    fun setJsonAndPkgName2(jsonStr: String,pkgName: String){
        serviceInfo.packageNames[0] = pkgName
    }
    override fun onServiceConnected() {
        super.onServiceConnected()
        isServiceCreated = true
        FileGet.setAc(this)
        serviceInfo.packageNames[0] = "1"

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d("TAG", "event?.eventType: ${serviceInfo.packageNames[0]}")
        if (isOpenActive){
            isOpenActive = false
            Toast.makeText(FileGet.getMac(), "触发isOpenActive", Toast.LENGTH_LONG).show();
            parserJson(jsonStr)
            Log.d("TAG", "event?.eventType: ${serviceInfo.packageNames[0]}")
//            触发了TYPE_WINDOW_STATE_CHANGED的事件
            when(event?.eventType){
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    //解析JSON

                }
            }

        }


    }


    override fun onInterrupt() {
//        isServiceCreated = false
    }

    fun parserJson(jsonStr: String){
        Toast.makeText(FileGet.getMac(), "触发parserJson", Toast.LENGTH_LONG).show();
        //转换JSOP成class
        val listForAcom = convertJsonToClass(jsonStr)
        //遍历判断属于什么类型 就执行对应的指令
        listForAcom?.forEach {
            Log.d("TAG", "parserJson: it${it.type}")

            val task: TimerTask = object : TimerTask() {
                override fun run() {
                    when(it.type){
                        Command_Type.Click.toString() -> {

                            Log.d("TAG", "parserJson: it${it.type}")
                            JsonPareseClick(it.x,it.y)
                        }
                        Command_Type.IdNode.toString() -> {

                            Log.d("TAG", "parserJson: it${it.type}")
                            JsonParseSelectNode(it.id,it.editText)
                        }
                    }
                }
            }
            val timer = Timer()
            timer.schedule(task, it.delay)



        }

    }


    fun JsonParseSelectNode(id : String,editText: String){
        var inputNode : AccessibilityNodeInfo? = null
        var nodeFlag  = false

        if(windows == null){
            Log.d("rootInActiveWindow", "JsonParseSelectNode: ")
        }
        else{
            Log.d("rootInActiveWindow", "windows: ${windows.size} ")
            for(ws in windows){
                val root = ws.root
                val listForAcNode = root.findAccessibilityNodeInfosByViewId("com.example.autoc:id/${id}")
                if (listForAcNode.size != 0){
                    inputNode = listForAcNode[0]
                    nodeFlag = true

//                    for (node in listForAcNode){
//                        Log.d("TAG", "JsonParseSelectNode: ${node.toString()} ")
//                    }
                    Log.d("TAG", "JsonParseSelectNode: ${listForAcNode.size}")
                    break
                }

            }

            if (nodeFlag){
                Log.d("TAG", "JsonParseSelectNode: YES!!!!!!!!!!!${inputNode?.toString()}!!!!!!!!!!!!!")

                var data = Bundle()
//                data.putCharSequence("android:text","2222222")
                data?.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE," ${editText}");
                inputNode?.performAction(AccessibilityNodeInfo.ACTION_FOCUS)

                inputNode?.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,data)
//                inputNode?.text = "FUCK"
                Log.d("TAG", "JsonParseSelectNode: YES!!!!!!!!!!!${inputNode?.text}!!!!!!!!!!!!!")
            }

            else{

            }

        }


    }

    fun JsonPareseClick(x:Float = 0f,y :Float = 0f){
        Log.d("DoubleClick", "onAccessibilityEvent: ")
        var path : Path = Path()
        path.moveTo(x,y)
        var builder = GestureDescription.Builder()
        var ges = builder.addStroke(GestureDescription.StrokeDescription(path, 0, 50)).build()
        dispatchGesture(ges,object : GestureResultCallback(){   override fun onCancelled(gestureDescription: GestureDescription?) {
            super.onCancelled(gestureDescription)
            Log.d("TonAccessibilityEvent", "onAccessibilityEvent: ")
            path.close()
        }
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                Log.d("TonAccessibilityEvent", "onAccessibilityEvent: ")
            }},null)

    }



}