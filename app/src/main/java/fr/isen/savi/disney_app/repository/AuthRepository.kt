package fr.isen.savi.disney_app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest // Import important
import com.google.firebase.database.FirebaseDatabase

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun signUp(email: String, password: String, pseudo: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result?.user
                    val userId = firebaseUser?.uid

                    if (userId != null) {
                        // --- ÉTAPE 1 : METTRE À JOUR LE PROFIL AUTH DE FIREBASE ---
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(pseudo)
                            .build()

                        firebaseUser.updateProfile(profileUpdates)
                            .addOnCompleteListener { profileTask ->
                                // On continue même si le profileUpdate échoue,
                                // mais on enregistre en base
                                val userProfile = mapOf(
                                    "uid" to userId,
                                    "displayName" to pseudo,
                                    "email" to email
                                )

                                // --- ÉTAPE 2 : ENREGISTRER DANS LA DATABASE ---
                                database.child("users").child(userId).setValue(userProfile)
                                    .addOnCompleteListener { dbTask ->
                                        if (dbTask.isSuccessful) {
                                            onResult(true, null)
                                        } else {
                                            onResult(false, "Erreur lors de la création du profil en base")
                                        }
                                    }
                            }
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

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

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun signOut() {
        auth.signOut()
    }
}