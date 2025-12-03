package com.aplimovil.rotappmp.ui.createcompany

import com.aplimovil.rotappmp.domain.model.CompanyCategory
import com.aplimovil.rotappmp.domain.repository.CompanyRepository
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

/** Estado UI compartido por la pantalla de creación de empresa en todos los targets. */
data class CreateCompanyUiState(
    val name: String = "",
    val category: CompanyCategory = CompanyCategory.GENERAL,
    val employeesCount: Int = 50,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

/** Eventos puntuales que la UI observa (por ejemplo, navegación). */
sealed interface CreateCompanyEvent {
    data object NavigateHome : CreateCompanyEvent
}

/** Gestiona la creación de empresas coordinando los repositorios de usuario y empresa. */
class CreateCompanyViewModel(
    private val companyRepository: CompanyRepository,
    private val userRepository: UserRepository,
) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    private val _uiState = MutableStateFlow(CreateCompanyUiState())
    val uiState: StateFlow<CreateCompanyUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CreateCompanyEvent>()
    val events: SharedFlow<CreateCompanyEvent> = _events.asSharedFlow()

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, errorMessage = null) }
    }

    fun onCategorySelected(category: CompanyCategory) {
        _uiState.update { it.copy(category = category) }
    }

    fun onEmployeesChange(count: Int) {
        _uiState.update { it.copy(employeesCount = count) }
    }

    fun onSubmit() {
        val state = _uiState.value
        if (state.name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es obligatorio") }
            return
        }
        scope.launch {
            val user = userRepository.getActiveUser()
            if (user == null) {
                _uiState.update { it.copy(errorMessage = "No hay usuario activo", isLoading = false) }
                return@launch
            }
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                companyRepository.createCompany(
                    ownerUserId = user.id,
                    name = state.name,
                    category = state.category,
                    employeesCount = state.employeesCount,
                )
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(CreateCompanyEvent.NavigateHome)
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Ocurrió un error",
                    )
                }
            }
        }
    }


}
