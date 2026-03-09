package fr.isen.savi.disney_app.repository

import com.google.firebase.database.FirebaseDatabase
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.model.MovieData

class FirebaseRepository {
    private val database = FirebaseDatabase.getInstance()
    private val filmsRef = database.getReference("films")

    // Fonction pour injecter tes données (à n'appeler qu'une fois)
    fun initialPopulate() {
        MovieData.catalog.forEach { film ->
            filmsRef.child(film.id).setValue(film)
        }
    }

    // Cette fonction sera utilisée par Zoé pour afficher les films
    fun getFilms(onResult: (List<Film>) -> Unit) {
        filmsRef.get().addOnSuccessListener { snapshot ->
            val list = snapshot.children.mapNotNull { it.getValue(Film::class.java) }
            onResult(list)
        }
    }
}