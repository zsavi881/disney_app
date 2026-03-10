package fr.isen.savi.disney_app.model

import com.google.firebase.database.PropertyName

data class SousSaga(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("nom")
    @set:PropertyName("nom")
    var nom: String = "",

    @get:PropertyName("films")
    @set:PropertyName("films")
    var films: List<Film> = emptyList()
)