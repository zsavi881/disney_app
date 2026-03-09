package fr.isen.savi.disney_app.model

object MovieData {
    val catalog = listOf(
        // STAR WARS
        Film(
            id = "sw_1",
            title = "Star Wars : La Menace Fantôme",
            universeId = "star_wars",
            universeName = "Star Wars",
            saga = "Saga Skywalker",
            category = "Grandes Sagas",
            releaseDate = "1999",
            genre = "Science-fiction"
        ),
        Film(
            id = "sw_4",
            title = "Star Wars : Un Nouvel Espoir",
            universeId = "star_wars",
            universeName = "Star Wars",
            saga = "Saga Skywalker",
            category = "Grandes Sagas",
            releaseDate = "1977",
            genre = "Science-fiction"
        ),
        // MARVEL
        Film(
            id = "mcu_1",
            title = "Iron Man",
            universeId = "marvel",
            universeName = "Marvel Cinematic Universe",
            saga = "La Saga de l'Infini - Phase 1",
            category = "Grandes Sagas",
            releaseDate = "2008",
            genre = "Fantastique"
        ),
        Film(
            id = "mcu_22",
            title = "Avengers : Endgame",
            universeId = "marvel",
            universeName = "Marvel Cinematic Universe",
            saga = "La Saga de l'Infini - Phase 3",
            category = "Grandes Sagas",
            releaseDate = "2019",
            genre = "Fantastique"
        ),
        // PIRATES DES CARAÏBES
        Film(
            id = "pdtc_1",
            title = "Pirates des Caraïbes : La Malédiction du Black Pearl",
            universeId = "disney",
            universeName = "Disney",
            saga = "Pirates des Caraïbes",
            category = "Grandes Sagas",
            releaseDate = "2003",
            genre = "Fantastique"
        ),
        // ALIEN
        Film(
            id = "alien_1",
            title = "Alien, le Huitième Passager",
            universeId = "20th_century",
            universeName = "20th Century Studios",
            saga = "Alien",
            category = "Grandes Sagas",
            releaseDate = "1979",
            genre = "Science-fiction"
        )
    )
}