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
import com.example.tedy.util.JsonUitl
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


        enum class Command_Type(val type : String) {
            Click("Click"),
            IdNode("IdNode"),
        }








    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        isServiceCreated = true
        FileGet.setAc(this)


    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d("TAG", "event?.eventType: ${serviceInfo.packageNames[0]}")
        if (isOpenActive){
            isOpenActive = false

        }



    }


    override fun onInterrupt() {
//        isServiceCreated = false
    }

    fun callAccessibilityServiceCommand(command: ()->Unit){
        command()
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
//                Log.d("rootInActiveWindow", "windows: ${root.contentDescription} ")
                val listForAcNode = root.findAccessibilityNodeInfosByViewId("${pkgName.trimIndent()}:id/${id}")
                if (listForAcNode.size != 0){
                    Log.d("listForAcNode", "JsonParseSelectNode:${listForAcNode.size} ")
                    inputNode = listForAcNode[0]
                    nodeFlag = true
                    for (node in listForAcNode){
                        Log.d("TAG", "JsonParseSelectNode: ${node.toString()} ")
                    }
                    Log.d("TAG", "JsonParseSelectNode: ${listForAcNode.size}")
                    break
                }
                else{
                    continue
                }

            }
//            if (rootInActiveWindow != null) {
//                var listForAcNode =
//                    rootInActiveWindow.findAccessibilityNodeInfosByViewId("${pkgName.trimIndent()}:id/${id}")
//                if (listForAcNode.size != 0){
//                    Log.d("listForAcNode", "JsonParseSelectNode:${listForAcNode.size} ")
//                    inputNode = listForAcNode[0]
//                    nodeFlag = true
//                    for (node in listForAcNode){
//                        Log.d("TAG", "JsonParseSelectNode: ${node.toString()} ")
//                    }
//                    Log.d("TAG", "JsonParseSelectNode: ${listForAcNode.size}")
//
//                }
//            }
            if (nodeFlag){
                Log.d("TAG", "JsonParseSelectNode: YES!!!!!!!!!!!${inputNode?.toString()}!!!!!!!!!!!!!")

                var data = Bundle()

                data?.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE," ${editText}");
                inputNode?.performAction(AccessibilityNodeInfo.ACTION_FOCUS)

                inputNode?.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,data)

                Log.d("TAG", "JsonParseSelectNode: YES!!!!!!!!!!!${inputNode?.text}!!!!!!!!!!!!!")
            }

            else{

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



}}