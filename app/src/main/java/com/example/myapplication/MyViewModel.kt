package com.example.myapplication

import android.widget.ImageView
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
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import org.xml.sax.InputSource


class MyViewModel : ViewModel() {
    private val mainScope = MainScope()
    private var _boardGameData = MutableLiveData<BoardGame?>()
    var boardGameData: LiveData<BoardGame?> = _boardGameData
    private var _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading
    //var boardGameList = MutableLiveData<ArrayList<String>>()
    private var image = MutableLiveData<ImageView>()

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
        boardGame.name = document.getElementsByTagName("name").item(0).textContent
        boardGame.minPlayers = document.getElementsByTagName("minplayers").item(0).textContent
        boardGame.maxPlayers = document.getElementsByTagName("maxplayers").item(0).textContent
        boardGame.yearPublished = document.getElementsByTagName("yearpublished").item(0).textContent
        boardGame.age = document.getElementsByTagName("age").item(0).textContent
        boardGame.description = document.getElementsByTagName("description").item(0).textContent
        boardGame.playingTime = document.getElementsByTagName("playingtime").item(0).textContent
        return boardGame
    }
}
