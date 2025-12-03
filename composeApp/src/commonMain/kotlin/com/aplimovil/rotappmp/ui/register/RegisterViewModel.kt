package com.aplimovil.rotappmp.ui.register

import com.aplimovil.rotappmp.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado UI expuesto a los observadores de `RegisterScreen`. */
data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
)

sealed interface RegisterEvent {
    data object NavigateToRoleSelection : RegisterEvent
}

/**
 * ViewModel compartido del flujo de registro. Valida entradas, persiste el nuevo usuario y emite
 * eventos de navegación para mantener la UI sencilla.
 */
class RegisterViewModel(private val userRepository: UserRepository) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RegisterEvent>()
    val events: SharedFlow<RegisterEvent> = _events.asSharedFlow()

    fun onFullNameChange(value: String) {
        _uiState.update { it.copy(fullName = value, errorMessage = null, isSuccess = false) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null, isSuccess = false) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null, isSuccess = false) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, errorMessage = null, isSuccess = false) }
    }

    fun onRegisterRequested() {
        val state = _uiState.value
        val error = validate(state)
        if (error != null) {
            _uiState.update { it.copy(errorMessage = error) }
            return
        }

        scope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                userRepository.registerUser(
                    fullName = state.fullName,
                    email = state.email,
                    password = state.password,
                )
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                _events.emit(RegisterEvent.NavigateToRoleSelection)
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = throwable.message ?: "Ocurrió un error inesperado",
                    )
                }
            }
        }
    }

    private fun validate(state: RegisterUiState): String? {
        if (state.fullName.isBlank()) return "El nombre es obligatorio"
        if (state.email.isBlank()) return "El correo es obligatorio"
        if (!state.email.contains('@')) return "Ingresa un correo válido"
        if (state.password.length < 6) return "La contraseña debe tener al menos 6 caracteres"
        if (state.password != state.confirmPassword) return "Las contraseñas no coinciden"
        return null
    }

    fun dispose() {
        job.cancel()
    }
}
