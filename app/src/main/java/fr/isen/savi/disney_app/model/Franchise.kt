package fr.isen.savi.disney_app.model

import com.google.firebase.database.PropertyName

data class Franchise(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("nom")
    @set:PropertyName("nom")
    var nom: String = "",

    @get:PropertyName("films")
    @set:PropertyName("films")
    var films: List<Film> = emptyList(),

    @get:PropertyName("sous_sagas")
    @set:PropertyName("sous_sagas")
    var sous_sagas: List<SousSaga> = emptyList()
)