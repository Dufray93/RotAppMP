package com.aplimovil.rotappmp.domain.model

/** Relación entre un usuario y una empresa con el rol asignado. */
data class CompanyMembership(
    val userId: Long,
    val companyId: Long,
    val role: UserRole,
)

