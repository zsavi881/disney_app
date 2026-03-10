package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.repository.FirebaseRepository // On utilise ton repository !
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilmListViewModel : ViewModel() {

    // On remplace le FilmoRepository par ton FirebaseRepository
    private val firebaseRepository = FirebaseRepository()

    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> = _films

    private val _universeTitle = MutableStateFlow("")
    val universeTitle: StateFlow<String> = _universeTitle

    fun loadFilms(universeId: String) {
        // On récupère TOUS les films depuis Firebase
        firebaseRepository.getFilms { allFilms ->
            // On filtre pour ne garder que ceux de l'univers cliqué (ex: "star_wars")
            val filteredFilms = allFilms.filter { it.universeId == universeId }

            _films.value = filteredFilms

            // On met à jour le titre de l'écran avec le nom de l'univers
            _universeTitle.value = filteredFilms.firstOrNull()?.universeName ?: universeId
        }
    }
}