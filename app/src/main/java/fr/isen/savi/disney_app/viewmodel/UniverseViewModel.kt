package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.model.Universe
import fr.isen.savi.disney_app.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UniverseViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    private val _universes = MutableStateFlow<List<Universe>>(emptyList())
    val universes: StateFlow<List<Universe>> = _universes

    init {
        loadUniverses()
    }

    private fun loadUniverses() {
        firebaseRepository.getFilms { allFilms ->
            // On récupère tous les univers uniques présents dans tes films
            val uniqueUniverses = allFilms.distinctBy { it.universeId }.map { film ->
                Universe(
                    id = film.universeId,
                    name = film.universeName,
                    description = "Découvrez l'univers ${film.universeName}",
                    //imageUrl = 0 // On laisse à 0, Zoé gère surement l'affichage via l'ID
                )
            }
            _universes.value = uniqueUniverses
        }
    }
}