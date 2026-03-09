package fr.isen.savi.filmo_disney.model

data class UserFilmStatus(
    val filmId: String = "",
    val userId: String = "",
    val watched: Boolean = false,
    val wantToWatch: Boolean = false,
    val ownPhysical: Boolean = false,
    val wantToGetRid: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)