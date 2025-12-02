package com.aplimovil.rotappmp.domain.model

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class User(
    val id: Long,
    val fullName: String,
    val email: String,
    val password: String, // Necesario para validación de credenciales
    val role: UserRole? = null,
    val isActive: Boolean = false,
) {
    companion object {
        fun fake(
            id: Long = Random.nextLong(1_000_000_000L),
            fullName: String = "Usuario Demo",
            email: String = "demo@rotapp.com",
            password: String = "demo123",
            role: UserRole? = UserRole.COLLABORATOR,
            isActive: Boolean = true,
        ) = User(id, fullName, email, password, role, isActive)
    }
}
