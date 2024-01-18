package com.example.myapplication

import android.graphics.Bitmap

data class BoardGameItem (

    var id: String = "",

    var name: String = "",

    var imgUrl: String = "",

    var bitmap: Bitmap? = null,

    var liked: String = "False",

    var playedCount: String = "0",

    var rating: String = "",

    var category: List<String> = emptyList()



)
