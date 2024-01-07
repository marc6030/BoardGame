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

    @field:Element(name = "Picture", required = false)
    var picture: ByteArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BoardGameItem

        if (picture != null) {
            if (other.picture == null) return false
            if (!picture.contentEquals(other.picture)) return false
        } else if (other.picture != null) return false

        return true
    }

    override fun hashCode(): Int {
        return picture?.contentHashCode() ?: 0
    }
}
