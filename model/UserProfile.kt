package fr.isen.savi.filmo_disney.model

data class UserProfile(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val avatarUrl: String = ""
)