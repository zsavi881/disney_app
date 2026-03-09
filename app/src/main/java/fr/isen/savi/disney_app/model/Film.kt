package fr.isen.savi.disney_app.model

data class Film(
    val id: String = "",
    val title: String = "",
    val universeId: String = "",
    val universeName: String = "",
    val saga: String? = null,
    val category: String? = null,
    val releaseDate: String = "",
    val posterUrl: String = "",
    val synopsis: String = "",
    val durationMinutes: Int = 0
)