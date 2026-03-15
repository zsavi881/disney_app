package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.model.UserProfile
import fr.isen.savi.disney_app.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilmDetailViewModel : ViewModel() {
    private val repository = FirebaseRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _film = MutableStateFlow<Film?>(null)
    val film: StateFlow<Film?> = _film

    private val _userStatusMap = MutableStateFlow<Map<String, Any>>(emptyMap())
    val userStatusMap: StateFlow<Map<String, Any>> = _userStatusMap

    // on stocke maintenant des objets UserProfile
    private val _ownersProfiles = MutableStateFlow<List<UserProfile>>(emptyList())
    val ownersProfiles: StateFlow<List<UserProfile>> = _ownersProfiles

    fun loadFilm(filmId: String) {
        repository.getFilmById(filmId) { foundFilm ->
            _film.value = foundFilm
        }

        val userId = auth.currentUser?.uid
        if (userId != null) {
            repository.getFilmStatus(userId, filmId) { status ->
                _userStatusMap.value = status ?: emptyMap()
            }
        }

        repository.getOwnersForFilm(filmId) { ownerIds ->
            if (ownerIds.isEmpty()) {
                _ownersProfiles.value = emptyList()
            } else {
                fetchProfilesFromIds(ownerIds)
            }
        }
    }

    private fun fetchProfilesFromIds(ids: List<String>) {
        val loadedProfiles = mutableListOf<UserProfile>()
        var count = 0

        ids.forEach { id ->
            repository.getUserInfo(id) { profile ->
                profile?.let { loadedProfiles.add(it) }
                count++

                if (count == ids.size) {
                    _ownersProfiles.value = loadedProfiles.toList()
                }
            }
        }
    }

    fun updateStatus(filmId: String, key: String, value: Any) {
        val userId = auth.currentUser?.uid ?: return
        val currentStatus = _userStatusMap.value.toMutableMap()
        currentStatus[key] = value
        _userStatusMap.value = currentStatus

        repository.updateFilmStatus(userId, filmId, currentStatus) { success ->
            if (!success) {
                loadFilm(filmId)
            } else {
                loadFilm(filmId)
            }
        }
    }
}