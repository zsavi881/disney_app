package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.model.UserProfile
import fr.isen.savi.disney_app.data.FilmoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {

    private val repository = FilmoRepository()

    private val _userProfile = MutableStateFlow(
        UserProfile(
            uid = "1",
            displayName = "Test User",
            email = "test@email.com"
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile

    private val _ownedFilms = MutableStateFlow<List<Film>>(emptyList())
    val ownedFilms: StateFlow<List<Film>> = _ownedFilms

    private val _watchedFilms = MutableStateFlow<List<Film>>(emptyList())
    val watchedFilms: StateFlow<List<Film>> = _watchedFilms

    private val _wishlistFilms = MutableStateFlow<List<Film>>(emptyList())
    val wishlistFilms: StateFlow<List<Film>> = _wishlistFilms

    private val _toGetRidFilms = MutableStateFlow<List<Film>>(emptyList())
    val toGetRidFilms: StateFlow<List<Film>> = _toGetRidFilms

    fun loadProfile(userId: String) {
        val statuses = repository.getAllUserStatuses(userId)

        _ownedFilms.value = repository.getFilmsByIds(
            statuses.filter { it.ownPhysical }.map { it.filmId }
        )

        _watchedFilms.value = repository.getFilmsByIds(
            statuses.filter { it.watched }.map { it.filmId }
        )

        _wishlistFilms.value = repository.getFilmsByIds(
            statuses.filter { it.wantToWatch }.map { it.filmId }
        )

        _toGetRidFilms.value = repository.getFilmsByIds(
            statuses.filter { it.wantToGetRid }.map { it.filmId }
        )
    }
}