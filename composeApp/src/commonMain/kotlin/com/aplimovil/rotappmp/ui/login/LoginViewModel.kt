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

/** Estado observable consumido por `LoginScreen` en todos los targets. */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

/**
 * ViewModel compartido que valida credenciales y actualiza el [UserRepository].
 *
 * Se ejecuta en un `Dispatchers.Default` para que Android, iOS, JVM y Web reutilicen la misma lógica.
 */
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
