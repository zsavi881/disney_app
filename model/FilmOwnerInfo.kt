package fr.isen.savi.filmo_disney.model

data class FilmOwnerInfo(
    val userId: String = "",
    val displayName: String = "",
    val ownPhysical: Boolean = false,
    val wantToGetRid: Boolean = false
)