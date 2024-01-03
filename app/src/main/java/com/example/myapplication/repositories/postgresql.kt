package com.example.myapplication.repositories
import android.database.SQLException
import com.example.myapplication.BoardGame
import com.example.myapplication.BoardGameItem
import com.example.myapplication.BoardGameItems
import com.example.myapplication.models.BoardGameSearch
import com.example.myapplication.models.BoardGameSearchItems
import java.sql.Connection
import java.sql.DriverManager

class postgresql() {

    fun connectToDatabase(): Connection? {
        val url = "jdbc:postgresql://135.181.106.80:5432/school"
        val user = "firstuser"
        val password = "Studyhard1234."

        return try {
            DriverManager.getConnection(url, user, password).also {
                println("Connected to the PostgreSQL server successfully.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun getBoardGame(id: String): BoardGame {
        connectToDatabase()?.use { connection ->
            val statement = connection.prepareStatement("SELECT * FROM boardgame WHERE id_actual = ?")
            statement.setString(1, id)
            val resultSet = statement.executeQuery()

            if (resultSet.next()) {
                return BoardGame(
                    id = resultSet.getString("id_actual"),
                    name = resultSet.getString("name"),
                )
            } else {
                throw NoSuchElementException("Board game with ID $id not found")
            }
        } ?: throw SQLException("Database connection failed")
    }

    fun getBoardGameList(): BoardGameItems {
        val boardGames = mutableListOf<BoardGameItem>()

        connectToDatabase()?.use { connection ->
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery("SELECT * FROM boardgame")

            while (resultSet.next()) {
                boardGames.add(BoardGameItem(
                    id = resultSet.getString("id_actual"),
                    name = resultSet.getString("name"),
                    imgUrl = resultSet.getString("thumbnail")

                ))
            }
        } ?: throw SQLException("Database connection failed")

        return BoardGameItems(boardGames)
    }

    fun getBoardGameSearch(userSearch: String): BoardGameSearchItems {
        val boardGameSearchItems = mutableListOf<BoardGameSearch>()

        connectToDatabase()?.use { connection ->
            val statement = connection.prepareStatement("SELECT * FROM boardgame WHERE name LIKE ?")
            statement.setString(1, "%$userSearch%")
            val resultSet = statement.executeQuery()

            while (resultSet.next()) {
                boardGameSearchItems.add(BoardGameSearch(
                    id = resultSet.getString("id"),
                    name = resultSet.getString("name"),
                ))
            }
        } ?: throw SQLException("Database connection failed")

        return BoardGameSearchItems(boardGameSearchItems)
    }

    fun getBoardGameFromDatabase(id: String): BoardGame {
        connectToDatabase()?.use { connection ->
            val statement = connection.prepareStatement("SELECT * FROM boardgame WHERE id_actual = ?")
            statement.setString(1, id)
            val resultSet = statement.executeQuery()
/*
            if (resultSet.next()) {
                println(resultSet.getString("mechanisms"))

            }

 */

            if (resultSet.next()) {
                return BoardGame(
                    id = resultSet.getString("id_actual"),
                    name = resultSet.getString("name"),
                    yearPublished = resultSet.getString("year") ?: "???",
                    minPlayers = resultSet.getString("min_players") ?: "???",
                    maxPlayers = resultSet.getString("max_players") ?: "???",
                    playingTime = resultSet.getString("play_time") ?: "???",
                    age = resultSet.getString("age") ?: "???",
                    description = resultSet.getString("description"),
                    imageURL = resultSet.getString("image") ?: "???",
                    averageWeight = resultSet.getString("average") ?: "???",
                    ratingBGG = resultSet.getString("users_rated") ?: "0",
                    mechanisms = convertSqlArrayToList(resultSet.getArray("mechanisms")),
                    publishers = convertSqlArrayToList(resultSet.getArray("publishers")),
                    categories = convertSqlArrayToList(resultSet.getArray("categories")),
                    families = convertSqlArrayToList(resultSet.getArray("families")),
                    designers = convertSqlArrayToList(resultSet.getArray("designers")),
                    artists = convertSqlArrayToList(resultSet.getArray("artists")),
                    overallRank = resultSet.getString("overall_rank") ?: "???",
                    categoryRank = resultSet.getString("category_rank") ?: "???",
                    category = resultSet.getString("category") ?: "???"
                    // Add other fields as per your BoardGame data class
                )
            } else {
                throw NoSuchElementException("Board game with ID $id not found")
            }

        } ?: throw SQLException("Database connection failed")

    }

    private fun convertSqlArrayToList(sqlArray: java.sql.Array?): List<String> {
        sqlArray ?: return listOf()

        // Retrieve the array in a safe manner and handle possible exceptions
        val array: Array<Any> = try {
            sqlArray.array as Array<Any>
        } catch (e: Exception) {
            return listOf()
        }

        // Filter instances of String and convert them to a List
        return array.filterIsInstance<String>()
    }


    fun getListFromDatabase(attribute: String, boardGameId: String): List<String> {
        val list = mutableListOf<String>()
        connectToDatabase()?.use { connection ->
            val statement = connection.prepareStatement("SELECT * FROM $attribute WHERE board_game_id = ?")
            statement.setString(1, boardGameId)
            val resultSet = statement.executeQuery()

            while (resultSet.next()) {
                list.add(resultSet.getString("name")) // assuming each table has a 'name' column
            }
        }
        return list
    }
}





fun main() {
    //val bg = postgresql().getBoardGame("54")
    //val bgg = postgresql().getBoardGameList()
    // val bgs = postgresql().getBoardGameSearch("what da faq")
    val bgt = postgresql().getBoardGameFromDatabase("54")
    //println(bg)
    //println(bgg)
    // println(bgs)
    println(bgt)
}

