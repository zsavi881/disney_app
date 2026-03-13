package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.repository.FirebaseRepository // On utilise ton repository !
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilmListViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> = _films

    fun loadFilmsByUniverse(universeId: String) {
        repository.getCategories { categories ->
            val allFilms = mutableListOf<Film>()
            categories.forEach { cat ->
                cat.franchises.forEach { franchise ->
                    franchise.films?.let { allFilms.addAll(it) }
                    franchise.sous_sagas?.forEach { saga ->
                        allFilms.addAll(saga.films)
                    }
                }
            }
            _films.value = allFilms.filter { it.getStableId().contains(universeId) }
        }
    }
}