package com.example.myapplication.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "boardgameitems", strict = false)
data class BoardGameSearchItems(
    @field:ElementList(inline = true, name = "boardgameitem")
    var boardGameSearchItems: MutableList<BoardGameSearch> = mutableListOf()
)

@Root(name = "boardgame", strict = false)
data class BoardGameSearch(

    @field:Element(name = "name", required = false)
    var name: String = "",

    @field:Element(name = "id", required = false)
    var id: String = ""
)
