package com.example.tedy

import com.squareup.moshi.JsonClass

data class AutoCommand(
    var type: String,
    var x: Float,
    var y: Float,
    var id: String,
    var editText: String,
    var delay: Long,

)
