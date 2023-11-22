package com.example.myapplication.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "boardgameitems", strict = false)
class BoardGameSearchItems {
    @field:ElementList(inline = true, name = "boardgameitem")
    var boardGameSearchItems: List<BoardGameSearch> = listOf()
}

@Root(name = "boardgame", strict = false)
class BoardGameSearch {
    @field:Element(name = "name", required = false)
    var name: String = ""

    @field:Element(name = "id", required = false)
    var id: String = ""

    // DIsabled for now. Can't retrieve thumbnails from the search function
    //@field:Element(name = "thumbnail", required = false)
    //var imgUrl: String = ""

    // debugging
    override fun toString(): String {
        return "BoardGame(name=$name, yearPublished=$id, minPlayers=imgUrl)"
    }
}
