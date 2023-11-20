package com.example.myapplication

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "boardgameitems", strict = false)
class BoardGameItems(emptyList: List<Any>) {

    @field:ElementList(inline = true, name = "boardgameitem")
    var boardGames: List<BoardGameItem> = listOf()
}

@Root(name = "boardgameitem", strict = false)
class BoardGameItem {

    @field:Element(name = "id")
    var id: String = ""

    @field:Element(name = "name")
    var name: String = ""

    @field:Element(name = "imgurl")
    var imgUrl: String = ""



    fun shortTitel(): String{
        val index = name.indexOf(":")
        return if (index != -1) {
            name.substring(0, index)
        } else {
            name
        }
    }
}
