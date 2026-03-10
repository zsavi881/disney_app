package fr.isen.savi.disney_app.model

data class Categorie(
    val categorie: String = "", // ex: "Grandes Sagas"
    val franchises: List<Franchise> = emptyList()
)