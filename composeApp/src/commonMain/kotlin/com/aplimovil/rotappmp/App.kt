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
import com.aplimovil.rotappmp.di.AppContainer
import com.aplimovil.rotappmp.navigation.RotAppNavigator
import com.aplimovil.rotappmp.navigation.RotAppRoute
import com.aplimovil.rotappmp.ui.about.AboutScreen
import com.aplimovil.rotappmp.ui.createcompany.CreateCompanyScreen
import com.aplimovil.rotappmp.ui.createcompany.CreateCompanyViewModel
import com.aplimovil.rotappmp.ui.login.LoginScreen
import com.aplimovil.rotappmp.ui.login.LoginViewModel
import com.aplimovil.rotappmp.ui.register.RegisterScreen
import com.aplimovil.rotappmp.ui.register.RegisterViewModel
import com.aplimovil.rotappmp.ui.roleselection.RoleSelectionScreen
import com.aplimovil.rotappmp.ui.roleselection.RoleSelectionViewModel
import com.aplimovil.rotappmp.ui.welcome.WelcomeScreen

/**
 * Composable raíz que conecta viewmodels, navegación y pantallas para todos los targets.
 *
 * @param appNavigator pila en memoria compartida entre plataformas; por defecto crea una instancia por composición.
 * @param appContainer punto de entrada de dependencias que expone los repositorios usados por los viewmodels.
 */
@Composable
fun App(
    appNavigator: RotAppNavigator = remember { RotAppNavigator() },
    appContainer: AppContainer,
) {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val stack by appNavigator.backstack.collectAsState()
            val destination = stack.last()
            val loginViewModel = remember { LoginViewModel(appContainer.userRepository) }
            val loginState by loginViewModel.uiState.collectAsState()
            val registerViewModel = remember { RegisterViewModel(appContainer.userRepository) }
            val registerState by registerViewModel.uiState.collectAsState()
            val roleSelectionViewModel = remember { RoleSelectionViewModel(appContainer.userRepository) }
            val roleSelectionState by roleSelectionViewModel.uiState.collectAsState()
            val createCompanyViewModel = remember { CreateCompanyViewModel(appContainer.companyRepository, appContainer.userRepository) }
            val createCompanyState by createCompanyViewModel.uiState.collectAsState()

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
                RotAppRoute.Register -> RegisterScreen(
                    state = registerState,
                    onBackClick = { if (!appNavigator.pop()) appNavigator.replaceAll(RotAppRoute.Welcome) },
                    onFullNameChange = registerViewModel::onFullNameChange,
                    onEmailChange = registerViewModel::onEmailChange,
                    onPasswordChange = registerViewModel::onPasswordChange,
                    onConfirmPasswordChange = registerViewModel::onConfirmPasswordChange,
                    onRegisterClick = registerViewModel::onRegisterRequested,
                    events = registerViewModel.events,
                    onNavigateToRoleSelection = { appNavigator.navigate(RotAppRoute.RoleSelection) },
                )
                RotAppRoute.RoleSelection -> RoleSelectionScreen(
                    state = roleSelectionState,
                    onRoleSelected = roleSelectionViewModel::onRoleSelected,
                    onContinue = roleSelectionViewModel::onContinue,
                    onBackClick = { if (!appNavigator.pop()) appNavigator.replaceAll(RotAppRoute.Welcome) },
                    events = roleSelectionViewModel.events,
                    onNavigateNext = { appNavigator.navigate(RotAppRoute.CreateCompany) },
                )
                RotAppRoute.CreateCompany -> CreateCompanyScreen(
                    state = createCompanyState,
                    onNameChange = createCompanyViewModel::onNameChange,
                    onCategorySelected = createCompanyViewModel::onCategorySelected,
                    onEmployeesChange = createCompanyViewModel::onEmployeesChange,
                    onSubmit = createCompanyViewModel::onSubmit,
                    onBackClick = { if (!appNavigator.pop()) appNavigator.replaceAll(RotAppRoute.Welcome) },
                    events = createCompanyViewModel.events,
                    onNavigateHome = { appNavigator.replaceAll(RotAppRoute.Welcome) },
                )
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
