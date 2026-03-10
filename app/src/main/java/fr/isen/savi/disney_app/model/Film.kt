package fr.isen.savi.disney_app.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
data class Film(
    @get:PropertyName("titre")
    @set:PropertyName("titre")
    var title: String = "",

    @get:PropertyName("annee")
    @set:PropertyName("annee")
    var releaseDate: Int = 0, // bien mettre int car les années sont des chiffres dans mon json

    @get:PropertyName("genre")
    @set:PropertyName("genre")
    var genre: String = "",

    @get:PropertyName("numero")
    @set:PropertyName("numero")
    var numero: Int = 0,

    var id: String = ""
) {
    fun getStableId(): String {
        return title.lowercase()
            .let { java.text.Normalizer.normalize(it, java.text.Normalizer.Form.NFD) }
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .replace("[^a-z0-9]".toRegex(), "_")
            .replace("_+".toRegex(), "_")
            .trim('_')
    }
}