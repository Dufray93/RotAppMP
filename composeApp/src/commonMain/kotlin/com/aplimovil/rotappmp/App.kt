package com.aplimovil.rotappmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.aplimovil.rotappmp.navigation.RotAppNavigator
import com.aplimovil.rotappmp.navigation.RotAppRoute
import com.aplimovil.rotappmp.ui.about.AboutScreen
import com.aplimovil.rotappmp.ui.login.LoginScreen
import com.aplimovil.rotappmp.ui.login.LoginViewModel
import com.aplimovil.rotappmp.ui.welcome.WelcomeScreen

@Composable
fun App(appNavigator: RotAppNavigator = remember { RotAppNavigator() }) {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val stack by appNavigator.backstack.collectAsState()
            val destination = stack.last()
            val loginViewModel = remember { LoginViewModel() }
            val loginState by loginViewModel.uiState.collectAsState()

            when (destination) {
                RotAppRoute.Welcome -> WelcomeScreen(
                    onLoginClick = { appNavigator.navigate(RotAppRoute.Login) },
                    onRegisterClick = { appNavigator.navigate(RotAppRoute.Register) },
                    onAboutClick = { appNavigator.navigate(RotAppRoute.About) },
                )
                RotAppRoute.Login -> LoginScreen(
                    email = loginState.email,
                    password = loginState.password,
                    onBackClick = { if (!appNavigator.pop()) appNavigator.replaceAll(RotAppRoute.Welcome) },
                    onEmailChange = loginViewModel::onEmailChange,
                    onPasswordChange = loginViewModel::onPasswordChange,
                    onLoginClick = loginViewModel::onLoginRequested,
                    onForgotPasswordClick = loginViewModel::onForgotPasswordRequested,
                )
                RotAppRoute.Register -> PlaceholderScreen("Pantalla Registro en construcción")
                RotAppRoute.RoleSelection -> PlaceholderScreen("Selección de rol en construcción")
                RotAppRoute.CreateCompany -> PlaceholderScreen("Crear empresa en construcción")
                RotAppRoute.About -> AboutScreen(
                    onBackClick = { if (!appNavigator.pop()) appNavigator.replaceAll(RotAppRoute.Welcome) },
                )
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
    }
}
