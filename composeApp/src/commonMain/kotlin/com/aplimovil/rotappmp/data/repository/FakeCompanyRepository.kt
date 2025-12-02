package com.aplimovil.rotappmp.data.repository

import com.aplimovil.rotappmp.domain.model.Company
import com.aplimovil.rotappmp.domain.model.CompanyCategory
import com.aplimovil.rotappmp.domain.repository.CompanyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Implementación temporal en memoria para la capa de datos. */
class FakeCompanyRepository : CompanyRepository {
    private val companies = MutableStateFlow<List<Company>>(emptyList())

    override fun observeCompaniesForUser(userId: Long): Flow<List<Company>> = companies.asStateFlow()

    override suspend fun createCompany(
        ownerUserId: Long,
        name: String,
        category: CompanyCategory,
        employeesCount: Int,
    ): Company {
        delay(600)
        val newCompany = Company.fake(name = name, category = category, employeesCount = employeesCount)
        companies.value = companies.value + newCompany
        return newCompany
    }
}

