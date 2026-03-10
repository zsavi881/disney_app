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

    // Champs techniques pour ton app (ID de navigation)
    val id: String = ""
) {
    // Helper pour générer l'ID si absent du JSON
    fun getStableId(): String {
        return title.lowercase()
            // 1. Enlever les accents (Normalizer)
            .let { java.text.Normalizer.normalize(it, java.text.Normalizer.Form.NFD) }
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            // 2. Remplacer tout ce qui n'est pas une lettre ou un chiffre par un underscore
            .replace("[^a-z0-9]".toRegex(), "_")
            // 3. Remplacer les suites de plusieurs underscores par un seul
            .replace("_+".toRegex(), "_")
            // 4. Enlever l'underscore au début ou à la fin si présent
            .trim('_')
    }
}