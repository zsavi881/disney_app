package fr.isen.savi.disney_app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import fr.isen.savi.disney_app.model.UserProfile

class AuthViewModel : ViewModel() {

    private val _user = MutableStateFlow<UserProfile?>(null)
    val user: StateFlow<UserProfile?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    fun login(email: String, password: String) {
        _isLoading.value = true

        // simulation login (Firebase plus tard)
        if (email.isNotBlank() && password.isNotBlank()) {
            _user.value = UserProfile(
                uid = "1",
                displayName = "Test User",
                email = email
            )
            _error.value = null
        } else {
            _error.value = "Invalid credentials"
        }

        _isLoading.value = false
    }


    fun register(name: String, email: String, password: String) {
        _isLoading.value = true

        if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
            _user.value = UserProfile(
                uid = "1",
                displayName = name,
                email = email
            )
            _error.value = null
        } else {
            _error.value = "Invalid registration"
        }

        _isLoading.value = false
    }


    fun logout() {
        _user.value = null
    }
}