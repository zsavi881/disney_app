package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import fr.isen.savi.disney_app.model.UserProfile
import fr.isen.savi.disney_app.repository.AuthRepository

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _user = MutableStateFlow<UserProfile?>(null)
    val user: StateFlow<UserProfile?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        checkCurrentUser()
    }

    // Extraction de la logique de vérification pour la réutiliser
    private fun checkCurrentUser() {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser != null) {
            _user.value = UserProfile(
                uid = currentUser.uid,
                // On évite le "Utilisateur" en dur ici pour voir si Firebase a la donnée
                displayName = currentUser.displayName ?: "",
                email = currentUser.email ?: ""
            )
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        _error.value = null

        authRepository.signIn(email, password) { success, errorMessage ->
            if (success) {
                // IMPORTANT : On recharge l'utilisateur depuis Firebase pour avoir le displayName à jour
                val firebaseUser = authRepository.getCurrentUser()
                _user.value = UserProfile(
                    uid = firebaseUser?.uid ?: "",
                    // Si Firebase n'a pas encore de nom, on peut mettre une chaîne vide ou le début de l'email
                    displayName = firebaseUser?.displayName ?: "",
                    email = firebaseUser?.email ?: email
                )
                _isLoading.value = false
            } else {
                _isLoading.value = false
                _error.value = errorMessage ?: "Erreur de connexion"
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        _error.value = null

        authRepository.signUp(email, password, name) { success, errorMessage ->
            if (success) {
                // Lors de l'inscription, on force le nom qu'on vient de saisir
                // car updateProfile peut mettre quelques millisecondes à se propager sur Firebase
                _user.value = UserProfile(
                    uid = authRepository.getCurrentUser()?.uid ?: "",
                    displayName = name,
                    email = email
                )
                _isLoading.value = false
            } else {
                _isLoading.value = false
                _error.value = errorMessage ?: "Erreur d'inscription"
            }
        }
    }

    fun logout() {
        authRepository.signOut()
        _user.value = null
    }
}