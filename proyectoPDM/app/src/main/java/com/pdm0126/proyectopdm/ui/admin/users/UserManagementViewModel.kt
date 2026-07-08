package com.pdm0126.proyectopdm.ui.admin.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm0126.proyectopdm.data.model.Profile
import com.pdm0126.proyectopdm.data.repository.ProfileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UserManagementUiState(
    val vendedoras: List<Profile> = emptyList(),
    val isLoading: Boolean = false
)

class UserManagementViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserManagementUiState())
    val uiState: StateFlow<UserManagementUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            profileRepository.syncProfiles()
            profileRepository.getProfiles().collect { profiles ->
                val vendedoras = profiles.filter { it.role == "vendedora" }
                _uiState.update { it.copy(vendedoras = vendedoras, isLoading = false) }
            }
        }
    }
}
