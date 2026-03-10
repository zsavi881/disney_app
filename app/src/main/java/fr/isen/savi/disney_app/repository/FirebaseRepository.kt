package fr.isen.savi.disney_app.repository

import com.google.firebase.database.FirebaseDatabase
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.model.MovieData

class FirebaseRepository {
    private val database = FirebaseDatabase.getInstance()
    private val filmsRef = database.getReference("films")
    private val statusRef = database.getReference("user_film_status")

    /**
     * POINT 11 : Injection initiale (à commenter après la première exécution)
     */
    fun initialPopulate() {
        MovieData.catalog.forEach { film ->
            filmsRef.child(film.id).setValue(film)
        }
    }

    /**
     * POINT 5 : Récupérer les films
     */
    fun getFilms(onResult: (List<Film>) -> Unit) {
        filmsRef.get().addOnSuccessListener { snapshot ->
            val list = snapshot.children.mapNotNull { it.getValue(Film::class.java) }
            onResult(list)
        }
    }

    /**
     * POINT 7 : Sauvegarder le statut d'un film pour un utilisateur
     * @param status Un dictionnaire type: ["watched" to true, "ownPhysical" to false...]
     */
    fun updateFilmStatus(userId: String, filmId: String, status: Map<String, Any>, onComplete: (Boolean) -> Unit) {
        statusRef.child(userId).child(filmId).setValue(status)
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }

    /**
     * POINT 7 : Récupérer le statut d'un film pour l'utilisateur connecté
     */
    fun getFilmStatus(userId: String, filmId: String, onResult: (Map<String, Any>?) -> Unit) {
        statusRef.child(userId).child(filmId).get().addOnSuccessListener { snapshot ->
            @Suppress("UNCHECKED_CAST")
            val status = snapshot.value as? Map<String, Any>
            onResult(status)
        }
    }

    /**
     * POINT 8 : Récupérer tous les utilisateurs qui possèdent un film précis
     * Cette fonction parcourt tous les statuts pour trouver ceux qui ont "ownPhysical" = true
     */
    fun getOwnersForFilm(filmId: String, onResult: (List<String>) -> Unit) {
        statusRef.get().addOnSuccessListener { snapshot ->
            val owners = mutableListOf<String>()
            snapshot.children.forEach { userSnapshot ->
                val userId = userSnapshot.key
                val filmStatus = userSnapshot.child(filmId)
                if (filmStatus.child("ownPhysical").value == true) {
                    userId?.let { owners.add(it) }
                }
            }
            onResult(owners)
        }
    }
}