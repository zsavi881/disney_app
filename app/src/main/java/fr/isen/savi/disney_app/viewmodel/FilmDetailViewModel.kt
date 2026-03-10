package fr.isen.savi.disney_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.repository.FirebaseRepository
import fr.isen.savi.disney_app.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilmDetailViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()
    private val authRepository = AuthRepository()

    private val _film = MutableStateFlow<Film?>(null)
    val film: StateFlow<Film?> = _film

    private val _owners = MutableStateFlow<List<String>>(emptyList())
    val owners: StateFlow<List<String>> = _owners

    private val _userStatusMap = MutableStateFlow<Map<String, Any>>(emptyMap())
    val userStatusMap: StateFlow<Map<String, Any>> = _userStatusMap

    /**
     * Charge les données du film en utilisant la recherche récursive du Repository
     */

    // Dans FilmDetailViewModel.kt
    fun loadFilm(filmId: String) {
        Log.d("DEBUG_DETAIL", "Recherche du film avec l'ID : $filmId")

        firebaseRepository.getFilmById(filmId) { foundFilm ->
            if (foundFilm != null) {
                _film.value = foundFilm
                Log.d("DEBUG_DETAIL", "Film trouvé : ${foundFilm.title}")
            } else {
                Log.e("DEBUG_DETAIL", "Film NON TROUVÉ pour l'ID : $filmId")
            }
        }
    }

   /*fun loadFilm(filmId: String) {
        Log.d("FilmDetail", "Chargement du film ID : $filmId")

        // 1. Charger les infos du film via la nouvelle méthode getFilmById
        // Cette méthode fouille dans les catégories et sagas pour toi
        firebaseRepository.getFilmById(filmId) { foundFilm ->
            if (foundFilm != null) {
                _film.value = foundFilm
                Log.d("FilmDetail", "Film trouvé dans la hiérarchie : ${foundFilm.title}")
            } else {
                Log.e("FilmDetail", "ERREUR : Impossible de trouver '$filmId' dans le catalogue.")
            }
        }

        // 2. Charger le statut (vu, possédé...)
        val userId = authRepository.getCurrentUser()?.uid
        if (userId != null) {
            firebaseRepository.getFilmStatus(userId, filmId) { status ->
                _userStatusMap.value = status ?: emptyMap()
            }
        }

        // 3. Charger les IDs des propriétaires (Point 8)
        firebaseRepository.getOwnersForFilm(filmId) { ownerList ->
            _owners.value = ownerList
        }
    }*/

    /**
     * Met à jour le statut (ex: "watched") dans Firebase
     */
    fun updateStatus(filmId: String, statusKey: String, value: Boolean) {
        val userId = authRepository.getCurrentUser()?.uid ?: return

        val updatedMap = _userStatusMap.value.toMutableMap()
        updatedMap[statusKey] = value

        firebaseRepository.updateFilmStatus(userId, filmId, updatedMap) { success ->
            if (success) {
                _userStatusMap.value = updatedMap
            }
        }
    }
}