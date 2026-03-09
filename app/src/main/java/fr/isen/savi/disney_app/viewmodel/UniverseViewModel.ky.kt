package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.data.FilmoRepository
import fr.isen.savi.disney_app.model.Universe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UniverseViewModel : ViewModel() {

    private val repository = FilmoRepository()

    private val _universes = MutableStateFlow<List<Universe>>(emptyList())
    val universes: StateFlow<List<Universe>> = _universes

    init {
        loadUniverses()
    }

    private fun loadUniverses() {
        _universes.value = repository.getUniverses()
    }
}