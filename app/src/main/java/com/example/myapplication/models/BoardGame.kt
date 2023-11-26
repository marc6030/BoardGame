package com.example.myapplication

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root



@Root(name = "boardgame", strict = false)
data class BoardGame(

    @field:Element(name = "isfavorite", required = false)
    var isfavorite: Boolean = false,

    @field:Element(name = "isRated", required = false)
    var isRated: Boolean = false,

    @field:Element(name = "ratingBGG", required = false)
    var ratingBGG: String = "",

    @field:Element(name = "ratingUser", required = false)
    var userRating: String = "",

    @field:Element(name = "averageRatingBB", required = false)
    var averageRatingBB: Int = 0,

    @field:Element(name = "id", required = false)
    var id: String = "",

    @field:Element(name = "yearpublished", required = false)
    var yearPublished: String = "",

    @field:Element(name = "minplayers", required = false)
    var minPlayers: String = "",

    @field:Element(name = "maxplayers", required = false)
    var maxPlayers: String = "",

    @field:Element(name = "playingtime", required = false)
    var playingTime: String = "",

    @field:Element(name = "name", required = false)
    var name: String = "",

    @field:Element(name = "description", required = false)
    var description: String = "",

    @field:Element(name = "age", required = false)
    var age: String = "",

    @field:Element(name = "image", required = false)
    var imageURL: String = "",

    @field:Element(name = "averageweight", required = false)
    var averageWeight: String = "",

    @field:Element(name = "OverallRank", required = false)
    var overallRank: String = "",

    @field:Element(name = "CategoryRank", required = false)
    var categoryRank: String = "",

    @field:Element(name = "Category", required = false)
    var category: String = "",

    @field:Element(name = "Mechanisms", required = false)
    var mechanisms: List<String> = emptyList(),

    @field:Element(name = "Publishers", required = false)
    var publishers: List<String> = emptyList(),

    @field:Element(name = "Categories", required = false)
    var categories: List<String> = emptyList(),

    @field:Element(name = "Families", required = false)
    var families: List<String> = emptyList(),

    @field:Element(name = "Designers", required = false)
    var designers: List<String> = emptyList(),

    @field:Element(name = "Artists", required = false)
    var artists: List<String> = emptyList(),

    ) {
    fun shortTitel(): String{
        val index = name.indexOf(":")
        return if (index != -1) {
            name.substring(0, index)
        } else {
            name
        }
    }

    // debugging
    override fun toString(): String {
        return "BoardGame(name=$name, yearPublished=$yearPublished, minPlayers=$minPlayers, maxPlayers=$maxPlayers, playingTime=$playingTime, description=$description, age=$age, imageURL=$imageURL, averageWeight=$averageWeight, overallRank=$overallRank)"
    }
}
