package fr.isen.savi.disney_app.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.savi.disney_app.model.Categorie
import fr.isen.savi.disney_app.model.Film

class FirebaseRepository {
    // L'instance récupère l'URL via le google-services.json
    private val database = FirebaseDatabase.getInstance()
    private val categoriesRef = database.getReference("categories")
    private val statusRef = database.getReference("user_film_status")

    fun getCategories(onResult: (List<Categorie>) -> Unit) {
        Log.d("DISNEY_DEBUG", "Lancement du .get()")
        categoriesRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                Log.d("DISNEY_DEBUG", "Succès ! Données : ${snapshot.value}")
                val list = mutableListOf<Categorie>()
                snapshot.children.forEach { child ->
                    child.getValue(Categorie::class.java)?.let { list.add(it) }
                }
                onResult(list)
            } else {
                Log.e("DISNEY_DEBUG", "Erreur : ", task.exception)
                onResult(emptyList())
            }
        }
    }

    // 2. Trouver un film par son ID (utilisé par FilmDetailViewModel)
    fun getFilmById(filmId: String, onResult: (Film?) -> Unit) {
        getCategories { categories ->
            var foundFilm: Film? = null
            for (cat in categories) {
                for (franchise in cat.franchises) {
                    // Recherche dans les films directs
                    foundFilm = franchise.films.find { it.getStableId() == filmId }
                    if (foundFilm != null) break

                    // Recherche dans les sous-sagas
                    franchise.sous_sagas.forEach { saga ->
                        val filmInSaga = saga.films.find { it.getStableId() == filmId }
                        if (filmInSaga != null) {
                            foundFilm = filmInSaga
                            return@forEach
                        }
                    }
                    if (foundFilm != null) break
                }
                if (foundFilm != null) break
            }
            onResult(foundFilm)
        }
    }

    // 3. Récupérer le statut (Vu, Possédé...) d'un film pour un utilisateur
    fun getFilmStatus(userId: String, filmId: String, onResult: (Map<String, Any>?) -> Unit) {
        statusRef.child(userId).child(filmId).get().addOnSuccessListener { snapshot ->
            @Suppress("UNCHECKED_CAST")
            val status = snapshot.value as? Map<String, Any>
            onResult(status)
        }.addOnFailureListener {
            onResult(null)
        }
    }

    // 4. Mettre à jour le statut
    fun updateFilmStatus(userId: String, filmId: String, status: Map<String, Any>, onComplete: (Boolean) -> Unit) {
        statusRef.child(userId).child(filmId).updateChildren(status)
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }

    // 5. Voir qui possède le film (pour la section "Propriétaires")
    fun getOwnersForFilm(filmId: String, onResult: (List<String>) -> Unit) {
        statusRef.get().addOnSuccessListener { snapshot ->
            val owners = mutableListOf<String>()
            snapshot.children.forEach { userSnapshot ->
                val userId = userSnapshot.key
                if (userSnapshot.child(filmId).child("ownPhysical").value == true) {
                    userId?.let { owners.add(it) }
                }
            }
            onResult(owners)
        }.addOnFailureListener {
            onResult(emptyList())
        }
    }
}