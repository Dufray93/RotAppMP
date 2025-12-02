package com.aplimovil.rotappmp.domain.model

import kotlin.random.Random

data class User(
    val id: Long,
    val fullName: String,
    val email: String,
    val role: UserRole?,
    val isActive: Boolean,
) {
    companion object {
        fun fake(
            id: Long = Random.nextLong(1_000_000_000L),
            fullName: String = "Usuario Demo",
            email: String = "demo@rotapp.com",
            role: UserRole? = UserRole.COLLABORATOR,
            isActive: Boolean = true,
        ) = User(id, fullName, email, role, isActive)
    }
}
