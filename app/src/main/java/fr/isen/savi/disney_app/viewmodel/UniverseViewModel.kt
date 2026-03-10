package fr.isen.savi.disney_app.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import fr.isen.savi.disney_app.model.Categorie
import fr.isen.savi.disney_app.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UniverseViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    // 1. On remplace la liste d'Univers par la liste des Catégories
    private val _categories = MutableStateFlow<List<Categorie>>(emptyList())
    val categories: StateFlow<List<Categorie>> = _categories

    // 2. Liste des éléments actuellement dépliés (survit à la rotation car dans le ViewModel)
    // On stocke les noms (String) des catégories/franchises/sagas ouvertes
    val expandedItems = mutableStateListOf<String>()

    init {
        loadData()
    }

    private fun loadData() {
        // On utilise la nouvelle fonction de ton Repository propre
        firebaseRepository.getCategories { allCategories ->
            _categories.value = allCategories
        }
    }
}