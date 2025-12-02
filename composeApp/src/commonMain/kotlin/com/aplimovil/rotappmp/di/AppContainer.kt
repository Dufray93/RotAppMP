package com.aplimovil.rotappmp.di

import com.aplimovil.rotappmp.data.local.LocalStorage
import com.aplimovil.rotappmp.data.repository.CompanyRepositoryImpl
import com.aplimovil.rotappmp.data.repository.UserRepositoryImpl
import com.aplimovil.rotappmp.domain.repository.CompanyRepository
import com.aplimovil.rotappmp.domain.repository.UserRepository

/** Contenedor de dependencias simple para KMP. */
interface AppContainer {
    val userRepository: UserRepository
    val companyRepository: CompanyRepository
}

class DefaultAppContainer(
    private val storage: LocalStorage = LocalStorage(),
) : AppContainer {

    override val userRepository: UserRepository = UserRepositoryImpl(storage)
    override val companyRepository: CompanyRepository = CompanyRepositoryImpl(storage)
}
