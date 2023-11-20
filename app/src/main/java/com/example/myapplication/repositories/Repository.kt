package com.example.myapplication.repositories

import com.example.myapplication.API.ApiService
import com.example.myapplication.BoardGame
import org.w3c.dom.Document
import org.xml.sax.InputSource
import retrofit2.HttpException
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element
import android.text.Html
import com.example.myapplication.BoardGameItem
import com.example.myapplication.BoardGameItems
import com.example.myapplication.models.BoardGameSearch
import com.example.myapplication.models.BoardGameSearchItems

class Repository(private val apiService: ApiService) {

    suspend fun getBoardGame(id: String): BoardGame {
        val response = apiService.getBoardGameXml(id)
        if (response.isSuccessful) {
            val xmlData = response.body() ?: throw NoSuchElementException("GameBoard connection error")
            return parseBoardGameXml(xmlData)
        } else {
            throw HttpException(response)
        }
    }

    suspend fun getBoardGameList(): BoardGameItems {
        val response = apiService.getBoardGameList()
        if (response.isSuccessful) {
            val xmlData = response.body() ?: throw NoSuchElementException("GameList connection error")
            return parseBoardGameList(xmlData)
        } else {
            throw HttpException(response)
        }
    }

    suspend fun getBoardGameSearch(userSearch: String): BoardGameSearchItems{
        val response = apiService.getBoardGameSearch(userSearch)
        if (response.isSuccessful) {
            val xmlData = response.body() ?: throw NoSuchElementException("GameList connection error")
            return parseSearchData(xmlData)
        } else {
            throw HttpException(response)
        }
    }

    fun parseSearchData(xmlData: String): BoardGameSearchItems {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document: Document = builder.parse(InputSource(StringReader(xmlData)))

        val boardGameSearchItems = BoardGameSearchItems()

        for (i in 0 until document.getElementsByTagName("item").length) {
            val newBoardGame = BoardGameSearch().apply {
                id = document.getElementsByTagName("item").item(i).attributes.getNamedItem("id").textContent
                name = document.getElementsByTagName("name").item(i).attributes.getNamedItem("value").textContent
            }
            boardGameSearchItems.boardGameSearchItems += newBoardGame
        }
        return boardGameSearchItems
    }

    fun parseBoardGameList(xmlData: String): BoardGameItems {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document: Document = builder.parse(InputSource(StringReader(xmlData)))

        val boardGameItems = BoardGameItems()

        for (i in 0 until document.getElementsByTagName("item").length) {
            val newBoardGame = BoardGameItem().apply {
                id = document.getElementsByTagName("item").item(i).attributes.getNamedItem("id").textContent
                name = document.getElementsByTagName("name").item(i).attributes.getNamedItem("value").textContent
                imgUrl  = document.getElementsByTagName("thumbnail").item(i).attributes.getNamedItem("value").textContent
            }
            boardGameItems.boardGames = boardGameItems.boardGames + newBoardGame
        }
        return boardGameItems
    }


    fun parseBoardGameXml(xmlData: String): BoardGame {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document: Document = builder.parse(InputSource(StringReader(xmlData)))

        val boardGame = BoardGame()

        // Extracting and setting the name
        val names = document.getElementsByTagName("name")
        var primaryNameFound = false
        for (i in 0 until names.length) {
            val nameElement = names.item(i) as Element
            if (nameElement.getAttribute("primary") == "true") {
                boardGame.name = nameElement.textContent
                primaryNameFound = true
                break
            }
        }
        if (!primaryNameFound && names.length > 0) {
            boardGame.name = names.item(0).textContent
        }

        // Extracting and setting other fields
        boardGame.yearPublished = document.getElementsByTagName("yearpublished").item(0)?.textContent ?: ""
        boardGame.minPlayers = document.getElementsByTagName("minplayers").item(0)?.textContent ?: ""
        boardGame.maxPlayers = document.getElementsByTagName("maxplayers").item(0)?.textContent ?: ""
        boardGame.playingTime = document.getElementsByTagName("playingtime").item(0)?.textContent ?: ""
        boardGame.age = document.getElementsByTagName("age").item(0)?.textContent ?: ""
        boardGame.description = Html.fromHtml(document.getElementsByTagName("description").item(0)?.textContent).toString()
        boardGame.imageURL = document.getElementsByTagName("image").item(0)?.textContent ?: ""
        boardGame.averageRating = document.getElementsByTagName("average").item(0)?.textContent ?: ""
        boardGame.averageWeight = document.getElementsByTagName("averageweight").item(0)?.textContent ?: ""

        var i = 0;
        while(document.getElementsByTagName("boardgamemechanic").item(i).textContent != "") {
            boardGame.mechanisms +=
                document.getElementsByTagName("boardgamemechanic").item(i)?.textContent ?: ""
            i++
        }
        val ranks = document.getElementsByTagName("rank")
        if (ranks.length > 0) {
            val overallRankElement = ranks.item(0) as Element
            val categoryRankElement = ranks.item(1) as Element
            boardGame.overallRank = overallRankElement.getAttribute("value")
            boardGame.categoryRank = categoryRankElement.getAttribute("value")
        }

        return boardGame
    }
}
