package com.aplimovil.rotappmp.data.repository

import com.aplimovil.rotappmp.domain.model.User
import com.aplimovil.rotappmp.domain.model.UserRole
import com.aplimovil.rotappmp.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Implementación temporal en memoria para acelerar el desarrollo multiplataforma. */
class FakeUserRepository : UserRepository {
    private val activeUser = MutableStateFlow<User?>(null)
    private val users = mutableListOf<User>()

    override val activeUserStream: Flow<User?> = activeUser.asStateFlow()

    override suspend fun getActiveUser(): User? = activeUser.value

    override suspend fun validateCredentials(email: String, password: String): Boolean {
        delay(400)
        val user = users.firstOrNull { it.email == email }
        val success = user != null && password.isNotBlank()
        if (success) activeUser.value = user
        return success
    }

    override suspend fun registerUser(fullName: String, email: String, password: String): User {
        delay(600)
        val user = User.fake(fullName = fullName, email = email, role = null)
        users.add(user)
        activeUser.value = user
        return user
    }

    override suspend fun updateUserRole(userId: Long, role: UserRole) {
        val index = users.indexOfFirst { it.id == userId }
        if (index >= 0) {
            val updated = users[index].copy(role = role)
            users[index] = updated
            if (activeUser.value?.id == userId) activeUser.value = updated
        }
    }
}

