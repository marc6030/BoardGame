package com.example.myapplication

import android.graphics.Bitmap
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


data class BoardGame(

    var isfavorite: Boolean = false,

    var isRated: Boolean = false,

    var ratingBGG: String = "",

    var userRating: String = "",

    var averageRatingBB: List<String> = emptyList(),

    var id: String = "",

    var yearPublished: String = "",

    var minPlayers: String = "",

    var maxPlayers: String = "",

    var playingTime: String = "",

    var name: String = "",

    var description: String = "",

    var age: String = "",

    var imageURL: String = "",

    var bitmap: Bitmap? = null,

    var averageWeight: String = "",

    var overallRank: String = "",

    var categoryRank: String = "",

    var mechanisms: List<String> = emptyList(),

    var publishers: List<String> = emptyList(),

    var categories: List<String> = emptyList(),

    var families: List<String> = emptyList(),

    var designers: List<String> = emptyList(),

    var artists: List<String> = emptyList(),

    var liked: String = "False",

    var user_rating: String = "0",

    var played_count: String = "0"

) {

    // debugging
    override fun toString(): String {
        return "BoardGame(name=$name, yearPublished=$yearPublished, minPlayers=$minPlayers, maxPlayers=$maxPlayers, playingTime=$playingTime, description=$description, age=$age, imageURL=$imageURL, averageWeight=$averageWeight, overallRank=$overallRank)"
    }

}
