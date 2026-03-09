package fr.isen.savi.disney_app.viewmodel

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

    // État pour stocker les statuts du film (vu, possédé, etc.)
    private val _userStatus = MutableStateFlow<Map<String, Any>>(emptyMap())
    val userStatus: StateFlow<Map<String, Any>> = _userStatus

    fun loadFilm(filmId: String) {
        // Le log nous permet de voir exactement ce que l'app reçoit comme ID
        android.util.Log.d("FilmDetail", "Tentative de chargement pour l'ID : $filmId")

        firebaseRepository.getFilms { allFilms ->
            // On cherche une correspondance exacte sur l'ID
            val foundFilm = allFilms.find { it.id == filmId }

            if (foundFilm != null) {
                _film.value = foundFilm
                android.util.Log.d("FilmDetail", "Film trouvé : ${foundFilm.title}")
            } else {
                android.util.Log.e("FilmDetail", "ERREUR : Aucun film avec l'ID '$filmId' dans Firebase.")
                // On affiche la liste des IDs dispos pour comparer
                android.util.Log.d("FilmDetail", "IDs dispos dans Firebase : ${allFilms.map { it.id }}")
            }
        }

        // Chargement des statuts (watched, own, etc.)
        val userId = authRepository.getCurrentUser()?.uid
        if (userId != null) {
            firebaseRepository.getFilmStatus(userId, filmId) { status ->
                _userStatus.value = status ?: emptyMap()
            }
        }
    }

    // Fonction appelée par les boutons de Zoé (Watched, Own DVD, etc.)
    fun updateStatus(filmId: String, statusKey: String, value: Boolean) {
        val userId = authRepository.getCurrentUser()?.uid ?: return

        // On prépare la nouvelle map de statuts en local
        val updatedMap = _userStatus.value.toMutableMap()
        updatedMap[statusKey] = value

        // On envoie à Firebase
        firebaseRepository.updateFilmStatus(userId, filmId, updatedMap) { success ->
            if (success) {
                _userStatus.value = updatedMap
            }
        }
    }
}