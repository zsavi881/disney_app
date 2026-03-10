package fr.isen.savi.disney_app.model

data class SousSaga(
    val nom: String = "", // ex: "Saga Skywalker"
    val films: List<Film> = emptyList()
)