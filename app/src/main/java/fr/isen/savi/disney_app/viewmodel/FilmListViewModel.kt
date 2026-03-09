package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.data.FilmoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilmListViewModel : ViewModel() {

    private val repository = FilmoRepository()

    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> = _films

    private val _universeTitle = MutableStateFlow("")
    val universeTitle: StateFlow<String> = _universeTitle

    fun loadFilms(universeId: String) {
        val result = repository.getFilmsByUniverse(universeId)
        _films.value = result
        _universeTitle.value = result.firstOrNull()?.universeName ?: universeId
    }
}