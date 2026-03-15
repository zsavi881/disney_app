package fr.isen.savi.disney_app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    // Créer un compte avec un pseudo
    fun signUp(email: String, password: String, pseudo: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid
                    if (userId != null) {
                        val userProfile = mapOf(
                            "uid" to userId,
                            "displayName" to pseudo,
                            "email" to email
                        )

                        database.child("users").child(userId).setValue(userProfile)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    onResult(true, null)
                                } else {
                                    onResult(false, "Erreur lors de la création du profil en base")
                                }
                            }
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    // Se connecter
    fun signIn(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    // Récupérer l'utilisateur actuel
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    // Se déconnecter
    fun signOut() {
        auth.signOut()
    }
}