package com.aplimovil.rotappmp.domain.model

/** Asocia un [UserRole] a un usuario dentro de una empresa específica. */
data class CompanyMembership(
    val userId: Long,
    val companyId: Long,
    val role: UserRole,
)

