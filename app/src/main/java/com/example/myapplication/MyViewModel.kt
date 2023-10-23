package com.example.myapplication


import android.text.Html
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory


class MyViewModel : ViewModel() {
    private val mainScope = MainScope()
    private var _isLoading = MutableLiveData<Boolean>()
    private var _boardGameData = MutableLiveData<BoardGame?>()
    private var _boardGameList = MutableLiveData<BoardGameItems?>()

    var isLoading: LiveData<Boolean> = _isLoading
    var boardGameDataList: LiveData<BoardGameItems?> = _boardGameList
    var boardGameData: LiveData<BoardGame?> = _boardGameData


     fun fetchBoardGameList(url: String) {             // Lige nu er det hot listen
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val boardGameList: BoardGameItems = getDataAsBoardGameList(url)
                _boardGameList.postValue(boardGameList)
            } catch (e: Exception) {
                _boardGameList.postValue(null)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }



    fun fetchBoardGameData(id: String) {
        val url: String = "https://api.geekdo.com/xmlapi/boardgame/$id"
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val boardGame: BoardGame = getDataAsBoardGame(url)
                _boardGameData.postValue(boardGame)
            } catch (e: Exception) {
                _boardGameData.postValue(null)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }


    private fun fetchData(url: String): Deferred<String> {
        return mainScope.async {
            try {
                makeNetworkRequest(url)
            } catch (e: Exception) {
                ""
            }
        }
    }

    private suspend fun makeNetworkRequest(url: String): String {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.string()?: ""
            } else {
                ""
            }
        }
    }

    private suspend fun getDataAsBoardGame(url: String): BoardGame{
        val strDeferred = fetchData(url)
        val xmlData = strDeferred.await()
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(InputSource(StringReader(xmlData)))
        val boardGame = BoardGame()
        val numberOfNames = document.getElementsByTagName("name").length
        println(numberOfNames)
        for(i in 0..numberOfNames){
           if(document.getElementsByTagName("name").item(i).hasAttributes())
               println(document.getElementsByTagName("name").item(i).attributes)
                boardGame.name = document.getElementsByTagName("name").item(0).textContent
        }
        boardGame.minPlayers = document.getElementsByTagName("minplayers").item(0).textContent
        boardGame.maxPlayers = document.getElementsByTagName("maxplayers").item(0).textContent
        boardGame.yearPublished = document.getElementsByTagName("yearpublished").item(0).textContent
        boardGame.age = document.getElementsByTagName("age").item(0).textContent
        boardGame.description = Html.fromHtml(document.getElementsByTagName("description").item(0).textContent).toString()
        boardGame.playingTime = document.getElementsByTagName("playingtime").item(0).textContent
        return boardGame
    }

    private suspend fun getDataAsBoardGameList(url: String): BoardGameItems{
        val strDeferred = fetchData(url)
        val xmlData = strDeferred.await()
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(InputSource(StringReader(xmlData)))

        val boardGameItems = BoardGameItems()

        for (i in 0 until document.getElementsByTagName("item").length) {
            val newBoardGame = BoardGameItem().apply {
                id = document.getElementsByTagName("item").item(i).attributes.getNamedItem("id").textContent
                name = document.getElementsByTagName("name").item(i).attributes.getNamedItem("value").textContent
            }
            boardGameItems.boardGames = boardGameItems.boardGames + newBoardGame
        }
        return boardGameItems

    }

}


