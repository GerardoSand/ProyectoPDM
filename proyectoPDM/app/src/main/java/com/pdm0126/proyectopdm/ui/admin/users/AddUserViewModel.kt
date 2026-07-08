package com.pdm0126.proyectopdm.ui.admin.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm0126.proyectopdm.data.model.Profile
import com.pdm0126.proyectopdm.data.remote.ApiClient
import com.pdm0126.proyectopdm.data.repository.ProfileRepository
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SupabaseSignupResponse(
    val id: String
)

class AddUserViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddUserUiState())
    val uiState: StateFlow<AddUserUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, error = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, error = null)
    }

    fun updateFullName(fullName: String) {
        _uiState.value = _uiState.value.copy(fullName = fullName, error = null)
    }

    fun createUser(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank() || state.fullName.isBlank()) {
            _uiState.value = state.copy(error = "Todos los campos son obligatorios")
            return
        }

        if (state.password.length < 6) {
            _uiState.value = state.copy(error = "La contraseña debe tener al menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)
            try {
                // 1. Crear el usuario en auth.users mediante llamada REST directa para evitar sobrescribir la sesión actual del admin.
                val response = ApiClient.client.post("${ApiClient.SUPABASE_URL}/auth/v1/signup") {
                    header("apikey", ApiClient.SUPABASE_KEY)
                    contentType(ContentType.Application.Json)
                    setBody(mapOf("email" to state.email, "password" to state.password))
                }

                if (!response.status.isSuccess()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error al crear usuario en Supabase: ${response.status}"
                    )
                    return@launch
                }

                // Parseamos la respuesta para obtener el ID generado
                val jsonFormat = Json { ignoreUnknownKeys = true }
                val signupResponse = jsonFormat.decodeFromString<SupabaseSignupResponse>(response.bodyAsText())

                // 2. Insertar en public.profiles con el ID obtenido
                val newProfile = Profile(
                    id = signupResponse.id,
                    fullName = state.fullName,
                    role = "vendedora"
                )
                profileRepository.insertProfile(newProfile)
                
                // Recargar perfiles para asegurar que aparezca
                profileRepository.syncProfiles()

                _uiState.value = _uiState.value.copy(isLoading = false, success = true)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Error desconocido"
                )
            }
        }
    }
}

data class AddUserUiState(
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
