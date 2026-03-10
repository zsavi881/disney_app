package fr.isen.savi.disney_app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository {
    // getInstance() ne prend pas d'arguments ici
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Créer un compte
    fun signUp(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
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