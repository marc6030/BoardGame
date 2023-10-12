package com.example.myapplication

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "boardgameitems")
class BoardGameItems {

    @field:ElementList(inline = true, name = "boardgameitem")
    var boardGames: List<BoardGameItem> = listOf()
}

@Root(name = "boardgameitem")
class BoardGameItem {

    @field:Element(name = "id")
    var id: String = ""

    @field:Element(name = "name")
    var name: String = ""
}
