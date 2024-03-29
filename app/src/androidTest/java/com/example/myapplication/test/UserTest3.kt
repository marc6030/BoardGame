package com.example.myapplication.test

/*
class UserTest3 {
    private val viewModel = SharedViewModel()
    private val boardGameDataViewModel = BoardDataViewModel(viewModel)


    @Given("the user is on the front page")
    fun doGiven() {
        try {
            boardGameDataViewModel.fetchBoardGameCategories()
            val start_time = System.currentTimeMillis()
            val timeout = start_time + 25000

            while (System.currentTimeMillis() < timeout && viewModel.boardGameList == null) {
                Thread.sleep(200)
            }
            Assert.assertTrue(viewModel.boardGameList != null)
        } catch(e: Exception) {
            e.printStackTrace()
            Assert.assertTrue(false)
        }
    }

    @When ("the user selects a game from trending section")
    fun doWhen() {
        // should probably be a ui test
        Assert.assertTrue(true)
    }

    @Then ("the game details are obtained")
    fun doThen() {
        boardGameDataViewModel.fetchBoardGameData(viewModel.boardGameList!!.boardGames.first().id)
        val start_time = System.currentTimeMillis()
        val timeout = start_time + 25000

        while (System.currentTimeMillis() < timeout && viewModel.boardGameData == null) {
            Thread.sleep(200)
        }

        Assert.assertTrue(viewModel.boardGameData?.age != null)
        Assert.assertTrue(viewModel.boardGameData?.mechanisms != null)
        Assert.assertTrue(viewModel.boardGameData?.ratingBGG != null)
    }

}

 */
