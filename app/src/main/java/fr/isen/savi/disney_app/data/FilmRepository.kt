package fr.isen.savi.disney_app.data

import fr.isen.savi.disney_app.model.Universe

class FilmoRepository {

    fun getUniverses(): List<Universe> {
        return listOf(
            Universe(
                id = "marvel",
                name = "Marvel",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/b/b9/Marvel_Logo.svg",
                description = "Super-héros et univers cinématographique Marvel"
            ),
            Universe(
                id = "disney",
                name = "Disney",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/3/3e/Disney_wordmark.svg",
                description = "Classiques et grands films Disney"
            ),
            Universe(
                id = "pixar",
                name = "Pixar",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/4/48/Pixar_logo.svg",
                description = "Films d’animation Pixar"
            ),
            Universe(
                id = "starwars",
                name = "Star Wars",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/6/6c/Star_Wars_Logo.svg",
                description = "Saga Star Wars et films associés"
            ),
            Universe(
                id = "avatar",
                name = "Avatar",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/b/b0/Avatar-Logo.svg",
                description = "Univers Avatar"
            )
        )
    }
}