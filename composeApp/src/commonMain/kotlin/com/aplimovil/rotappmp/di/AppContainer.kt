package com.aplimovil.rotappmp.di

import com.aplimovil.rotappmp.data.repository.FakeCompanyRepository
import com.aplimovil.rotappmp.data.repository.FakeUserRepository
import com.aplimovil.rotappmp.domain.repository.CompanyRepository
import com.aplimovil.rotappmp.domain.repository.UserRepository

/** Contenedor de dependencias simple para KMP. */
interface AppContainer {
    val userRepository: UserRepository
    val companyRepository: CompanyRepository
}

class DefaultAppContainer : AppContainer {
    override val userRepository: UserRepository = FakeUserRepository()
    override val companyRepository: CompanyRepository = FakeCompanyRepository()
}

