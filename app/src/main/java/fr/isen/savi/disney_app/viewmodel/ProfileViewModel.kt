package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.model.UserProfile
import fr.isen.savi.disney_app.repository.FirebaseRepository
import fr.isen.savi.disney_app.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()
    private val authRepository = AuthRepository()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    private val _ownedFilms = MutableStateFlow<List<Film>>(emptyList())
    val ownedFilms: StateFlow<List<Film>> = _ownedFilms

    private val _watchedFilms = MutableStateFlow<List<Film>>(emptyList())
    val watchedFilms: StateFlow<List<Film>> = _watchedFilms

    private val _wishlistFilms = MutableStateFlow<List<Film>>(emptyList())
    val wishlistFilms: StateFlow<List<Film>> = _wishlistFilms

    private val _toGetRidFilms = MutableStateFlow<List<Film>>(emptyList())
    val toGetRidFilms: StateFlow<List<Film>> = _toGetRidFilms

    /**
     * Charge le profil et filtre les films selon les statuts Firebase
     */
    fun loadProfile() {
        val user = authRepository.getCurrentUser() ?: return

        // 1. Mettre à jour les infos de base du profil
        _userProfile.value = UserProfile(
            uid = user.uid,
            displayName = user.displayName ?: "Utilisateur Disney",
            email = user.email ?: ""
        )

        // 2. Récupérer TOUTES les catégories (pour avoir la liste complète des films)
        firebaseRepository.getCategories { categories ->
            val allFilms = mutableListOf<Film>()
            categories.forEach { cat ->
                cat.franchises.forEach { franchise ->
                    franchise.films?.let { allFilms.addAll(it) }
                    franchise.sous_sagas?.forEach { saga ->
                        allFilms.addAll(saga.films)
                    }
                }
            }

            // 3. Pour chaque film, vérifier son statut dans Firebase pour cet utilisateur
            // On fait cela en récupérant les statuts du noeud "user_film_status"
            val userId = user.uid

            // On réinitialise les listes avant de les remplir
            val owned = mutableListOf<Film>()
            val watched = mutableListOf<Film>()
            val wishlist = mutableListOf<Film>()
            val toGetRid = mutableListOf<Film>()

            // On parcourt tous les films du catalogue pour voir lesquels l'utilisateur a marqué
            allFilms.forEach { film ->
                val stableId = film.getStableId()
                firebaseRepository.getFilmStatus(userId, stableId) { statusMap ->
                    if (statusMap != null) {
                        if (statusMap["ownPhysical"] == true) owned.add(film)
                        if (statusMap["watched"] == true) watched.add(film)
                        if (statusMap["wantToWatch"] == true) wishlist.add(film)
                        if (statusMap["wantToGetRid"] == true) toGetRid.add(film)

                        // On met à jour les flows à chaque fois qu'un film est traité
                        _ownedFilms.value = owned.toList()
                        _watchedFilms.value = watched.toList()
                        _wishlistFilms.value = wishlist.toList()
                        _toGetRidFilms.value = toGetRid.toList()
                    }
                }
            }
        }
    }
}