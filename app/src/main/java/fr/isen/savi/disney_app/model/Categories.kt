package fr.isen.savi.disney_app.model

import com.google.firebase.database.PropertyName

data class Categorie(
    @get:PropertyName("categorie")
    @set:PropertyName("categorie")
    var categorie: String = "", // Correspond à "Grandes Sagas"

    @get:PropertyName("franchises")
    @set:PropertyName("franchises")
    var franchises: List<Franchise> = emptyList()
)