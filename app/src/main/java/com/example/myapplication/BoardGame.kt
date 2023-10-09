package com.example.myapplication

import androidx.compose.runtime.Composable
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import androidx.compose.ui.graphics.ImageBitmap
import android.os.Bundle



@Root(name = "boardgame")
class BoardGame {
    @field:Element(name = "yearpublished")
    var yearPublished: String = ""

    @field:Element(name = "minplayers")
    var minPlayers: String = ""

    @field:Element(name = "maxplayers")
    var maxPlayers: String = ""

    @field:Element(name = "playingtime")
    var playingTime: String = ""

    @field:Element(name = "name")
    var name: String = ""

    @field:Element(name = "description")
    var description: String = ""

    @field:Element(name = "age")
    var age: String = ""

    @field:Element(name = "image")
    var imageURL: String = ""

}
