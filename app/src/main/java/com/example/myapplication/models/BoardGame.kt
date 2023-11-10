package com.example.myapplication

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root



@Root(name = "boardgame", strict = false)
class BoardGame {
    @field:Element(name = "yearpublished", required = false)
    var yearPublished: String = ""

    @field:Element(name = "minplayers", required = false)
    var minPlayers: String = ""

    @field:Element(name = "maxplayers", required = false)
    var maxPlayers: String = ""

    @field:Element(name = "playingtime", required = false)
    var playingTime: String = ""

    @field:Element(name = "name", required = false)
    var name: String = ""

    @field:Element(name = "description", required = false)
    var description: String = ""

    @field:Element(name = "age", required = false)
    var age: String = ""

    @field:Element(name = "image", required = false)
    var imageURL: String = ""

    @field:Element(name = "average", required = false)
    var averageRating : String = ""

    @field:Element(name = "averageweight", required = false)
    var averageWeight : String = ""

    @field:Element(name = "OverallRank", required = false)
    var overallRank : String = ""

    // debugging
    override fun toString(): String {
        return "BoardGame(name=$name, yearPublished=$yearPublished, minPlayers=$minPlayers, maxPlayers=$maxPlayers, playingTime=$playingTime, description=$description, age=$age, imageURL=$imageURL, averageRating=$averageRating, averageWeight=$averageWeight, overallRank=$overallRank)"
    }
}
