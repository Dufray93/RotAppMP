package com.aplimovil.rotappmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.aplimovil.rotappmp.navigation.RotAppDestination
import com.aplimovil.rotappmp.ui.about.AboutScreen
import com.aplimovil.rotappmp.ui.login.LoginScreen
import com.aplimovil.rotappmp.ui.welcome.WelcomeScreen

@Composable
fun App(appNavigator: AppNavigator = remember { AppNavigator() }) {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            var loginEmail by remember { mutableStateOf("") }
            var loginPassword by remember { mutableStateOf("") }

            when (appNavigator.currentDestination) {
                RotAppDestination.Welcome -> WelcomeScreen(
                    onLoginClick = { appNavigator.navigate(RotAppDestination.Login) },
                    onRegisterClick = { appNavigator.navigate(RotAppDestination.Register) },
                    onAboutClick = { appNavigator.navigate(RotAppDestination.About) },
                )
                RotAppDestination.Login -> LoginScreen(
                    email = loginEmail,
                    password = loginPassword,
                    onBackClick = { appNavigator.navigate(RotAppDestination.Welcome) },
                    onEmailChange = { loginEmail = it },
                    onPasswordChange = { loginPassword = it },
                    onLoginClick = {
                        // TODO: reemplazar con autenticación real
                    },
                    onForgotPasswordClick = {
                        // TODO: navegar a flujo de recuperación
                    },
                )
                RotAppDestination.Register -> PlaceholderScreen("Pantalla Registro en construcción")
                RotAppDestination.RoleSelection -> PlaceholderScreen("Selección de rol en construcción")
                RotAppDestination.CreateCompany -> PlaceholderScreen("Crear empresa en construcción")
                RotAppDestination.About -> AboutScreen(
                    onBackClick = { appNavigator.navigate(RotAppDestination.Welcome) },
                )
            }
        }
    }
}

class AppNavigator(initial: RotAppDestination = RotAppDestination.Welcome) {
    var currentDestination by mutableStateOf(initial)
        private set

    fun navigate(destination: RotAppDestination) {
        currentDestination = destination
    }

    fun popTo(destination: RotAppDestination) {
        currentDestination = destination
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
    }
}
