package fr.isen.savi.disney_app.model

data class FilmOwnerInfo(
    val userId: String = "",
    val displayName: String = "",
    val ownPhysical: Boolean = false,
    val wantToGetRid: Boolean = false
)