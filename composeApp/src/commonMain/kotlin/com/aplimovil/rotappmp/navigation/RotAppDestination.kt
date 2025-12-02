package com.aplimovil.rotappmp.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Destinos declarativos para la navegación compartida de RotApp. */
sealed class RotAppRoute {
    data object Welcome : RotAppRoute()
    data object Login : RotAppRoute()
    data object Register : RotAppRoute()
    data object RoleSelection : RotAppRoute()
    data object CreateCompany : RotAppRoute()
    data object About : RotAppRoute()
}

/** Navegador basado en stack usando MutableStateFlow para todos los targets. */
class RotAppNavigator(initial: RotAppRoute = RotAppRoute.Welcome) {
    private val _backstack = MutableStateFlow(listOf(initial))
    val backstack: StateFlow<List<RotAppRoute>> = _backstack.asStateFlow()

    val current: RotAppRoute get() = _backstack.value.last()

    fun navigate(route: RotAppRoute) {
        _backstack.value = _backstack.value + route
    }

    fun pop(): Boolean {
        val stack = _backstack.value
        if (stack.size <= 1) return false
        _backstack.value = stack.dropLast(1)
        return true
    }

    fun replaceAll(route: RotAppRoute) {
        _backstack.value = listOf(route)
    }
}
