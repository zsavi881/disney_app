package fr.isen.savi.disney_app.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.isen.savi.disney_app.model.Categorie
import fr.isen.savi.disney_app.network.TmdbApi
import fr.isen.savi.disney_app.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UniverseViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    private val TMDB_API_KEY = "3bce6c52326af039cd5fa177fb61cd1d"

    private val tmdbApi = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TmdbApi::class.java)

    private val _categories = MutableStateFlow<List<Categorie>>(emptyList())
    val categories: StateFlow<List<Categorie>> = _categories

    val expandedItems = mutableStateListOf<String>()

    init {
        loadData()
    }

    private fun loadData() {
        firebaseRepository.getCategories { allCategories ->
            _categories.value = allCategories
        }
    }


    suspend fun fetchImageUrl(movieTitle: String): String {
        return try {
            val cleanTitle = movieTitle.trim()

            val response = tmdbApi.searchMovie(TMDB_API_KEY, cleanTitle)
            var movie = response.results.firstOrNull()

            val posterPath = movie?.poster_path

            if (posterPath != null) {
                "https://image.tmdb.org/t/p/w500$posterPath"
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}