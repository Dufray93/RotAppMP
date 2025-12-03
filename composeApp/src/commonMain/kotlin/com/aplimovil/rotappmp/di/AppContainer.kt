package com.aplimovil.rotappmp.di

import com.aplimovil.rotappmp.data.local.LocalStorage
import com.aplimovil.rotappmp.data.repository.CompanyRepositoryImpl
import com.aplimovil.rotappmp.data.repository.UserRepositoryImpl
import com.aplimovil.rotappmp.domain.repository.CompanyRepository
import com.aplimovil.rotappmp.domain.repository.UserRepository

/** Punto de entrada de dependencias que separa la capa de datos concreta de la UI. */
interface AppContainer {
    val userRepository: UserRepository
    val companyRepository: CompanyRepository
}

/** Implementación por defecto que comparte una única instancia de [LocalStorage] entre los repositorios. */
class DefaultAppContainer(
    private val storage: LocalStorage = LocalStorage(),
) : AppContainer {

    override val userRepository: UserRepository = UserRepositoryImpl(storage)
    override val companyRepository: CompanyRepository = CompanyRepositoryImpl(storage)
}
