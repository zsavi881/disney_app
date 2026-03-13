package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import fr.isen.savi.disney_app.model.Film
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

    private val _owners = MutableStateFlow<List<String>>(emptyList())
    val owners: StateFlow<List<String>> = _owners

    fun loadFilm(filmId: String) {
        //  Charger les données du film
        repository.getFilmById(filmId) { foundFilm ->
            _film.value = foundFilm
        }

        // Charger le statut de l'utilisateur actuel
        val userId = auth.currentUser?.uid
        if (userId != null) {
            repository.getFilmStatus(userId, filmId) { status ->
                _userStatusMap.value = status ?: emptyMap()
            }
        }

        // Charger la liste des propriétaires à proximité
        repository.getOwnersForFilm(filmId) { ownerList ->
            _owners.value = ownerList
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
            }
        }
    }
}