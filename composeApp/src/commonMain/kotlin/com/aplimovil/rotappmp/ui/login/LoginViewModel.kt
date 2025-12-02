package com.aplimovil.rotappmp.ui.login

import com.aplimovil.rotappmp.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Estado observable de la pantalla de login. */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

/** ViewModel simple basado en coroutines compartidas para todos los targets. */
class LoginViewModel(private val userRepository: UserRepository) {
    private val job = SupervisorJob()
    private val scope: CoroutineScope = CoroutineScope(job + Dispatchers.Default)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onLoginRequested() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Completa los campos", isLoading = false) }
            return
        }
        scope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val success = userRepository.validateCredentials(state.email, state.password)
            _uiState.update {
                if (success) it.copy(isLoading = false)
                else it.copy(isLoading = false, errorMessage = "Credenciales inválidas")
            }
        }
    }

    fun onForgotPasswordRequested() {
        // TODO: emitir evento/navegación según se defina posteriormente.
    }

    fun dispose() {
        job.cancel()
    }
}
