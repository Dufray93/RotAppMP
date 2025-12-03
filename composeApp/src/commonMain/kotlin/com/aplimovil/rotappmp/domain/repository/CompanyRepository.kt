package com.aplimovil.rotappmp.domain.repository

import com.aplimovil.rotappmp.domain.model.Company
import com.aplimovil.rotappmp.domain.model.CompanyCategory
import kotlinx.coroutines.flow.Flow

/** Contrato que la UI y los ViewModels usan para gestionar empresas sin importar la plataforma. */
interface CompanyRepository {
    fun observeCompaniesForUser(userId: Long): Flow<List<Company>>

    suspend fun createCompany(
        ownerUserId: Long,
        name: String,
        category: CompanyCategory,
        employeesCount: Int,
    ): Company
}

