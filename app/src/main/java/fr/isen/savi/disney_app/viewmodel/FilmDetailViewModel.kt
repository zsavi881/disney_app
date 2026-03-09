package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.data.FilmoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilmDetailViewModel : ViewModel() {

    private val repository = FilmoRepository()

    private val _film = MutableStateFlow<Film?>(null)
    val film: StateFlow<Film?> = _film

    fun loadFilm(filmId: String) {
        _film.value = repository.getFilmById(filmId)
    }
}