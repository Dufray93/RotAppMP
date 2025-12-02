package com.aplimovil.rotappmp.data.local

import com.aplimovil.rotappmp.domain.model.Company
import com.aplimovil.rotappmp.domain.model.User
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Almacenamiento local simple usando Multiplatform Settings.
 * Funciona en todas las plataformas: Android, iOS, JVM, JS, etc.
 */
class LocalStorage(private val settings: Settings = createSettings()) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // Flows para datos reactivos
    private val _activeUserFlow = MutableStateFlow<User?>(null)
    val activeUserFlow: Flow<User?> = _activeUserFlow.asStateFlow()

    init {
        // Cargar usuario activo al iniciar
        _activeUserFlow.value = getActiveUser()
    }

    // ==================== USUARIOS ====================

    fun saveUser(user: User) {
        val users = getUsers().toMutableList()
        users.removeAll { it.id == user.id }
        users.add(user)
        settings.putString("users", json.encodeToString(users))
        if (_activeUserFlow.value?.id == user.id) {
            _activeUserFlow.value = if (user.isActive) user else null
        }
    }

    fun getUsers(): List<User> {
        val usersJson = settings.getStringOrNull("users") ?: return emptyList()
        return try {
            json.decodeFromString<List<User>>(usersJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getUserByEmail(email: String): User? {
        return getUsers().find { it.email == email }
    }

    // ==================== USUARIO ACTIVO ====================

    fun setActiveUser(userId: Long) {
        settings.putLong("active_user_id", userId)
        _activeUserFlow.value = getUsers().find { it.id == userId }
    }

    fun getActiveUser(): User? {
        val userId = settings.getLongOrNull("active_user_id") ?: return null
        return getUsers().find { it.id == userId }
    }

    fun clearActiveUser() {
        settings.remove("active_user_id")
        _activeUserFlow.value = null
    }

    // ==================== EMPRESAS ====================

    fun saveCompany(company: Company) {
        val companies = getCompanies().toMutableList()
        companies.removeAll { it.id == company.id }
        companies.add(company)
        settings.putString("companies", json.encodeToString(companies))
    }

    fun getCompanies(): List<Company> {
        val companiesJson = settings.getStringOrNull("companies") ?: return emptyList()
        return try {
            json.decodeFromString<List<Company>>(companiesJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getCompaniesByUserId(userId: Long): List<Company> {
        val userCompaniesJson = settings.getStringOrNull("user_companies_$userId") ?: return emptyList()
        val companyIds = try {
            json.decodeFromString<List<Long>>(userCompaniesJson)
        } catch (e: Exception) {
            emptyList()
        }

        val allCompanies = getCompanies()
        return companyIds.mapNotNull { id -> allCompanies.find { it.id == id } }
    }

    fun linkCompanyToUser(companyId: Long, userId: Long) {
        val currentLinks = getCompaniesByUserId(userId).map { it.id }.toMutableList()
        if (!currentLinks.contains(companyId)) {
            currentLinks.add(companyId)
            settings.putString("user_companies_$userId", json.encodeToString(currentLinks))
        }
    }

    fun clear() {
        settings.clear()
        _activeUserFlow.value = null
    }
}

