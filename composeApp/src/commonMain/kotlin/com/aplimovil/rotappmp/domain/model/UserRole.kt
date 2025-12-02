package com.aplimovil.rotappmp.domain.model

/** Roles soportados dentro de RotApp. */
enum class UserRole(val id: String, val displayName: String) {
    ADMIN(id = "admin", displayName = "Administrador"),
    COLLABORATOR(id = "collaborator", displayName = "Colaborador"),
    VIEW_ONLY(id = "viewer", displayName = "Solo lectura"),
}
