package com.aplimovil.rotappmp.domain.repository

import com.aplimovil.rotappmp.domain.model.Company
import com.aplimovil.rotappmp.domain.model.CompanyCategory
import kotlinx.coroutines.flow.Flow

/** Contrato de empresas y membresías. */
interface CompanyRepository {
    fun observeCompaniesForUser(userId: Long): Flow<List<Company>>

    suspend fun createCompany(
        ownerUserId: Long,
        name: String,
        category: CompanyCategory,
        employeesCount: Int,
    ): Company
}

