package fr.isen.savi.disney_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.repository.FirebaseRepository
import fr.isen.savi.disney_app.repository.AuthRepository
import fr.isen.savi.disney_app.model.UserFilmStatus
import fr.isen.savi.disney_app.model.FilmOwnerInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilmDetailViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()
    private val authRepository = AuthRepository()

    // --- ÉTATS (STATES) ---

    private val _film = MutableStateFlow<Film?>(null)
    val film: StateFlow<Film?> = _film

    // Pour la liste des gens qui possèdent le film (Point 8 de Zoé)
    private val _owners = MutableStateFlow<List<String>>(emptyList())
    val owners: StateFlow<List<String>> = _owners

    // État pour stocker les statuts du film sous forme de Map (Firebase)
    private val _userStatusMap = MutableStateFlow<Map<String, Any>>(emptyMap())
    val userStatusMap: StateFlow<Map<String, Any>> = _userStatusMap

    // --- FONCTIONS ---

    /**
     * Charge les données du film et le statut de l'utilisateur depuis Firebase
     */
    fun loadFilm(filmId: String) {
        Log.d("FilmDetail", "Tentative de chargement pour l'ID : $filmId")

        // 1. Charger les infos du film
        firebaseRepository.getFilms { allFilms ->
            val foundFilm = allFilms.find { it.id == filmId }
            if (foundFilm != null) {
                _film.value = foundFilm
                Log.d("FilmDetail", "Film trouvé : ${foundFilm.title}")
            } else {
                Log.e("FilmDetail", "ERREUR : Aucun film avec l'ID '$filmId' dans Firebase.")
            }
        }

        // 2. Charger le statut (vu, possédé...) pour l'utilisateur actuel
        val userId = authRepository.getCurrentUser()?.uid
        if (userId != null) {
            firebaseRepository.getFilmStatus(userId, filmId) { status ->
                _userStatusMap.value = status ?: emptyMap()
            }
        }

        // 3. Charger les propriétaires du film (Point 8)
        firebaseRepository.getOwnersForFilm(filmId) { ownerList ->
            _owners.value = ownerList
        }
    }

    /**
     * Met à jour un statut spécifique (ex: "watched" ou "ownPhysical") dans Firebase
     */
    fun updateStatus(filmId: String, statusKey: String, value: Boolean) {
        val userId = authRepository.getCurrentUser()?.uid ?: return

        // On prépare la nouvelle map de statuts en local pour une mise à jour rapide de l'UI
        val updatedMap = _userStatusMap.value.toMutableMap()
        updatedMap[statusKey] = value

        // On envoie à Firebase
        firebaseRepository.updateFilmStatus(userId, filmId, updatedMap) { success ->
            if (success) {
                _userStatusMap.value = updatedMap
                Log.d("FilmDetail", "Statut mis à jour : $statusKey = $value")
            }
        }
    }
}