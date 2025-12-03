package com.aplimovil.rotappmp.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Mapa de navegación declarativo compartido por todos los targets de la aplicación.
 *
 * Cada ruta se modela como subtipo sellado para que los `when` se mantengan exhaustivos al agregar destinos nuevos.
 */
sealed class RotAppRoute {
    data object Welcome : RotAppRoute()
    data object Login : RotAppRoute()
    data object Register : RotAppRoute()
    data object RoleSelection : RotAppRoute()
    data object CreateCompany : RotAppRoute()
    data object About : RotAppRoute()
}

/** Navegador que expone una pila en memoria mediante `StateFlow` para todos los targets. */
class RotAppNavigator(initial: RotAppRoute = RotAppRoute.Welcome) {
    private val _backstack = MutableStateFlow(listOf(initial))
    val backstack: StateFlow<List<RotAppRoute>> = _backstack.asStateFlow()

    val current: RotAppRoute get() = _backstack.value.last()

    /** Agrega una ruta a la pila y notifica a los observadores para cambiar de pantalla. */
    fun navigate(route: RotAppRoute) {
        _backstack.value = _backstack.value + route
    }

    /** Quita la última ruta; devuelve `false` cuando ya se está en la raíz. */
    fun pop(): Boolean {
        val stack = _backstack.value
        if (stack.size <= 1) return false
        _backstack.value = stack.dropLast(1)
        return true
    }

    /** Reemplaza toda la pila por una única ruta (útil para reiniciar flujos). */
    fun replaceAll(route: RotAppRoute) {
        _backstack.value = listOf(route)
    }
}
