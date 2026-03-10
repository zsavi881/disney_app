package fr.isen.savi.disney_app.model

import com.google.gson.annotations.SerializedName

data class Film(
    @SerializedName("titre")
    val title: String = "",

    @SerializedName("annee")
    val releaseDate: Int = 0,

    @SerializedName("genre")
    val genre: String = "",

    @SerializedName("numero")
    val numero: Int = 0,

    val id: String = ""
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