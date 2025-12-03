package com.aplimovil.rotappmp.domain.repository

import com.aplimovil.rotappmp.domain.model.User
import com.aplimovil.rotappmp.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

/** Contrato multiplataforma que expone la persistencia de usuarios y operaciones de sesión. */
interface UserRepository {
    /** Flujo reactivo con el usuario autenticado actualmente (si existe). */
    val activeUserStream: Flow<User?>

    suspend fun getActiveUser(): User?

    suspend fun validateCredentials(email: String, password: String): Boolean

    suspend fun registerUser(fullName: String, email: String, password: String): User

    suspend fun updateUserRole(userId: Long, role: UserRole)

    suspend fun logout()
}

