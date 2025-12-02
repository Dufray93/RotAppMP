package com.aplimovil.rotappmp.domain.model

/** Opción seleccionable dentro del flujo de onboarding para definir el rol del usuario. */
data class RoleOption(
    val role: UserRole,
    val title: String,
    val description: String,
)
