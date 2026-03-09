package fr.isen.savi.disney_app.data

import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.model.Universe
import fr.isen.savi.disney_app.model.UserFilmStatus

class FilmoRepository {

    fun getUniverses(): List<Universe> {
        return listOf(
            Universe(
                id = "marvel",
                name = "Marvel",
                imageUrl = "",
                description = "Super-héros et univers cinématographique Marvel"
            ),
            Universe(
                id = "disney",
                name = "Disney",
                imageUrl = "",
                description = "Classiques et grands films Disney"
            ),
            Universe(
                id = "pixar",
                name = "Pixar",
                imageUrl = "",
                description = "Films d’animation Pixar"
            ),
            Universe(
                id = "starwars",
                name = "Star Wars",
                imageUrl = "",
                description = "Saga Star Wars et films associés"
            ),
            Universe(
                id = "avatar",
                name = "Avatar",
                imageUrl = "",
                description = "Univers Avatar"
            )
        )
    }

    private fun getAllFilms(): List<Film> {
        return listOf(
            Film(
                id = "sw1",
                title = "Star Wars: Episode I - The Phantom Menace",
                universeId = "starwars",
                universeName = "Star Wars",
                saga = "Skywalker Saga",
                category = "Prequel Trilogy",
                releaseDate = "1999-05-19",
                posterUrl = "",
                synopsis = "La chute de la République commence.",
                durationMinutes = 136
            ),
            Film(
                id = "sw4",
                title = "Star Wars: Episode IV - A New Hope",
                universeId = "starwars",
                universeName = "Star Wars",
                saga = "Skywalker Saga",
                category = "Original Trilogy",
                releaseDate = "1977-05-25",
                posterUrl = "",
                synopsis = "Luke rejoint la Rébellion contre l’Empire.",
                durationMinutes = 121
            ),
            Film(
                id = "av1",
                title = "The Avengers",
                universeId = "marvel",
                universeName = "Marvel",
                saga = "Avengers",
                category = "Infinity Saga",
                releaseDate = "2012-04-25",
                posterUrl = "",
                synopsis = "Les héros s’unissent contre Loki.",
                durationMinutes = 143
            ),
            Film(
                id = "im1",
                title = "Iron Man",
                universeId = "marvel",
                universeName = "Marvel",
                saga = "Iron Man",
                category = "Infinity Saga",
                releaseDate = "2008-05-02",
                posterUrl = "",
                synopsis = "Tony Stark devient Iron Man.",
                durationMinutes = 126
            ),
            Film(
                id = "frozen",
                title = "Frozen",
                universeId = "disney",
                universeName = "Disney",
                saga = null,
                category = "Animated Classics",
                releaseDate = "2013-11-27",
                posterUrl = "",
                synopsis = "Elsa et Anna dans le royaume d’Arendelle.",
                durationMinutes = 102
            ),
            Film(
                id = "lionking",
                title = "The Lion King",
                universeId = "disney",
                universeName = "Disney",
                saga = null,
                category = "Animated Classics",
                releaseDate = "1994-06-24",
                posterUrl = "",
                synopsis = "Le destin de Simba.",
                durationMinutes = 88
            ),
            Film(
                id = "toystory",
                title = "Toy Story",
                universeId = "pixar",
                universeName = "Pixar",
                saga = "Toy Story",
                category = "Saga",
                releaseDate = "1995-11-22",
                posterUrl = "",
                synopsis = "Les jouets prennent vie.",
                durationMinutes = 81
            ),
            Film(
                id = "cars",
                title = "Cars",
                universeId = "pixar",
                universeName = "Pixar",
                saga = "Cars",
                category = "Saga",
                releaseDate = "2006-06-09",
                posterUrl = "",
                synopsis = "Flash McQueen change de trajectoire.",
                durationMinutes = 117
            ),
            Film(
                id = "avatar1",
                title = "Avatar",
                universeId = "avatar",
                universeName = "Avatar",
                saga = "Avatar",
                category = "Main Films",
                releaseDate = "2009-12-18",
                posterUrl = "",
                synopsis = "Jake Sully découvre Pandora.",
                durationMinutes = 162
            ),
            Film(
                id = "avatar2",
                title = "Avatar: The Way of Water",
                universeId = "avatar",
                universeName = "Avatar",
                saga = "Avatar",
                category = "Main Films",
                releaseDate = "2022-12-16",
                posterUrl = "",
                synopsis = "La famille Sully fait face à une nouvelle menace.",
                durationMinutes = 192
            )
        )
    }

    fun getFilmsByUniverse(universeId: String): List<Film> {
        return getAllFilms()
            .filter { it.universeId == universeId }
            .sortedBy { it.releaseDate }
    }

    fun getFilmById(filmId: String): Film? {
        return getAllFilms().find { it.id == filmId }
    }
    companion object {
        private val userFilmStatuses = mutableListOf<UserFilmStatus>()
    }
    fun getUserFilmStatus(userId: String, filmId: String): UserFilmStatus? {
        return userFilmStatuses.find {
            it.userId == userId && it.filmId == filmId
        }
    }

    fun saveUserFilmStatus(status: UserFilmStatus) {

        userFilmStatuses.removeAll {
            it.userId == status.userId && it.filmId == status.filmId
        }

        userFilmStatuses.add(status)
    }
    fun getAllUserStatuses(userId: String): List<UserFilmStatus> {
        return userFilmStatuses.filter { it.userId == userId }
    }

    fun getFilmsByIds(filmIds: List<String>): List<Film> {
        return getAllFilms().filter { it.id in filmIds }
    }
}