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
        val currentUser = authRepository.getCurrentUser()
        if (currentUser != null) {
            _user.value = UserProfile(
                uid = currentUser.uid,
                displayName = currentUser.displayName ?: "Utilisateur",
                email = currentUser.email ?: ""
            )
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        _error.value = null

        authRepository.signIn(email, password) { success, errorMessage ->
            _isLoading.value = false
            if (success) {
                val firebaseUser = authRepository.getCurrentUser()
                _user.value = UserProfile(
                    uid = firebaseUser?.uid ?: "",
                    displayName = firebaseUser?.displayName ?: "Utilisateur",
                    email = firebaseUser?.email ?: email
                )
            } else {
                _error.value = errorMessage ?: "Erreur de connexion"
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        _error.value = null

        authRepository.signUp(email, password, name) { success, errorMessage ->
            _isLoading.value = false
            if (success) {
                val firebaseUser = authRepository.getCurrentUser()
                _user.value = UserProfile(
                    uid = firebaseUser?.uid ?: "",
                    displayName = name,
                    email = email
                )
            } else {
                _error.value = errorMessage ?: "Erreur d'inscription"
            }
        }
    }

    fun logout() {
        authRepository.signOut()
        _user.value = null
    }
}