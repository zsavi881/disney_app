package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.data.FilmoRepository
import fr.isen.savi.disney_app.model.UserFilmStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilmDetailViewModel : ViewModel() {

    private val repository = FilmoRepository()

    private val _film = MutableStateFlow<Film?>(null)
    val film: StateFlow<Film?> = _film

    fun loadFilm(
        filmId: String,
        userId: String
    ) {

        _film.value = repository.getFilmById(filmId)

        _status.value = repository.getUserFilmStatus(
            userId,
            filmId
        )
    }
    fun updateStatus(
        userId: String,
        filmId: String,
        watched: Boolean = false,
        wantToWatch: Boolean = false,
        own: Boolean = false,
        getRid: Boolean = false
    ) {

        val newStatus = UserFilmStatus(
            filmId = filmId,
            userId = userId,
            watched = watched,
            wantToWatch = wantToWatch,
            ownPhysical = own,
            wantToGetRid = getRid
        )

        repository.saveUserFilmStatus(newStatus)

        _status.value = newStatus
    }
    private val _status = MutableStateFlow<UserFilmStatus?>(null)
    val status: StateFlow<UserFilmStatus?> = _status
}