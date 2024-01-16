package com.example.myapplication

import android.graphics.Bitmap
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "boardgameitem", strict = false)
data class BoardGameItem (

    @field:Element(name = "id")
    var id: String = "",

    @field:Element(name = "name")
    var name: String = "",

    @field:Element(name = "imgurl")
    var imgUrl: String = "",

    @field:Element(name = "bitmap", required = false)
    var bitmap: Bitmap? = null,

    var liked: String = "False",

    var playedCount: String = "0",

    var rating: String = "",

    var category: List<String> = emptyList()



)
