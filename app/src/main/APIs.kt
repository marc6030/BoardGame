interface BoardGameAPI{
    @GET("hot")
    suspend fun searchGames(@Query("parameter") saerchTerm: String): Response<ResponseBody>
}