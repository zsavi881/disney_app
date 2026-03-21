package fr.isen.savi.disney_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.model.UserProfile
import fr.isen.savi.disney_app.repository.FirebaseRepository
import fr.isen.savi.disney_app.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import fr.isen.savi.disney_app.ui.theme.ThemeState

class ProfileViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()
    private val authRepository = AuthRepository()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _ownedFilms = MutableStateFlow<List<Film>>(emptyList())
    val ownedFilms: StateFlow<List<Film>> = _ownedFilms.asStateFlow()

    private val _watchedFilms = MutableStateFlow<List<Film>>(emptyList())
    val watchedFilms: StateFlow<List<Film>> = _watchedFilms.asStateFlow()

    private val _wishlistFilms = MutableStateFlow<List<Film>>(emptyList())
    val wishlistFilms: StateFlow<List<Film>> = _wishlistFilms.asStateFlow()

    private val _wantedlistFilms = MutableStateFlow<List<Film>>(emptyList())
    val wantedlistFilms: StateFlow<List<Film>> = _wantedlistFilms.asStateFlow()

    fun toggleDarkMode() {
        ThemeState.isDarkMode.value = !ThemeState.isDarkMode.value
    }

    fun loadProfile() {
        val user = authRepository.getCurrentUser() ?: return
        val userId = user.uid

        _userProfile.value = UserProfile(
            uid = userId,
            displayName = user.displayName ?: "Utilisateur Disney",
            email = user.email ?: ""
        )

        clearLists()

        firebaseRepository.getCategories { categories ->
            val allFilms = mutableListOf<Film>()
            categories.forEach { cat ->
                cat.franchises.forEach { franchise ->
                    allFilms.addAll(franchise.films)
                    franchise.sous_sagas.forEach { saga -> allFilms.addAll(saga.films) }
                }
            }

            if (allFilms.isEmpty()) return@getCategories

            val owned = mutableListOf<Film>()
            val watched = mutableListOf<Film>()
            val wishlist = mutableListOf<Film>()
            val wantedlist = mutableListOf<Film>()

            allFilms.forEach { film ->
                val stableId = film.getStableId()
                firebaseRepository.getFilmStatus(userId, stableId) { statusMap ->
                    if (statusMap != null) {
                        if (statusMap["ownPhysical"] == true) owned.add(film)
                        if (statusMap["watched"] == true) watched.add(film)
                        if (statusMap["wantToWatch"] == true) wishlist.add(film)
                        if (statusMap["wantedPhysical"] == true) wantedlist.add(film)
                    }

                    _ownedFilms.value = owned.toList()
                    _watchedFilms.value = watched.toList()
                    _wishlistFilms.value = wishlist.toList()
                    _wantedlistFilms.value = wantedlist.toList()
                }
            }
        }
    }

    fun removeFilmFromSection(userId: String, filmId: String, field: String) {
        firebaseRepository.updateSingleFilmField(userId, filmId, field, false) { success ->
            if (success) loadProfile()
        }
    }

    fun logout(onSuccess: () -> Unit) {
        authRepository.signOut()
        _userProfile.value = null
        clearLists()
        onSuccess()
    }

    private fun clearLists() {
        _ownedFilms.value = emptyList()
        _watchedFilms.value = emptyList()
        _wishlistFilms.value = emptyList()
        _wantedlistFilms.value = emptyList()
    }
}