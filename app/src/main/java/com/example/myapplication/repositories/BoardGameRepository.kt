
import android.util.Log
import com.example.myapplication.BoardGame
import com.example.myapplication.BoardGameItem
import com.example.myapplication.models.BoardGameSearch
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

    private val baseUrl = "http://135.181.106.80:5050" // Replace with your Flask API URL

    private fun makeApiRequest(urlPath: String): String {
        val url = URL(baseUrl + urlPath)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        BufferedReader(InputStreamReader(connection.inputStream)).use {
            return it.readText()
        }
    }

    fun getBoardGameList(limit: Int, offset: Int, category: String? = null): List<BoardGameItem> {
        val urlPath = if (category != null) {
            "/boardgameitems/$category/$limit/$offset/"
        } else {
            "/boardgameitems/none/$limit/$offset/"
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

    fun getBoardGameSearch(userSearch: String, limit: Int, offset: Int): List<BoardGameSearch> {
        val urlPath = "/boardgamesearch/$userSearch/$limit/$offset/"
        val jsonResponse = makeApiRequest(urlPath)
        val jsonArray = JSONArray(jsonResponse)
        val boardGameSearchItems = mutableListOf<BoardGameSearch>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            boardGameSearchItems.add(
                BoardGameSearch(
                    id = jsonObject.getString("id_actual"),
                    name = jsonObject.getString("name")
                )
            )
        }
        return boardGameSearchItems
    }


    fun getBoardGame(id: String): BoardGame {

        val urlPath = "/boardgame/$id/"
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
            // picture = jsonObject.optByteArray("image_data") // Uncomment if needed
        )
    }
    fun convertJsonArrayToList(jsonArray: JSONArray): List<String> {
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }
        return list
    }
    fun toggleFavoriteGame(username: String, id: String) {
        val urlPath = "/favoritetoggle/$id/$username/"
        makeApiRequest(urlPath) // Assuming this is a POST request
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




fun main() {
    //val bg = postgresql().getBoardGame("54")
    //val bgg = postgresql().getBoardGameList()
    // val bgs = postgresql().getBoardGameSearch("what da faq")
    print(BoardGameRepository().getBoardGameList(10, 10, "fighting"))
    //println(bg)
    //println(bgg)
    // println(bgs)
    // println(bgt)
}

