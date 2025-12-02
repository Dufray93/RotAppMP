package com.aplimovil.rotappmp.ui.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import rotappmp.composeapp.generated.resources.Res
import rotappmp.composeapp.generated.resources.bg
import rotappmp.composeapp.generated.resources.rotapp_logo

private val RegisterBackgroundColor = Color(0xFFFBEFED)
private val RegisterCardColor = Color(0xFFF6DCD7)
private val RegisterPrimary = Color(0xFF8C3A33)
private val RegisterTextSecondary = Color(0xFF4C2D27)
private const val RegisterHeroZoom = 1.45f
private val RegisterHeroHeight = 340.dp
private val RegisterCardTopOverlap = 260.dp
private val RegisterLogoOffset = (-100).dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    state: RegisterUiState,
    onBackClick: () -> Unit,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    events: kotlinx.coroutines.flow.Flow<RegisterEvent>?,
    onNavigateToRoleSelection: () -> Unit,
) {
    LaunchedEffect(events) {
        events?.collectLatest { event ->
            if (event is RegisterEvent.NavigateToRoleSelection) {
                onNavigateToRoleSelection()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = RegisterBackgroundColor,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = RegisterTextSecondary,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        },
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 24.dp),
        ) {
            val heroHeight = minOf(RegisterHeroHeight, maxHeight * 0.45f)
            val cardOverlap = minOf(RegisterCardTopOverlap, heroHeight - 60.dp)

            RegisterHero(modifier = Modifier.fillMaxWidth(), heroHeight = heroHeight)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(top = cardOverlap, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RegisterForm(
                    state = state,
                    onFullNameChange = onFullNameChange,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onConfirmPasswordChange = onConfirmPasswordChange,
                    onRegisterClick = onRegisterClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun RegisterHero(modifier: Modifier = Modifier, heroHeight: Dp) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(heroHeight),
    ) {
        Image(
            painter = painterResource(Res.drawable.bg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(heroHeight)
                .graphicsLayer {
                    scaleX = RegisterHeroZoom
                    scaleY = RegisterHeroZoom
                    translationY = (-45).dp.toPx()
                },
            contentScale = ContentScale.Crop,
        )
        Image(
            painter = painterResource(Res.drawable.rotapp_logo),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(180.dp)
                .offset(y = RegisterLogoOffset),
        )
    }
}

@Composable
private fun RegisterForm(
    state: RegisterUiState,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(containerColor = RegisterCardColor.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = "Crear cuenta en RotApp",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = RegisterTextSecondary,
            )

            RegisterTextField(
                value = state.fullName,
                onValueChange = onFullNameChange,
                placeholder = "Nombre completo",
                keyboardType = KeyboardType.Text,
            )
            RegisterTextField(
                value = state.email,
                onValueChange = onEmailChange,
                placeholder = "Correo",
                keyboardType = KeyboardType.Email,
            )
            RegisterTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                placeholder = "Contraseña",
                keyboardType = KeyboardType.Password,
                isPassword = true,
            )
            RegisterTextField(
                value = state.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                placeholder = "Confirmar contraseña",
                keyboardType = KeyboardType.Password,
                isPassword = true,
            )

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = Color(0xFFB00020),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RegisterPrimary,
                    contentColor = Color.White,
                ),
                enabled = !state.isLoading,
            ) {
                Text(text = "Crear cuenta")
            }
        }
    }
}

@Composable
private fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = Color(0xFFB9A8A3),
                    start = androidx.compose.ui.geometry.Offset(0f, size.height),
                    end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                    strokeWidth = strokeWidth,
                )
            },
        placeholder = { Text(text = placeholder, color = Color(0xFF9C8C87)) },
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = RegisterTextSecondary),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
            cursorColor = RegisterPrimary,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        ),
    )
}
