package fr.isen.savi.disney_app.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.isen.savi.disney_app.model.Categorie
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.model.Franchise
import fr.isen.savi.disney_app.model.SousSaga

class FirebaseRepository {
    private val database = FirebaseDatabase.getInstance()
    private val categoriesRef = database.getReference("categories")
    private val statusRef = database.getReference("user_film_status")
    private val gson = Gson()

    /**
     * RÉCUPÉRATION : Charge toute la hiérarchie (Catégories > Franchises > Sagas > Films)
     * Utilise la méthode Gson de ton supérieur pour un mapping précis.
     */
    fun getCategories(onResult: (List<Categorie>) -> Unit) {
        categoriesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val rawValue = snapshot.value
                val jsonString = gson.toJson(rawValue)
                val type = object : TypeToken<List<Categorie>>() {}.type

                try {
                    val categories: List<Categorie> = gson.fromJson(jsonString, type)
                    onResult(categories)
                } catch (e: Exception) {
                    Log.e("FirebaseRepo", "Erreur mapping GSON: ${e.message}")
                    onResult(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseRepo", "Erreur Database: ${error.message}")
                onResult(emptyList())
            }
        })
    }

    /**
     * RECHERCHE : Trouve un film par son titre (ou ID généré) dans toute l'arborescence.
     * Très important pour ton FilmDetailViewModel.
     */
    fun getFilmById(filmId: String, onResult: (Film?) -> Unit) {
        getCategories { categories ->
            var foundFilm: Film? = null

            Log.d("REPO_SEARCH", "Début de recherche récursive pour : $filmId")

            for (cat in categories) {
                for (franchise in cat.franchises) {
                    // 1. Chercher dans les films directs de la franchise
                    franchise.films?.let { filmsDirects ->
                        foundFilm = filmsDirects.find { it.getStableId() == filmId }
                    }

                    if (foundFilm != null) {
                        Log.d("REPO_SEARCH", "Trouvé dans les films directs de ${franchise.nom}")
                        break
                    }

                    // 2. Chercher dans les sous-sagas
                    franchise.sous_sagas?.forEach { saga ->
                        val filmInSaga = saga.films.find { it.getStableId() == filmId }
                        if (filmInSaga != null) {
                            foundFilm = filmInSaga
                            Log.d("REPO_SEARCH", "Trouvé dans la saga ${saga.nom}")
                            return@forEach
                        }
                    }

                    if (foundFilm != null) break
                }
                if (foundFilm != null) break
            }

            if (foundFilm == null) {
                Log.e("REPO_SEARCH", "ÉCHEC : Film non trouvé après avoir parcouru ${categories.size} catégories")
            }

            onResult(foundFilm)
        }
    }

    /**
     * STATUTS : Sauvegarder les préférences utilisateur (Watched, WantToWatch, Own)
     */
    fun updateFilmStatus(userId: String, filmId: String, status: Map<String, Any>, onComplete: (Boolean) -> Unit) {
        statusRef.child(userId).child(filmId).setValue(status)
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }

    /**
     * STATUTS : Récupérer les préférences pour un film donné
     */
    fun getFilmStatus(userId: String, filmId: String, onResult: (Map<String, Any>?) -> Unit) {
        statusRef.child(userId).child(filmId).get().addOnSuccessListener { snapshot ->
            @Suppress("UNCHECKED_CAST")
            val status = snapshot.value as? Map<String, Any>
            onResult(status)
        }
    }

    /**
     * PROPRIÉTAIRES : Trouve tous les utilisateurs possédant le film physiquement
     */
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
        }
    }
}