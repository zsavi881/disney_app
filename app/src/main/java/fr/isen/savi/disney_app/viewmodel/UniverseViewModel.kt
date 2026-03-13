package fr.isen.savi.disney_app.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.model.Categorie
import fr.isen.savi.disney_app.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UniverseViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    private val _categories = MutableStateFlow<List<Categorie>>(emptyList())
    val categories: StateFlow<List<Categorie>> = _categories

    val expandedItems = mutableStateListOf<String>()

    init {
        loadData()
    }

    private fun loadData() {
        firebaseRepository.getCategories { allCategories ->
            _categories.value = allCategories
        }
    }
}