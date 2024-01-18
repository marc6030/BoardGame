
import android.util.Log
import com.example.myapplication.BoardGame
import com.example.myapplication.BoardGameItem
import com.example.myapplication.models.BoardGameSearch
import com.example.myapplication.models.Categories
import com.example.myapplication.models.User
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class BoardGameRepository {

    private val baseUrl = "http://135.181.106.80:5050"
    private val youtubeUrl = "https://www.googleapis.com/youtube/v3/search?key=AIzaSyCUsP8-FIzZFeCNKk4yVgVUiY6pYAsl5SQ&q="

    private fun makeApiRequest(urlPath: String): String {
        val url = URL(baseUrl + urlPath)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        BufferedReader(InputStreamReader(connection.inputStream)).use {
            return it.readText()
        }
    }

    private fun makeApiPostRequest(urlPath: String): String {
        val url = URL(baseUrl + urlPath)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true

        BufferedReader(InputStreamReader(connection.inputStream)).use {
            return it.readText()
        }
    }

    suspend fun youtubeApiRequest(urlPath: String): String {
        val type = "&type=video"
        val url = URL(youtubeUrl + urlPath + type)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        BufferedReader(InputStreamReader(connection.inputStream)).use {
            return it.readText()
        }
    }

    suspend fun searchYoutube(name: String): String {

        val jsonResponse = youtubeApiRequest(name)
        val jsonObject = JSONObject(jsonResponse)

        val itemsArray = jsonObject.getJSONArray("items")
        val firstItem = itemsArray.getJSONObject(0)
        val idObject = firstItem.getJSONObject("id")

        val videoId = idObject.getString("videoId")

        return videoId
    }

    suspend fun getBoardGameList(limit: Int, offset: Int, category: String? = null, username: String): List<BoardGameItem> {
        val urlPath = if (category != null) {
            val safeCategory = category.replace("/", "--")
            "/boardgameitems/$safeCategory/$limit/$offset/$username/"
        } else {
            "/boardgameitems/none/$limit/$offset/$username/"
        }
        val jsonResponse = makeApiRequest(urlPath)
        val jsonArray = JSONArray(jsonResponse)
        val boardGames = mutableListOf<BoardGameItem>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            Log.v("tada", "${jsonArray.getJSONObject(i)}")
            boardGames.add(
                BoardGameItem(
                    id = jsonObject.getString("id_actual"),
                    name = jsonObject.getString("name"),
                    imgUrl = jsonObject.getString("image")
                )
            )
        }
        return boardGames
    }

    suspend fun getBoardGameToRecentList(userID: String): List<BoardGameItem>{
        val urlPath = "/recents/$userID/"
        val jsonResponse = makeApiRequest(urlPath)
        val jsonArray = JSONArray(jsonResponse)
        val recentBoardGameItems = mutableListOf<BoardGameItem>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            recentBoardGameItems.add(
                BoardGameItem(
                    id = jsonObject.getString("id_actual"),
                    name = jsonObject.getString("name"),
                    imgUrl = jsonObject.getString("image")
                )
            )
        }
        return recentBoardGameItems
    }

    suspend fun checkOrCreateUser(userID: String): Unit{
        val urlPath = "/check_or_create_user/$userID/"
        makeApiPostRequest(urlPath)
        return
    }

    suspend fun getNumberOfGamesAndStreak(UserID : String) : List<User> {
        val urlPath = "/users_key_info/$UserID/"
        val jsonResponse = makeApiRequest(urlPath)
        val jsonArray = JSONArray(jsonResponse)
        val users = mutableListOf<User>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            users.add(
                User(
                    streak = jsonObject.getString("streak"),
                    playedGames = jsonObject.optString("played_games"),
                    ratedGames = jsonObject.getString("rated_games"),
                    likedGames = jsonObject.getString("liked_games")
                )
            )
        }
        return users
    }

    suspend fun addBoardGameToRecentList(userID: String, gameID : String){
        val urlPath = "/recents/$userID/$gameID"
        makeApiRequest(urlPath)
    }

    suspend fun getBoardGameSearch(userSearch: String, limit: Int, offset: Int, categories: Map<String, Boolean>): List<BoardGameSearch> {
        var urlPath = "/boardgamesearch/$userSearch/$limit/$offset/"
        var first = true
        categories.forEach { (category, state) ->
            if (state) {
                if (first) {
                    urlPath += "?categories=$category"
                    first = false
                } else {
                    urlPath += "&categories=$category"
                }
            }
        }

        val jsonResponse = makeApiRequest(urlPath)
        val jsonArray = JSONArray(jsonResponse)
        val boardGameSearchItems = mutableListOf<BoardGameSearch>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val textContent = convertHtmlToStructuredText(jsonObject.getString("description"))
            boardGameSearchItems.add(
                BoardGameSearch(
                    id = jsonObject.getString("id_actual"),
                    name = jsonObject.getString("name"),
                    imgUrl = jsonObject.getString("image"),
                    description = textContent
                )
            )
        }
        return boardGameSearchItems
    }

    suspend fun getBoardGame(id: String, username: String): BoardGame {

        val urlPath = "/boardgame/$id/$username"
        val jsonResponse = makeApiRequest(urlPath)
        val jsonObject = JSONObject(jsonResponse)

        val textContent = convertHtmlToStructuredText(jsonObject.getString("description"))
        return BoardGame(
            id = jsonObject.getString("id_actual"),
            name = jsonObject.getString("name"),
            yearPublished = jsonObject.optString("year", "???"),
            minPlayers = jsonObject.optString("min_players", "???"),
            maxPlayers = jsonObject.optString("max_players", "???"),
            playingTime = jsonObject.optString("play_time", "???"),
            age = jsonObject.optString("age", "???"),
            description = textContent,
            imageURL = jsonObject.optString("image", "???"),
            averageWeight = jsonObject.optString("weight", "???"),
            ratingBGG = jsonObject.optString("average", "0"),
            mechanisms = convertJsonArrayToList(jsonObject.getJSONArray("mechanisms")),
            publishers = convertJsonArrayToList(jsonObject.getJSONArray("publishers")),
            categories = convertJsonArrayToList(jsonObject.getJSONArray("categories")),
            families = convertJsonArrayToList(jsonObject.getJSONArray("families")),
            designers = convertJsonArrayToList(jsonObject.getJSONArray("designers")),
            artists = convertJsonArrayToList(jsonObject.getJSONArray("artists")),
            overallRank = jsonObject.optString("overall_rank", "???"),
            categoryRank = jsonObject.optString("category_rank", "???"),
            liked = jsonObject.optString("is_liked", "False"),
            user_rating = jsonObject.optString("user_rating", "0"))
    }
    suspend fun fetchAverageBbRating(id : String): Double {
        val urlPath = "/getbbratings/$id/"
        val jsonResponse = makeApiRequest(urlPath)
        val jsonArray = JSONArray(jsonResponse)
        val bbRatings = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            bbRatings.add(jsonObject.getString("liked"))
        }
        var averageRating = 0.0
        for (rating: String in bbRatings) {
            averageRating += rating.toFloat()
        }
        if(averageRating != 0.0){
            averageRating /= bbRatings.size
        }
        return averageRating
    }
    suspend fun convertJsonArrayToList(jsonArray: JSONArray): List<String> {
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }
        return list
    }
    suspend fun toggleFavoriteGame(username: String, id: String) {
        val urlPath = "/favoritetoggle/$id/$username/"
        makeApiRequest(urlPath) // Assuming this is a POST request
    }

     suspend fun getFavoriteGames(username: String, limit: Int, offset: Int) : List<BoardGameItem> {
        val urlPath = "/favorite-gameboard-all/$username/$limit/$offset/"
        val jsonResponse = makeApiRequest(urlPath)
        val jsonArray = JSONArray(jsonResponse)
        val favoriteBoardGames = mutableListOf<BoardGameItem>()

         if (jsonArray.length() == 0) {
             return favoriteBoardGames
         }

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            favoriteBoardGames.add(
                BoardGameItem(
                    id = jsonObject.getString("id_actual"),
                    name = jsonObject.getString("name"),
                    imgUrl = jsonObject.getString("image")
                )
            )
        }
        return favoriteBoardGames
    }

    suspend fun getPlayedGames(username: String, limit: Int, offset: Int): List<BoardGameItem>{
        val urlPath = "/get_user_played/$username/$limit/$offset/"
        val jsonResponse = makeApiRequest(urlPath)
        val jsonArray = JSONArray(jsonResponse)
        val playedBoardGames = mutableListOf<BoardGameItem>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            playedBoardGames.add(
                BoardGameItem(
                    id = jsonObject.getString("id_actual"),
                    name = jsonObject.getString("name"),
                    imgUrl = jsonObject.getString("image"),
                    playedCount = jsonObject.getString("played_count")
                )
            )
        }
        return playedBoardGames
    }

    suspend fun getRatedGames(UserID: String, limit : Int, offset: Int): List<BoardGameItem>{
        val urlPath = "/get_user_ratings/$UserID/$limit/$offset/"
        val jsonResponse = makeApiRequest(urlPath)
        val jsonArray = JSONArray(jsonResponse)
        val ratedBoardGames = mutableListOf<BoardGameItem>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            ratedBoardGames.add(
                BoardGameItem(
                    id = jsonObject.getString("id_actual"),
                    name = jsonObject.getString("name"),
                    imgUrl = jsonObject.getString("image"),
                    rating = jsonObject.getString("liked")
                )
            )
        }
        return ratedBoardGames
    }

    suspend fun addOrRemovePlayedGame(UserID: String, gameID: String, increment : String){
        val urlPath = "/update_played_games/$UserID/$gameID/$increment/"
        print(makeApiRequest(urlPath))
    }

    suspend fun toggleRatingGame(username: String, id: String, rating: String) {
        val urlPath = "/ratingstoggle/$id/$username/$rating/"
        makeApiPostRequest(urlPath) // Assuming this is a POST request
    }

    suspend fun getAllCategories(): Categories {
        val urlPath = "/boardGameCategories/"
        val jsonResponse = makeApiRequest(urlPath)
        val jsonObject = JSONObject(jsonResponse)
        val jsonCategoriesArray = jsonObject.getJSONArray("categories")
        val categoriesList = mutableListOf<String>()

        for (i in 0 until jsonCategoriesArray.length()) {
            val category = jsonCategoriesArray.getString(i)
            categoriesList.add(category)
        }

        return Categories(categoriesList)
    }

    private fun convertHtmlToStructuredText(html: String): String {
        val document = Jsoup.parse(html)
        return buildStructuredText(document.body())
    }

    private fun buildStructuredText(element: Element): String {
        val sb = StringBuilder()
        for (node in element.childNodes()) {
            when (node) {
                is TextNode -> {
                    sb.append(node.text().trim { it <= ' ' })
                }
                is Element -> {
                    when (node.tagName()) {
                        "p" -> sb.append("\n\n").append(buildStructuredText(node))
                        "br" -> sb.append("\n")
                        // Add more tags here as needed
                        else -> sb.append(buildStructuredText(node))
                    }
                }
            }
        }
        return sb.toString().trim()
    }

}
