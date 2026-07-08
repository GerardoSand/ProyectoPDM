package com.pdm0126.proyectopdm.ui.auth

import io.github.jan.supabase.postgrest.query.*
import io.github.jan.supabase.postgrest.result.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm0126.proyectopdm.data.remote.ApiClient
import com.pdm0126.proyectopdm.data.remote.ProfileDto
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthState {
    data object Loading : AuthState
    data object Unauthenticated : AuthState
    data class Authenticated(val role: String) : AuthState
}

class LoginViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _isLoginLoading = MutableStateFlow(false)
    val isLoginLoading: StateFlow<Boolean> = _isLoginLoading.asStateFlow()

    init {
        viewModelScope.launch {
            ApiClient.supabase.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> fetchRole(status.session.user?.id)
                    is SessionStatus.Initializing -> _authState.value = AuthState.Loading
                    is SessionStatus.RefreshFailure -> _authState.value = AuthState.Unauthenticated
                    is SessionStatus.NotAuthenticated -> _authState.value = AuthState.Unauthenticated
                }
            }
        }
    }

    private suspend fun fetchRole(userId: String?) {
        if (userId == null) {
            _authState.value = AuthState.Unauthenticated
            return
        }
        try {
            val profile = ApiClient.supabase.postgrest["profiles"]
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<ProfileDto>()
            _authState.value = AuthState.Authenticated(profile.role)
        } catch (e: Exception) {
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginError.value = null
            _isLoginLoading.value = true
            try {
                ApiClient.supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
            } catch (e: Exception) {
                _loginError.value = "Fallo de autenticación: ${e.message}"
            } finally {
                _isLoginLoading.value = false
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                ApiClient.supabase.auth.signOut()
            } catch (e: Exception) {
            }
        }
    }
}
