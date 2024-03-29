package com.example.myapplication.test

/*
class UserTest2 {
    private val viewModel = SharedViewModel()
    private val ratingsViewModel = RatingsViewModel(viewModel)
    private val boardGameDataViewModel = BoardDataViewModel(viewModel)
    private val boardGameSearchViewModel = BoardSearchViewModel(viewModel)

    @Given("the game is in the search results")
    fun doGiven() {
        try {
            boardGameSearchViewModel.fetchGameBoardSearch("Anima")
            val start_time = System.currentTimeMillis()
            val timeout = start_time + 15000

            while (System.currentTimeMillis() < timeout && boardGameSearchViewModel.boardGameSearch == null) {
                Thread.sleep(200)
            }
            val searchResults = boardGameSearchViewModel.boardGameSearch?.boardGameSearchItems
            val isAnimaPresent = searchResults?.any { it.name.contains("Anima", ignoreCase = true)}
            Assert.assertTrue(isAnimaPresent != null && isAnimaPresent)
        } catch(e: Exception) {
            Assert.assertTrue(false)
            e.printStackTrace()
        }
    }

    @When ("the user selects Anima")
    fun doWhen() {
        // should probably be a ui test
        Assert.assertTrue(true)
    }

    @Then ("the details of the game should be accessible")
    fun doThen() {
        val animaGameSearchResult = boardGameSearchViewModel.boardGameSearch!!.boardGameSearchItems.find{ it.name == "Anima" }
        try {
            boardGameDataViewModel.fetchBoardGameData(animaGameSearchResult!!.id)
            val start_time = System.currentTimeMillis()
            val timeout = start_time + 15000

            while (System.currentTimeMillis() < timeout && viewModel.boardGameData == null) {
                Thread.sleep(200)
            }
        } catch (e: Exception) {
            Assert.assertTrue(false)
            e.printStackTrace()
        }
        Log.v("AnimaAge", viewModel.boardGameData!!.age)
        Assert.assertTrue(viewModel.boardGameData?.age == "4")
        Log.v("AnimaAge", viewModel.boardGameData!!.mechanisms.first())
        Assert.assertTrue(viewModel.boardGameData?.mechanisms!!.contains("Dice Rolling"))
        Log.v("AnimaAge", viewModel.boardGameData!!.ratingBGG)
        Assert.assertFalse(viewModel.boardGameData?.ratingBGG.isNullOrEmpty())
    }
}

 */
