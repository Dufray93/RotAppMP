package com.aplimovil.rotappmp.domain.model

import kotlinx.serialization.Serializable

/** Roles soportados dentro de RotApp. */
@Serializable
enum class UserRole(val id: String, val displayName: String) {
    ADMIN(id = "admin", displayName = "Administrador"),
    COLLABORATOR(id = "collaborator", displayName = "Colaborador"),
    VIEW_ONLY(id = "viewer", displayName = "Solo lectura"),
}
