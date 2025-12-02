package com.aplimovil.rotappmp.data.repository

import com.aplimovil.rotappmp.data.local.LocalStorage
import com.aplimovil.rotappmp.domain.model.Company
import com.aplimovil.rotappmp.domain.model.CompanyCategory
import com.aplimovil.rotappmp.domain.repository.CompanyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class CompanyRepositoryImpl(private val storage: LocalStorage) : CompanyRepository {

    override fun observeCompaniesForUser(userId: Long): Flow<List<Company>> = flow {
        emit(storage.getCompaniesByUserId(userId))
    }

    override suspend fun createCompany(
        ownerUserId: Long,
        name: String,
        category: CompanyCategory,
        employeesCount: Int,
    ): Company {
        val companyId = Random.nextLong(1_000_000_000)
        val newCompany = Company(
            id = companyId,
            name = name,
            category = category,
            employeesCount = employeesCount
        )

        storage.saveCompany(newCompany)
        storage.linkCompanyToUser(companyId, ownerUserId)

        return newCompany
    }
}

