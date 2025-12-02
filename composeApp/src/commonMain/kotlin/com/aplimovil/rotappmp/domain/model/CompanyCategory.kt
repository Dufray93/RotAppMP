package com.aplimovil.rotappmp.domain.model

import kotlinx.serialization.Serializable

/** Categorías generales de empresas soportadas en RotApp. */
@Serializable
enum class CompanyCategory(val id: String, val displayName: String) {
    GENERAL(id = "general", displayName = "General"),
    HEALTH(id = "health", displayName = "Salud"),
    RETAIL(id = "retail", displayName = "Retail"),
    SERVICES(id = "services", displayName = "Servicios"),
    MANUFACTURING(id = "manufacturing", displayName = "Manufactura"),
}
