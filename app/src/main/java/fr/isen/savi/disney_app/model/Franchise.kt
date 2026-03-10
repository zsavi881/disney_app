package fr.isen.savi.disney_app.model

data class Franchise(
    val nom: String = "", // ex: "Star Wars"
    val films: List<Film>? = null, // Pour les franchises sans sous-sagas
    val sous_sagas: List<SousSaga>? = null
) {
    fun tousLesFilms(): List<Film> = films ?: emptyList()
}