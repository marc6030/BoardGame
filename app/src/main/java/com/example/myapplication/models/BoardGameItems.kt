package com.example.myapplication

import android.graphics.Bitmap
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "boardgameitems", strict = false)
data class BoardGameItems(

    @field:ElementList(inline = true, name = "boardgameitem")
    var boardGames: List<BoardGameItem> = listOf()

)

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
)
