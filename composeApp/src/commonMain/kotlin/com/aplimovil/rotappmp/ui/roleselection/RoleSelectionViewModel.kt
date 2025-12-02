package com.aplimovil.rotappmp.ui.roleselection

import com.aplimovil.rotappmp.domain.model.UserRole
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Opciones disponibles para seleccionar el rol en RotApp. */
enum class RoleOption { ADMIN, COLLABORATOR }

/** Estado observable de la pantalla de selección de rol. */
data class RoleSelectionUiState(
    val selectedRole: RoleOption? = null,
    val errorMessage: String? = null,
)

sealed interface RoleSelectionEvent {
    data object NavigateNext : RoleSelectionEvent
}

/** ViewModel multiplataforma para manejar la selección y confirmación del rol. */
class RoleSelectionViewModel(private val userRepository: UserRepository) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    private val _uiState = MutableStateFlow(RoleSelectionUiState())
    val uiState: StateFlow<RoleSelectionUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RoleSelectionEvent>()
    val events: SharedFlow<RoleSelectionEvent> = _events.asSharedFlow()

    init {
        scope.launch {
            userRepository.activeUserStream.collectLatest { user ->
                val role = when (user?.role) {
                    UserRole.ADMIN -> RoleOption.ADMIN
                    UserRole.COLLABORATOR -> RoleOption.COLLABORATOR
                    else -> null
                }
                if (role != null) {
                    _uiState.update { it.copy(selectedRole = role) }
                }
            }
        }
    }

    fun onRoleSelected(option: RoleOption) {
        _uiState.update { it.copy(selectedRole = option, errorMessage = null) }
    }

    fun onContinue() {
        val current = _uiState.value
        val selectedRole = current.selectedRole ?: run {
            _uiState.update { it.copy(errorMessage = "Selecciona un rol para continuar") }
            return
        }
        scope.launch {
            val user = userRepository.getActiveUser()
            if (user != null) {
                val targetRole = when (selectedRole) {
                    RoleOption.ADMIN -> UserRole.ADMIN
                    RoleOption.COLLABORATOR -> UserRole.COLLABORATOR
                }
                userRepository.updateUserRole(user.id, targetRole)
            }
            _events.emit(RoleSelectionEvent.NavigateNext)
        }
    }

    fun dispose() {
        job.cancel()
    }
}
