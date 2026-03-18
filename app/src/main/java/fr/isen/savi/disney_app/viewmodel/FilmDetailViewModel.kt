package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.model.UserProfile
import fr.isen.savi.disney_app.network.TmdbApi
import fr.isen.savi.disney_app.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FilmDetailViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    private val auth = FirebaseAuth.getInstance()

    private val TMDB_API_KEY = "3bce6c52326af039cd5fa177fb61cd1d"
    private val tmdbApi = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TmdbApi::class.java)

    private val _film = MutableStateFlow<Film?>(null)
    val film: StateFlow<Film?> = _film

    private val _userStatusMap = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val userStatusMap: StateFlow<Map<String, Boolean>> = _userStatusMap

    private val _ownersProfiles = MutableStateFlow<List<UserProfile>>(emptyList())
    val ownersProfiles: StateFlow<List<UserProfile>> = _ownersProfiles

    suspend fun fetchImageUrl(movieTitle: String): String = withContext(Dispatchers.IO) {
        try {
            val response = tmdbApi.searchMovie(TMDB_API_KEY, movieTitle.trim())
            val posterPath = response.results.firstOrNull()?.poster_path
            if (posterPath != null) "https://image.tmdb.org/t/p/w500$posterPath" else ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun loadFilm(filmId: String) {
        repository.getFilmById(filmId) { foundFilm ->
            _film.value = foundFilm
        }

        val userId = auth.currentUser?.uid
        if (userId != null) {
            repository.getFilmStatus(userId, filmId) { status ->
                val formattedStatus = status?.mapValues { it.value as? Boolean ?: false } ?: emptyMap()
                _userStatusMap.value = formattedStatus
            }
        }

        repository.getOwnersForFilm(filmId) { ownerIds ->
            fetchProfilesFromIds(ownerIds)
        }
    }

    private fun fetchProfilesFromIds(ids: List<String>) {
        if (ids.isEmpty()) {
            _ownersProfiles.value = emptyList()
            return
        }
        val loadedProfiles = mutableListOf<UserProfile>()
        var count = 0
        ids.forEach { id ->
            repository.getUserInfo(id) { profile ->
                profile?.let { loadedProfiles.add(it) }
                count++
                if (count == ids.size) _ownersProfiles.value = loadedProfiles.toList()
            }
        }
    }

    fun updateStatus(filmId: String, key: String, value: Boolean) {
        val userId = auth.currentUser?.uid ?: return

        val currentStatus = _userStatusMap.value.toMutableMap()
        currentStatus[key] = value
        _userStatusMap.value = currentStatus

        repository.updateFilmStatus(userId, filmId, currentStatus as Map<String, Any>) { success ->
            repository.getOwnersForFilm(filmId) { ownerIds ->
                fetchProfilesFromIds(ownerIds)
            }
        }
    }
}