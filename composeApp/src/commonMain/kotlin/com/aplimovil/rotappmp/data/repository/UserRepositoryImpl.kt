package com.aplimovil.rotappmp.data.repository

import com.aplimovil.rotappmp.data.local.LocalStorage
import com.aplimovil.rotappmp.domain.model.User
import com.aplimovil.rotappmp.domain.model.UserRole
import com.aplimovil.rotappmp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

/**
 * Persiste entidades `User` usando [LocalStorage], centralizando las reglas de negocio en KMP.
 */
class UserRepositoryImpl(private val storage: LocalStorage) : UserRepository {

    override val activeUserStream: Flow<User?> = storage.activeUserFlow

    override suspend fun getActiveUser(): User? {
        return storage.getActiveUser()
    }

    override suspend fun validateCredentials(email: String, password: String): Boolean {
        val user = storage.getUserByEmail(email)

        return if (user != null && user.password == password) {
            val updatedUser = user.copy(isActive = true)
            storage.saveUser(updatedUser)
            storage.setActiveUser(user.id)
            true
        } else {
            false
        }
    }

    override suspend fun registerUser(fullName: String, email: String, password: String): User {
        val id = Random.nextLong(1_000_000_000)
        val newUser = User(
            id = id,
            fullName = fullName,
            email = email,
            password = password,
            role = null,
            isActive = true
        )

        storage.saveUser(newUser)
        storage.setActiveUser(id)

        return newUser
    }

    override suspend fun updateUserRole(userId: Long, role: UserRole) {
        val users = storage.getUsers()
        val user = users.find { it.id == userId }

        user?.let {
            val updatedUser = it.copy(role = role)
            storage.saveUser(updatedUser)
        }
    }

    override suspend fun logout() {
        val activeUser = storage.getActiveUser()
        activeUser?.let {
            val updatedUser = it.copy(isActive = false)
            storage.saveUser(updatedUser)
        }
        storage.clearActiveUser()
    }
}

