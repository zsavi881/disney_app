package fr.isen.savi.disney_app.network
import retrofit2.http.GET
import retrofit2.http.Query


data class TmdbSearchResponse(val results: List<TmdbMovie>)
data class TmdbMovie(val poster_path: String?)

interface TmdbApi {
    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "fr-FR"
    ): TmdbSearchResponse
}