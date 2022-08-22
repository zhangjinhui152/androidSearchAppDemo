package com.example.tedy.util

import com.example.tedy.AutoCommand

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object JsonUitl {


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
    fun convertJsonToClass(jsonStr :String ) : List<AutoCommand>?{

        val fromJson = jsonAdapter.fromJson(jsonStr)
        println("fromJson = ${fromJson}")

        return  fromJson
    }
}