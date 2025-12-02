package com.aplimovil.rotappmp.domain.model

import kotlinx.serialization.Serializable
import kotlin.random.Random

/** Representa los datos principales de una empresa administrada en RotApp. */
@Serializable
data class Company(
    val id: Long,
    val name: String,
    val category: CompanyCategory,
    val employeesCount: Int,
) {
    companion object {
        fun fake(
            id: Long = Random.nextLong(1_000_000_000L),
            name: String = "RotApp Corp",
            category: CompanyCategory = CompanyCategory.GENERAL,
            employeesCount: Int = 50,
        ) = Company(id, name, category, employeesCount)
    }
}

